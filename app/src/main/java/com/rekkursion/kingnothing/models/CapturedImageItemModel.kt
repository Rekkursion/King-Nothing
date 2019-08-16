package com.rekkursion.kingnothing.models

data class CapturedImageItemModel (var id: Long,
                                   var filename: String = "<unsaved>",
                                   var hasBeenSaved: Boolean = false)