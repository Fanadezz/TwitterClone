package com.androidshowtime.twitterclone

import android.app.Application
import com.parse.Parse
import com.parse.ParseACL
import timber.log.Timber

class ParseInitializer : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Parse.enableLocalDatastore(this)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                    .applicationId("myappID")
                    .clientKey("jwXZwyd2Odbg") //jwXZwyd2Odbg
                    .server("http://3.133.91.42/parse/")
                    .build()
                        )


        val defaultACL = ParseACL()
        defaultACL.publicWriteAccess = true
        defaultACL.publicReadAccess = true
        ParseACL.setDefaultACL(defaultACL, true)
    }

}