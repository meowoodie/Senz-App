package com.senz.core;

import com.senz.utils.L;

/**
 * Created by woodie on 14/12/5.
 */
// A item of App info
public class App{
    public String appName="";
    public String packageName="";
    public String versionName="";
    public int    versionCode=0;

    public void print()
    {
        L.i("- APP:" + appName + " INFO -");
        L.i(" Package:" + packageName + " versionName:" + versionName + " versionCode:" + versionCode);
    }
}
