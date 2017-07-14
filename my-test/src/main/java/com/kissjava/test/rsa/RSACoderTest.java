package com.kissjava.test.rsa;

import java.util.Map;     

public class RSACoderTest {
	private String publicKey;     
    private String privateKey;     
  
    public void setUp() throws Exception {     
        Map<String, Object> keyMap = RSACoder.initKey();     
  
        publicKey = RSACoder.getPublicKey(keyMap);     
        privateKey = RSACoder.getPrivateKey(keyMap);     
        System.err.println("公钥: \n\r" + publicKey);     
        System.err.println("私钥： \n\r" + privateKey);     
    }     
  
    public void test() throws Exception {     
        System.err.println("公钥加密——私钥解密");     
        String inputStr = "abc";     
        byte[] data = inputStr.getBytes();     
  
        byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);     
  
        byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData, privateKey);     
  
        String outputStr = new String(decodedData);     
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);     
        assertEquals(inputStr, outputStr);     
  
    }     
  
    public void testSign() throws Exception {     
        System.err.println("私钥加密——公钥解密");     
        String inputStr = "sign";     
        byte[] data = inputStr.getBytes();     
  
        byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);     
  
        byte[] decodedData = RSACoder.decryptByPublicKey(encodedData, publicKey);     
  
        String outputStr = new String(decodedData);     
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);     
        assertEquals(inputStr, outputStr);     
  
        System.err.println("私钥签名——公钥验证签名");     
        // 产生签名     
        String sign = RSACoder.sign(encodedData, privateKey);     
        System.err.println("签名:\r" + sign);     
  
        // 验证签名     
        boolean status = RSACoder.verify(encodedData, publicKey, sign);     
        System.err.println("状态:\r" + status);     
        assertTrue(status);     
    }   
    
    private void assertEquals(String str1, String str2){
    	System.out.println(str1.compareTo(str2));
    }
    
    private void assertTrue(boolean bool){
    	System.out.println(Boolean.compare(bool, true));
    }
    
    public static void main(String[] args) throws Exception{
    	RSACoderTest test = new RSACoderTest();
    	test.setUp();
    	//test.test();
    	test.testSign();
    	String sn = "2017-03-06 15:58:29:838F7C9170B4EE6A065E2FA4E46F80051D1123456789qw";
    	System.out.println(sn.substring(0, 64));
    }
}
