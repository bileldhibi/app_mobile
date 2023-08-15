package com.example.myapplication;

public class Claim {
    String machineName, status, timestamp;

    public Claim(String machineName, String status, String timestamp) {
        this.machineName = machineName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Claim() {
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
