package com.example.miniproject.AccountManagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.miniproject.AccountManagement.classes.DAOStudent;
import com.example.miniproject.AccountManagement.classes.Student;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.example.miniproject.R;

public class StudentInfoActivity extends AppCompatActivity {

    TextInputEditText stuID_input, fullname_input;
    MaterialButton submit_btn;


    private static final String TAG_stuInfo = "StudentInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        SharedPreferences sharedPref_userID = getApplicationContext().getSharedPreferences("StudentInfo", 0);
        String firebaseAuthUserID = sharedPref_userID.getString( "Current user ID", "Fail to retrieve userID from shared preferences" );
        Log.d(TAG_stuInfo, "SharedPref UID: " + firebaseAuthUserID);

        submit_btn = findViewById(R.id.submit_btn);
        stuID_input = findViewById(R.id.info_id_input);
        fullname_input = findViewById(R.id.info_fullname_input);

        Context context = StudentInfoActivity.this;

        submit_btn.setOnClickListener(view -> {
            DAOStudent daoStudent = new DAOStudent();

            String stuID = stuID_input.getText().toString();
            String fullname = fullname_input.getText().toString();

            Student student = new Student(firebaseAuthUserID, stuID, fullname);

            daoStudent.add( student ).addOnSuccessListener( suc->{
                Toast.makeText(context, "Data is recorded", Toast.LENGTH_LONG).show();

                SharedPreferences sharedPref_StudentInfo = getSharedPreferences("StudentInfo", 0 );
                SharedPreferences.Editor sharedPrefEditor_userID = sharedPref_StudentInfo.edit();
                sharedPrefEditor_userID.putString("StudentID", student.studentID);
                sharedPrefEditor_userID.putString("Student Full Name", student.fullname);
                sharedPrefEditor_userID.commit();

                startActivity( new Intent( StudentInfoActivity.this, LoginSignupActivity.class ) );

            }).addOnFailureListener( err->{
                Toast.makeText(context, ""+ err.getMessage(), Toast.LENGTH_LONG).show();
            });
        });

    }

}