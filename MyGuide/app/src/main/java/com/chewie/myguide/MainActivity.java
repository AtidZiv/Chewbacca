package com.chewie.myguide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final static public String TAG = "MyGuide";
    static boolean playedMedia = false;
    MediaPlayer mediaPlayer = null;

    void ensureServiceIsRunning() {
        if (!Utility.isServiceOn(this)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setComponent(intent.resolveActivity(getPackageManager()));
            startActivity(intent);
        }
        Log.v(TAG, "Service is running: "+TrackerService.isServiceRunning());
    }

    void showPermissions() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    void ensureDrawOverPermission() {
//        showPermissions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ensureServiceIsRunning();
        ensureDrawOverPermission();
    }

    @Override
    protected void onStart(){
        if (!playedMedia) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
            playedMedia = true;
            mediaPlayer.start();
        }
        super.onStart();
    }

    @Override
    protected void onPause(){
        if (mediaPlayer != null) {
            playedMedia = !mediaPlayer.isPlaying();
            mediaPlayer.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop(){
        playedMedia = false;
        super.onStop();
    }

    public void onCategories(View view)
    {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }

}
