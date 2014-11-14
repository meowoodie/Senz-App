package com.senz.utils;

import android.os.Environment;
import android.util.JsonWriter;
import com.senz.core.Utils;

import java.io.*;
import java.text.SimpleDateFormat;

/***********************************************************************************************************************
 * @ClassName:   Writer
 * @Author:      Woodie
 * @CreateAt:    Fri, Nov 14, 2014
 * @Description: It's a tool to write file to root dir.
 *               I also design a method to write data to file in json.
 ***********************************************************************************************************************/
public class Writer {

    private File sdCradDir = null;
    private File saveFile  = null;

    // Constructor
    public Writer(String filename)
    {
        // Find the system root dir.
        sdCradDir = Environment.getExternalStorageDirectory();
        // new a file if the file is not exist.
        saveFile  = new File(sdCradDir, filename);
    }

    // Write at root dir.
    public void writeFileSdcard(String msg)
    {
        try{
            FileOutputStream os = new FileOutputStream(saveFile,true);
            byte[] bytes = msg.getBytes();
            os.write(bytes);
            os.close();
            //L.i("write over!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            L.e("write error!");
        }
    }

    // Write Acce to file
    public void writeAcceToFile(float acce[])
    {
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            StringWriter sw = new StringWriter(100);
            // transfer data to json
            writeAcceToJson(new JsonWriter(sw), acce[0], acce[1], acce[2], date);
            // Write json data to file
            //L.i(sw.toString());
            writeFileSdcard(sw.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Generate Gyro data to json
    private void writeAcceToJson(JsonWriter writer, float acc0, float acc1, float acc2, String date) throws IOException {
        writer.beginObject();
        writer.name("Time").value(date);
        writer.name("acce_x").value(acc0);
        writer.name("acce_y").value(acc1);
        writer.name("acce_z").value(acc2);
        writer.endObject();
        writer.close();
    }

    // Write Gyro to file
    public void writeGyroToFile(float gyro[])
    {
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            StringWriter sw = new StringWriter(100);
            // transfer data to json
            writeGyroToJson(new JsonWriter(sw), gyro[0], gyro[1], gyro[2], date);
            // Write json data to file
            //L.i(sw.toString());
            writeFileSdcard(sw.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Generate Gyro data to json
    private void writeGyroToJson(JsonWriter writer, float gy0, float gy1, float gy2, String date) throws IOException {
        writer.beginObject();
        writer.name("Time").value(date);
        writer.name("gyro_x").value(gy0);
        writer.name("gyro_y").value(gy1);
        writer.name("gyro_z").value(gy2);
        writer.endObject();
        writer.close();
    }
}
