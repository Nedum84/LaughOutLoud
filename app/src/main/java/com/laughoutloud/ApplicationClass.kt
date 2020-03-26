package com.laughoutloud

import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig


class ApplicationClass : VolleySingleton() {

    override fun onCreate() {
        super.onCreate()

        //For PRDownloaderConfig
        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .build()
        PRDownloader.initialize(this, config)
    }
}