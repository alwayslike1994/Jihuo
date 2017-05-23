package com.example.alwayslike.jihuo.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alwayslike on 2017/5/9.
 */

public class LogUtil {
    private static LogUtil instance = null;

    public static LogUtil getInstance() {
        if (instance == null) {
            synchronized (LogUtil.class) {
                if (instance == null) {
                    instance = new LogUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 保存str信息到本地文件夹
     *
     * @param str
     */
    public void saveFile(String str) {

        String filePath = null;

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        String user_id = SharePreferenceHanler.readString(Constant.Key.USERNAME);

        String PATH = Environment.getExternalStorageDirectory()
                .toString() + File.separator + "ParkApp" + File.separator + "Log";

        Date date = new Date();
        str = new SimpleDateFormat("HH:mm:ss").format(date) + "--" + str;

        if (hasSDCard) {
            filePath = PATH + File.separator + "PDA1" + new SimpleDateFormat("yyyyMMdd").format(date) + user_id + ".txt";
        } else
            filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + "ParkApp" +
                    File.separator + "Log" + File.separator + "PDA1" + new SimpleDateFormat("yyyyMMdd").format(date) + user_id + ".txt";

        FileOutputStream outStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            outStream = new FileOutputStream(file, true);
            outStream.write(str.getBytes());
            outStream.write(System.getProperty("line.separator", "\n").getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStream != null)
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


}
