package com.example.serverex3;

public class RequestCounter {
    private static int requestNumber = 0;

    public static int getRequestNumber() {
        return requestNumber;
    }

    public static void incrementRequestNumber() {
        requestNumber++;
    }
}
