package com.example.gesparkmove;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5Tools {
    public String encode(String msg){
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(msg.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++){
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
