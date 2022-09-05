package com.example.miniproject.AccountManagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniproject.R;
import com.google.android.material.button.MaterialButton;

public class LoginSignupActivity extends AppCompatActivity {

    MaterialButton signup_btn, login_btn;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        signup_btn = findViewById(R.id.signup_btn);
        login_btn = findViewById(R.id.login_btn);

        signup_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSignupActivity.this, SignUpActivity.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSignupActivity.this, LogInActivity.class));
            }
        });


        // Animation
        signup_btn.setTranslationY(100);
        login_btn.setTranslationY(300);
        signup_btn.setAlpha(v);
        login_btn.setAlpha(v);
        signup_btn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        login_btn.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(300).start();

    }
}