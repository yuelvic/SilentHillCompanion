package com.xurpas.integration.utils

/**
 * Created by Emmanuel Victor Garcia on 10/14/17.
 */
object StepProvider {

    val HOSPITAL = 1000
    val SIREN = 20000
    val OTHER_WORLD = 40000
    val PYRAMID = 60000
    val POV = 70000
    val STOP = 90000

    private val steps = arrayOf(
            "Liquid Zest Plus \t Hospital",
            "T02 \t Siren",
            "1201 \t Other",
            "Asus_X013D \t Pyramid",
            "HTC One_M8 \t POV"
    )

    fun provide(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        list += steps

        return list
    }

}