package com.senz.core;

import android.os.Parcelable;
import android.util.JsonWriter;
import com.senz.utils.Jsonable;
import com.senz.utils.L;

import java.io.IOException;

/**
 * Created by woodie on 14/12/5.
 */
// A item of App info
public class App implements Jsonable {
    private String appName="";
    private String packageName="";
    private String versionName="";
    private int    versionCode=0;

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void print()
    {
        L.i("- APP:" + appName + " INFO -");
        L.i(" Package:" + packageName + " versionName:" + versionName + " versionCode:" + versionCode);
    }

    public void writeToJsonNoBeginEnd(JsonWriter writer) throws IOException {
        writer.name("app_name").value(this.getAppName());
        writer.name("package_name").value(this.getPackageName());
        writer.name("version_name").value(this.getVersionName());
        writer.name("version_code").value(this.getVersionCode());
    }

    @Override
    public void writeToJson(JsonWriter writer) throws IOException {
        writer.beginObject();
        this.writeToJsonNoBeginEnd(writer);
        writer.endObject();
    }

}
