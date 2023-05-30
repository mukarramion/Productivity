package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.idUsername);
        password = findViewById(R.id.idPassword);
        login = findViewById(R.id.idLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCredentials();
            }
        });
    }

    private void verifyCredentials() {
        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Input fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            if(username.getText().toString().equals("Muskaan") && password.getText().toString().equals("EduCare@M")){
                username.setText("");
                password.setText("");
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}