package cn.itcast.courseclient.client;

public interface clientInterface {

    public void setMainActivityInfo(int buildNum,int floorNum,int classNum,String serverIP,int serverPORT);
    public void setTelephone(String studentNum,String tele,String mac);
    public void connectServer();
    public void sign();
    public void time();
    public void cancelTimer();
}
