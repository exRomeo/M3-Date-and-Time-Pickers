package com.example.datetimepickers

import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen

class AndroidApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}