package com.dudar;

public class ConnectionData {
    private String url;
    private String user;
    private String pass;

    public ConnectionData(String url, String user, String pass){
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public String getUrl(){
        return this.url;
    }

    public String getUser(){
        return this.user;
    }

    public String getPass(){
        return this.pass;
    }
}
