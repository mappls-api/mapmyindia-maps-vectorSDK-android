package com.mapmyindia.sdk.demo.java.activity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mapmyindia.sdk.demo.R;

/**
 * Created by CEINFO on 26-02-2019.
 */

public class SplashActivity extends AppCompatActivity {
    String[] PERMISSIONS_REQUIRED = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // redirect();
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean permissionGranted = true;

        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PERMISSION_GRANTED) {
                    permissionGranted = false;
                    break;
                }
            }
        } else {
            //PERMISSION REQ
            permissionGranted = false;
        }

        if (!permissionGranted) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.permission_grant_msg))
                    .setPositiveButton("OK", (dialog, which) -> requestPermissions()).show();
        } else {
            redirect();
        }
    }

    void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUIRED[0]) == PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUIRED[1]) == PERMISSION_GRANTED) {
            redirect();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS_REQUIRED[1])
            ) {
                //showPermissionRequiredDialog
                new AlertDialog.Builder(this).setTitle(getString(R.string.permission_acceptance_msg))
                        .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS_REQUIRED, 100)).show();
            } else {
                //askPermission
                ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, 100);
            }
        }
    }


    private void redirect() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), FeaturesListActivity.class));
            finish();
        }, 500);
    }

}
