package com.self.ex.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.self.ex.myapplication.R
import com.zubair.permissionmanager.PermissionManager
import com.zubair.permissionmanager.PermissionUtils
import com.zubair.permissionmanager.enums.PermissionEnum
import com.zubair.permissionmanager.interfaces.FullCallback
import java.io.File

class MainActivity : AppCompatActivity(), FullCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val path = filesDir.absolutePath
        if (isStoragePermissionGranted()) {


        } else {
            reqStoragePermission()
        }

        ZipBuilderN.Builder()
            .getContext(this)
            .getPath(path)
            .getName("Name")
            .build()
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
            PermissionUtils.isGranted(this, PermissionEnum.READ_EXTERNAL_STORAGE)) {
            flag = true
        }
        return flag
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }

    override fun result(permissionsGranted: ArrayList<PermissionEnum>, permissionsDenied: ArrayList<PermissionEnum>,
                        permissionsDeniedForever: ArrayList<PermissionEnum>, permissionsAsked: ArrayList<PermissionEnum>) {
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
            .setMessage("Give Permissions Text Here")
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
