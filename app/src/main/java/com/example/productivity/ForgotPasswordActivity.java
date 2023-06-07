package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText inputEmail;
    private Button submit;
    private Dialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputEmail = findViewById(R.id.idFEmail);
        submit = findViewById(R.id.idSend);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog_forgot);
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void validateData() {
        String email = inputEmail.getText().toString();
        if(email.isEmpty()){
            inputEmail.setError("Email cannot be empty");
            inputEmail.requestFocus();
        }
        else{
            progressDialog.setTitle("Sending Link");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(ForgotPasswordActivity.this,"Check your Email", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this,""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}