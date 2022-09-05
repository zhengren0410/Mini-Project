package com.example.miniproject.QRCodeScanner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.example.miniproject.R;

public class QRScanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private ArrayList<String> intentExtra = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        getSupportActionBar().hide();

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCodeScanner.releaseResources();
                        intentExtra.add(result.getText());

                        if (ActivityCompat.checkSelfPermission(QRScanner.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(QRScanner.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(QRScanner.this,
                                    new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION },
                                    101);
                            return;
                        }
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                            @Override
                            public void onLocationChanged(@NonNull Location location) {
                                String time = ZonedDateTime
                                        .now(ZoneId.of("Asia/Singapore"))
                                        .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                                intentExtra.add(String.valueOf(location.getLongitude()));
                                intentExtra.add(String.valueOf(location.getLatitude()));
                                intentExtra.add(time);

                                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                                boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

                                Intent intent;

                                if (connected) {
                                    intent = new Intent(QRScanner.this, Attendance.class);
                                    intent.putStringArrayListExtra("attrs", intentExtra);
                                } else {
                                    intent = new Intent(QRScanner.this, NoInternet.class);
                                    intent.putStringArrayListExtra("attrs", intentExtra);
                                }
                                startActivity(intent);
                            }
                        }, null);
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.releaseResources();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                break;
        }
    }
}