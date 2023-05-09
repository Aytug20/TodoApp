package com.example.todoapp.model

import android.media.Image
import java.util.*


class ToDoModel {
    var dataTitle: String? = null
    var dataPriority: String? = null
    var dataImage: String? = null

    constructor(dataTitle: String?, dataPriority:String?,dataImage:String?){
        this.dataTitle = dataTitle
        this.dataPriority=dataPriority
        this.dataImage= dataImage
    }

    constructor()
    {}


}
