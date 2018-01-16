package com.dudar;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.util.HashMap;
import java.util.Map;

public class Record {
    Long id;
    String number;
    String summary;
    Map<String, Long> logwork;

    public Record(Long id, String number, String summery, String workLogAuthor, long loggedTime){
        this.id = id;
        this.number = number;
        this.summary = summery;
        logwork = new HashMap<>();
        if(!workLogAuthor.isEmpty()){
            logwork.put(workLogAuthor, loggedTime);
        }
    }

    public void updateAuthorAndTime(String author, long time){
        logwork.put(author, logwork.containsKey(author) ? logwork.get(author)+time : time);
    }

    public void print(){
        System.out.println(this.id);
        System.out.println(this.number);
        System.out.println(this.summary);
        for(String id : logwork.keySet()){
            System.out.println("Author: " + id);
            System.out.println("Logged time: "+ DurationFormatUtils.formatDuration(logwork.get(id)*1000, "HH:mm:ss"));
        }
        System.out.println("***************************");
    }
}
