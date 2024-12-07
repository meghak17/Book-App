package com.megha.bookapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.megha.bookapp.databinding.ActivityDashboardUserBinding;

public class DashboardUserActivity extends AppCompatActivity {

    //view binding
    private ActivityDashboardUserBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //handle click ,logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

    }
    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in
            //start main screen
            startActivity(new Intent(DashboardUserActivity.this, MainActivity.class));
            finish();//finishes the activity
        }
        else {
            //get user info
            String email = firebaseUser.getEmail();
            //set in textview of toolbar
            binding.subTitleTv.setText(email);
        }
    }
}