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

    object Device {

        val ALL = "All"
        val ACER_BLACK = "T02"
        val OPPO_BLACK = "1201"
        val HTC_RED = "HTC One_M8"
        val ASUS_BLACK = "ASUS_X013D"
        val SAMSUNG_WHITE = "SM-J710GN"
        val SAMSUNG_BLACK = "Galaxy S8"
        val OPPO_WHITE = "A1601"
        val ACER_WHITE = "Liquid Zest Plus"

    }

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