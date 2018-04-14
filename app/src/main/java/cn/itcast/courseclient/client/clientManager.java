package cn.itcast.courseclient.client;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import org.json.JSONObject;

import cn.itcast.courseclient.publicClass.activityImp;
import cn.itcast.courseclient.publicClass.desImp;
import cn.itcast.courseclient.publicClass.interfaceImp;

public class clientManager {

    private clientManager(){}
    private static clientManager CM = new clientManager();
    public static clientManager getCM(){
        return CM;
    }

    public void CONACK(String value){
        String randomKeyStr = desImp.getDes().decry(
                clientInfo.getClient().getMainKey()
                ,value);
        String[] randomKeyChars = randomKeyStr.split(",");
        byte[] randomKey = new byte[randomKeyChars.length];
        for(int i=0;i<randomKeyChars.length;i++){
            randomKey[i] = (byte)(Integer.parseInt(randomKeyChars[i]));
        }
        clientInfo.getClient().setRandomKey(randomKey);
    }

    public void SIGNRSP(String value){
        try{
            final JSONObject result = new JSONObject(
                    desImp.getDes().decry(
                            clientInfo.getClient().getRandomKey(),
                            value));
            clientInfo.getClient().setTimeActivity(
                    result.getInt("keepTime"),
                    result.getInt("courseTime"),
                    result.getInt("score")
            );
            if(result.getInt("score") > 100){
                Dialog dialog = new AlertDialog
                        .Builder(activityImp.getActivity().getTimeActivity())
                        .setTitle("签到提示")
                        .setMessage("已经获得满分，下节课继续努力")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .show();
            } else {
                Dialog dialog = new AlertDialog
                        .Builder(activityImp.getActivity().getSignActivity())
                        .setTitle("签到提示")
                        .setMessage(result.getString("signrsp"))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    if(result.getString("signrsp").equals("签到成功，请认真上课") ||
                                            result.getString("signrsp").equals("返回计时成功，请认真上课")){
                                        activityImp.getActivity().getSignActivity().changeToTimeActivity();
                                    }
                                } catch(Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        })
                        .show();
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void TIMERSP(String value) {
        try{
            final JSONObject result = new JSONObject(
                    desImp.getDes().decry(
                            clientInfo.getClient().getRandomKey(),
                            value));
            clientInfo.getClient().setTimeActivity(
                    result.getInt("keepTime"),
                    result.getInt("courseTime"),
                    result.getInt("score")
            );
            activityImp.getActivity().getTimeActivity().setMinutePro(
                    result.getInt("keepTime"),
                    result.getInt("courseTime"),
                    result.getInt("score")
            );
            if(result.getInt("score") > 100){
                Dialog dialog = new AlertDialog
                        .Builder(activityImp.getActivity().getTimeActivity())
                        .setTitle("签到提示")
                        .setMessage("已经获得满分，下节课继续努力")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
