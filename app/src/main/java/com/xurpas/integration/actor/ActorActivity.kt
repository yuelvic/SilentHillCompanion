package com.xurpas.integration.actor

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.xurpas.integration.BaseActivity
import com.xurpas.integration.R
import com.xurpas.integration.models.Message
import com.xurpas.integration.ui.CallFragment
import com.xurpas.integration.utils.Action
import com.xurpas.integration.utils.Actor
import java.util.concurrent.ThreadLocalRandom


/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
class ActorActivity: BaseActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var torchCallback: TorchCallback

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager
    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = ""

    private var selectedAction: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor)

        init() // initialize components
        registerActions() // registers ui actions
    }

    override fun onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            cameraManager.unregisterTorchCallback(torchCallback)
        releasePlayer()
        super.onStop()
    }

    override fun onReceive(text: String) {
        val message: Message = gson.fromJson(text, Message::class.java) // parse command

        if (message.device != deviceName && message.device != "All") return

        when (message.action) {
            Action.CALL_ON.action -> call()
            Action.CALL_OFF.action -> destroyCall()
            Action.LIGHT_ON.action -> turnOnLight()
            Action.LIGHT_OFF.action -> turnOffLight()
            Action.BLINK_ON.action -> {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                    torchCallback.blink = true
            }
            Action.BLINK_OFF.action -> {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
                    torchCallback.blink = false
            }

            Action.SFX_STOP.action -> releasePlayer()
            Action.SFX_THEME.action -> playSFX(R.raw.theme, false)
            Action.SFX_BANG.action -> playSFX(R.raw.bang, false)
            Action.SFX_PYRAMID.action -> playSFX(R.raw.pyramid, true)
            Action.SFX_PYRAMID_POV.action -> playSFX(R.raw.pyramid_pov, true)
            Action.SFX_SIREN.action -> playSFX(R.raw.siren, true)
            Action.SFX_INSECT.action -> playSFX(R.raw.insect, false)
            Action.SFX_HOSPITAL.action -> playSFX(R.raw.hospital, true)
            Action.SFX_OTHER_WORLD.action -> playSFX(R.raw.otherworld, true)

            Action.VOLUME_UP.action -> volumeUp()
            Action.VOLUME_DOWN.action -> volumeDown()
        }
        super.onReceive(message.action)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>, p1: View, position: Int, id: Long) {
        Log.d(TAG, parent.getItemAtPosition(position).toString())
        selectedAction = position
    }

    /**
     * Initializes components
     */
    private fun init() {
        mediaPlayer = MediaPlayer.create(this, R.raw.siren) // siren sound

        audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager // audio manager

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager // camera manager
        cameraId = cameraManager.cameraIdList[0] // Usually front camera is at 0 position.

        // Register flashlight
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            torchCallback = TorchCallback(cameraManager) // torch callback
            cameraManager.registerTorchCallback(torchCallback, null) // register torch callback
        }
    }

    /**
     * Registers UI functions
     */
    private fun registerActions() {
        val spinner: Spinner = findViewById(R.id.spinner_actor)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                this, R.array.array_actors, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    /**
     * Initializes player
     */
    private fun playSound(resId: Int, looping: Boolean) {
        mediaPlayer = MediaPlayer.create(this, resId) // siren sound
        mediaPlayer.isLooping = looping
        mediaPlayer.start()
    }

    /**
     * Release player
     */
    private fun releasePlayer() {
        mediaPlayer.release()
    }

    /**
     * Play a certain sound
     */
    private fun playSFX(resId: Int, looping: Boolean) {
        if (selectedAction != Actor.SFX.actor && resId != R.raw.light) return

        releasePlayer()
        playSound(resId, looping)
    }

    /**
     * Volume up
     */
    private fun volumeUp() {
        adjustVolume(true)
    }

    /**
     * Volume down
     */
    private fun volumeDown() {
        adjustVolume(false)
    }

    /**
     * Adjust volume
     * @param volume true to increase, false to decrease
     */
    private fun adjustVolume(volume: Boolean) {
        val step = if (volume) AudioManager.ADJUST_RAISE else AudioManager.ADJUST_LOWER
        audioManager.adjustVolume(step, AudioManager.FLAG_PLAY_SOUND)
    }

    /**
     * Turn on light
     */
    private fun turnOnLight() {
        toggleLight(255, true)
        playSFX(R.raw.light, true)
    }

    /**
     * Turn off light
     */
    private fun turnOffLight() {
        toggleLight(0, false)
        releasePlayer()
    }

    /**
     * Toggles flash light
     *
     * @param value Brightness level
     * @param torchMode Whether to turn on flashlight or not
     */
    private fun toggleLight(value: Int, torchMode: Boolean) {
        if (selectedAction != Actor.FLASHLIGHT.actor) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            cameraManager.setTorchMode(cameraId, torchMode) // torch toggle

        try {
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Opens call screen
     */
    private fun call() {
        if (selectedAction != Actor.CALLEE.actor) return
        supportFragmentManager.beginTransaction()
                .add(R.id.content_actor, CallFragment(), CallFragment::class.java.simpleName)
                .addToBackStack(CallFragment::class.java.simpleName)
                .commit()
    }

    /**
     * Removes call screen
     */
    private fun destroyCall() {
        supportFragmentManager.popBackStack()
    }

    /**
     * Torch callback
     */
    class TorchCallback(private val cameraManager: CameraManager): CameraManager.TorchCallback() {

        var blink: Boolean = false // blink toggle

        override fun onTorchModeChanged(cameraId: String?, enabled: Boolean) {
            super.onTorchModeChanged(cameraId, enabled)
            Log.d("Torch mode", enabled.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && blink) {
                Thread(Runnable {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50, 1000).toLong())
                    cameraManager.setTorchMode(cameraId, !enabled)
                }).start()
            }
        }

    }

}