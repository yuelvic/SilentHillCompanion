package com.xurpas.integration

import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.xurpas.integration.utils.DeviceName
import com.xurpas.integration.utils.WebSocketManager

/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
open class BaseActivity: AppCompatActivity(), WebSocketManager.Callback {

    val TAG: String? = BaseActivity::class.java.simpleName

    lateinit var webSocket: WebSocketManager

    val gson = Gson() // json parser
    val deviceName = DeviceName.getDeviceName() // device name

    override fun onStart() {
        super.onStart()

        webSocket = WebSocketManager(gson, callback = this) // Our WebSocket manager
    }

    override fun onStop() {
        webSocket.close()
        super.onStop()
    }

    override fun onReceive(text: String) {

    }

}