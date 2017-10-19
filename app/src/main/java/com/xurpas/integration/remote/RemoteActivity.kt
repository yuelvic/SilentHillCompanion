package com.xurpas.integration.remote

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import com.xurpas.integration.BaseActivity
import com.xurpas.integration.R
import com.xurpas.integration.models.Message
import com.xurpas.integration.ui.adapters.SceneAdapter
import com.xurpas.integration.utils.Action
import com.xurpas.integration.utils.StepProvider

/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
class RemoteActivity: BaseActivity(), CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var deviceAdapter: ArrayAdapter<CharSequence>
    private var selectedDevice: String = ""

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
        sendMessage(selectedDevice, action)
    }

    override fun onClick(view: View) {
        var action = ""
        when (view.id) {
            R.id.btn_auto -> {
                dryRun() // run test
                return
            }
            R.id.btn_stop -> action = Action.SFX_STOP.action
            R.id.btn_theme -> action = Action.SFX_THEME.action
            R.id.btn_bang -> action = Action.SFX_BANG.action
            R.id.btn_pyramid -> action = Action.SFX_PYRAMID.action
            R.id.btn_pov -> action = Action.SFX_PYRAMID_POV.action
            R.id.btn_nurse -> action = Action.SFX_NURSE.action
            R.id.btn_alessa -> action = Action.SFX_ALESSA.action
            R.id.btn_siren -> action = Action.SFX_SIREN.action
            R.id.btn_insect -> action = Action.SFX_INSECT.action
            R.id.btn_hospital -> action = Action.SFX_HOSPITAL.action
            R.id.btn_other -> action = Action.SFX_OTHER_WORLD.action
        }
        sendMessage(selectedDevice, action)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        super.onKeyDown(keyCode, event)

        val action = if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            Action.VOLUME_UP.action else Action.VOLUME_DOWN.action
        sendMessage(selectedDevice, action)
        return true
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(adapter: AdapterView<*>, spinner: View?, position: Int, id: Long) {
        selectedDevice = adapter.getItemAtPosition(position).toString()
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
        findViewById<Button>(R.id.btn_pov).setOnClickListener(this)
        findViewById<Button>(R.id.btn_nurse).setOnClickListener(this)
        findViewById<Button>(R.id.btn_alessa).setOnClickListener(this)
        findViewById<Button>(R.id.btn_siren).setOnClickListener(this)
        findViewById<Button>(R.id.btn_insect).setOnClickListener(this)
        findViewById<Button>(R.id.btn_hospital).setOnClickListener(this)
        findViewById<Button>(R.id.btn_other).setOnClickListener(this)
        findViewById<Button>(R.id.btn_auto).setOnClickListener(this)

        // Device specific command
        val spinner: Spinner = findViewById(R.id.spinner_device)
        deviceAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_devices, android.R.layout.simple_spinner_item
        )
        deviceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = deviceAdapter
        spinner.onItemSelectedListener = this

        // Step guide
        val recyclerView: RecyclerView = findViewById(R.id.rv_steps)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerView.adapter = SceneAdapter(this, StepProvider.provide())
        recyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect.set(1,1,1,1)
            }
        })
    }

    /**
     * Run auto script
     */
    private fun dryRun() {
        Log.d(TAG, "auto started")
        var step = 0
        var time = 0
        Thread(Runnable {
            while (time < StepProvider.STOP + 1000) {
                try {
                    Thread.sleep(1000)
                } catch (ex: InterruptedException) { ex.printStackTrace() }

                time += 1000 // inc one second

                when (time) {
                    StepProvider.HOSPITAL -> sendMessage(deviceAdapter.getItem(++step).toString(), Action.SFX_HOSPITAL.action)
                    StepProvider.SIREN -> sendMessage(deviceAdapter.getItem(++step).toString(), Action.SFX_SIREN.action)
                    StepProvider.OTHER_WORLD -> sendMessage(deviceAdapter.getItem(++step).toString(), Action.SFX_OTHER_WORLD.action)
                    StepProvider.PYRAMID -> sendMessage(deviceAdapter.getItem(++step).toString(), Action.SFX_PYRAMID.action)
                    StepProvider.POV -> sendMessage(deviceAdapter.getItem(++step).toString(), Action.SFX_PYRAMID_POV.action)
                    StepProvider.STOP -> sendMessage("All", Action.SFX_STOP.action)
                }
            }
        }).start()
    }

    /**
     * Send message to server
     */
    private fun sendMessage(device: String, action: String) {
        webSocket.send(gson.toJson(Message(device, action)))
    }

}