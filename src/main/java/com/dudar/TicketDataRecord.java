package com.dudar;

import javafx.beans.property.SimpleStringProperty;

public class TicketDataRecord {
    private final SimpleStringProperty authorName;
    private final SimpleStringProperty ticketNumber;
    private final SimpleStringProperty ticketSummary;
    private final SimpleStringProperty loggedHours;

    public TicketDataRecord(String name, String number, String summary, String hours) {
        this.authorName = new SimpleStringProperty(name);
        this.ticketNumber = new SimpleStringProperty(number);
        this.ticketSummary = new SimpleStringProperty(summary);
        this.loggedHours = new SimpleStringProperty(hours);
    }

    public String getAuthorName() {
        return authorName.get();
    }

    public void setAuthorName(String fName) {
        authorName.set(fName);
    }

    public String getTicketNumber() {
        return ticketNumber.get();
    }

    public void setTicketNumber(String fName) {
        ticketNumber.set(fName);
    }

    public String getTicketSummary() {
        return ticketSummary.get();
    }

    public void setTicketSummary(String fName) {
        ticketSummary.set(fName);
    }

    public String getLoggedHours() {
        return loggedHours.get();
    }

    public void setLoggedHours(String fName) {
        loggedHours.set(fName);
    }
}

