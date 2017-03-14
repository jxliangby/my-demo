package com.kissjava.test.rsa;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Coder {  
  
    /** 
     * Base64解码 
     * @param key 
     * @return 
     */  
    public static byte[] decryptBASE64(String key)throws Exception {  
    	return (new BASE64Decoder()).decodeBuffer(key);
    }  
      
    /** 
     * Base64编码 
     * @param sign 
     * @return 
     */  
    public static String encryptBASE64(byte[] sign){  
    	return (new BASE64Encoder()).encodeBuffer(sign);
    } 
    


}  