package com.laughoutloud.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.laughoutloud.*
import com.laughoutloud.ui.fragment.*
import com.laughoutloud.viewmodel.ModelHomeActivity
import kotlinx.android.synthetic.main.activity_home.*


class ActivityHome : AppCompatActivity() {
    lateinit var thisContext: Activity
    lateinit var modelHomeActivity: ModelHomeActivity
    var currentTabPosition = 1
    var TAB_POSITION = "TAB_POSITION"
    lateinit var viewpagerAdapter: ViewPagerToggler





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        thisContext = this
        ClassUtilities().createDirs(thisContext)
        modelHomeActivity = ViewModelProvider(this).get(ModelHomeActivity::class.java)



        //LOAD DATA...
        addFragmentsAndViewpager()


        modelHomeActivity.refreshJokeType.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { joke_type ->
                ClassAlertDialog(thisContext).toast("$joke_type from activity...")
                when(joke_type){
                    "text"->{

                    }
                    "image"->{

                    }
                    "video"->{

                    }
                }
            }
        })

        //downloading file(img or video)
        modelHomeActivity.textBinderChange.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { data ->

            }
        })




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
        tabs.setupWithViewPager(view_pager)


        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                currentTabPosition = position
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })

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
                startActivity(Intent(this,   ActivityWatchVideo::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.action_menu_feedback->{
                startActivity(Intent(this,   ActivityFileDownload::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addJokeDialog() {
        val dataList = arrayListOf("Type Joke", "Add Image", "Upload Video")
        val builder = AlertDialog.Builder(this)
        // add a list
        builder.setItems(dataList.toTypedArray()) { dialog, which ->
            when(which){
                0->{
                    viewCreateJokeDialogShow()
                }
                1->{
                    imgCreateJokeDialogShow()
                }
                2->{
                    videoCreateJokeDialogShow()
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
























    class ViewPagerToggler(manager: FragmentManager) : FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val titles: MutableList<String> = ArrayList()


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
        if(dialogFragmentCreateJokeVideo.isAdded)return

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(FragmentDialogUploadVideoJoke::class.java.name)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        dialogFragmentCreateJokeVideo.show(ft, FragmentDialogUploadVideoJoke::class.java.name)
    }


}
