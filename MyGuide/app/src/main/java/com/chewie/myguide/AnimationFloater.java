package com.chewie.myguide;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static com.chewie.myguide.MainActivity.TAG;

/**
 * Created by moshe on 01/11/2017.
 */

public class AnimationFloater extends Floater {

    Runnable moveViewRunnable;
    int stepX = 0;
    int stepY = 0;
    int currentX = 0;
    int currentY = 0;
    int posX = 0;
    int posY = 0;
    ImageView animView;

    public AnimationFloater(Context context, int xpos, int ypos) {
        super(context);

        posX = xpos;
        posY = ypos;

        animView = new ImageView(context);
        animView.setImageResource(R.drawable.finger_pointing);

        moveViewRunnable = new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "Running animation inside thread: moving view");
                MoveView();
            }
        };

        width = 200;
        height = 300;
        stepX = 20;
        stepY = 20;

        showView();
    }

    void showView() {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        windowManager.getDefaultDisplay().getSize(screenSize);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                posX,
                posY,
                floaterType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        animView.setLayoutParams(params);
        try {
            windowManager.addView(animView, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    boolean startMove = false;

    void MoveView() {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        currentX -= stepX;
        currentY -= stepY;
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                currentX,
                currentY,
                floaterType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        windowManager.updateViewLayout(animView, params);
    }

    void RunAnimation() {

        Point screenSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(screenSize);

        final Handler mainHandler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int t = 0; t < 5; t++) {
                    currentX = 0;
                    currentY = 0;
                    for (int i = 0; i < 20; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                        mainHandler.post(moveViewRunnable);
                    }
                }
            }
        }).start();
    }
}
