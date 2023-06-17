package com.example.productivity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"
    })
    @POST("chat/completions")
    Call<ChatGptResponse> getChatResponse(@Body ChatGptRequest chatRequest);
}
