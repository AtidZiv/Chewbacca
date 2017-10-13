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
    Button btnStartOver, btnNext, btnPlayAgain, btnBackToActivity, btnMore, btnExit;

    void playCurrentStep() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(context, steps[curStep]);
        mediaPlayer.start();
    }

    void showLess(Button[] btns) {
        resizeAndClear(btnWidth, btnHeight*btns.length);
        int y = marginY;
        for(Button btn : btns) {
            AddChild(btn, marginX, y, btnWidth, btnHeight);
            y += btnHeight;
        }
        AudioFloater.super.Display();
    }

    void showLess() {
        if (Utility.isServiceOn(context))
            showLess(new Button[]{btnMore});
        else
            showLess(new Button[]{btnMore, btnNext});
    }

     void showMore() {
        resizeAndClear(btnWidth*2, btnHeight*3);
        AddChild(btnStartOver, marginX, marginY, btnWidth, btnHeight);
        AddChild(btnBackToActivity, marginX+btnWidth, marginY, btnWidth, btnHeight);
        AddChild(btnPlayAgain, marginX, btnHeight, btnWidth, btnHeight);
        if (!Utility.isServiceOn(context))
            AddChild(btnNext, marginX+btnWidth, btnHeight, btnWidth, btnHeight);
        AddChild(btnExit, marginX+btnWidth, btnHeight*2, btnWidth, btnHeight);
        AudioFloater.super.Display();
    }

    void clearTrackerSteps() {
        Intent tracker = new Intent(context, TrackerService.class);
        context.startService(tracker);
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
                curStep++;
                playCurrentStep();
                if (curStep == steps.length-1)
                    showLess(new Button[]{btnMore, btnExit});
                else
                    showLess();
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
                clearTrackerSteps();
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

        btnExit = new Button(context);
        btnExit.setText(R.string.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null)
                    mediaPlayer.stop();
                Dismiss();
                clearTrackerSteps();
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
