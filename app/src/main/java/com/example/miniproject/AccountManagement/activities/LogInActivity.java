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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.miniproject.R;

public class LogInActivity extends AppCompatActivity {

    MaterialTextView forgotpw;
    TextInputEditText email_input, password_input;
    MaterialButton login_btn;
    private static final String TAG_login = "LogInActivity";
    FirebaseAuth mAuth; // Singleton


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login_btn = findViewById(R.id.login_btn);
        forgotpw = findViewById(R.id.forgotpw_label);
        Context context = LogInActivity.this;

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_input = findViewById(R.id.login_email_input);
                password_input = findViewById(R.id.login_password_input);
                String email = email_input.getText().toString();
                String password = password_input.getText().toString();
                logInToAccount(mAuth, context, email, password);

                Log.d(TAG_login, email);
                Log.d(TAG_login, password);
            }
        });
    }

    private void logInToAccount(FirebaseAuth mAuth, Context context, String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_login, "signInWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // TODO: Output UserID
                            String currentUserID = user.getUid();
//                            Toast.makeText(LoginSignupActivity.this, "Go to MainActivity; uid: " + currentUserID, Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPref_userID = getSharedPreferences("StudentInfo", 0 );
                            SharedPreferences.Editor sharedPrefEditor_userID = sharedPref_userID.edit();
                            sharedPrefEditor_userID.putString("Current user ID", currentUserID);
                            sharedPrefEditor_userID.commit();

                            // Go to page if log in is successful
                            Intent accInfo_intent = new Intent(context , AccountInfoActivity.class );
                            startActivity(accInfo_intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_login, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.\nPlease try again.",
                                    Toast.LENGTH_LONG).show();
                            // TODO
                        }
                    }
                });
        // [END sign_in_with_email]
    }
}