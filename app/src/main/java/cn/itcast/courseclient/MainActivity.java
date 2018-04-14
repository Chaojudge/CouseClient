package cn.itcast.courseclient;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import cn.itcast.courseclient.publicClass.activityImp;
import cn.itcast.courseclient.publicClass.interfaceImp;

public class MainActivity extends AppCompatActivity {

    private EditText et_buildNum;
    private EditText et_floorNum;
    private EditText et_classNum;
    private EditText et_serverIP;
    private EditText et_serverPORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.activity_main);
        et_buildNum = (EditText)findViewById(R.id.et_buildNum);
        et_floorNum = (EditText)findViewById(R.id.et_floorNum);
        et_classNum = (EditText)findViewById(R.id.et_classNum);
        et_serverIP = (EditText)findViewById(R.id.et_serverIP);
        et_serverPORT = (EditText)findViewById(R.id.et_serverPORT);
        et_buildNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_floorNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_classNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_serverPORT.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        activityImp.getActivity().setMainActivity(this,MainActivity.this);
        getServerIp();
    }



    public void cancelEvent(View view){
        // 正常退出程序
        System.exit(0);
    }

    public void connectionEvent(View view){

        // 连接PC服务端

        Dialog tip ;
        if(et_buildNum.getText().toString().equals("")){
            tip = new AlertDialog
                    .Builder(this)
                    .setTitle("连接提示")
                    .setMessage("请输入楼号")
                    .setPositiveButton("确定",null)
                    .show();
        } else if(et_floorNum.getText().toString().equals("")){
            tip = new AlertDialog
                    .Builder(this)
                    .setTitle("连接提示")
                    .setMessage("请输入第几层")
                    .setPositiveButton("确定",null)
                    .show();
        } else if(et_classNum.getText().toString().equals("")){
            tip = new AlertDialog
                    .Builder(this)
                    .setTitle("连接提示")
                    .setMessage("请输入第几课室")
                    .setPositiveButton("确定",null)
                    .show();
        } else if(et_serverIP.getText().toString().equals("")){
            tip = new AlertDialog
                    .Builder(this)
                    .setTitle("连接提示")
                    .setMessage("请输入连接服务端的IP")
                    .setPositiveButton("确定",null)
                    .show();
        } else if(et_serverPORT.getText().toString().equals("")){
            tip = new AlertDialog
                    .Builder(this)
                    .setTitle("连接提示")
                    .setMessage("请输入连接服务端端口")
                    .setPositiveButton("确定",null)
                    .show();
        } else {
            /* 1.保存信息
            *  2.启动连接线程
            *  3.连接成功后跳转到签到页面
            * */
            interfaceImp.getInterfaceImp().getClient().setMainActivityInfo(
                    Integer.parseInt(et_buildNum.getText().toString()),
                    Integer.parseInt(et_floorNum.getText().toString()),
                    Integer.parseInt(et_classNum.getText().toString()),
                    et_serverIP.getText().toString(),
                    Integer.parseInt(et_serverPORT.getText().toString())
            );
            interfaceImp.getInterfaceImp().getClient().connectServer();
        }
    }

    private void getServerIp(){
        try{
            String serverIP = null;
            WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            if(null == serverIP){
                serverIP = "";
            }
            int ipAddress = wifiInfo.getIpAddress();
            serverIP = String.format("%d.%d.%d.1",
                    (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff));
            et_serverIP.setText(serverIP);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void changeToSignActivity(){
        Intent intent = new Intent();
        intent.setClass(this,SignActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //listen return key of function
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Dialog dialog = new AlertDialog.Builder(this)
                    .setTitle("退出提示")
                    .setMessage("是否退出该应用")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else if(keyCode == KeyEvent.KEYCODE_HOME){
            interfaceImp.getInterfaceImp().getClient().cancelTimer();
            System.exit(0);
        } else if(keyCode == KeyEvent.KEYCODE_MENU){
            interfaceImp.getInterfaceImp().getClient().cancelTimer();
            System.exit(0);
        }
        return false;
    }
}
