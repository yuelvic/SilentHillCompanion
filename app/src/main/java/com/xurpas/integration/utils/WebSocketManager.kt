package com.xurpas.integration.utils

import android.util.Log
import com.google.gson.Gson
import com.xurpas.integration.models.Message
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit


/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
class WebSocketManager(gson: Gson, callback: Callback): WebSocketListener() {

    private val TAG: String = WebSocketManager::class.java.simpleName

    private var callback: Callback
    private var webSocket: WebSocket
    private var gson: Gson

    init {
        val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build()

        val request = Request.Builder()
                .url("ws://192.168.2.193:8000")
                .build()
        webSocket = client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()

        this.gson = gson
        this.callback = callback
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        super.onMessage(webSocket, bytes)
    }

    override fun onMessage(webSocket: WebSocket?, text: String) {
        Log.d(TAG, text)
        callback.onReceive(text)
        super.onMessage(webSocket, text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response?) {
        super.onOpen(webSocket, response)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String?) {
        super.onClosed(webSocket, code, reason)
        Log.d(TAG, reason)
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, reason)
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.e(TAG, t.toString())
    }

    /**
     * Send action as String
     */
    fun send(message: String) {
        webSocket.send(message)
    }

    /**
     * Send action as Message
     */
    fun send(message: Message) {
        webSocket.send(gson.toJson(message))
    }

    /**
     * Closes WebSocket
     */
    fun close() {
        webSocket.close(1000, "disconnect")
    }

    /**
     * WebSocket Callback
     */
    interface Callback {
        fun onReceive(text: String)
    }

}