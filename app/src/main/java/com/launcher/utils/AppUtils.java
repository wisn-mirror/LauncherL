package com.launcher.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wisn on 2017/10/8.
 */

public class AppUtils {

    /**
     * get  ApplicationInfo
     * @return
     */
    public static ApplicationInfo getApplicationInfo() {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = Utils.getApp().getPackageManager().getApplicationInfo(Utils.getApp().getPackageName(),
                                                                             PackageManager.GET_META_DATA);
            return applicationInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断应用是否已安装
     *
     * @param packageName
     *
     * @return
     */
    public static boolean isInstalled( String packageName) {
        boolean hasInstalled = false;
        PackageManager pm = Utils.getApp().getPackageManager();
        List<PackageInfo> list = pm
                //                .getInstalledPackages(PackageManager.PERMISSION_GRANTED);
                .getInstalledPackages(0);
        for (PackageInfo p : list) {
            if (packageName != null && packageName.equals(p.packageName)) {
                hasInstalled = true;
                break;
            }
        }
        return hasInstalled;
    }

    /**
     * 判断应用是否正在运行
     *
     * @param packageName
     *
     * @return
     */
    public static boolean isRunning( String packageName) {
        ActivityManager am = (ActivityManager) Utils.getApp()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : list) {
            String processName = appProcess.processName;
            if (processName != null && processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 启动一个app
     *
     * @param packageName
     */
    public static void openApp( String packageName) {
        try {
            PackageInfo pi = Utils.getApp().getPackageManager().getPackageInfo(packageName, 0);
            PackageManager packageManager = Utils.getApp().getPackageManager();
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName_new = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName(packageName_new, className);
                intent.setComponent(cn);
                Utils.getApp().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public static String getAppVersionName() {
        try {
            return Utils.getApp().getPackageManager().getPackageInfo(Utils.getApp().getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     *
     * @return
     */
    public static int getAppVersionCode() {
        try {
            return Utils.getApp().getPackageManager().getPackageInfo(Utils.getApp().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *
     * @return
     */
    public static String getPackageName() {
        try {
            return Utils.getApp().getPackageManager().getPackageInfo(Utils.getApp().getPackageName(), 0).packageName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param file
     */
    public static void installApk( File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                              "application/vnd.android.package-archive");
        Utils.getApp().startActivity(intent);
    }

    /**
     * 安装apk
     *
     * @param file    APK文件uri
     */
    public static void installApk(Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        Utils.getApp().startActivity(intent);
    }

    /**
     * @param packageName
     */
    public static void uninstallApk(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        Utils.getApp().startActivity(intent);
    }

    /**
     * 获取系统中所有的应用
     *
     *
     * @return 应用信息List
     */
    public static List<PackageInfo> getAllApps() {

        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = Utils.getApp().getPackageManager();
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = paklist.get(i);
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
                apps.add(pak);
        }
        return apps;
    }

    /**
     * @return
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * @param packageName
     *
     * @return
     */
    public static String getSign(String packageName) {
        try {
            PackageInfo pis = Utils.getApp().getPackageManager()
                                     .getPackageInfo(packageName,
                                                     PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     *
     * @return 32位签名字符串
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                                  98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() >> 10;
    }

    /**
     * @return
     */
    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory() >> 10;
    }

    /**
     * @return
     */
    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory() >> 10;
    }

    /**
     *
     * @return
     */
    public static int getDeviceUsableMemory() {
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService(
                Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem >> 10);
    }
}
