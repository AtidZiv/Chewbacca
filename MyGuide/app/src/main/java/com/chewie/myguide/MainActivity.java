package com.chewie.myguide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final public static String SEND_TEXT_KEY = "SendText";
    static boolean playedMedia = false;
    MediaPlayer mediaPlayer = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart(){
        super.onStart();
        if (playedMedia)
            return;
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        playedMedia = true;
        mediaPlayer.start();
    }

    @Override
    protected void onPause(){
        if (mediaPlayer != null)
            mediaPlayer.stop();
        super.onPause();
    }

    public void onCategories(View view)
    {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }

}
