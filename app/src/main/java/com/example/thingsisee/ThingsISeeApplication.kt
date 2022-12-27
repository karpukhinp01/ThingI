package com.example.thingsisee

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors

class ThingsISeeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        Log.d("app", "applied")
    }
}