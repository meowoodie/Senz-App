package com.senz.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import com.senz.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woodie on 14/12/5.
 */
public class AppInfo {

    public ArrayList<AppItem> appList = new ArrayList<AppItem>();

    // A item of App info
    class AppItem{
        public String appName="";
        public String packageName="";
        public String versionName="";
        public int    versionCode=0;

        public void print()
        {
            L.i("- APP:"+appName+" INFO -");
            L.i(" Package:" + packageName + " versionName:" + versionName + " versionCode:" + versionCode);
        }
    }

    public AppInfo(Context ctx)
    {
        L.i("=== APP INFO ===");
        List<PackageInfo> packages = ctx.getPackageManager().getInstalledPackages(0);
        for(PackageInfo packageInfo : packages) {
            AppItem tmp =new AppItem();
            tmp.appName = packageInfo.applicationInfo.loadLabel(ctx.getPackageManager()).toString();
            tmp.packageName = packageInfo.packageName;
            tmp.versionName = packageInfo.versionName;
            tmp.versionCode = packageInfo.versionCode;
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0)
            {
                tmp.print();
                appList.add(tmp);//If it is non system app, then add it into the list
            }
        }
    }


}
