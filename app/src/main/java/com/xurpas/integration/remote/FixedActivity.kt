package com.xurpas.integration.remote

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.xurpas.integration.BaseActivity
import com.xurpas.integration.R
import com.xurpas.integration.models.Message
import com.xurpas.integration.utils.Action
import com.xurpas.integration.utils.StepProvider

/**
 * Created by Emmanuel Victor Garcia on 10/19/17.
 */
class FixedActivity: BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fixed)

        registerActions() // register UI functions
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_hospital -> triggerHospital()
            R.id.btn_siren -> triggerSiren()
            R.id.btn_pyramid -> triggerPyramid()
            R.id.btn_growl -> triggerGrowl()
            R.id.btn_other -> triggerOtherWorld()
            R.id.btn_insect -> triggerInsect()
            R.id.btn_nurse -> triggerNurse()
            R.id.btn_alessa -> triggerAlessa()
            R.id.btn_stop -> triggerStop()
        }
    }

    /**
     * Registers UI functions
     */
    private fun registerActions() {
        findViewById<Button>(R.id.btn_hospital).setOnClickListener(this)
        findViewById<Button>(R.id.btn_siren).setOnClickListener(this)
        findViewById<Button>(R.id.btn_pyramid).setOnClickListener(this)
        findViewById<Button>(R.id.btn_growl).setOnClickListener(this)
        findViewById<Button>(R.id.btn_other).setOnClickListener(this)
        findViewById<Button>(R.id.btn_insect).setOnClickListener(this)
        findViewById<Button>(R.id.btn_nurse).setOnClickListener(this)
        findViewById<Button>(R.id.btn_alessa).setOnClickListener(this)
        findViewById<Button>(R.id.btn_stop).setOnClickListener(this)
    }

    /**
     * Play hospital sound
     */
    private fun triggerHospital() {
        webSocket.send(Message(StepProvider.Device.ACER_BLACK, Action.SFX_HOSPITAL.action))
    }

    /**
     * Play siren sound
     */
    private fun triggerSiren() {
        webSocket.send(Message(StepProvider.Device.OPPO_BLACK, Action.SFX_SIREN.action))
    }

    /**
     * Play pyramid sound
     */
    private fun triggerPyramid() {
        webSocket.send(Message(StepProvider.Device.HTC_RED, Action.SFX_PYRAMID.action))
    }

    /**
     * Play growl sound
     */
    private fun triggerGrowl() {
        webSocket.send(Message(StepProvider.Device.ASUS_BLACK, Action.SFX_PYRAMID_POV.action))
    }

    /**
     * Play otherworld sound
     */
    private fun triggerOtherWorld() {
        triggerVolume(StepProvider.Device.HTC_RED, 20, false)
        triggerVolume(StepProvider.Device.ASUS_BLACK, 20, false)
        triggerVolume(StepProvider.Device.ACER_BLACK, 20, false)
        triggerVolume(StepProvider.Device.OPPO_BLACK, 20, false)
        webSocket.send(Message(StepProvider.Device.SAMSUNG_WHITE, Action.SFX_OTHER_WORLD.action))
    }

    /**
     * Play insect sound
     */
    private fun triggerInsect() {
        webSocket.send(Message(StepProvider.Device.SAMSUNG_BLACK, Action.SFX_INSECT.action))
    }

    /**
     * Play insect sound
     */
    private fun triggerNurse() {
        triggerVolume(StepProvider.Device.SAMSUNG_BLACK, 20, false)
        webSocket.send(Message(StepProvider.Device.OPPO_WHITE, Action.SFX_NURSE.action))
    }

    /**
     * Play insect sound
     */
    private fun triggerAlessa() {
        triggerStop()
        webSocket.send(Message(StepProvider.Device.ACER_WHITE, Action.SFX_ALESSA.action))
    }

    /**
     * Stop sound
     */
    private fun triggerStop() {
        webSocket.send(Message(StepProvider.Device.ALL, Action.SFX_STOP.action))
    }

    /**
     * Decrease volume
     */
    private fun triggerVolume(device: String, step: Int, increase: Boolean) {
        var count = step
        Thread( Runnable {
            do {
                val action = if (increase) Action.VOLUME_UP.action else Action.VOLUME_DOWN.action
                webSocket.send(Message(device, action))
                count--

                try {
                    Thread.sleep(2000)
                } catch (ex: InterruptedException) { ex.printStackTrace() }
            } while (count > 0)
        } ).start()
    }

}