package com.nguyen.nytimeskt

import android.app.Application

class MyApplication : Application() {
    val appComponent = DaggerAppComponent.create()
}