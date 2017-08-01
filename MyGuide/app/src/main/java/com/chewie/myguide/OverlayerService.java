package com.chewie.myguide;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverlayerService extends Service {

    public static String PACKAGE_NAME = "PACKAGE_NAME";
    public static String NEXT_ACTIVITY = "NEXT_ACTIVITY";

    private ScheduledExecutorService scheduler;
    Runnable taskMonitor = null;
    Class nextActivity = null;
    String packageTrack = "";

    public OverlayerService() {
        this.scheduler = Executors.newScheduledThreadPool(5);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            packageTrack = intent.getStringExtra(PACKAGE_NAME);
            nextActivity = Class.forName(intent.getStringExtra(NEXT_ACTIVITY));
            monitorProcesses();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    String runningAppProcessesAPI(Context context) {
        String foregroundPkg = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Field processState = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            Iterator appProcessesIterator = am.getRunningAppProcesses().iterator();
            while (appProcessesIterator.hasNext()) {
                ActivityManager.RunningAppProcessInfo currentProcess = (ActivityManager.RunningAppProcessInfo) appProcessesIterator.next();
                if (currentProcess.importance == 100) {
                    if (currentProcess.importanceReasonCode == 0) {
                        Integer state = Integer.valueOf(processState.getInt(currentProcess));
                        if (state == 2) {
                            foregroundPkg = currentProcess.pkgList[0];
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return foregroundPkg;
    }

    String getForegroundPkgFromRunningTasksAPI(Context context) {
        String foregroundPkg = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List taskList = am.getRunningTasks(1);
        if((taskList != null) && (!taskList.isEmpty())) {
            foregroundPkg =  ((ActivityManager.RunningTaskInfo)taskList.get(0)).topActivity.getPackageName();
        }

        return foregroundPkg;
    }

    String getForegroundPkg(Context androidContext) {
        String foregroundPkg = "";

        if (Build.VERSION.SDK_INT >= 21) {
            foregroundPkg = runningAppProcessesAPI(androidContext);
        }
        else if (Build.VERSION.SDK_INT < 21) {
            foregroundPkg = getForegroundPkgFromRunningTasksAPI(androidContext);
        }

        return foregroundPkg;
    }

    boolean checkProcess()
    {
        String pkg = getForegroundPkg(OverlayerService.this);
        return pkg.equals(packageTrack);
    }

    void monitorProcesses()
    {
        taskMonitor = new Runnable() {
            @Override
            public void run() {
                if (!checkProcess())
                    return;
                scheduler.shutdown();
                Intent overlayAct = new Intent(OverlayerService.this, nextActivity);
                overlayAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                OverlayerService.this.startActivity(overlayAct);
                OverlayerService.this.stopSelf();
            }
        };
        this.scheduler.scheduleAtFixedRate(this.taskMonitor, 500, 1000, TimeUnit.MILLISECONDS);
    }
}
