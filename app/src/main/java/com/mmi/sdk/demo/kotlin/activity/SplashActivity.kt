package com.mmi.sdk.demo.kotlin.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker.PERMISSION_GRANTED
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.mmi.sdk.demo.MainActivity
import com.mmi.sdk.demo.R

/**
 * Created by CEINFO on 26-02-2019.
 */
class SplashActivity : AppCompatActivity() {

    internal var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        //  streamExample()
        requestPermissions()
    }


    private fun redirect() {
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 500)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var permissionGranted = true

        if (grantResults.size > 0) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PERMISSION_GRANTED) {
                    permissionGranted = false
                    break
                }
            }
        } else {
            //PERMISSION REQ
            permissionGranted = false
        }

        if (!permissionGranted) {
            AlertDialog.Builder(this).setTitle("Please grant all the permissions to continue. \nYou can go to phone's settings >> Applications >> Orrder Driver and manually grant the permissions.")
                    .setPositiveButton("OK") { dialog, which -> requestPermissions() }.show()
        } else {
            redirect()
        }
    }

    internal fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUIRED[0]) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUIRED[1]) == PERMISSION_GRANTED) {

            redirect()

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[1])) {
                //showPermissionRequiredDialog
                AlertDialog.Builder(this).setTitle("Please Accept all the permissions.")
                        .setPositiveButton("OK") { dialog, which -> ActivityCompat.requestPermissions(this@SplashActivity, PERMISSIONS_REQUIRED, 100) }.show()
            } else {
                //askPermission
                ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, 100)
            }
        }

    }

    /*  @RequiresApi(Build.VERSION_CODES.N)
      fun streamExample(){
          var list = listOf(1,4, 5, 6,2, 9, 8, 3,  0)
          list.stream()
                  .anyMatch()
                  .sorted()
                  .forEach{ println(it) }
      }*/
}