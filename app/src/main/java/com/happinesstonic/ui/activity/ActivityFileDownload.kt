package com.happinesstonic.ui.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.downloader.Error
import com.downloader.OnCancelListener
import com.downloader.OnDownloadListener
import com.downloader.OnPauseListener
import com.downloader.OnProgressListener
import com.downloader.OnStartOrResumeListener
import com.downloader.PRDownloader
import com.downloader.Progress
import com.downloader.Status
import com.happinesstonic.R
import java.io.File
import java.util.*


class ActivityFileDownload: AppCompatActivity() {
    internal val URL1 = "http://www.appsapk.com/downloading/latest/Facebook-119.0.0.23.70.apk"
    internal val URL2 = "http://www.appsapk.com/downloading/latest/WeChat-6.5.7.apk"
    internal val URL3 = "http://www.appsapk.com/downloading/latest/Instagram.apk"
    internal val URL4 = "http://www.appsapk.com/downloading/latest/Emoji%20Flashlight%20-%20Brightest%20Flashlight%202018-2.0.1.apk"
    internal val URL5 = "http://www.appsapk.com/downloading/latest/Screen%20Recorder-7.7.apk"
    internal val URL6 = "http://www.appsapk.com/downloading/latest/Call%20Recorder%20-%20Automatic%20Call%20Recorder-1.6.0.apk"
    internal val URL7 = "http://www.appsapk.com/downloading/latest/Sound%20Profile%20(+%20volume%20scheduler)-5.25.apk"
    internal val URL8 = "http://www.appsapk.com/downloading/latest/Evernote%20-%20stay%20organized.-7.9.7.apk"
    internal val URL9 = "http://www.appsapk.com/downloading/latest/UC-Browser.apk"
    internal val URL10 = "http://www.appsapk.com/downloading/latest/Barcode%20Scanner-1.2.apk"
    internal val URL11 = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_640x360.m4v"
    internal val URL12 = "http://www2.sdfi.edu.cn/netclass/jiaoan/englit/download/Harry%20Potter%20and%20the%20Sorcerer's%20Stone.pdf"
    internal val URL13 = "https://media.giphy.com/media/Bk0CW5frw4qfS/giphy.gif"
    internal val URL14 = "http://techslides.com/demos/sample-videos/small.mp4"
    internal val URL15 = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_10mb.mp4"
    lateinit var buttonOne:Button
    lateinit var buttonTwo:Button
    lateinit var buttonThree:Button
    lateinit var buttonFour:Button
    lateinit var buttonFive:Button
    lateinit var buttonSix:Button
    lateinit var buttonSeven:Button
    lateinit var buttonEight:Button
    lateinit var buttonNine:Button
    lateinit var buttonTen:Button
    lateinit var buttonEleven:Button
    lateinit var buttonTwelve:Button
    lateinit var buttonThirteen:Button
    lateinit var buttonFourteen:Button
    lateinit var buttonFifteen:Button
    lateinit var buttonCancelOne:Button
    lateinit var buttonCancelTwo:Button
    lateinit var buttonCancelThree:Button
    lateinit var buttonCancelFour:Button
    lateinit var buttonCancelFive:Button
    lateinit var buttonCancelSix:Button
    lateinit var buttonCancelSeven:Button
    lateinit var buttonCancelEight:Button
    lateinit var buttonCancelNine:Button
    lateinit var buttonCancelTen:Button
    lateinit var buttonCancelEleven:Button
    lateinit var buttonCancelTwelve:Button
    lateinit var buttonCancelThirteen:Button
    lateinit var buttonCancelFourteen:Button
    lateinit var buttonCancelFifteen:Button
    lateinit var textViewProgressOne:TextView
    lateinit var textViewProgressTwo:TextView
    lateinit var textViewProgressThree:TextView
    lateinit var textViewProgressFour:TextView
    lateinit var textViewProgressFive:TextView
    lateinit var textViewProgressSix:TextView
    lateinit var textViewProgressSeven:TextView
    lateinit var textViewProgressEight:TextView
    lateinit var textViewProgressNine:TextView
    lateinit var textViewProgressTen:TextView
    lateinit var textViewProgressEleven:TextView
    lateinit var textViewProgressTwelve:TextView
    lateinit var textViewProgressThirteen:TextView
    lateinit var textViewProgressFourteen:TextView
    lateinit var textViewProgressFifteen:TextView
    lateinit var progressBarOne:ProgressBar
    lateinit var progressBarTwo:ProgressBar
    lateinit var progressBarThree:ProgressBar
    lateinit var progressBarFour:ProgressBar
    lateinit var progressBarFive:ProgressBar
    lateinit var progressBarSix:ProgressBar
    lateinit var progressBarSeven:ProgressBar
    lateinit var progressBarEight:ProgressBar
    lateinit var progressBarNine:ProgressBar
    lateinit var progressBarTen:ProgressBar
    lateinit var progressBarEleven:ProgressBar
    lateinit var progressBarTwelve:ProgressBar
    lateinit var progressBarThirteen:ProgressBar
    lateinit var progressBarFourteen:ProgressBar
    lateinit var progressBarFifteen:ProgressBar
    internal var downloadIdOne:Int = 0
    internal var downloadIdTwo:Int = 0
    internal var downloadIdThree:Int = 0
    internal var downloadIdFour:Int = 0
    internal var downloadIdFive:Int = 0
    internal var downloadIdSix:Int = 0
    internal var downloadIdSeven:Int = 0
    internal var downloadIdEight:Int = 0
    internal var downloadIdNine:Int = 0
    internal var downloadIdTen:Int = 0
    internal var downloadIdEleven:Int = 0
    internal var downloadIdTwelve:Int = 0
    internal var downloadIdThirteen:Int = 0
    internal var downloadIdFourteen:Int = 0
    internal var downloadIdFifteen:Int = 0
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_download)
        dirPath = getRootDirPath(applicationContext)!!
        init()
        onClickListenerOne()
        onClickListenerTwo()
        onClickListenerThree()
        onClickListenerFour()
        onClickListenerFive()
        onClickListenerSix()
        onClickListenerSeven()
        onClickListenerEight()
        onClickListenerNine()
        onClickListenerTen()
        onClickListenerEleven()
        onClickListenerTwelve()
        onClickListenerThirteen()
        onClickListenerFourteen()
        onClickListenerFifteen()
    }
    private fun init() {
        buttonOne = findViewById(R.id.buttonOne)
        buttonTwo = findViewById(R.id.buttonTwo)
        buttonThree = findViewById(R.id.buttonThree)
        buttonFour = findViewById(R.id.buttonFour)
        buttonFive = findViewById(R.id.buttonFive)
        buttonSix = findViewById(R.id.buttonSix)
        buttonSeven = findViewById(R.id.buttonSeven)
        buttonEight = findViewById(R.id.buttonEight)
        buttonNine = findViewById(R.id.buttonNine)
        buttonTen = findViewById(R.id.buttonTen)
        buttonEleven = findViewById(R.id.buttonEleven)
        buttonTwelve = findViewById(R.id.buttonTwelve)
        buttonThirteen = findViewById(R.id.buttonThirteen)
        buttonFourteen = findViewById(R.id.buttonFourteen)
        buttonFifteen = findViewById(R.id.buttonFifteen)
        buttonCancelOne = findViewById(R.id.buttonCancelOne)
        buttonCancelTwo = findViewById(R.id.buttonCancelTwo)
        buttonCancelThree = findViewById(R.id.buttonCancelThree)
        buttonCancelFour = findViewById(R.id.buttonCancelFour)
        buttonCancelFive = findViewById(R.id.buttonCancelFive)
        buttonCancelSix = findViewById(R.id.buttonCancelSix)
        buttonCancelSeven = findViewById(R.id.buttonCancelSeven)
        buttonCancelEight = findViewById(R.id.buttonCancelEight)
        buttonCancelNine = findViewById(R.id.buttonCancelNine)
        buttonCancelTen = findViewById(R.id.buttonCancelTen)
        buttonCancelEleven = findViewById(R.id.buttonCancelEleven)
        buttonCancelTwelve = findViewById(R.id.buttonCancelTwelve)
        buttonCancelThirteen = findViewById(R.id.buttonCancelThirteen)
        buttonCancelFourteen = findViewById(R.id.buttonCancelFourteen)
        buttonCancelFifteen = findViewById(R.id.buttonCancelFifteen)
        textViewProgressOne = findViewById(R.id.textViewProgressOne)
        textViewProgressTwo = findViewById(R.id.textViewProgressTwo)
        textViewProgressThree = findViewById(R.id.textViewProgressThree)
        textViewProgressFour = findViewById(R.id.textViewProgressFour)
        textViewProgressFive = findViewById(R.id.textViewProgressFive)
        textViewProgressSix = findViewById(R.id.textViewProgressSix)
        textViewProgressSeven = findViewById(R.id.textViewProgressSeven)
        textViewProgressEight = findViewById(R.id.textViewProgressEight)
        textViewProgressNine = findViewById(R.id.textViewProgressNine)
        textViewProgressTen = findViewById(R.id.textViewProgressTen)
        textViewProgressEleven = findViewById(R.id.textViewProgressEleven)
        textViewProgressTwelve = findViewById(R.id.textViewProgressTwelve)
        textViewProgressThirteen = findViewById(R.id.textViewProgressThirteen)
        textViewProgressFourteen = findViewById(R.id.textViewProgressFourteen)
        textViewProgressFifteen = findViewById(R.id.textViewProgressFifteen)
        progressBarOne = findViewById(R.id.progressBarOne)
        progressBarTwo = findViewById(R.id.progressBarTwo)
        progressBarThree = findViewById(R.id.progressBarThree)
        progressBarFour = findViewById(R.id.progressBarFour)
        progressBarFive = findViewById(R.id.progressBarFive)
        progressBarSix = findViewById(R.id.progressBarSix)
        progressBarSeven = findViewById(R.id.progressBarSeven)
        progressBarEight = findViewById(R.id.progressBarEight)
        progressBarNine = findViewById(R.id.progressBarNine)
        progressBarTen = findViewById(R.id.progressBarTen)
        progressBarEleven = findViewById(R.id.progressBarEleven)
        progressBarTwelve = findViewById(R.id.progressBarTwelve)
        progressBarThirteen = findViewById(R.id.progressBarThirteen)
        progressBarFourteen = findViewById(R.id.progressBarFourteen)
        progressBarFifteen = findViewById(R.id.progressBarFifteen)
    }
    fun onClickListenerOne() {
        buttonOne.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdOne)) {
                    PRDownloader.pause(downloadIdOne)
                    return
                }
                buttonOne.isEnabled = false
                progressBarOne.isIndeterminate = true
                progressBarOne.indeterminateDrawable?.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdOne)) {
                    PRDownloader.resume(downloadIdOne)
                    return
                }
                downloadIdOne = PRDownloader.download(URL1, dirPath, "facebook.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override  fun onStartOrResume() {
                            progressBarOne.isIndeterminate = false
                            buttonOne.isEnabled = true
                            buttonOne.setText(R.string.pause)
                            buttonCancelOne.isEnabled = true
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override  fun onPause() {
                            buttonOne.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override  fun onCancel() {
                            buttonOne.setText(R.string.start)
                            buttonCancelOne.isEnabled = false
                            progressBarOne.progress = 0
                            textViewProgressOne.text = ""
                            downloadIdOne = 0
                            progressBarOne.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarOne.progress = progressPercent.toInt()
                            textViewProgressOne.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                            progressBarOne.isIndeterminate = false
                        }
                    })
                    .start(object:OnDownloadListener {
                        override  fun onDownloadComplete() {
                            buttonOne.isEnabled = false
                            buttonCancelOne.isEnabled = false
                            buttonOne.setText(R.string.completed)
                        }

                        override  fun onError(error:Error) {
                            buttonOne.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "1", Toast.LENGTH_SHORT).show()
                            textViewProgressOne.text = ""
                            progressBarOne.progress = 0
                            downloadIdOne = 0
                            buttonCancelOne.isEnabled = false
                            progressBarOne.isIndeterminate = false
                            buttonOne.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelOne.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                PRDownloader.cancel(downloadIdOne)
            }
        })
    }
    fun onClickListenerTwo() {
        buttonTwo.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdTwo)) {
                    PRDownloader.pause(downloadIdTwo)
                    return
                }
                buttonTwo.isEnabled = false
                progressBarTwo.isIndeterminate = true
                progressBarTwo.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdTwo)) {
                    PRDownloader.resume(downloadIdTwo)
                    return
                }
                downloadIdTwo = PRDownloader.download(URL2, dirPath, "wechat.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override  fun onStartOrResume() {
                            progressBarTwo.isIndeterminate = false
                            buttonTwo.isEnabled = true
                            buttonTwo.setText(R.string.pause)
                            buttonCancelTwo.isEnabled = true
                            buttonCancelTwo.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override  fun onPause() {
                            buttonTwo.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override  fun onCancel() {
                            downloadIdTwo = 0
                            buttonTwo.setText(R.string.start)
                            buttonCancelTwo.isEnabled = false
                            progressBarTwo.progress = 0
                            textViewProgressTwo.text = ""
                            progressBarTwo.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarTwo.progress = progressPercent.toInt()
                            textViewProgressTwo.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override  fun onDownloadComplete() {
                            buttonTwo.isEnabled = false
                            buttonCancelTwo.isEnabled = false
                            buttonTwo.setText(R.string.completed)
                        }

                        override  fun onError(error:Error) {
                            buttonTwo.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "2", Toast.LENGTH_SHORT).show()
                            textViewProgressTwo.text = ""
                            progressBarTwo.progress = 0
                            downloadIdTwo = 0
                            buttonCancelTwo.isEnabled = false
                            progressBarTwo.isIndeterminate = false
                            buttonTwo.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelTwo.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                PRDownloader.cancel(downloadIdTwo)
            }
        })
    }
    fun onClickListenerThree() {
        buttonThree.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdThree)) {
                    PRDownloader.pause(downloadIdThree)
                    return
                }
                buttonThree.isEnabled = false
                progressBarThree.isIndeterminate = true
                progressBarThree.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdThree)) {
                    PRDownloader.resume(downloadIdThree)
                    return
                }
                downloadIdThree = PRDownloader.download(URL3, dirPath, "instagram.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override  fun onStartOrResume() {
                            progressBarThree.isIndeterminate = false
                            buttonThree.isEnabled = true
                            buttonThree.setText(R.string.pause)
                            buttonCancelThree.isEnabled = true
                            buttonCancelThree.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override  fun onPause() {
                            buttonThree.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override  fun onCancel() {
                            downloadIdThree = 0
                            buttonThree.setText(R.string.start)
                            buttonCancelThree.isEnabled = false
                            progressBarThree.progress = 0
                            textViewProgressThree.text = ""
                            progressBarThree.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarThree.progress = progressPercent.toInt()
                            textViewProgressThree.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonThree.isEnabled = false
                            buttonCancelThree.isEnabled = false
                            buttonThree.setText(R.string.completed)
                        }

                        override  fun onError(error:Error) {
                            buttonThree.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "3", Toast.LENGTH_SHORT).show()
                            textViewProgressThree.text = ""
                            progressBarThree.progress = 0
                            downloadIdThree = 0
                            buttonCancelThree.isEnabled = false
                            progressBarThree.isIndeterminate = false
                            buttonThree.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelThree.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                PRDownloader.cancel(downloadIdThree)
            }
        })
    }
    fun onClickListenerFour() {
        buttonFour.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdFour))
                {
                    PRDownloader.pause(downloadIdFour)
                    return
                }
                buttonFour.isEnabled = false
                progressBarFour.isIndeterminate = true
                progressBarFour.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdFour))
                {
                    PRDownloader.resume(downloadIdFour)
                    return
                }
                downloadIdFour = PRDownloader.download(URL4, dirPath, "flashlight.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override  fun onStartOrResume() {
                            progressBarFour.isIndeterminate = false
                            buttonFour.isEnabled = true
                            buttonFour.setText(R.string.pause)
                            buttonCancelFour.isEnabled = true
                            buttonCancelFour.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override  fun onPause() {
                            buttonFour.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override  fun onCancel() {
                            downloadIdFour = 0
                            buttonFour.setText(R.string.start)
                            buttonCancelFour.isEnabled = false
                            progressBarFour.progress = 0
                            textViewProgressFour.text = ""
                            progressBarFour.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarFour.progress = progressPercent.toInt()
                            textViewProgressFour.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override  fun onDownloadComplete() {
                            buttonFour.isEnabled = false
                            buttonCancelFour.isEnabled = false
                            buttonFour.setText(R.string.completed)
                        }
                        override  fun onError(error:Error) {
                            buttonFour.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "4", Toast.LENGTH_SHORT).show()
                            textViewProgressFour.text = ""
                            progressBarFour.progress = 0
                            downloadIdFour = 0
                            buttonCancelFour.isEnabled = false
                            progressBarFour.isIndeterminate = false
                            buttonFour.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelFour.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                PRDownloader.cancel(downloadIdFour)
            }
        })
    }
    fun onClickListenerFive() {
        buttonFive.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdFive))
                {
                    PRDownloader.pause(downloadIdFive)
                    return
                }
                buttonFive.isEnabled = false
                progressBarFive.isIndeterminate = true
                progressBarFive.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdFive))
                {
                    PRDownloader.resume(downloadIdFive)
                    return
                }
                downloadIdFive = PRDownloader.download(URL5, dirPath, "screenrecorder.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override  fun onStartOrResume() {
                            progressBarFive.isIndeterminate = false
                            buttonFive.isEnabled = true
                            buttonFive.setText(R.string.pause)
                            buttonCancelFive.isEnabled = true
                            buttonCancelFive.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override  fun onPause() {
                            buttonFive.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override  fun onCancel() {
                            downloadIdFive = 0
                            buttonFive.setText(R.string.start)
                            buttonCancelFive.isEnabled = false
                            progressBarFive.progress = 0
                            textViewProgressFive.text = ""
                            progressBarFive.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarFive.progress = progressPercent.toInt()
                            textViewProgressFive.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonFive.isEnabled = false
                            buttonCancelFive.isEnabled = false
                            buttonFive.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonFive.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "5", Toast.LENGTH_SHORT).show()
                            textViewProgressFive.text = ""
                            progressBarFive.progress = 0
                            downloadIdFive = 0
                            buttonCancelFive.isEnabled = false
                            progressBarFive.isIndeterminate = false
                            buttonFive.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelFive.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdFive)
            }
        })
    }
    fun onClickListenerSix() {
        buttonSix.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdSix))
                {
                    PRDownloader.pause(downloadIdSix)
                    return
                }
                buttonSix.isEnabled = false
                progressBarSix.isIndeterminate = true
                progressBarSix.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdSix))
                {
                    PRDownloader.resume(downloadIdSix)
                    return
                }
                downloadIdSix = PRDownloader.download(URL6, dirPath, "callrecorder.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarSix.isIndeterminate = false
                            buttonSix.isEnabled = true
                            buttonSix.setText(R.string.pause)
                            buttonCancelSix.isEnabled = true
                            buttonCancelSix.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonSix.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdSix = 0
                            buttonSix.setText(R.string.start)
                            buttonCancelSix.isEnabled = false
                            progressBarSix.progress = 0
                            textViewProgressSix.text = ""
                            progressBarSix.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarSix.progress = progressPercent.toInt()
                            textViewProgressSix.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonSix.isEnabled = false
                            buttonCancelSix.isEnabled = false
                            buttonSix.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonSix.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "6", Toast.LENGTH_SHORT).show()
                            textViewProgressSix.text = ""
                            progressBarSix.progress = 0
                            downloadIdSix = 0
                            buttonCancelSix.isEnabled = false
                            progressBarSix.isIndeterminate = false
                            buttonSix.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelSix.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdSix)
            }
        })
    }
    fun onClickListenerSeven() {
        buttonSeven.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdSeven))
                {
                    PRDownloader.pause(downloadIdSeven)
                    return
                }
                buttonSeven.isEnabled = false
                progressBarSeven.isIndeterminate = true
                progressBarSeven.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdSeven))
                {
                    PRDownloader.resume(downloadIdSeven)
                    return
                }
                downloadIdSeven = PRDownloader.download(URL7, dirPath, "soundprofile.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarSeven.isIndeterminate = false
                            buttonSeven.isEnabled = true
                            buttonSeven.setText(R.string.pause)
                            buttonCancelSeven.isEnabled = true
                            buttonCancelSeven.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonSeven.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdSeven = 0
                            buttonSeven.setText(R.string.start)
                            buttonCancelSeven.isEnabled = false
                            progressBarSeven.progress = 0
                            textViewProgressSeven.text = ""
                            progressBarSeven.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarSeven.progress = progressPercent.toInt()
                            textViewProgressSeven.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonSeven.isEnabled = false
                            buttonCancelSeven.isEnabled = false
                            buttonSeven.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonSeven.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "7", Toast.LENGTH_SHORT).show()
                            textViewProgressSeven.text = ""
                            progressBarSeven.progress = 0
                            downloadIdSeven = 0
                            buttonCancelSeven.isEnabled = false
                            progressBarSeven.isIndeterminate = false
                            buttonSeven.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelSeven.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdSeven)
            }
        })
    }
    fun onClickListenerEight() {
        buttonEight.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdEight))
                {
                    PRDownloader.pause(downloadIdEight)
                    return
                }
                buttonEight.isEnabled = false
                progressBarEight.isIndeterminate = true
                progressBarEight.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdEight))
                {
                    PRDownloader.resume(downloadIdEight)
                    return
                }
                downloadIdEight = PRDownloader.download(URL8, dirPath, "evernote.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarEight.isIndeterminate = false
                            buttonEight.isEnabled = true
                            buttonEight.setText(R.string.pause)
                            buttonCancelEight.isEnabled = true
                            buttonCancelEight.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonEight.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdEight = 0
                            buttonEight.setText(R.string.start)
                            buttonCancelEight.isEnabled = false
                            progressBarEight.progress = 0
                            textViewProgressEight.text = ""
                            progressBarEight.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarEight.progress = progressPercent.toInt()
                            textViewProgressEight.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonEight.isEnabled = false
                            buttonCancelEight.isEnabled = false
                            buttonEight.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonEight.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "8", Toast.LENGTH_SHORT).show()
                            textViewProgressEight.text = ""
                            progressBarEight.progress = 0
                            downloadIdEight = 0
                            buttonCancelEight.isEnabled = false
                            progressBarEight.isIndeterminate = false
                            buttonEight.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelEight.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdEight)
            }
        })
    }
    fun onClickListenerNine() {
        buttonNine.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdNine))
                {
                    PRDownloader.pause(downloadIdNine)
                    return
                }
                buttonNine.isEnabled = false
                progressBarNine.isIndeterminate = true
                progressBarNine.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdNine))
                {
                    PRDownloader.resume(downloadIdNine)
                    return
                }
                downloadIdNine = PRDownloader.download(URL9, dirPath, "ucbrowser.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarNine.isIndeterminate = false
                            buttonNine.isEnabled = true
                            buttonNine.setText(R.string.pause)
                            buttonCancelNine.isEnabled = true
                            buttonCancelNine.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonNine.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdNine = 0
                            buttonNine.setText(R.string.start)
                            buttonCancelNine.isEnabled = false
                            progressBarNine.progress = 0
                            textViewProgressNine.text = ""
                            progressBarNine.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarNine.progress = progressPercent.toInt()
                            textViewProgressNine.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonNine.isEnabled = false
                            buttonCancelNine.isEnabled = false
                            buttonNine.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonNine.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "9", Toast.LENGTH_SHORT).show()
                            textViewProgressNine.text = ""
                            progressBarNine.progress = 0
                            downloadIdNine = 0
                            buttonCancelNine.isEnabled = false
                            progressBarNine.isIndeterminate = false
                            buttonNine.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelNine.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdNine)
            }
        })
    }
    fun onClickListenerTen() {
        buttonTen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdTen))
                {
                    PRDownloader.pause(downloadIdTen)
                    return
                }
                buttonTen.isEnabled = false
                progressBarTen.isIndeterminate = true
                progressBarTen.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdTen))
                {
                    PRDownloader.resume(downloadIdTen)
                    return
                }
                downloadIdTen = PRDownloader.download(URL10, dirPath, "barcodescanner.apk")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarTen.isIndeterminate = false
                            buttonTen.isEnabled = true
                            buttonTen.setText(R.string.pause)
                            buttonCancelTen.isEnabled = true
                            buttonCancelTen.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonTen.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdTen = 0
                            buttonTen.setText(R.string.start)
                            buttonCancelTen.isEnabled = false
                            progressBarTen.progress = 0
                            textViewProgressTen.text = ""
                            progressBarTen.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarTen.progress = progressPercent.toInt()
                            textViewProgressTen.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonTen.isEnabled = false
                            buttonCancelTen.isEnabled = false
                            buttonTen.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonTen.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "10", Toast.LENGTH_SHORT).show()
                            textViewProgressTen.text = ""
                            progressBarTen.progress = 0
                            downloadIdTen = 0
                            buttonCancelTen.isEnabled = false
                            progressBarTen.isIndeterminate = false
                            buttonTen.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelTen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdTen)
            }
        })
    }
    fun onClickListenerEleven() {
        buttonEleven.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdEleven))
                {
                    PRDownloader.pause(downloadIdEleven)
                    return
                }
                buttonEleven.isEnabled = false
                progressBarEleven.isIndeterminate = true
                progressBarEleven.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdEleven))
                {
                    PRDownloader.resume(downloadIdEleven)
                    return
                }
                downloadIdEleven = PRDownloader.download(URL11, dirPath, "BigBuckBunny.m4v")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarEleven.isIndeterminate = false
                            buttonEleven.isEnabled = true
                            buttonEleven.setText(R.string.pause)
                            buttonCancelEleven.isEnabled = true
                            buttonCancelEleven.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonEleven.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdEleven = 0
                            buttonEleven.setText(R.string.start)
                            buttonCancelEleven.isEnabled = false
                            progressBarEleven.progress = 0
                            textViewProgressEleven.text = ""
                            progressBarEleven.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarEleven.progress = progressPercent.toInt()
                            textViewProgressEleven.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonEleven.isEnabled = false
                            buttonCancelEleven.isEnabled = false
                            buttonEleven.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonEleven.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "11", Toast.LENGTH_SHORT).show()
                            textViewProgressEleven.text = ""
                            progressBarEleven.progress = 0
                            downloadIdEleven = 0
                            buttonCancelEleven.isEnabled = false
                            progressBarEleven.isIndeterminate = false
                            buttonEleven.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelEleven.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdEleven)
            }
        })
    }
    fun onClickListenerTwelve() {
        buttonTwelve.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdTwelve))
                {
                    PRDownloader.pause(downloadIdTwelve)
                    return
                }
                buttonTwelve.isEnabled = false
                progressBarTwelve.isIndeterminate = true
                progressBarTwelve.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdTwelve))
                {
                    PRDownloader.resume(downloadIdTwelve)
                    return
                }
                downloadIdTwelve = PRDownloader.download(URL12, dirPath, "harry-porter.pdf")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarTwelve.isIndeterminate = false
                            buttonTwelve.isEnabled = true
                            buttonTwelve.setText(R.string.pause)
                            buttonCancelTwelve.isEnabled = true
                            buttonCancelTwelve.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonTwelve.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdTwelve = 0
                            buttonTwelve.setText(R.string.start)
                            buttonCancelTwelve.isEnabled = false
                            progressBarTwelve.progress = 0
                            textViewProgressTwelve.text = ""
                            progressBarTwelve.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarTwelve.progress = progressPercent.toInt()
                            textViewProgressTwelve.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonTwelve.isEnabled = false
                            buttonCancelTwelve.isEnabled = false
                            buttonTwelve.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonTwelve.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "12", Toast.LENGTH_SHORT).show()
                            textViewProgressTwelve.text = ""
                            progressBarTwelve.progress = 0
                            downloadIdTwelve = 0
                            buttonCancelTwelve.isEnabled = false
                            progressBarTwelve.isIndeterminate = false
                            buttonTwelve.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelTwelve.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdTwelve)
            }
        })
    }
    fun onClickListenerThirteen() {
        buttonThirteen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdThirteen))
                {
                    PRDownloader.pause(downloadIdThirteen)
                    return
                }
                buttonThirteen.isEnabled = false
                progressBarThirteen.isIndeterminate = true
                progressBarThirteen.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdThirteen))
                {
                    PRDownloader.resume(downloadIdThirteen)
                    return
                }
                downloadIdThirteen = PRDownloader.download(URL13, dirPath, "giphy.gif")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarThirteen.isIndeterminate = false
                            buttonThirteen.isEnabled = true
                            buttonThirteen.setText(R.string.pause)
                            buttonCancelThirteen.isEnabled = true
                            buttonCancelThirteen.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonThirteen.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdThirteen = 0
                            buttonThirteen.setText(R.string.start)
                            buttonCancelThirteen.isEnabled = false
                            progressBarThirteen.progress = 0
                            textViewProgressThirteen.text = ""
                            progressBarThirteen.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarThirteen.progress = progressPercent.toInt()
                            textViewProgressThirteen.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonThirteen.isEnabled = false
                            buttonCancelThirteen.isEnabled = false
                            buttonThirteen.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonThirteen.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "13", Toast.LENGTH_SHORT).show()
                            textViewProgressThirteen.text = ""
                            progressBarThirteen.progress = 0
                            downloadIdThirteen = 0
                            buttonCancelThirteen.isEnabled = false
                            progressBarThirteen.isIndeterminate = false
                            buttonThirteen.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelThirteen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdThirteen)
            }
        })
    }
    fun onClickListenerFourteen() {
        buttonFourteen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdFourteen))
                {
                    PRDownloader.pause(downloadIdFourteen)
                    return
                }
                buttonFourteen.isEnabled = false
                progressBarFourteen.isIndeterminate = true
                progressBarFourteen.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdFourteen))
                {
                    PRDownloader.resume(downloadIdFourteen)
                    return
                }
                downloadIdFourteen = PRDownloader.download(URL14, dirPath, "small.mp4")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarFourteen.isIndeterminate = false
                            buttonFourteen.isEnabled = true
                            buttonFourteen.setText(R.string.pause)
                            buttonCancelFourteen.isEnabled = true
                            buttonCancelFourteen.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonFourteen.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdFourteen = 0
                            buttonFourteen.setText(R.string.start)
                            buttonCancelFourteen.isEnabled = false
                            progressBarFourteen.progress = 0
                            textViewProgressFourteen.text = ""
                            progressBarFourteen.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarFourteen.progress = progressPercent.toInt()
                            textViewProgressFourteen.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonFourteen.isEnabled = false
                            buttonCancelFourteen.isEnabled = false
                            buttonFourteen.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonFourteen.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "14", Toast.LENGTH_SHORT).show()
                            textViewProgressFourteen.text = ""
                            progressBarFourteen.progress = 0
                            downloadIdFourteen = 0
                            buttonCancelFourteen.isEnabled = false
                            progressBarFourteen.isIndeterminate = false
                            buttonFourteen.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelFourteen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                PRDownloader.cancel(downloadIdFourteen)
            }
        })
    }
    fun onClickListenerFifteen() {
        buttonFifteen.setOnClickListener(object:View.OnClickListener {
            override fun onClick(view:View) {
                if (Status.RUNNING === PRDownloader.getStatus(downloadIdFifteen))
                {
                    PRDownloader.pause(downloadIdFifteen)
                    return
                }
                buttonFifteen.isEnabled = false
                progressBarFifteen.isIndeterminate = true
                progressBarFifteen.indeterminateDrawable.setColorFilter(
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)
                if (Status.PAUSED === PRDownloader.getStatus(downloadIdFifteen))
                {
                    PRDownloader.resume(downloadIdFifteen)
                    return
                }
                downloadIdFifteen = PRDownloader.download(URL15, dirPath, "big_buck_bunny_720p_10mb.mp4")
                    .build()
                    .setOnStartOrResumeListener(object:OnStartOrResumeListener {
                        override fun onStartOrResume() {
                            progressBarFifteen.isIndeterminate = false
                            buttonFifteen.isEnabled = true
                            buttonFifteen.setText(R.string.pause)
                            buttonCancelFifteen.isEnabled = true
                            buttonCancelFifteen.setText(R.string.cancel)
                        }
                    })
                    .setOnPauseListener(object:OnPauseListener {
                        override fun onPause() {
                            buttonFifteen.setText(R.string.resume)
                        }
                    })
                    .setOnCancelListener(object:OnCancelListener {
                        override fun onCancel() {
                            downloadIdFifteen = 0
                            buttonFifteen.setText(R.string.start)
                            buttonCancelFifteen.isEnabled = false
                            progressBarFifteen.progress = 0
                            textViewProgressFifteen.text = ""
                            progressBarFifteen.isIndeterminate = false
                        }
                    })
                    .setOnProgressListener(object:OnProgressListener {
                        override fun onProgress(progress:Progress) {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarFifteen.progress = progressPercent.toInt()
                            textViewProgressFifteen.text =
                                getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    })
                    .start(object:OnDownloadListener {
                        override fun onDownloadComplete() {
                            buttonFifteen.isEnabled = false
                            buttonCancelFifteen.isEnabled = false
                            buttonFifteen.setText(R.string.completed)
                        }
                        override fun onError(error:Error) {
                            buttonFifteen.setText(R.string.start)
                            Toast.makeText(applicationContext, getString(R.string.some_error_occurred) + " " + "15", Toast.LENGTH_SHORT).show()
                            textViewProgressFifteen.text = ""
                            progressBarFifteen.progress = 0
                            downloadIdFifteen = 0
                            buttonCancelFifteen.isEnabled = false
                            progressBarFifteen.isIndeterminate = false
                            buttonFifteen.isEnabled = true
                        }
                    })
            }
        })
        buttonCancelFifteen.setOnClickListener(object:View.OnClickListener {
            override  fun onClick(view:View) {
                PRDownloader.cancel(downloadIdFifteen)
            }
        })
    }


    companion object {
        private var dirPath:String = ""
    }


    fun getRootDirPath(context: Context): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(context.applicationContext, null)[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    fun getProgressDisplayLine(currentBytes: Long,totalBytes: Long): String? {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes)
    }

    private fun getBytesToMBString(bytes: Long): String {
        return java.lang.String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00))
    }
}


