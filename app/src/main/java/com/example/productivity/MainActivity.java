package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.productivity.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private FirebaseAuth mAuth;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        replaceFragment(new NotesFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){

                case R.id.bottom_notes:
                    replaceFragment(new NotesFragment());
                    break;
                case R.id.bottom_deadline:
                    replaceFragment(new DeadlineFragment());
                    break;
                case R.id.bottom_suggestion:
                    replaceFragment(new SuggestionFragment());
                    break;
                case R.id.bottom_logout:
                    sendUserToNextActivity();
                    break;
            }

            return true;
        });
    }

    private void sendUserToNextActivity() {
        mAuth.signOut();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.apply();
        Toast.makeText(this,"Logout Successful", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}