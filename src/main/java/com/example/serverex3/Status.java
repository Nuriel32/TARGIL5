package com.example.serverex3;

import org.apache.catalina.core.AprLifecycleListener;

public enum Status {
    PENDING(0),
    LATE(1),
    DONE(2);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
