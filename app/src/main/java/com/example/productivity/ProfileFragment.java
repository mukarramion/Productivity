package com.example.productivity;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private TextView profile;
    private CardView todo;
    private CardView pbot;
    private CardView pForgot;
    private CardView logout;

    private FirebaseAuth mAuth;

    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile = view.findViewById(R.id.profile_title);
        todo = view.findViewById(R.id.todo_CV);
        pbot = view.findViewById(R.id.chatGPT_CV);
        pForgot = view.findViewById(R.id.Pforgot_CV);
        logout = view.findViewById(R.id.logout_CV);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        profile.setText(sharedPreferences.getString("email",""));
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendUserToNextActivity();
            }
        });

        pForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        pbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ChatBotActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void sendUserToNextActivity() {
        mAuth.signOut();
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("email","");
        editor.apply();
        Toast.makeText(getActivity(),"Logout Successful", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}