package com.rekkursion.kingnothing.factories

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

object ImageProcessFactory {
    // cover the bitmap by a pure color
    fun colorCover(origBitmap: Bitmap?, colorInt: Int): Bitmap? {
        if (origBitmap == null)
            return null

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

    // turn any degrees
    fun turnAnyDegrees(origBitmap: Bitmap?, degree: Float): Bitmap? {
        if (origBitmap == null)
            return null

        val matrix = Matrix()
        val offsetX = origBitmap.width.toFloat() / 2.0F
        val offsetY = origBitmap.height.toFloat() / 2.0F

        matrix.postTranslate(-offsetX, -offsetY)
        matrix.postRotate(degree)
        matrix.postTranslate(offsetX, offsetY)

        return Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.width, origBitmap.height, matrix, true)
    }

    // turn left the bitmap by 90 degrees
    fun turnLeft90Degrees(origBitmap: Bitmap?): Bitmap? {
        return turnAnyDegrees(origBitmap, -90.0F)
    }

    // turn right the bitmap by 90 degrees
    fun turnRight90Degrees(origBitmap: Bitmap?): Bitmap? {
        return turnAnyDegrees(origBitmap, 90.0F)
    }

    // turn the bitmap by 180 degrees
    fun turn180Degrees(origBitmap: Bitmap?): Bitmap? {
        return turnAnyDegrees(origBitmap, 180.0F)
    }

    // flip the bitmap vertically
    fun verticalFlip(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val matrix = Matrix()
        val offsetX = origBitmap.width.toFloat() / 2.0F
        val offsetY = origBitmap.height.toFloat() / 2.0F

        matrix.postScale(1.0F, -1.0F, offsetX, offsetY)

        return Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.width, origBitmap.height, matrix, true)
    }

    // flip the bitmap horizontally
    fun horizontalFlip(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val matrix = Matrix()
        val offsetX = origBitmap.width.toFloat() / 2.0F
        val offsetY = origBitmap.height.toFloat() / 2.0F

        matrix.postScale(-1.0F, 1.0F, offsetX, offsetY)

        return Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.width, origBitmap.height, matrix, true)
    }
}