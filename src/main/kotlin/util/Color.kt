package com.ebicep.warlordsplusplus.util

import java.awt.Color

fun Color.convertToArgb(): Int {
    return (this.alpha shl 24) or
            (this.red shl 16) or
            (this.green shl 8) or
            (this.blue)
}

enum class Colors(val red: Int, val green: Int, val blue: Int) {
    DEF(34, 34, 39),
    BRONZE(144, 89, 35),
    SILVER(148, 152, 161),
    PLATINUM(229, 228, 226),
    DIAMOND(185, 242, 255),
    MASTER(255, 92, 51),
    GRANDMASTER(143, 0, 0),

    BLACK(0, 0, 0),
    DARK_BLUE(0, 0, 170),
    DARK_GREEN(0, 170, 0),
    DARK_AQUA(0, 170, 170),
    DARK_RED(170, 0, 0),
    DARK_PURPLE(170, 0, 170),
    DARK_GRAY(85, 85, 85),
    GOLD(255, 170, 0),
    GRAY(170, 170, 170),
    BLUE(85, 85, 255),
    GREEN(85, 255, 85),
    AQUA(85, 255, 255),
    RED(255, 85, 85),
    LIGHT_PURPLE(255, 85, 255),
    YELLOW(255, 255, 85),
    WHITE(255, 255, 255),
    ORANGE(255, 140, 0);

    val FULL: Int = Color(red, green, blue, 255).convertToArgb()
    val ALPHA_100: Int = Color(red, green, blue, 100).convertToArgb()
    val ALPHA_200: Int = Color(red, green, blue, 200).convertToArgb()

    fun convertToArgb(alpha: Int = 255): Int {
        return (alpha shl 24) or
                (this.red shl 16) or
                (this.green shl 8) or
                (this.blue)
    }
}

