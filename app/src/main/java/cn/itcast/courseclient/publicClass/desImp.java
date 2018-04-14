package cn.itcast.courseclient.publicClass;


import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class desImp {

    private desImp(){}
    private static desImp des = new desImp();
    public static desImp getDes(){
        return des;
    }

    public String encry(byte[] Key,String value){
        //加密方法
        String encryStr = "";
        try{
            /*从DES_KEY中获取可信任的随机数源*/
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(Key);
            //创建一个密钥工厂，把DESKeySpec转换成SecretKey
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key,sr);
            encryStr = new cn.itcast
                    .courseclient.misc
                    .BASE64Encoder().encode(
                    cipher.doFinal(
                            value.getBytes()
                    ));
        } catch(Exception ex){
            System.out.println("加密失败"+ex);
        }
        return encryStr;
    }

    public String decry(byte[] Key,String value){
        //解密方法
        String decryStr = "";
        try{
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec(Key);
            //创建一个密钥工厂，把DESKeySpec转换成SecretKey
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key,sr);
            decryStr = new String(cipher.doFinal(new cn.itcast
                    .courseclient.misc
                    .BASE64Decoder().decodeBuffer(
                    value)));
        } catch(Exception ex){
            System.out.println("加密失败"+ex);
        }
        return decryStr;
    }
}
