package com.chewie.myguide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SubCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);
        MediaPlayer mediaPlayer = MediaPlayer.create(SubCategoriesActivity.this, R.raw.select_sub_category);
        mediaPlayer.start();
    }

    public void onWidgets(View view)
    {
        Intent intent = new Intent(this, WidgetsActivity.class);
        startActivity(intent);
        finish();
    }
}
