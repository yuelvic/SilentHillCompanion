package com.xurpas.integration.remote

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.xurpas.integration.BaseActivity
import com.xurpas.integration.R
import com.xurpas.integration.models.Message
import com.xurpas.integration.utils.Action

/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
class RemoteActivity: BaseActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote)

        registerActions() // register UI functions
    }

    override fun onCheckedChanged(view: CompoundButton, enabled: Boolean) {
        var action = ""
        when (view.id) {
            R.id.toggle_light -> action = if (enabled) Action.LIGHT_ON.action else Action.LIGHT_OFF.action
            R.id.toggle_light_blink -> action = if (enabled) Action.BLINK_ON.action else Action.BLINK_OFF.action
            R.id.toggle_call -> action = if (enabled) Action.CALL_ON.action else Action.CALL_OFF.action
        }
        webSocket.send(action)
    }

    override fun onClick(view: View) {
        var action = ""
        when (view.id) {
            R.id.btn_stop -> action = Action.SFX_STOP.action
            R.id.btn_theme -> action = Action.SFX_THEME.action
            R.id.btn_bang -> action = Action.SFX_BANG.action
            R.id.btn_pyramid -> action = Action.SFX_PYRAMID.action
            R.id.btn_siren -> action = Action.SFX_SIREN.action
        }
        webSocket.send(gson.toJson(Message(deviceName, action)))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        super.onKeyDown(keyCode, event)

        val action = if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            Action.VOLUME_UP.action else Action.VOLUME_DOWN.action
        webSocket.send(action)
        return true
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    /**
     * Registers UI functions
     */
    private fun registerActions() {
        // Toggles
        findViewById<ToggleButton>(R.id.toggle_light).setOnCheckedChangeListener(this)
        findViewById<ToggleButton>(R.id.toggle_light_blink).setOnCheckedChangeListener(this)
        findViewById<ToggleButton>(R.id.toggle_call).setOnCheckedChangeListener(this)

        // SFX
        findViewById<Button>(R.id.btn_stop).setOnClickListener(this)
        findViewById<Button>(R.id.btn_theme).setOnClickListener(this)
        findViewById<Button>(R.id.btn_bang).setOnClickListener(this)
        findViewById<Button>(R.id.btn_pyramid).setOnClickListener(this)
        findViewById<Button>(R.id.btn_siren).setOnClickListener(this)

        // Device specific command
        val spinner: Spinner = findViewById(R.id.spinner_device)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                this, R.array.array_devices, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

}