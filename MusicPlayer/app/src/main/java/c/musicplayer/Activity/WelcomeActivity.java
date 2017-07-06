package c.musicplayer.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import c.musicplayer.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler();
        if(Build.VERSION.SDK_INT>=23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
                    overridePendingTransition(R.anim.abc_popup_enter,R.anim.abc_popup_exit);
                }
                WelcomeActivity.this.finish();
            }
        }, 3000);
        //finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   //获取权限的回调
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&permissions[0]==Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    Log.e("welcome","同意了");
                } else {
                   //finish();
                }
            }
            break;
            default:

        }
    }
}
