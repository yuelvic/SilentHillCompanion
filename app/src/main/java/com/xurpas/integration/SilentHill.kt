package com.xurpas.integration

import android.app.Application
import android.util.Log
import com.xurpas.integration.utils.DeviceName

/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
class SilentHill : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("Device name", DeviceName.getDeviceName())
    }

}