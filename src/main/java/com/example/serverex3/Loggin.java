package com.example.serverex3;
import java.io.File;
import java.io.IOException;

public class Loggin {
    String datetime;
    String loglevel;
    String logmessage;
    int requestnumber;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    public String getLogmessage() {
        return logmessage;
    }

    public void setLogmessage(String logmessage) {
        this.logmessage = logmessage;
    }

    public int getRequestnumber() {
        return requestnumber;
    }

    public void setRequestnumber(int requestnumber) {
        this.requestnumber = requestnumber;
    }

    public static void createLogFolder() {
        String serverPath = System.getProperty("user.dir");
        File logsFolder = new File(serverPath, "Logs");

        if (logsFolder.exists()) {
            System.out.println("Logs folder already exists.");
        } else {
            if (logsFolder.mkdir()) {
                System.out.println("Logs folder created successfully.");
            } else {
                System.out.println("Failed to create Logs folder.");
            }
        }
    }



}


