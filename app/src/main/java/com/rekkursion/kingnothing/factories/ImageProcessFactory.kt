package com.rekkursion.kingnothing.factories

import android.graphics.Bitmap
import android.graphics.Color

object ImageProcessFactory {
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

    fun turnLeft90Degrees(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val origWidth: Int = origBitmap.width
        val origHeight: Int = origBitmap.height
        val retBitmap: Bitmap = Bitmap.createBitmap(origHeight, origWidth, Bitmap.Config.ARGB_8888)

        for (origR in 0 until origHeight) {
            for (origC in 0 until origWidth) {
                val newR = origWidth - origC - 1
                val newC = origR
                retBitmap.setPixel(newC, newR, origBitmap.getPixel(origC, origR))
            }
        }

        return retBitmap
    }

    fun turnRight90Degrees(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val origWidth: Int = origBitmap.width
        val origHeight: Int = origBitmap.height
        val retBitmap: Bitmap = Bitmap.createBitmap(origHeight, origWidth, Bitmap.Config.ARGB_8888)

        for (origR in 0 until origHeight) {
            for (origC in 0 until origWidth) {
                val newR = origC
                val newC = origHeight - origR - 1
                retBitmap.setPixel(newC, newR, origBitmap.getPixel(origC, origR))
            }
        }

        return retBitmap
    }

    fun turn180Degrees(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val origWidth: Int = origBitmap.width
        val origHeight: Int = origBitmap.height
        val retBitmap: Bitmap = Bitmap.createBitmap(origWidth, origHeight, Bitmap.Config.ARGB_8888)

        for (origR in 0 until origHeight) {
            for (origC in 0 until origWidth) {
                val newR = origHeight - origR - 1
                val newC = origWidth - origC - 1
                retBitmap.setPixel(newC, newR, origBitmap.getPixel(origC, origR))
            }
        }

        return retBitmap
    }

    fun verticalFlip(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val origWidth: Int = origBitmap.width
        val origHeight: Int = origBitmap.height
        val retBitmap: Bitmap = Bitmap.createBitmap(origWidth, origHeight, Bitmap.Config.ARGB_8888)

        for (origR in 0 until origHeight) {
            for (origC in 0 until origWidth) {
                val newR = origHeight - origR - 1
                val newC = origC
                retBitmap.setPixel(newC, newR, origBitmap.getPixel(origC, origR))
            }
        }

        return retBitmap
    }

    fun horizontalFlip(origBitmap: Bitmap?): Bitmap? {
        if (origBitmap == null)
            return null

        val origWidth: Int = origBitmap.width
        val origHeight: Int = origBitmap.height
        val retBitmap: Bitmap = Bitmap.createBitmap(origWidth, origHeight, Bitmap.Config.ARGB_8888)

        for (origR in 0 until origHeight) {
            for (origC in 0 until origWidth) {
                val newR = origR
                val newC = origWidth - origC - 1
                retBitmap.setPixel(newC, newR, origBitmap.getPixel(origC, origR))
            }
        }

        return retBitmap
    }
}