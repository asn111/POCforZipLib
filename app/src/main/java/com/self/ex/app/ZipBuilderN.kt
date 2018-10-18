package com.self.ex.app

import android.content.Context

data class ZipBuilderN(var mContext: Context,
                       var mPath: String,
                       var name: String = "Untitled "){
    class Builder{
        var mCOntext: Context? = null
        var mPAth: String? = null
        var nAme: String? = "Untitled"

        fun getContext(context: Context) = apply { this.mCOntext = context }
        fun getPath(path: String) = apply { this.mPAth = path }
        fun getName(name: String) = apply { this.nAme = name }

        fun build() = ZipBuilderN(mCOntext?: error("Context can not be null"),
                                  mPAth?: error("Path can not be null"),
                                   nAme!!)
    }

}