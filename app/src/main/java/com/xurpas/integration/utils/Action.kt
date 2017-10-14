package com.xurpas.integration.utils

/**
 * Created by Emmanuel Victor Garcia on 10/6/17.
 */
enum class Action(val action: String) {

    BLINK_ON("blink_on"),
    BLINK_OFF("blink_off"),
    LIGHT_ON("light_on"),
    LIGHT_OFF("light_off"),
    CALL_ON("call_on"),
    CALL_OFF("call_off"),

    SFX_STOP("sfx_stop"),
    SFX_THEME("sfx_theme"),
    SFX_PYRAMID("sfx_pyramid"),
    SFX_PYRAMID_POV("sfx_pyramid_pov"),
    SFX_SIREN("sfx_siren"),
    SFX_INSECT("sfx_insect"),
    SFX_HOSPITAL("sfx_hospital"),
    SFX_OTHER_WORLD("sfx_other_world"),
    SFX_BANG("sfx_bang"),

    VOLUME_UP("volume_up"),
    VOLUME_DOWN("volume_down")

}