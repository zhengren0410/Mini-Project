package com.example.miniproject.AccountManagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.miniproject.R;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText email_input, password_input;
    MaterialButton signup_btn;
    FirebaseAuth mAuth; // Singleton
    private static final String TAG_signup = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_btn = findViewById(R.id.signup_btn);
        Context context = SignUpActivity.this;

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_input = findViewById(R.id.signup_email_input);
                password_input = findViewById(R.id.signup_password_input);
                String email = email_input.getText().toString();
                String password = password_input.getText().toString();
                createAccount(mAuth, context, email, password);

                Log.d(TAG_signup, email);
                Log.d(TAG_signup, password);
            }
        });

    }

    private void createAccount(FirebaseAuth mAuth, Context context, String email, String password) {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_signup, "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            String currentUserID = currentUser.getUid();

                            // [START update_profile]
                            if (currentUser != null) {
                                Toast.makeText(context, "Sign Up successful", Toast.LENGTH_LONG).show();

                                SharedPreferences sharedPref_userID = getSharedPreferences("StudentInfo", 0 );
                                SharedPreferences.Editor sharedPrefEditor_userID = sharedPref_userID.edit();
                                sharedPrefEditor_userID.putString("Current user ID", currentUserID);
                                sharedPrefEditor_userID.commit();
                            }

                            // Go to main page after successful sign up
                            Intent stuinfo_intent = new Intent(context , StudentInfoActivity.class );
                            startActivity(stuinfo_intent);

                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w(TAG_signup, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Sign Up failed.\nPlease Try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}