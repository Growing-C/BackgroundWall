package com.growingc.backgroundwall;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by RB-cgy on 2016/11/10.
 */
public class FileUtils {
    public static String sFilePath = Environment.getExternalStorageDirectory() + "/A背景设计/";


    public static void saveBitmap(Context context, Bitmap bitmap, String fileName) {
        if (bitmap == null)
            return;
        //这个文件夹里面的东西app删除的时候会自动删除
        System.out.println("context dir:" + context.getExternalFilesDir(null));///storage/emulated/0/Android/data/com.growingc.backgroundwall/files
        //这个文件夹里面的东西app删除的时候不会自动删除
        System.out.println("Environment dir:" + Environment.getExternalStorageDirectory());///storage/emulated/0

        File file = new File(sFilePath + fileName);///storage/emulated/0/growingc/background.png
        if (file.exists()) {
            file.delete();
        }
        System.out.println("file dir:" + file);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            System.out.println("saved ok+++++++++");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
