package com.happinesstonic.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.downloader.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.happinesstonic.R
import com.happinesstonic.ui.fragment.*
import com.happinesstonic.utils.*
import com.happinesstonic.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.alert_dialog_add_joke.view.*
import kotlinx.android.synthetic.main.alert_dialog_admin_login.view.*
import kotlinx.android.synthetic.main.progress_dialog_download.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ActivityHome : AppCompatActivity() {
    lateinit var thisContext: Activity
    lateinit var modelHomeActivity: ModelHomeActivity
    var currentTabPosition = 0
    var TAB_POSITION = "TAB_POSITION"
    lateinit var viewpagerAdapter: ViewPagerToggler
    lateinit var prefs: ClassSharedPreferences


    private lateinit var mInterstitialAd: InterstitialAd



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        thisContext = this
        modelHomeActivity = ViewModelProvider(this).get(ModelHomeActivity::class.java)
        prefs = ClassSharedPreferences(thisContext)


        if (prefs.getUserId()==null){
            val randId = (System.currentTimeMillis()/1000)+(100000 until 999999).shuffled().last()
            prefs.setUserId("$randId")
        }

        //LOAD DATA...
        addFragmentsAndViewpager()



        //ads(BANNER) -> SMALL
        //loading ads starts
        MobileAds.initialize(this,  getString(R.string.ads_app_id))
        val adRequest = AdRequest.Builder().build()
//        adsView.adUnitId = getString(R.string.banner_ads1)
        adsView.loadAd(adRequest)



//        for testing
//        val request = AdRequest.Builder()
//                .addTestDevice("33BE2250B43518CCDA7DE426D04EE231")  // An example device ID
//                .build()
////        adsView.adUnitId = getString(R.string.banner_test_ad_unit_id)
//        adsView.loadAd(request);
//        loading ads stops





        //ADS(INTERSTITIAL) -> BIG

        //loading full page ads
        MobileAds.initialize(this,  getString(R.string.ads_app_id))
        mInterstitialAd = InterstitialAd(this)
//        //real values starts
        mInterstitialAd.adUnitId = getString(R.string.interstitial_ads1)//real ID
        mInterstitialAd.loadAd(AdRequest.Builder().build())//real One

        //testing starts
//        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"//testing one
//        mInterstitialAd.loadAd(AdRequest.Builder().addTestDevice("33BE2250B43518CCDA7DE426D04EE231").build())//testing one

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ClassUtilities().createDirs(thisContext)
            }else{
                ClassAlertDialog(thisContext).alertMessage("Camera and storage permission is needed to use application")
            }
        }
    }
    override fun onBackPressed() {
        if (view_pager.currentItem != 2) {
            view_pager.currentItem = 2
        } else {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            }

            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        //for normal tab change and refresh
        modelHomeActivity.getRefreshJokeTab().observe(this, Observer {
            it?.getContentIfNotHandled()?.let { joke_type ->
                when(joke_type){
                    "text"->{
                        if(view_pager.currentItem != 0)
                            view_pager.currentItem = 0
                        modelHomeActivity.fragRefreshJokeTabText(joke_type)
                    }
                    "image"->{
                        if(view_pager.currentItem != 1)
                            view_pager.currentItem = 1
                        deleteFiles("tmp")
                        modelHomeActivity.fragRefreshJokeTabImage(joke_type)
                    }
                    "video"->{
                        if(view_pager.currentItem != 2)
                            view_pager.currentItem = 2
                        deleteFiles("gif")
                        modelHomeActivity.fragRefreshJokeTabVideo(joke_type)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.statusBarColor = ContextCompat.getColor(thisContext, R.color.colorPrimaryDark)
                }
            }
        })

        //for image download
        modelHomeActivity.downloadJokeFile.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { data ->
                if (Build.VERSION.SDK_INT >= 23) {
                    if(ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||
                        (ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(thisContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                    else{
                        downloadFile(data)
                    }
                } else {
                    downloadFile(data)
                }
            }
        })
    }

    private fun deleteFiles(file_type:String){
        val tmpFiles = ClassUtilities().getDirPath(thisContext, "tmp")
        val gifFiles = ClassUtilities().getDirPath(thisContext, "gif")
        val path = if(file_type == "tmp") tmpFiles else gifFiles

        val directory = File(path)
        val files = directory.listFiles()

        if(files!!.isNotEmpty()){
            for (f in files) {
                try {
                    f.delete()
                } catch (e: Exception) {e.printStackTrace()}
            }
        }

    }

    var downloadFileId:Int = 0
    @SuppressLint("SetTextI18n")
    private fun downloadFile(data: TextClassBinder) {
        val vidFileFolder = ClassUtilities().getDirPath(thisContext, "vid")
        val imgFileFolder = ClassUtilities().getDirPath(thisContext, "pic")

        val inflater = LayoutInflater.from(thisContext).inflate(R.layout.progress_dialog_download, null)
        val builder = AlertDialog.Builder(thisContext)
        builder.setView(inflater)
        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        if (Status.RUNNING === PRDownloader.getStatus(downloadFileId)){
            PRDownloader.pause(downloadFileId)
            return
        }
        inflater.progressBarDownload.isIndeterminate = true
        inflater.progressBarDownload.indeterminateDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
        if (Status.PAUSED === PRDownloader.getStatus(downloadFileId)){
            PRDownloader.resume(downloadFileId)
            return
        }
        downloadFileId = (if(data.joke_type=="image") PRDownloader.download(data.joke_image_url, imgFileFolder, ClassUtilities()
            .getImageName(data))
                            else PRDownloader.download(data.joke_video_url, vidFileFolder, ClassUtilities()
            .getVideoName(data)))
            .build()
            .setOnStartOrResumeListener {
                inflater.progressBarDownload.isIndeterminate = false
            }
            .setOnPauseListener {
//                        buttonFourteen.setText(R.string.resume)
            }
            .setOnCancelListener {
                downloadFileId = 0
                inflater.progressBarDownload.progress = 0
                inflater.progressBarDownload.isIndeterminate = false
                inflater.tvProgressPercent.text = "(0%)"
            }
            .setOnProgressListener { progress ->
//                val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                val progressPercent = progress.currentBytes * 100 / (data.joke_no_of_comment!!.toLong())
                inflater.progressBarDownload.progress = progressPercent.toInt()
//                inflater.tvProgressPercent.text = "(${progress.currentBytes*100/progress.totalBytes}%)"
                inflater.tvProgressPercent.text = "(${progress.currentBytes*100/(data.joke_no_of_comment!!.toLong())}%)"
            }
            .start(object: OnDownloadListener {
                override fun onDownloadComplete() {
                    alertDialog.dismiss()

                    val fileName = if(data.joke_type=="image") "$imgFileFolder/${ClassUtilities()
                        .getImageName(data)}"
                                else "$vidFileFolder/${ClassUtilities()
                        .getVideoName(data)}"

                    if(File(fileName).exists()){
                        ClassShareApp(thisContext)
                            .shareFileFromPath(fileName, "${data.share_to}")
                    }else{
                        ClassAlertDialog(thisContext).toast("Error occurred, Try again...")
                    }
                }
                override fun onError(error: Error) {
                    alertDialog.dismiss()

                    downloadFileId = 0
                    inflater.progressBarDownload.progress = 0
                    inflater.progressBarDownload.isIndeterminate = false
                    inflater.tvProgressPercent.text = "(0%)"

                    ClassAlertDialog(thisContext).toast("Error occurred, Try again...")
                }
            })
    }
    fun getProgressDisplayLine(currentBytes: Long,totalBytes: Long): String? {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
    }

    private fun getBytesToMBString(bytes: Long): String {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }


    private fun addFragmentsAndViewpager(){
        viewpagerAdapter = ViewPagerToggler(supportFragmentManager)
        viewpagerAdapter.addFragment(FragmentTextJoke(), "Jokes")
        viewpagerAdapter.addFragment(FragmentImage(), "Images")
        viewpagerAdapter.addFragment(FragmentVideo(), "Videos")

        // Finally, data bind the view pager widget with pager viewpagerAdapter
        view_pager.adapter = viewpagerAdapter
        view_pager.offscreenPageLimit = viewpagerAdapter.count-1
        view_pager.currentItem = currentTabPosition
        tabLayout.setupWithViewPager(view_pager)


        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                currentTabPosition = position
                updateTabView()
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
        var tIndex = 0
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tIndex++
                if (tIndex>=6){
                    if(currentTabPosition !=0){
                        ClassAlertDialog(thisContext).toast("Admin mode...")
                        adminLoginDialog()
                    }else{
                        ClassSharedPreferences(thisContext)
                            .setIsUserAdmin(false)
                        finish()
                        startActivity(intent)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tIndex = 1
            }

        })


        //set custom tablayout
        setupTabLayout()
    }

    fun adminLoginDialog(){
        val inflater = LayoutInflater.from(thisContext).inflate(R.layout.alert_dialog_admin_login, null)
        val builder = AlertDialog.Builder(thisContext)
        builder.setView(inflater)
        val aDialog = builder.create()
        aDialog.show()


        val ACCESS_CODE = "5544%$"
        inflater.admin_login.setOnClickListener {
            if(inflater.admin_access_code.text.toString().trim()==ACCESS_CODE){
                ClassSharedPreferences(thisContext).setIsUserAdmin(true)
                finish()
                startActivity(intent)
            }else{
                ClassAlertDialog(thisContext).toast("Wrong access code..")
            }
            aDialog.dismiss()
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_POSITION, currentTabPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentTabPosition = savedInstanceState.getInt(TAB_POSITION)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_add_joke -> {
                addJokeDialog()
            }
            R.id.action_menu_add_joke2 -> {
                addJokeDialog()
            }
            R.id.action_menu_share_app ->{
//                startActivity(Intent(this,   ActivityWatchVideo::class.java))
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                ClassShareApp(thisContext).shareApp()
            }
            R.id.action_menu_update_app->{
//                startActivity(Intent(this,   ActivityFileDownload::class.java))
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                ClassAlertDialog(thisContext).redirectToPlayStore()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addJokeDialog() {
//        val dataList = arrayListOf("Type Joke", "Add Image", "Upload Video")
//        val builder = AlertDialog.Builder(this)
//        // add a list
//        builder.setItems(dataList.toTypedArray()) { dialog, which ->
//            when(which){
//                0->{
//                    viewCreateJokeDialogShow()
//                }
//                1->{
//                    imgCreateJokeDialogShow()
//                }
//                2->{
//                    videoCreateJokeDialogShow()
//                }
//            }
//        }
//        val dialog = builder.create()
//        dialog.show()

        val inflater = LayoutInflater.from(thisContext).inflate(R.layout.alert_dialog_add_joke, null)
        val builder = AlertDialog.Builder(thisContext)
        builder.setView(inflater)
        val aDialog = builder.create()
        aDialog.show()


        inflater.type_post.setOnClickListener {
            viewCreateJokeDialogShow()
            aDialog.dismiss()
        }
        inflater.upload_photo.setOnClickListener {
            imgCreateJokeDialogShow()
            aDialog.dismiss()
        }
        inflater.upload_video.setOnClickListener {
            videoCreateJokeDialogShow()
            aDialog.dismiss()
        }

    }






















    private val tabTitles = arrayOf("Texts", "Images", "Videos")
    private val imageResId = intArrayOf(R.drawable.ic_sort_by_alpha_white, R.drawable.ic_linked_camera, R.drawable.ic_videocam_white)

    private fun setupTabLayout(){
        // Iterate over all tabs and set the custom view
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            tab!!.customView = viewpagerAdapter.getTabViewAt(i)
        }

        updateTabView()
    }

    fun updateTabView() {
        for (i in 0 until tabLayout.tabCount) {
            val custom_tablayout = LayoutInflater.from(thisContext).inflate(R.layout.custom_tab, null)
            val tab_title = custom_tablayout.findViewById(R.id.tab_title) as TextView
            val tab_icon = custom_tablayout.findViewById(R.id.tab_icon) as ImageView
            tab_title.text = tabTitles[i]
            tab_icon.setImageResource(imageResId[i])


            val tab = tabLayout.getTabAt(i)
            if (tab!!.isSelected){
//                tab_title.setTextColor(ContextCompat.getColor(thisContext,R.color.colorOnTabSelected), PorterDuff.Mode.SRC_IN)
                tab_icon.setColorFilter(ContextCompat.getColor(thisContext,R.color.colorOnTabSelected), PorterDuff.Mode.SRC_IN)
                tab_icon.layoutParams = LinearLayout.LayoutParams(55, 55)
            }else{
//                tab_title.setTextColor(ContextCompat.getColor(thisContext,R.color.colorOnUnTabSelected), PorterDuff.Mode.SRC_IN)
                tab_icon.setColorFilter(ContextCompat.getColor(thisContext,R.color.colorOnUnTabSelected), PorterDuff.Mode.SRC_IN)
                tab_icon.layoutParams = LinearLayout.LayoutParams(30, 30)
            }

            tab.customView = null
            tab.customView = custom_tablayout
        }
    }


    inner class ViewPagerToggler(manager: FragmentManager) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val titles: MutableList<String> = ArrayList()

        fun getTabViewAt(position: Int): View? {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            val v = LayoutInflater.from(thisContext).inflate(R.layout.custom_tab, null)
            val tv = v.findViewById(R.id.tab_title) as TextView
            tv.text = tabTitles[position]
            val img = v.findViewById(R.id.tab_icon) as ImageView
            img.setImageResource(imageResId[position])
            return v
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }
        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }


        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }
    }





    //create text joke frag dialog class
    private val dialogFragmentCreateJoke = FragmentDialogCreateJoke()
    private fun viewCreateJokeDialogShow(){
        if(dialogFragmentCreateJoke.isAdded)return

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(FragmentDialogCreateJoke::class.java.name)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragmentCreateJoke.show(ft, FragmentDialogCreateJoke::class.java.name)
    }
    //create image joke frag dialog class
    private val dialogFragmentCreateJokeImg = FragmentDialogUploadImageJoke()
    private fun imgCreateJokeDialogShow(){
        if(dialogFragmentCreateJokeImg.isAdded)return

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(FragmentDialogUploadImageJoke::class.java.name)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragmentCreateJokeImg.show(ft, FragmentDialogUploadImageJoke::class.java.name)
    }
    //create video joke frag dialog class
    private val dialogFragmentCreateJokeVideo = FragmentDialogUploadVideoJoke()
    private fun videoCreateJokeDialogShow(){
//        if(dialogFragmentCreateJokeVideo.isAdded)return

        try {
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag(FragmentDialogUploadVideoJoke::class.java.name)
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            dialogFragmentCreateJokeVideo.show(ft, FragmentDialogUploadVideoJoke::class.java.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
