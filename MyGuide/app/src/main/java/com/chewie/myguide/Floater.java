package com.chewie.myguide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by moshe on 12/07/2017.
 */

public class Floater {

    class FloaterChild {
        View view;
        int x;
        int y;
        int width;
        int height;

        FloaterChild(View view, int x, int y, int w, int h) {
            this.view = view;
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
        }
    }

    Context context;
    int floaterType = WindowManager.LayoutParams.TYPE_PHONE;
    int height;
    int width;
    ArrayList<FloaterChild> children = new ArrayList<>();
    int bgColor = Color.DKGRAY;
    RelativeLayout topView = null;

    void setParams(Context context, int floaterType, int height, int width) {
        this.floaterType = floaterType;
        this.context = context.getApplicationContext();
        this.height = height;
        this.width = width;
    }

    public Floater(Context context, int floaterType, int width, int height) {
        setParams(context, floaterType, width, height);
    }

    public Floater(Context context, int floaterType) {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        windowManager.getDefaultDisplay().getSize(screenSize);
        setParams(context, floaterType, screenSize.x/2, screenSize.y*3/4);
    }

    public Floater(Context context, int width, int height) {
        this(context, WindowManager.LayoutParams.TYPE_PHONE, width, height);
    }

    public Floater(Context context) {
        this(context, WindowManager.LayoutParams.TYPE_PHONE);
    }

    public void AddChild(View child, int posx, int posy, int width, int height) {
        children.add(new FloaterChild(child, posx, posy, width, height));
    }

    public void SetBackgroundColor(int color) {
        bgColor = color;
    }

    public void Display()
    {
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        windowManager.getDefaultDisplay().getSize(screenSize);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                screenSize.x-width,
                0,
                floaterType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        if (topView != null) {
            windowManager.removeView(topView);
            topView.removeAllViews();
        }
        else
            topView = new RelativeLayout(context);
        topView.setLayoutParams(params);

        for(FloaterChild child : children) {
            RelativeLayout.LayoutParams childParams=new RelativeLayout.LayoutParams(child.width, child.height);
            childParams.setMargins(child.x, child.y, 0, 0);
            child.view.setLayoutParams(childParams);
            topView.addView(child.view);
        }

        topView.setBackgroundColor(bgColor);

        try {
            windowManager.addView(topView, params);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void resizeAndClear(int newwidth, int newheight) {
        width = newwidth;
        height = newheight;
        children.clear();
        if (topView != null)
            topView.removeAllViews();
    }

    public void Dismiss() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(topView);
        topView = null;
    }
}
