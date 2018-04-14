package cn.itcast.courseclient.client;

public class clientInfo {

    /*MainActivity*/
    private int buildNum;
    private int floorNum;
    private int classNum;
    private String serverIP;
    private int serverPORT;

    /*SignActivity*/
    private String studentNum;
    private String tele;
    private String mac;

    /*TimeActivity*/
    private int keepTime;                   //已经计时的时间
    private int courseTime;                 //课堂上课时间段
    private int score;

    /*client*/
    private byte[] randomKey = new byte[8];
    private byte[] mainKey = new byte[8];

    private boolean if_time;                //是否开启计时模式

    private clientInfo(){}
    private static clientInfo client = new clientInfo();
    public static clientInfo getClient(){
        return client;
    }

    /*SET*/
    public void setMainActivityInfo(int buildNum,int floorNum,int classNum,String serverIP,int serverPORT){
        this.buildNum = buildNum;
        this.floorNum = floorNum;
        this.classNum = classNum;
        this.serverIP = serverIP;
        this.serverPORT = serverPORT;
    }

    public void setSignActivity(String studentNum,String tele,String mac){
        this.studentNum = studentNum;
        this.tele = tele;
        this.mac = mac;
    }

    public void setTimeActivity(int keepTime,int courseTime,int score){
        this.keepTime = keepTime;
        this.courseTime = courseTime;
        this.score = score;
    }

    public void setRandomKey(byte[] randomKey){
        this.randomKey = randomKey;
    }

    public void setMainKey(byte[] mainKey){
        this.mainKey = mainKey;
    }

    public void setIf_time(boolean if_time){
        this.if_time = if_time;
    }

    /*GET*/
    public int getBuildNum(){
        return buildNum;
    }

    public int getFloorNum(){
        return floorNum;
    }

    public int getClassNum(){
        return classNum;
    }

    public String getServerIP(){
        return serverIP;
    }

    public int getServerPORT(){
        return serverPORT;
    }

    public String getStudentNum(){
        return studentNum;
    }

    public String getTele(){
        return tele;
    }

    public String getMac(){
        return mac;
    }

    public int getKeepTime(){
        return keepTime;
    }

    public int getCourseTime(){
        return courseTime;
    }

    public byte[] getRandomKey(){
        return randomKey;
    }

    public byte[] getMainKey(){
        return mainKey;
    }

    public boolean getIf_time(){ return if_time; }

    public int getScore(){
        return score;
    }
}
