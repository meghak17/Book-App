package com.megha.bookapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.megha.bookapp.databinding.ActivityCategoryAddBinding;

import java.util.HashMap;
import java.util.Objects;

public class CategoryAddActivity extends AppCompatActivity {

    //view binding
    private ActivityCategoryAddBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //handle click,go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //handle click,begin upload category
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String category="";
    private void validateData() {
        category = binding.categoryEt.getText().toString().trim();
        //validate if not empty
        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Please enter category...", Toast.LENGTH_SHORT).show();
        }else{
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        progressDialog.setMessage("Adding Category...");
        progressDialog.show();
        //get timestamp
        long timestamp = System.currentTimeMillis();
        //set up info to add in firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("category",""+category);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("uid",""+firebaseAuth.getUid());

        //add to firebase db....Database Root->Categories->categoryId>category info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //category add success
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Category added successfully...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //category add failed
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}