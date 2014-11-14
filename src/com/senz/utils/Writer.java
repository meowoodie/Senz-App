package com.senz.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/***********************************************************************************************************************
 * @ClassName:   Writer
 * @Author:      Woodie
 * @CreateAt:    Fri, Nov 14, 2014
 * @Description: It's a tool to write file to root dir.
 ***********************************************************************************************************************/
public class Writer {

    private FileOutputStream os = null;

    // Constructor
    public Writer(String filename)
    {
        File sdCradDir = Environment.getExternalStorageDirectory();
        File saveFile  = new File(sdCradDir, filename);
        try {
            this.os = new FileOutputStream(saveFile,true);
        }
        catch (FileNotFoundException e) {
            L.e("Open File failed");
            e.printStackTrace();
        }
    }

    // Write at root dir.
    public void writeFileSdcard(String msg)
    {
        try{
            //os = new FileOutputStream(this.file);
            byte[] bytes = msg.getBytes();
            os.write(bytes);
            os.close();
            L.i("write over!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            L.e("write error!");
        }
    }
}
