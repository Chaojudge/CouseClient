package cn.itcast.courseclient.publicClass;

import android.app.Activity;
import android.content.Context;

import cn.itcast.courseclient.MainActivity;
import cn.itcast.courseclient.SignActivity;
import cn.itcast.courseclient.TimeActivity;

public class activityImp {
    private activityImp(){}
    private static activityImp activity = new activityImp();
    public static activityImp getActivity(){
        return activity;
    }

    /*MainActivity*/
    private Context mainContext;
    private MainActivity mainActivity;
    public void setMainActivity(Context mainContext,Activity mainActivity){
        this.mainContext = (MainActivity) mainContext;
        this.mainActivity = (MainActivity) mainActivity;
    }
    public Context getMainContext(){
        return mainContext;
    }
    public MainActivity getMainActivity(){
        return mainActivity;
    }

    /*SignActivity*/
    private Context signContext;
    private SignActivity signActivity;
    public void setSignActivity(Context signContext,Activity signActivity){
        this.signContext = (SignActivity) signContext;
        this.signActivity = (SignActivity) signActivity;
    }
    public Context getSignContext(){
        return signContext;
    }
    public SignActivity getSignActivity(){
        return signActivity;
    }

    /*TimeActivity*/
    private Context timeContext;
    private TimeActivity timeActivity;
    public void setTimeActivity(Context timeContext,Activity timeActivity){
        this.timeContext = (TimeActivity) timeContext;
        this.timeActivity = (TimeActivity) timeActivity;
    }
    public Context getTimeContext(){
        return timeContext;
    }
    public TimeActivity getTimeActivity(){
        return timeActivity;
    }
}
