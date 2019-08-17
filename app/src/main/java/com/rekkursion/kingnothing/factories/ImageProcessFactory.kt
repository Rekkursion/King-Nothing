package com.rekkursion.kingnothing.factories

import android.graphics.Bitmap
import android.graphics.Color
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

    fun turnAnyDegrees(origBitmap: Bitmap?, degree: Double): Bitmap? {
        if (origBitmap == null)
            return null

        val width: Int = origBitmap.width
        val height: Int = origBitmap.height

        val radian = (degree * Math.PI) / 180.0
        val cosVal = cos(radian)
        val sinVal = sin(radian)

        val newPixelsMap: HashMap<Pair<Int, Int>, ArrayList<Int> > = HashMap()
        var startX: Int? = null
        var endX: Int? = null
        var topY: Int? = null
        var bottomY: Int? = null
        var offsetX = 0
        var offsetY = 0

        for (r in 0 until height) {
            for (c in 0 until width) {
                // get the pixel at the original location
                val origPixel: Int = origBitmap.getPixel(c, r)

                // calculate the new location
                val newC: Int = (cosVal * c.toDouble() - sinVal * r.toDouble()).toInt()
                val newR: Int = (sinVal * c.toDouble() + cosVal * r.toDouble()).toInt()

                // put the pixel with new location into the map
                if (newPixelsMap.containsKey(Pair(newR, newC)))
                    newPixelsMap[Pair(newR, newC)]?.add(origPixel)
                else
                    newPixelsMap[Pair(newR, newC)] = arrayListOf(origPixel)

                // find the range of new bitmap
                startX = if (startX == null) newC else min(startX, newC)
                endX = if (endX == null) newC else max(endX, newC)
                topY = if (topY == null) newR else min(topY, newR)
                bottomY = if (bottomY == null) newR else max(bottomY, newR)
            }
        }

        // assure the ranges are start with 0s
        offsetX = -startX!!
        offsetY = -topY!!

        val retBitmap = Bitmap.createBitmap((endX!! - startX!!) + 1, (bottomY!! - topY!!) + 1, Bitmap.Config.ARGB_8888)
        newPixelsMap.forEach { entry ->
            val newY: Int = entry.key.first + offsetY
            val newX = entry.key.second + offsetX

            // this 'if' statement is just yi-fang-wan-yi
            if (newX in 0 until retBitmap.width && newY in 0 until retBitmap.height) {
                var avgRed = 0
                var avgGreen = 0
                var avgBlue = 0
                var avgAlpha = 0

                entry.value.forEach { pixel ->
                    avgRed += Color.red(pixel)
                    avgGreen += Color.green(pixel)
                    avgBlue += Color.blue(pixel)
                    avgAlpha += Color.alpha(pixel)
                }
                avgRed = (avgRed.toDouble() / entry.value.size.toDouble()).toInt()
                avgGreen = (avgGreen.toDouble() / entry.value.size.toDouble()).toInt()
                avgBlue = (avgBlue.toDouble() / entry.value.size.toDouble()).toInt()
                avgAlpha = (avgAlpha.toDouble() / entry.value.size.toDouble()).toInt()

                retBitmap.setPixel(newX, newY, Color.argb(avgAlpha, avgRed, avgGreen, avgBlue))
//                retBitmap.setPixel(newX, newY, entry.value[0])
            }
        }

        return retBitmap
    }

    // turn left the bitmap by 90 degrees
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

    // turn right the bitmap by 90 degrees
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

    // turn the bitmap by 180 degrees
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

    // flip the bitmap vertically
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

    // flip the bitmap horizontally
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