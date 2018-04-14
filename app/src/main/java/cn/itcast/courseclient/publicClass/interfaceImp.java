package cn.itcast.courseclient.publicClass;

import cn.itcast.courseclient.client.clientImp;
import cn.itcast.courseclient.client.clientInterface;

public class interfaceImp {

    private interfaceImp(){}
    private static interfaceImp interfaceImp = new interfaceImp();
    public static interfaceImp getInterfaceImp(){
        return interfaceImp;
    }

    /*clientInterface*/
    private clientInterface client = new clientImp();
    public clientInterface getClient(){
        return client;
    }
}
