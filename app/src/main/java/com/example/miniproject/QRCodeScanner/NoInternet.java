package com.example.miniproject.QRCodeScanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.miniproject.QRCodeScanner.Attendance;
import com.example.miniproject.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.SortedSet;
import java.util.TimeZone;

public class NoInternet extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        getSupportActionBar().hide();

        ArrayList<String> attrs = getIntent().getStringArrayListExtra("attrs");
        TextView tv = findViewById(R.id.noInternetTV);

        if (Float.valueOf(attrs.get(1)) >= Attendance.UTAR_LEFT && Float.valueOf(attrs.get(1)) <= Attendance.UTAR_RIGHT &&
                Float.valueOf(attrs.get(2)) >= Attendance.UTAR_BOTTOM && Float.valueOf(attrs.get(2)) <= Attendance.UTAR_TOP) {

            SharedPreferences pref = getSharedPreferences("OfflineAttendance", 0);
            SharedPreferences.Editor editor = pref.edit();

            String time = ZonedDateTime
                    .now(ZoneId.of("Asia/Singapore"))
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            editor.putString("QRContent", attrs.get(0));
            editor.putString("Time", time);
            editor.putString("Longitude", attrs.get(1));
            editor.putString("Latitude", attrs.get(2));

            editor.commit();
            tv.setText("Seems like you are not connected to the Internet." +
                    "\nYour attendance will be recorded once you are connected to the Internet.");
        }
        else {
            tv.setText("Location authentication failed!\nPlease contact your instructor if you have any enquiries.");
        }
    }
}