package com.rekkursion.kingnothing.factories

import android.graphics.Bitmap
import android.graphics.Color

object ImageProcessFactory {
    fun colorCover(origBitmap: Bitmap, colorInt: Int): Bitmap {
        val width: Int = origBitmap.width
        val height: Int = origBitmap.height
        val retBitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val alphaRatio: Double = Color.alpha(colorInt) / 255.0

        for (r in 0 until height) {
            for (c in 0 until width) {
                val origPixel: Int = origBitmap.getPixel(c, r)

                val newPixel_red: Int = (Color.red(colorInt) * alphaRatio + Color.red(origPixel) * (1.0 - alphaRatio)).toInt()
                val newPixel_green: Int = (Color.green(colorInt) * alphaRatio + Color.green(origPixel) * (1.0 - alphaRatio)).toInt()
                val newPixel_blue: Int = (Color.blue(colorInt) * alphaRatio + Color.blue(origPixel) * (1.0 - alphaRatio)).toInt()
                val newPixel_alpha: Int = Color.alpha(origPixel)

                val newPixel: Int = Color.argb(newPixel_alpha, newPixel_red, newPixel_green, newPixel_blue)
                retBitmap.setPixel(c, r, newPixel)
            }
        }

        return retBitmap
    }
}