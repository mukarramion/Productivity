package com.example.productivity;

public class Choice {
    private String role;
    private Message message;

    // Constructor
    public Choice(String role, Message message) {
        this.role = role;
        this.message = message;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for message
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}


