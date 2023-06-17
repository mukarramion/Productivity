package com.example.productivity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGptRequest {

    @SerializedName("messages")
    private List<Message> messages;

    @SerializedName("max_tokens")
    private int maxTokens;

    @SerializedName("model")
    private String model;

    public ChatGptRequest() {
        this.messages = messages;
        this.maxTokens = maxTokens;
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

