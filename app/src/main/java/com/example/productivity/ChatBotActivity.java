package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatBotActivity extends AppCompatActivity {
    private RecyclerView chatsRV;
    private EditText userMsgEdit;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModel> chatsModelArrayList;
    private ChatRVAdapter chatRVAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdit = findViewById(R.id.idEditMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);
        imageView = findViewById(R.id.chatGPT_back);
        chatsModelArrayList = new ArrayList<>();
        chatsModelArrayList.add(new ChatsModel("Hello I'm OpenAI Assistant. I chat with people and help them using my A.I.",BOT_KEY));
        chatRVAdapter = new ChatRVAdapter(chatsModelArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMsgEdit.getText().toString().isEmpty()){
                    Toast.makeText(ChatBotActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getChatGPTResponse(userMsgEdit.getText().toString());
                userMsgEdit.setText("");
            }
        });
    }
    private void getChatGPTResponse(String message){
        chatsModelArrayList.add(new ChatsModel(message,USER_KEY));
        chatRVAdapter.notifyItemInserted(chatsModelArrayList.size());
        chatsRV.scrollToPosition(chatRVAdapter.getItemCount() - 1);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        ChatGptRequest chatGptRequest = new ChatGptRequest();
        List<Message> messages = new ArrayList<>();
        Message userMessage = new Message("user", message);
        messages.add(userMessage);
        chatGptRequest.setMessages(messages);
        int maxTokens = 100; // Set the desired value
        chatGptRequest.setMaxTokens(maxTokens);
        String model = "gpt-3.5-turbo-0613"; // Set the desired model
        chatGptRequest.setModel(model);


        Call<ChatGptResponse> call = retrofitAPI.getChatResponse(chatGptRequest);

        call.enqueue(new Callback<ChatGptResponse>() {
            @Override
            public void onResponse(Call<ChatGptResponse> call, Response<ChatGptResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    ChatGptResponse chatGptResponse = response.body();
                    // Process the response data
                    List<Choice> choices = chatGptResponse.getChoices();
                    if (choices != null && !choices.isEmpty()) {
                        // Choose a specific index from the choices list
                        int index = 0; // Set the desired index here

                        if (index >= 0 && index < choices.size()) {
                            Choice choice = choices.get(index);
                            String role = choice.getRole();
                            String content = choice.getMessage().getContent();

                            // Handle the choice data as needed
                            // You can display it in your UI, process it further, or perform any other actions
                            chatsModelArrayList.add(new ChatsModel(content,BOT_KEY));
                            chatRVAdapter.notifyItemInserted(chatsModelArrayList.size());
                            chatsRV.scrollToPosition(chatRVAdapter.getItemCount() - 1);
                        }

                    } else {
                        // Handle error response
                        // Extract error information from the response
                        chatsModelArrayList.add(new ChatsModel("No Response Generated",BOT_KEY));
                        chatRVAdapter.notifyItemInserted(chatsModelArrayList.size());
                        chatsRV.scrollToPosition(chatRVAdapter.getItemCount() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatGptResponse> call, Throwable t) {
                chatsModelArrayList.add(new ChatsModel("Network Error",BOT_KEY));
                chatRVAdapter.notifyItemInserted(chatsModelArrayList.size());
                chatsRV.scrollToPosition(chatRVAdapter.getItemCount() - 1);
            }
        });
    }
}