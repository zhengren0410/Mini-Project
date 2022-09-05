package com.example.miniproject.AccountManagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.miniproject.AccountManagement.classes.DAOStudent;
import com.example.miniproject.QRCodeScanner.Attendance;
import com.example.miniproject.QRCodeScanner.MainActivity;
import com.example.miniproject.QRCodeScanner.QRScanner;
import com.google.android.material.button.MaterialButton;

import com.example.miniproject.R;

import java.util.ArrayList;

public class AccountInfoActivity extends AppCompatActivity {

    TextView fullname, stuid;
    MaterialButton editName_btn, finishedit_btn, canceledit_btn, takeattendance_btn;
    EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        fullname = findViewById(R.id.profile_fullname_value);
        stuid = findViewById(R.id.profile_stuid_value);
        editName = findViewById(R.id.profile_edit_fullname);
        editName_btn = findViewById(R.id.editprofile_btn);
        finishedit_btn = findViewById(R.id.profile_finishedit_btn);
        canceledit_btn = findViewById(R.id.profile_canceledit_btn);
        takeattendance_btn = findViewById(R.id.takeAttendance_btn);

        SharedPreferences sharedPref_stuInfo = getApplicationContext().getSharedPreferences("StudentInfo", 0);

        fullname.setText(
                sharedPref_stuInfo.getString( "Student Full Name", "Fail to retrieve StudentID from shared preferences" )
        );
        stuid.setText(
                sharedPref_stuInfo.getString( "StudentID", "Fail to retrieve Student Full Name from shared preferences")
        );

        editName_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setVisibility(view.VISIBLE);
                fullname.setVisibility(view.INVISIBLE);
                finishedit_btn.setVisibility(view.VISIBLE);
                canceledit_btn.setVisibility(view.VISIBLE);
                editName_btn.setVisibility(view.INVISIBLE);
                takeattendance_btn.setVisibility(view.INVISIBLE);
            }
        });

        finishedit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname.setText(editName.getText().toString());
                DAOStudent daoStudent = new DAOStudent();
                daoStudent.editName(
                        sharedPref_stuInfo.getString("Current user ID", "Fail to retrieve UserID from shared preferences"),
                        editName.getText().toString()
                );

                editName.setVisibility(view.INVISIBLE);
                fullname.setVisibility(view.VISIBLE);
                editName_btn.setVisibility(view.VISIBLE);
                finishedit_btn.setVisibility(view.INVISIBLE);
                canceledit_btn.setVisibility(view.INVISIBLE);
            }
        });

        canceledit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName.setVisibility(view.INVISIBLE);
                fullname.setVisibility(view.VISIBLE);
                editName_btn.setVisibility(view.VISIBLE);
                finishedit_btn.setVisibility(view.INVISIBLE);
                canceledit_btn.setVisibility(view.INVISIBLE);
                takeattendance_btn.setVisibility(view.VISIBLE);
            }
        });

        SharedPreferences pref = getSharedPreferences("OfflineAttendance", 0);
        String QRContent = pref.getString("QRContent", null);

        if (QRContent != null){
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

            if (connected){
                ArrayList<String> attrs = new ArrayList<>();
                attrs.add(QRContent);
                attrs.add(pref.getString("Longitude", "0"));
                attrs.add(pref.getString("Latitude", "0"));
                attrs.add(pref.getString("Time", "0"));

                SharedPreferences.Editor editor = pref.edit();
                editor.remove("QRContent");
                editor.remove("Time");
                editor.remove("Longitude");
                editor.remove("Latitude");
                editor.remove("Longtitude");
                editor.commit();

                Intent intent = new Intent(AccountInfoActivity.this, Attendance.class);
                intent.putStringArrayListExtra("attrs", attrs);
                startActivity(intent);

            }
        }

        takeattendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountInfoActivity.this, QRScanner.class));
            }
        });

        if (ActivityCompat.checkSelfPermission(AccountInfoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AccountInfoActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AccountInfoActivity.this,
                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION },
                    101);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, 102);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_NETWORK_STATE }, 103);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Location permission granted");
                    return;
                }
                break;
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("Permission", "Camera permission granted");
                    return;
                }
                break;
            case 103:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("Permission", "Network permission granted");
                    return;
                }
        }
    }
}