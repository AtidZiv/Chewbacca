package com.chewie.myguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WidgetsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widgets_audio);

        final Button btnPrevStep = (Button)findViewById(R.id.btnPrevStep);

        final Button btnNextStep = (Button)findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(WidgetsActivity.this, R.raw.widgets_clock_step1);
                mediaPlayer.start();
            }
        });

        int[] steps = {
                R.raw.intro,
                R.raw.select_category,
                R.raw.select_sub_category,
                R.raw.widgets_clock_step1
        };
        final AudioFloater floater = new AudioFloater(this, steps, SubCategoriesActivity.class);
        floater.Display();
        finish();
    }

    public void onButtonClose(View view)
    {
        String pkgName = "com.android.launcher";
        Intent serviceIntent = new Intent(this, OverlayerService.class);
        serviceIntent.putExtra(OverlayerService.PACKAGE_NAME, pkgName);
        serviceIntent.putExtra(OverlayerService.NEXT_ACTIVITY, MainActivity.class.getCanonicalName());
        startService(serviceIntent);
        finish();
    }
}
