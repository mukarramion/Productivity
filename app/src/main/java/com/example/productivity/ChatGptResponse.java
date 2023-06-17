package com.example.productivity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGptResponse {
    @SerializedName("choices")
    private List<Choice> choices;

    public ChatGptResponse(List<Choice> choices) {
        this.choices = choices;
    }

    // Getters and setters
    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}


