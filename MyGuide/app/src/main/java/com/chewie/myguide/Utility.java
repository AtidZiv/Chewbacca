package com.chewie.myguide;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by moshe on 02/10/2017.
 */

public class Utility {

    static public boolean isServiceOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }

        return accessibilityEnabled == 1;
    }

}
