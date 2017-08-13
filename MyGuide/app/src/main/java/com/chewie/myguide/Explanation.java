package com.chewie.myguide;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Explanation extends AppCompatActivity {

    MediaPlayer mediaPlayer = null;
    String chosenAction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);

        chosenAction = getIntent().getStringExtra("action");
        MediaPlayer mediaPlayer = MediaPlayer.create(Explanation.this, R.raw.explanation);
        mediaPlayer.start();
    }
    @Override
    protected void onPause(){
        if (mediaPlayer != null)
            mediaPlayer.stop();
        super.onPause();
    }

    public void onWidgets(View view)
    {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        Intent intent = new Intent(this, WidgetsActivity.class);
        intent.putExtra("action", chosenAction);
        startActivity(intent);
        finish();
    }
}
