package com.example.miniproject.QRCodeScanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SnapshotHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.example.miniproject.R;

public class Attendance extends AppCompatActivity {
    public static final float UTAR_LEFT = 101.135f, UTAR_RIGHT = 101.146f, UTAR_TOP = 4.343f, UTAR_BOTTOM = 4.333f ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        getSupportActionBar().hide();

        ArrayList<String> attrs = getIntent().getStringArrayListExtra("attrs");
        String QRContent = attrs.get(0);
        TextView tv = findViewById(R.id.attendanceTV);

        SharedPreferences pref = getSharedPreferences("StudentInfo", 0);
        String studentName = pref.getString("Student Full Name", null);
        String studentID = pref.getString("StudentID", null);

        if (Float.valueOf(attrs.get(1)) >= UTAR_LEFT && Float.valueOf(attrs.get(1)) <= UTAR_RIGHT &&
                Float.valueOf(attrs.get(2)) >= UTAR_BOTTOM && Float.valueOf(attrs.get(2)) <= UTAR_TOP){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://miniproject-87501-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference db = database.getReference();
            db.child("attendancelist")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                boolean flag = false;
                                Map<String, Object> attendance = new ObjectMapper().readValue(QRContent, HashMap.class);
                                Map<String, Map<String, Object>> result = (Map<String, Map<String, Object>>) snapshot.getValue();

                                for (Map.Entry<String, Map<String, Object>> e : result.entrySet()) {
                                    Map<String, Object> map = e.getValue();
                                    if (!flag) {
                                        flag = map.get("SubjectCode").toString().equals(attendance.get("CourseCode").toString()) &&
                                                map.get("ClassGroup").toString().equals(attendance.get("ClassGroup").toString()) &&
                                                map.get("StudentID").toString().equals(studentID);
                                    }
                                }

                                if (flag) {
                                    tv.setText("Your attendance is already taken!");
                                } else {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://miniproject-87501-default-rtdb.asia-southeast1.firebasedatabase.app/");
                                    DatabaseReference db = database.getReference();

                                    Map<String, Object> attendancelist = new HashMap<>();
                                    attendancelist.put("AttendanceStatus", "presence");
                                    attendancelist.put("ClassGroup", attendance.get("ClassGroup").toString());
                                    attendancelist.put("Date", attendance.get("ClassDate").toString());
                                    attendancelist.put("StudentID", studentID);
                                    attendancelist.put("StudentName", studentName);
                                    attendancelist.put("SubjectCode", attendance.get("CourseCode").toString());
                                    attendancelist.put("SubjectTitle", attendance.get("Subject").toString());
                                    attendancelist.put("Time", attrs.get(3).toString());

                                    db.child("attendancelist")
                                            .push().setValue(attendancelist)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    String message = "Name\t\t\t\t:\t" + studentName +
                                                            "\nStudent ID\t\t:\t" + studentID +
                                                            "\nCourse Code\t:\t" + attendance.get("CourseCode").toString() +
                                                            "\nSubject\t\t\t:\t" + attendance.get("Subject").toString() +
                                                            "\nClass Group\t:\t" + attendance.get("ClassGroup").toString() +
                                                            "\nClass Date\t\t:\t" + attendance.get("ClassDate").toString() +
                                                            "\nClass Day\t\t:\t" + attendance.get("ClassDay").toString() +
                                                            "\nClass Time\t:\t" + attendance.get("ClassTime").toString() +
                                                            "\nClass Hour\t\t:\t" + attendance.get("ClassHour").toString() +
                                                            "\n\nYour class attendance have been recorded!";
                                                    tv.setText(message);
                                                }
                                            });
                                }

                            }catch (Exception e){
                                tv.setText(e.getMessage());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else {
            tv.setText("Location authentication failed!\nPlease contact your instructor if you have any enquiries.");
        }
    }

}