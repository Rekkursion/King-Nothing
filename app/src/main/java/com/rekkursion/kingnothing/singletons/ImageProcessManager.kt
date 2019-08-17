package com.rekkursion.kingnothing.singletons

import android.graphics.Bitmap
import java.util.*

object ImageProcessManager {
    private val MAX_SIZE_OF_PROCESSING_LIST: Int = 1000

    private var processingDeque: ArrayDeque<Bitmap>? = null
    private var currentDequePointer: Int = 0

    // init when a new image comes to be processed
    fun initialize(bitmap: Bitmap) {
        if (processingDeque != null) {
            processingDeque?.clear()
            processingDeque = null
        }

        processingDeque = ArrayDeque()
        processingDeque?.offerLast(bitmap)

        currentDequePointer = 1
    }

    // when user make a new edition
    fun addNewProcessedBitmap(newBitmap: Bitmap?) {
        if (newBitmap == null)
            return

        if (processingDeque == null) {
            processingDeque = ArrayDeque()
            currentDequePointer = 0
        }
        else {
            while (currentDequePointer < processingDeque?.size!!)
                processingDeque?.pollLast()
        }

        // if the list is full
        if (processingDeque?.size!! == MAX_SIZE_OF_PROCESSING_LIST) {
            processingDeque?.pollFirst()
            --currentDequePointer
        }

        processingDeque?.add(newBitmap)
        ++currentDequePointer
    }

    // undo the last edition
    fun undo() {
        if (currentDequePointer > 1)
            --currentDequePointer
    }

    // redo the last undone edition
    fun redo() {
        if (currentDequePointer < processingDeque?.size!!)
            ++currentDequePointer
    }

    // get the latest-processed bitmap
    fun getCurrentBitmap(): Bitmap? {
        return if (processingDeque == null || processingDeque?.size == 0 || currentDequePointer == 0)
            null
        else
            processingDeque?.elementAtOrNull(currentDequePointer - 1)
    }
}