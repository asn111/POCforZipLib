package com.self.ex.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.self.ex.myapplication.R
import com.zubair.permissionmanager.PermissionManager
import com.zubair.permissionmanager.PermissionUtils
import com.zubair.permissionmanager.enums.PermissionEnum
import com.zubair.permissionmanager.interfaces.FullCallback
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.net.URISyntaxException


class MainActivity : AppCompatActivity(), FullCallback {

    //var file = File(this.getFilesDir() + "/Documents/" + fileInfo.getFilename() + "." + fileInfo.getFiletype())

    var file: String = "logFile"
    lateinit var filee: File
    var REQUEST_CHOOSER = 1234
    val oPath = Environment.getExternalStorageDirectory().path+File.separator+"Documents/name"+File.separator+"myZip.zip"
    val pathO = Environment.getExternalStorageDirectory().path+File.separator+"Documents"


    var newFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isStoragePermissionGranted()) {
             readFile()

        } else {
            reqStoragePermission()
        }

        btn_open_strg.setOnClickListener {

            observe()

        }


    }
    fun observe () {

        val zip = zip(listOf(filee),oPath)

        zip.subscribeBy(onNext = { println(it) },
                onError =  { it.printStackTrace() },
                onComplete = { println("Done!") }
            )

    }

    fun observer2() {

        val zip = zipAll(pathO,oPath)
        zip.subscribeBy(onNext = { println(it) },
            onError =  { it.printStackTrace() },
            onComplete = { println("Done!") }
        )
    }


    fun readFile() {


        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Zip"),
                REQUEST_CHOOSER
            )
        } catch (ex: android.content.ActivityNotFoundException) {


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHOOSER -> if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.data
                Log.i("TAGG@", "File Uri: " + uri!!.toString())
                val path = getPath(this, uri)
                Log.i("TAGG", "File Path: $path")

                filee = File(path)


            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor?

            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index = cursor.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {

            }

        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    fun builder() {


        val zipBuilder = ZipBuilderN.Builder()
            .getFile(listOf(filee))
            .getOutputPath(oPath)
            .getName("myZip2.zip")
            .build()


//        ZipBuilderN.Builder()
//            .getContext(context)
//            .getPath(path)
//            .getName("Name")
//            .build()

    }

    fun reqStoragePermission() {
        PermissionManager.Builder().key(1)
            .permission(PermissionEnum.READ_EXTERNAL_STORAGE, PermissionEnum.WRITE_EXTERNAL_STORAGE)
            .callback(this@MainActivity)
            .ask(this@MainActivity)
    }

    fun isStoragePermissionGranted(): Boolean {
        var flag = false

        if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) &&
            PermissionUtils.isGranted(this, PermissionEnum.READ_EXTERNAL_STORAGE)
        ) {
            flag = true
        }
        return flag
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }

    override fun result(
        permissionsGranted: ArrayList<PermissionEnum>, permissionsDenied: ArrayList<PermissionEnum>,
        permissionsDeniedForever: ArrayList<PermissionEnum>, permissionsAsked: ArrayList<PermissionEnum>
    ) {
        if (permissionsGranted.size == permissionsAsked.size) {
            //Do some action

        } else if (permissionsDeniedForever.size > 0) {
            //If user answer "Never ask again" to a request for permission, you can redirect user to app settings, with an utils
            showDialog(true)
        } else {
            showDialog(false)
        }
    }


    fun showDialog(isNeverAskAgainChecked: Boolean) {
        AlertDialog.Builder(this)
            .setTitle("Permission needed")
            .setMessage("App needs to access your device ")
            .setPositiveButton(android.R.string.ok) { dialogInterface, i ->

                if (!isNeverAskAgainChecked) {
                    reqStoragePermission()
                } else {
                    PermissionUtils.openApplicationSettings(this@MainActivity, R::class.java.getPackage().name)
                }
            }
            .setNegativeButton(android.R.string.cancel) { dialogInterface, i -> dialogInterface.dismiss() }
            .show()
    }
}
