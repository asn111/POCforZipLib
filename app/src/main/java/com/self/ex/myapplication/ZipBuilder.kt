package com.self.ex.myapplication

import android.content.Context
import java.io.Serializable

class ZipFileBulder{
    private var context: Context? = null
    private var path: String? = null
    private var name: String? = "Untitled Zip"

    fun getContext(context: Context): ZipFileBulder {
        this.context = context
        return this
    }

    fun getPath(path: String): ZipFileBulder {
        this.path = path
        return  this
    }

    fun getName(name: String): ZipFileBulder {
        this.name = name
        return this
    }

    fun build(): ZipFileBulder {

        if(context == null){
            throw RuntimeException("Context can not be a null value")
        }
        if(path == null){
            throw RuntimeException("Path can not be a null value")
        }


        return this
    }
}
