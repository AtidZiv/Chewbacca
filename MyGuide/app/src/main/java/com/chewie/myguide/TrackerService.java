package com.chewie.myguide;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayDeque;
import java.util.Deque;

public class TrackerService extends AccessibilityService {

    enum ARROW_DIRECTION {
        Up,
        Down
    }

    final static public String STEPS = "steps";
    final static public String ACTION_NEXT_STEP = "NextStep";

    LocalBroadcastManager localBroadcastManager = null;
    View _floater = null;
    JSONArray _steps = null;
    int _curStep = -1;
    static boolean _isServiceRunning = false;
    AccessibilityNodeInfo _lastNode = null;
    ARROW_DIRECTION _arrowDirection = ARROW_DIRECTION.Up;

    static public boolean isServiceRunning() {
        return _isServiceRunning;
    }

    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
        }
        return "default";
    }

    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }

    private void nextStep() {
        _curStep++;
        Intent intent = new Intent(ACTION_NEXT_STEP);
        getApplicationContext().sendBroadcast(intent);
        Log.d(MainActivity.TAG, "Send broadcast with action "+intent.getAction());
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(MainActivity.TAG, String.format(
                "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
                getEventType(event), event.getClassName(), event.getPackageName(),
                event.getEventTime(), getEventText(event)));
        if (_curStep < 0)
            return;

        try {
            JSONArray step_captions = _steps.getJSONArray(_curStep);
            String eventText = getEventText(event).toLowerCase();

            boolean match = false;
            for(int i=0; i<step_captions.length(); i++) {
                String caption = step_captions.getString(i);
                if ((match = eventText.contains(caption)) == true)
                    break;
            }
            if (match) {
                nextStep();
            }

            if (_curStep == _steps.length()) {
                showViewForNode(null);
                _curStep = -1;
                return;
            }

            for(int i=0; i<step_captions.length(); i++) {
                String caption = step_captions.getString(i);
                AccessibilityNodeInfo node = findTextNode(getRootInActiveWindow(), caption);
                showViewForNode(node);
                if (node != null)
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
        Log.v(MainActivity.TAG, "onInterrupt");
    }

    private AccessibilityNodeInfo findTextNode(AccessibilityNodeInfo root, String text) {
        if (root == null)
            return null;
        text = text.toLowerCase();
        Deque<AccessibilityNodeInfo> deque = new ArrayDeque<>();
        deque.add(root);
        AccessibilityNodeInfo node_found = null;
        while (!deque.isEmpty()) {
            AccessibilityNodeInfo node = deque.removeFirst();
            if (node.getText() != null) {
                String txt = node.getText().toString().toLowerCase();
                if (txt.contains(text)) {
                    node_found = node;
                }
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                try {
                    deque.addLast(node.getChild(i));
                } catch (Exception ex) {

                }
            }
        }
//        Log.d(MainActivity.TAG, "Found node: " + node_found);
        return node_found;
    }

    boolean showAnimator = true;
    void showViewForNode(AccessibilityNodeInfo node) {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Object parent = _floater == null ? null : _floater.getParent();
        if (_floater != null && parent != null)
            wm.removeView(_floater);
        if (node == null || _lastNode == node)
            return;

        int width = 260;
        int height = 260;
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);
        Rect bounds = new Rect();
        node.getBoundsInScreen(bounds);
        if (bounds.top < 0 || bounds.top > screenSize.y ||
                bounds.left < 0 || bounds.left > screenSize.x) {
            return;
        }

        ARROW_DIRECTION arrow = (bounds.top < screenSize.y/2) ? ARROW_DIRECTION.Up : ARROW_DIRECTION.Down;
        if (_arrowDirection != arrow)
            _floater = null;

        _arrowDirection = arrow;
        _lastNode = node;

        int nodeh = bounds.height();
        if (_arrowDirection == ARROW_DIRECTION.Down) {
            nodeh = - nodeh - 20;
        }

        int xpos = bounds.left + bounds.width()/2 - screenSize.x/2;
        int ypos = bounds.top + nodeh - screenSize.y/2;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                width,
                height,
                xpos,
                ypos,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        if (_floater == null) {
            ImageView img = new ImageView(this);
            img.setImageResource(_arrowDirection == ARROW_DIRECTION.Up ? R.drawable.arrow_up : R.drawable.arrow_down);
            _floater = img;
        }
        wm.addView(_floater, params);

        if (showAnimator && _curStep == _steps.length()-1) {
            AnimationFloater anim = new AnimationFloater(getApplicationContext(), xpos, ypos);
            anim.RunAnimation();
            showAnimator = false;
        }
    }

    @Override
    protected void onServiceConnected() {
        Log.v(MainActivity.TAG, "service connected");
        _isServiceRunning = true;
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getApplicationContext());
    }

    @Override
    public void onDestroy() {
        _isServiceRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String steps = intent.getStringExtra(STEPS);
            _steps = new JSONArray(steps);
            _curStep = 0;
        } catch (Exception e) {
            _curStep = -1;
            showViewForNode(null);
        }
        return super.onStartCommand(intent,flags,startId);
    }
}

