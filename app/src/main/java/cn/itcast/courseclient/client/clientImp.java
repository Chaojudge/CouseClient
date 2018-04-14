package cn.itcast.courseclient.client;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.courseclient.SignActivity;
import cn.itcast.courseclient.publicClass.activityImp;
import cn.itcast.courseclient.publicClass.desImp;
import cn.itcast.courseclient.publicClass.vibratorUtil;

public class clientImp implements clientInterface{

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private void setMainKey(){
        byte[] mainKey = new byte[8];
        int all = clientInfo.getClient().getBuildNum()
                + clientInfo.getClient().getFloorNum()
                + clientInfo.getClient().getClassNum();
        mainKey[0] = (byte)(all + clientInfo.getClient().getBuildNum());
        mainKey[1] = (byte)(all + clientInfo.getClient().getFloorNum());
        mainKey[2] = (byte)(all + clientInfo.getClient().getClassNum());
        mainKey[3] = (byte)(all);
        mainKey[4] = (byte)(all - clientInfo.getClient().getBuildNum());
        mainKey[5] = (byte)(all - clientInfo.getClient().getFloorNum());
        mainKey[6] = (byte)(all - clientInfo.getClient().getClassNum());
        mainKey[7] = (byte)(-all);
        clientInfo.getClient().setMainKey(mainKey);
    }

    public void setTelephone(String studentNum,String tele,String mac){
        clientInfo.getClient().setSignActivity(studentNum,tele,mac);
    }

    public void setMainActivityInfo(int buildNum,int floorNum,int classNum,String serverIP,int serverPORT){
        // 保存MainActivity页面数据
        clientInfo.getClient().setMainActivityInfo(buildNum,floorNum,classNum,serverIP,serverPORT);
        setMainKey();
    }

