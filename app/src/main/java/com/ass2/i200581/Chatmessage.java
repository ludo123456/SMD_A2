package com.ass2.i200581;

public class Chatmessage {
    private String sender;
    private String recipient;
    private String text;
    private long timestamp;

    // Constructors, getters, and setters

    // Default constructor (required for Firebase)
    public Chatmessage() {
    }
    public Chatmessage(String sender, String recipient, String text, long timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
