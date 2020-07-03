package com.example.gesparkmove;

import android.app.Application;

//class que guarda os paths e credenciais para a base de dados e o SSH
public class Globals extends Application {

    public String getSshPass() {
        return "Hy27#ABpaF$PbnYY";
    }

    public String getSshUsername() {
        return "gpark";
    }

    public String getSshHost() {
        return "92.222.70.24";
    }

    public int getSshPort() {
        return 58022;
    }

    public int getSshPFLPort() {
        return 3306;
    }

    public String getSshPFHost() {
        return "127.0.0.1";
    }

    public int getSshPFRPort() {
        return 3306;
    }

    public String getMySqlPass() {
        return "GespPW01";
    }

    public String getMySqlUsername() {
        return "gremote";
    }

    public String getMySqlUrl() {
        return "jdbc:mysql://127.0.0.1:3306/cgs_gespark?autoReconnect=true&useSSL=false";
    }

    public String getMailUsername() {
        return "info.gespark@gespark.pt";}

    public String getMailPass() {
        return "3xSMg8nSgUNB";}
}