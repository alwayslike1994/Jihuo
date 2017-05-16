package com.example.alwayslike.jihuo.http;

import android.util.Log;


import com.example.alwayslike.jihuo.Jihuo;
import com.example.alwayslike.jihuo.util.Constant;
import com.example.alwayslike.jihuo.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yyx on 2016/11/17.
 */
public class HttpClient {
    public static final String TAG = "HttpClient";

    public static interface HttpListener {
        void onSuccess(String response);

        void onFail(Exception e);
    }

    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 5, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(80));

    public void sendRequest(final String pushdata, final HttpListener listener) {

        try {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    URL url = null;
                    HttpURLConnection conn = null;
                    BufferedReader reader = null;
                    PrintWriter writer = null;
                    try {
                        url = new URL(Constant.Address.HTTP_ADDRESS );
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "close");
                        conn.setRequestProperty("Accept-Encoding", "utf-8");
                        conn.setRequestProperty("Content-Type",
                                "application/json");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setConnectTimeout(50 * 1000);
                        conn.setReadTimeout(50 * 1000);
                        conn.connect();


                        writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()));
                        writer.write(pushdata);
                        writer.flush();

                        if (conn.getResponseCode() == 200) {

                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder result = new StringBuilder();
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }

                            String resultStr = result.toString();
//
//                            if (result.length() > 16)
//                                resultStr = result.substring(9, result.length() - 7);

                            if (Jihuo.DEBUG) {
                                LogUtil.getInstance().saveFile(resultStr);
                                Log.i(TAG, resultStr);
                            }

                            if (listener != null) {
                                listener.onSuccess(resultStr);
                            }

                        } else {

                            if (listener != null) {
                                Log.i(TAG, "响应码：" + conn.getResponseCode());
                                listener.onFail(new ErrorResponseCodeException("错误响应码：" + conn.getResponseCode()));
                            }

                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        if (listener != null)
                            listener.onFail(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (listener != null)
                            listener.onFail(e);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (listener != null)
                            listener.onFail(e);
                    } finally {

                        if (writer != null)
                            writer.close();

                        try {
                            if (reader != null)
                                reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    }

                }
            };
            poolExecutor.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
