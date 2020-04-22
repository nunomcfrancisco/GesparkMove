package com.example.gesparkmove;

import android.app.Application;

public class Globals extends Application {
    private int userID;
    private String sshPass = "GespPW01";
    private String sshUsername = "gpark";
    private String sshHost = "92.222.70.24";
    private int sshPort = 58022;
    private int sshPFLPort = 3306;
    private String sshPFHost = "127.0.0.1";
    private int sshPFRPort = 3306;
    private String mySqlPass = "GespPW01";
    private String mySqlUsername = "gremote";
    private String mySqlUrl = "jdbc:mysql://127.0.0.1:3306/cgs_gespark";

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getSshPass() {
        return sshPass;
    }

    public String getSshUsername() {
        return sshUsername;
    }

    public String getSshHost() {
        return sshHost;
    }

    public int getSshPort() {
        return sshPort;
    }

    public int getSshPFLPort() {
        return sshPFLPort;
    }

    public String getSshPFHost() {
        return sshPFHost;
    }

    public int getSshPFRPort() {
        return sshPFRPort;
    }

    public String getMySqlPass() {
        return mySqlPass;
    }

    public String getMySqlUsername() {
        return mySqlUsername;
    }

    public String getMySqlUrl() {
        return mySqlUrl;
    }
}