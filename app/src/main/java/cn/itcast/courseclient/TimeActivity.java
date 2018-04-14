package cn.itcast.courseclient;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;

import cn.itcast.courseclient.client.clientInfo;
import cn.itcast.courseclient.publicClass.activityImp;
import cn.itcast.courseclient.publicClass.interfaceImp;

public class TimeActivity extends AppCompatActivity {
    private ProgressBar pro_minute ;
    private ProgressBar pro_second;
    private TextView txt_score;
    private TextView txt_minute;
    private TextView txt_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityImp.getActivity().setTimeActivity(this, TimeActivity.this);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.activity_time);
        pro_minute = (ProgressBar) findViewById(R.id.pro_minute);
        pro_second = (ProgressBar) findViewById(R.id.pro_second);
        txt_score = (TextView) findViewById(R.id.txt_score);
        txt_minute = (TextView) findViewById(R.id.txt_minute);
        setMinutePro(
                clientInfo.getClient().getKeepTime(),
                clientInfo.getClient().getCourseTime(),
                clientInfo.getClient().getScore()
        );
        interfaceImp.getInterfaceImp().getClient().time();
    }

    public void setMinutePro(int keepTime,int courseTime,int score){
        pro_minute.setMax(courseTime);
        pro_minute.setProgress(keepTime);
        txt_score.setText("当前签到分数:            "+score);
        txt_minute.setText("时间/总时间(分钟):    "+keepTime+"/"+courseTime);
    }

    public void setSecondPro(int value){
        pro_second.setMax(60);
        pro_second.setProgress(value);
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
