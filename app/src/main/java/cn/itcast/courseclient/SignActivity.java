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
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import cn.itcast.courseclient.publicClass.activityImp;
import cn.itcast.courseclient.publicClass.interfaceImp;

public class SignActivity extends AppCompatActivity {

    private EditText et_studentNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.activity_sign);
        activityImp.getActivity().setSignActivity(this,SignActivity.this);
        et_studentNum = (EditText) findViewById(R.id.et_studentNum);
        et_studentNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
    }

    public void changeToTimeActivity(){
        Intent intent = new Intent();
        intent.setClass(SignActivity.this,TimeActivity.class);
        startActivity(intent);
        this.finish();
    }


    private void setTeleInfo(String studentNum){
        try{
            String tele= null;
            String mac=null;
            TelephonyManager mTele = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            if(null == tele || null == mac){
                interfaceImp.getInterfaceImp().getClient().setTelephone("","","");
            }
            interfaceImp.getInterfaceImp().getClient().setTelephone(
                    studentNum,
                    mTele.getLine1Number(),
                    wifiInfo.getMacAddress());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void signEvent(View view){
        if(et_studentNum.getText().toString().equals("")){
            Dialog dialog = new AlertDialog
                    .Builder(this)
                    .setTitle("签到提示")
                    .setMessage("请输入学号")
                    .setPositiveButton("确定",null)
                    .show();
        } else {
            setTeleInfo(et_studentNum.getText().toString());
            interfaceImp.getInterfaceImp().getClient().sign();
        }
    }

    public void returnEvent(View view){
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
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
                           System.exit(0);
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
