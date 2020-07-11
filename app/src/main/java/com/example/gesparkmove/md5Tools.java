package com.example.gesparkmove;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//classe para aplicar o MD5 nas passwords antes de serem guardadas na base de dados
public class md5Tools {
    public String encode(String msg){
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = digest.digest(msg.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return String.format("%032x", number);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
