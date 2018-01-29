package com.chewie.myguide;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;

import java.io.File;

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

    static public Bitmap getBitmapFromFile(Context context, String fileName) {
        try {
            PackageInfo pmi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String imgPath = pmi.applicationInfo.dataDir + fileName;
            return BitmapFactory.decodeFile(imgPath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