    public void connectServer(){
        new Thread(){
            @Override
            public void run(){
                try{
                    socket = new Socket(
                            clientInfo.getClient().getServerIP(),
                            clientInfo.getClient().getServerPORT());
                    writer = new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));
                    reader = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
                    String line = "";
                    String all= "";
                    while((line = reader.readLine()) != null){
                        all += line;
                        if(line.indexOf("END") > -1){
                            all = all.replace("END","");
                            Bundle bundle = new Bundle();
                            bundle.putString("msg",all);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.setData(bundle);
                            UI.sendMessage(msg);
                            all = "";
                        }
                    }
                    writer.close();
                    reader.close();
                    writer = null;
                    reader = null;
                } catch(Exception ex){
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","客户端端口连接,原因："+ex);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.setData(bundle);
                    UI.sendMessage(msg);
                }
            }
        }.start();
    }

    private Handler UI = new Handler(){
        Dialog dialog;
        @Override
        public void handleMessage(Message msg) {
            try{
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        Bundle bundle = msg.getData();
                        String all = bundle.getString("msg");
                        //
                        System.out.println("数据："+all);
                        //
                        final JSONObject rsp = new JSONObject(all);
                        if(rsp.getString("signal").equals("CONACK")){
                            //客户端获取服务端发送randomKey的密文后解密并保存到clientInfo类中
                            clientManager.getCM().CONACK(rsp.getString("result"));
                            if(rsp.getString("type").equals("sign")){
                                clientInfo.getClient().setIf_time(false);
                            } else if(rsp.getString("type").equals("time")){
                                clientInfo.getClient().setIf_time(true);
                            }
                            activityImp.getActivity().getMainActivity().changeToSignActivity();
                        } else if(rsp.getString("signal").equals("SIGNRSP")){
                            if(rsp.getString("type").equals("sign")){
                                JSONObject signrsp = new JSONObject(
                                        desImp.getDes().decry(
                                                clientInfo.getClient().getRandomKey(),
                                                rsp.getString("result")));
                                dialog = new AlertDialog
                                        .Builder(activityImp.getActivity().getMainActivity())
                                        .setTitle("签到提示")
                                        .setMessage(signrsp.getString("signrsp"))
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                try{
                                                    if(rsp.getString("signrsp").equals("签到成功，请认真上课")){
                                                        System.exit(0);
                                                    }
                                                } catch(Exception ex){
                                                    ex.printStackTrace();
                                                }
                                            }
                                        })
                                        .show();

                            } else if(rsp.getString("type").equals("time")){
                                clientManager.getCM().SIGNRSP(rsp.getString("result"));
                            }
                        } else if(rsp.getString("signal").equals("TIMERSP")){
                            clientManager.getCM().TIMERSP(rsp.getString("result"));
                        } else if(rsp.getString("signal").equals("classEnd")){
                            timer.cancel();
                            dialog = new AlertDialog
                                    .Builder(activityImp.getActivity().getTimeActivity())
                                    .setTitle("计时提示")
                                    .setMessage("下课结束")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            System.exit(0);
                                        }
                                    })
                                    .show();
                        };break;
                        case 1:
                            Bundle exBundle = msg.getData();
                            String result = exBundle.getString("msg");
                            final long[] time = {500,1000,500,1000,500,1000,500,1000,500,1000};
                            vibratorUtil.Vibrate(activityImp.getActivity().getMainActivity(), time,true,false);
                            dialog = new AlertDialog
                                    .Builder(activityImp.getActivity().getMainActivity())
                                    .setCancelable(false)
                                    .setTitle("异常提示")
                                    .setMessage(result)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            vibratorUtil.Vibrate(activityImp.getActivity().getMainActivity(), time,true,true);
                                        }
                                    })
                                    .show();
                            break;
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    };

    public void sign(){
        if(writer != null){
            try{
                JSONObject signMsg = new JSONObject();
                signMsg.put("studentNum",clientInfo.getClient().getStudentNum());
                signMsg.put("telephoneNum",clientInfo.getClient().getTele());
                signMsg.put("telephoneMAC", clientInfo.getClient().getMac());
                String encrySignMsg = desImp.getDes().encry(
                        clientInfo.getClient().getRandomKey(),
                        signMsg.toString());
                JSONObject result = new JSONObject();
                result.put("signal","SIGNREQ");
                result.put("result",encrySignMsg);
                //
                System.out.println(result.toString());
                //
                writer.write(result.toString()+"END"+"\n");
                writer.flush();
            } catch(Exception ex){
                Dialog dialog = new AlertDialog
                        .Builder(activityImp.getActivity().getSignActivity())
                        .setTitle("签到提示")
                        .setMessage("异常错误"+ex)
                        .setPositiveButton("确定",null)
                        .show();
                ex.printStackTrace();
            }

        } else {
            Dialog dialog = new AlertDialog
                    .Builder(activityImp.getActivity().getSignActivity())
                    .setTitle("签到提示")
                    .setMessage("客户端连接中断")
                    .setPositiveButton("确定",null)
                    .show();
        }
    }

    int second = 0;
    int minute = 0;
    JSONObject timereq = new JSONObject();
    JSONObject result = new JSONObject();
    private final Timer timer = new Timer();
    public void time(){
        second = clientInfo.getClient().getKeepTime() * 60;
        minute = clientInfo.getClient().getKeepTime();
        try{
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    second ++;
                    activityImp.getActivity().getTimeActivity().setSecondPro(second%60);
                    minute = second / 60;
                    if(second % 60 == 0){
                        try{
                            result.put("studentNum",clientInfo.getClient().getStudentNum());
                            result.put("keepTime",minute);
                            timereq.put("signal","TIMEREQ");
                            timereq.put("result",desImp.getDes().encry(
                                    clientInfo.getClient().getRandomKey(),
                                    result.toString()));
                            writer.write(timereq+"END"+"\n");
                            writer.flush();
                        } catch(Exception ex){
                            timer.cancel();
                            ex.printStackTrace();
                        }
                    }
                }
            },1000,1000);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void cancelTimer(){
        timer.cancel();
    }
}
