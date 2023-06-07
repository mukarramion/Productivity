package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    private TextView alreadyHaveAnAccount;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button registenBtn;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Dialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        alreadyHaveAnAccount = findViewById(R.id.idUser);
        inputEmail = findViewById(R.id.idEmail);
        inputPassword = findViewById(R.id.idRPassword);
        inputConfirmPassword = findViewById(R.id.idRConfirmPassword);
        registenBtn = findViewById(R.id.idRegister);
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        registenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAuthentication();
            }
        });
    }

    private void performAuthentication() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confrimPassword = inputConfirmPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty() || confrimPassword.isEmpty()){
            Toast.makeText(this, "Input fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!email.matches(emailPattern)){
                inputEmail.setError("Enter Correct Email");
                inputEmail.requestFocus();
            }
            else{
                if(password.length()<8){
                    inputPassword.setError("The password must be at least 8 characters long.");
                    inputPassword.requestFocus();
                }
                else{
                    if(!password.equals(confrimPassword)){
                        inputConfirmPassword.setError("Password does not match");
                        inputConfirmPassword.requestFocus();
                    }
                    else{
                        progressDialog.setTitle("Registration");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  progressDialog.dismiss();
                                  sendUserToNextActivity();
                                  Toast.makeText(RegisterActivity.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                              }
                              else{
                                  progressDialog.dismiss();
                                  Toast.makeText(RegisterActivity.this,""+task.getException() , Toast.LENGTH_SHORT).show();
                              }
                           }
                       });
                    }
                }
            }
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}