package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private TextView createNewAccount;
    private TextView forgotPassword;
    private Dialog progressDialog;

    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.idUsername);
        password = findViewById(R.id.idPassword);
        login = findViewById(R.id.idLogin);
        forgotPassword = findViewById(R.id.idForgotPassword);
        createNewAccount = findViewById(R.id.idCreate);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog_login);
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();
        isLoggedIn();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCredentials();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
            }
        });
        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean loginStatus = sharedPreferences.getBoolean("isLoggedIn",false);
        if(loginStatus){
            sendUserToNextActivity();
            Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyCredentials() {
        String email = username.getText().toString();
        String pass = password.getText().toString();
        if(email.isEmpty() || pass.isEmpty()){
            if(email.isEmpty()){
                username.setError("Email cannot be empty");
                username.requestFocus();
            }
            else if(pass.isEmpty()){
                password.setError("Password cannot be empty");
                password.requestFocus();
            }
        }
        else{

            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn",true);
                        editor.apply();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,""+task.getException() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}