package com.konka.kktripclient.layout.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.regex.Pattern;

public class CommonFunction {

    private static HandlerThread sCommonSubThread;
    private static Handler sCommonSubHandler;

    public static Handler getCommonSubHandler() {
        if (sCommonSubHandler == null) {
            synchronized (CommonFunction.class) {
                if (sCommonSubHandler == null) {
                    sCommonSubThread = new HandlerThread("KK-CommonSubThread");
                    sCommonSubThread.setPriority(Thread.MIN_PRIORITY);
                    sCommonSubThread.start();
                    sCommonSubHandler = new Handler(sCommonSubThread.getLooper());
                }
            }
        }

        return sCommonSubHandler;
    }

    public static void close() {
        try {
            if (sCommonSubThread != null) {
                sCommonSubThread.quit();
            }
            sCommonSubHandler = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Point getScreenSize() {
        WindowManager a = (WindowManager) ActivityHandler.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display d = a.getDefaultDisplay();
        Point size = new Point();
        d.getSize(size);
        return size;
    }

    /**
     * 获取系统总内存
     *
     * @return
     */
    public static int getSystemTotalMemory() {
        String tStr;
        FileReader tFileReader = null;
        BufferedReader tBufferedReader = null;
        String[] tArray = null;
        try {
            tFileReader = new FileReader("/proc/meminfo");
            tBufferedReader = new BufferedReader(tFileReader, 1024);
            while ((tStr = tBufferedReader.readLine()) != null) {
                if (tStr.contains("MemTotal")) {
                    tArray = tStr.split("\\s+");
                    if (tArray != null && tArray.length == 3) {
                        return Integer.valueOf(tArray[1]);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                tBufferedReader.close();
                tFileReader.close();
                tArray = null;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int getCPUCount() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new KKCpuFilter());
            return files.length;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static class KKCpuFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                return true;
            }
            return false;
        }
    }
}
