package com.example.topdoctest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends SignInActivity {

    ImageView imageView;
    TextView currtTimeTv, userNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageViewID);
        userNameTv = findViewById(R.id.userNameTextView);
        currtTimeTv= findViewById(R.id.currentTimeTextView);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        String LogTime = intent.getStringExtra("current_time");

        userNameTv.setText(userName);
        currtTimeTv.setText(LogTime);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}