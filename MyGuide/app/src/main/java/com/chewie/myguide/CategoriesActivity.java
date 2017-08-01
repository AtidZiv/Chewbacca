package com.chewie.myguide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        MediaPlayer mediaPlayer = MediaPlayer.create(CategoriesActivity.this, R.raw.select_category);
        mediaPlayer.start();
    }
    public void onSubCatrgories(View view)
    {
        Intent intent = new Intent(this, SubCategoriesActivity.class);
        startActivity(intent);
        finish();
    }
}
