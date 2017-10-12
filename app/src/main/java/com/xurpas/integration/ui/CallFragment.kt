package com.xurpas.integration.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import at.markushi.ui.CircleButton
import com.xurpas.integration.R

/**
 * Created by Emmanuel Victor Garcia on 10/10/17.
 */
class CallFragment: Fragment() {

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_call, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init() // initialize components
        registerActions(view) // set ui actions
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()
    }

    /**
     * Initializes components
     */
    private fun init() {
        initPlayer(R.raw.ring)
    }

    /**
     * Registers UI functions
     */
    private fun registerActions(view: View) {
        view.findViewById<CircleButton>(R.id.btn_call_pick).setOnClickListener {
            releasePlayer()
            initPlayer(R.raw.jam)

            view.findViewById<ImageView>(R.id.iv_call_avatar) // Change to ghost avatar
                    .setImageResource(R.drawable.sharon)
        }

        view.findViewById<CircleButton>(R.id.btn_call_drop).setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
    }

    /**
     * Initializes player
     */
    private fun initPlayer(resId: Int) {
        mediaPlayer = MediaPlayer.create(activity, resId)
        mediaPlayer.start()
    }

    /**
     * Releases MediaPlayer
     */
    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

}