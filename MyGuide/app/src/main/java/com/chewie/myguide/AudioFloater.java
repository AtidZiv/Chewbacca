package com.chewie.myguide;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

/**
 * Created by moshe on 13/07/2017.
 */

public class AudioFloater extends Floater {

    Class backToActivityClass = MainActivity.class;
    MediaPlayer mediaPlayer;
    int[] steps;
    int curStep = 0;
    int btnWidth = 280;
    int btnHeight = 150;
    int marginX = 10;
    int marginY = 10;
    Button btnStartOver, btnNext, btnPlayAgain, btnBackToActivity, btnMore;

    void playCurrentStep() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(context, steps[curStep]);
        mediaPlayer.start();
    }

    void showLess() {
        resizeAndClear(btnWidth, btnHeight*2);
        AddChild(btnMore, marginX, marginY, btnWidth, btnHeight);
        AddChild(btnNext, marginX, btnHeight, btnWidth, btnHeight);
        AudioFloater.super.Display();
    }

    void showMore() {
        resizeAndClear(btnWidth*2, btnHeight*2);
        AddChild(btnStartOver, marginX, marginY, btnWidth, btnHeight);
        AddChild(btnBackToActivity, marginX+btnWidth, marginY, btnWidth, btnHeight);
        AddChild(btnPlayAgain, marginX, btnHeight, btnWidth, btnHeight);
        AddChild(btnNext, marginX+btnWidth, btnHeight, btnWidth, btnHeight);
        AudioFloater.super.Display();
    }

    void setButtons() {
        btnStartOver = new Button(context);
        btnStartOver.setText(R.string.btnStartOver);
        btnStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLess();
                curStep = 0;
                playCurrentStep();
                btnNext.setEnabled(true);
            }
        });

        btnNext = new Button(context);
        btnNext.setText(R.string.btnNextStep);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLess();
                curStep++;
                playCurrentStep();
                if (curStep == steps.length-1)
                    btnNext.setEnabled(false);
            }
        });

        btnPlayAgain = new Button(context);
        btnPlayAgain.setText(R.string.btnAgain);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLess();
                playCurrentStep();
            }
        });

        btnBackToActivity = new Button(context);
        btnBackToActivity.setText(R.string.btnBack);
        btnBackToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null)
                    mediaPlayer.stop();
                Dismiss();
                Intent intent = new Intent(context, backToActivityClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        btnMore = new Button(context);
        btnMore.setText(R.string.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });
    }

    public AudioFloater(final Context context, int[] steps, Class backToActivityClass) {
        super(context, 300, 250);
        setButtons();

        this.steps = steps;
        this.backToActivityClass = backToActivityClass;
        curStep = 0;
        playCurrentStep();
    }

    @Override
    public void Display() {
        showLess();
        super.Display();
    }
}
