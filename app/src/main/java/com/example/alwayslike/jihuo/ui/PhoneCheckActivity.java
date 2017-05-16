package com.example.alwayslike.jihuo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alwayslike.jihuo.Jihuo;
import com.example.alwayslike.jihuo.R;
import com.example.alwayslike.jihuo.bean.BaseBean;
import com.example.alwayslike.jihuo.bean.VST5ReqBean;
import com.example.alwayslike.jihuo.bean.VST5RspBean;
import com.example.alwayslike.jihuo.http.HttpClient;
import com.example.alwayslike.jihuo.http.ParamsHelper;
import com.example.alwayslike.jihuo.util.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.alwayslike.jihuo.util.Constant.Key.BST5;
import static com.example.alwayslike.jihuo.util.Constant.Key.VST5;

/**
 * Created by alwayslike on 2017/5/16.
 */

public class PhoneCheckActivity extends BaseActivity{
    static int Totol_Send_bytes = 0;
    private TextView textView;
    static String SendString = "0123";
    private List<Integer> integers=new ArrayList<>();
    private List<Byte> sendBytes=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonecheck);
        initView();
        sendDataToOBU();
        sendDataToServer();
    }
    public Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(PhoneCheckActivity.this,"手机认证成功",Toast.LENGTH_LONG);
                    textView.setText("手机认证成功");
                    PhoneCheckActivity.this.setResult(RESULT_OK);
                    PhoneCheckActivity.this.finish();
                    break;
                case 0:
                    Toast.makeText(PhoneCheckActivity.this,"手机认证失败",Toast.LENGTH_LONG);

                    break;
                case 2:
                    Toast.makeText(PhoneCheckActivity.this,"系统错误",Toast.LENGTH_LONG);
                    break;
                case 3:
                    Toast.makeText(PhoneCheckActivity.this,"获取参数错误",Toast.LENGTH_LONG);

                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                break;
        }
        return true;

    }
    public void initView(){

        textView=(TextView)findViewById(R.id.phonecheck);
        textView.setText("正在进行手机认证");
    }
    private void Send_Bytes(byte bytes[])
    {
        // 分包发送 每包最大18个字节
        int total_len = bytes.length;
        int cur_pos = 0;
        int i = 0;
        while(cur_pos<total_len)
        {
            int lenSub = 0;
            if(total_len-cur_pos>18)
                lenSub = 18;
            else
                lenSub = total_len - cur_pos;

            byte[] bytes_sub = new byte[lenSub];

            for(i = 0; i<lenSub; i++)
            {
                bytes_sub[i] = bytes[cur_pos + i];
            }
            cur_pos += lenSub;
            ConnectToOBUAcitivity.writeChar6_in_bytes(bytes_sub);

            // 延时 一会
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    }
   public  void sendDataToOBU(){
       if (true) {
           // 转成 16进制发送
           // 检测是否是 16进制
           if(true)
           {
               boolean hex_flag = true;

               String s1 = BST5;
               for (int i = 0; i < s1.length(); i++) {
                   char charV = s1.charAt(i);
                   if((charV >= '0' && charV <= '9') || (charV >= 'a' && charV <= 'f') || (charV >= 'A' && charV <= 'F'))
                   {

                   }
                   else
                   {
                       hex_flag = false;
                   }
               }

               if(hex_flag)
               {
                   if(0 == s1.length()%2)
                   {
                       byte bytes[] = Utils.hexStringToBytes(s1);

                       // 分包发送 每包最大18个字节
                       Send_Bytes(bytes);

                       Totol_Send_bytes += (s1.length()/2);

                       SendString = BST5;
                   }
                   else
                   {
                       Toast toast = Toast.makeText(getApplicationContext(), "【错误】: 输入的不是完整的 16进制",
                               Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();
                   }
               }
               else
               {
                   Toast toast = Toast.makeText(getApplicationContext(), "【错误】: 输入的字符不是 16进制",
                           Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER, 0, 0);
                   toast.show();
               }
           }
           else
           {
               String s1 = BST5;
               byte bytes[] = s1.getBytes();

               // 分包发送 每包最大18个字节
               Send_Bytes(bytes);


               Totol_Send_bytes += s1.length();

               SendString = BST5;
           }
       }
   }
    /**
     *反转义和校验
     * */
    /**
     * 转义
     * */
    public void Escape(byte[] bytes){


    }
    public void startCheck(){
        Byte flag=Byte.valueOf((byte)0xFF);

        if (Jihuo.recBytes.contains(flag)){
            for (int i=0;i<Jihuo.recBytes.size();i++)
            {
                if(Jihuo.recBytes.get(i)==flag){
                    if(Jihuo.recBytes.get(i+1)==flag){

                        integers.add(Integer.valueOf(i));
                        }
                    }
                }
            }



    }
    public void sendDataToServer(){
        final VST5ReqBean vst5ReqBean=new VST5ReqBean(VST5);
        new HttpClient().sendRequest(ParamsHelper.getVST5Params(vst5ReqBean), new HttpClient.HttpListener() {
            @Override
            public void onSuccess(String response) {
                BaseBean baseBean=new Gson().fromJson(response,BaseBean.class);
                VST5RspBean vst5RspBean=new Gson().fromJson(baseBean.getData(),VST5RspBean.class);
                if(vst5RspBean.getStatus()=="1"){
                    mhandler.sendEmptyMessage(1);
                }
                else{
                    mhandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onFail(Exception e) {

                mhandler.sendEmptyMessage(3);
            }
        });
    }


}
