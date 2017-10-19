package com.xurpas.integration

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.xurpas.integration.actor.ActorActivity
import com.xurpas.integration.remote.FixedActivity
import com.xurpas.integration.remote.RemoteActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_fixed).setOnClickListener {
            startActivity(Intent(this, FixedActivity::class.java))
        }

        findViewById<Button>(R.id.btn_remote).setOnClickListener {
            startActivity(Intent(this, RemoteActivity::class.java))
        }

        findViewById<Button>(R.id.btn_actor).setOnClickListener {
            startActivity(Intent(this, ActorActivity::class.java))
        }
    }
}
