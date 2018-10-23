package com.self.ex.app

import android.content.Context
import io.reactivex.rxkotlin.subscribeBy
import java.io.File

data class ZipBuilderN(var file: List<File>,
                       var mOpPath: String,
                       var name: String = "Untitled "){
    class Builder{
        var file: List<File>? = null
        var mOpPAth: String? = null
        var nAme: String? = "Untitled"

        fun getFile(file: List<File>) = apply { this.file = file }
        fun getOutputPath(path: String) = apply { this.mOpPAth = path }
        fun getName(name: String) = apply { this.nAme = name }

        fun build() = ZipBuilderN(file?: error("Context can not be null"),
                                  mOpPAth?: error("Path can not be null"),
                                   nAme!!)
        
    }

}