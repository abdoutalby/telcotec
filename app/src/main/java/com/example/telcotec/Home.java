package com.example.telcotec;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {
    private LinearLayout call, detail;
    private LinearLayout ftp;
    private LinearLayout stream;
    private LinearLayout navigation;
    private LinearLayout info;
    private TextView etatr, typr, typA, imei, sim, simiso, netiso, version, operateur, longi, lati;
    private LinearLayout infoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        detail = findViewById(R.id.detail);
        etatr = findViewById(R.id.etatr);
        typr = findViewById(R.id.typr);
        typA = findViewById(R.id.typA);
        imei = findViewById(R.id.imei);
        sim = findViewById(R.id.sim);
        simiso = findViewById(R.id.simiso);
        netiso = findViewById(R.id.netiso);
        version = findViewById(R.id.version);
        operateur = findViewById(R.id.operateur);
        longi = findViewById(R.id.longi);
        lati = findViewById(R.id.lati);
        call = findViewById(R.id.call);
        ftp = findViewById(R.id.ftp);
        stream = findViewById(R.id.streaming);
        navigation = findViewById(R.id.web);
        info = findViewById(R.id.info);
        infoBtn = findViewById(R.id.infoBtn);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Call.class);
                startActivity(i);
            }
        });

        ftp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Ftp.class);
                startActivity(i);
            }
        });
        stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Streaming.class);
                startActivity(i);
            }
        });
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Navigation.class);
                startActivity(i);
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceinfo();
                detail.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getDeviceinfo() {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        version.setText("Version du software : " + System.getProperty("os.version"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        sim.setText("Numéro de série SIM : " + tManager.getSimSerialNumber());
        netiso.setText("Network country ISO : " + String.valueOf(tManager.getNetworkCountryIso()));
        simiso.setText("SIM country ISO : " + tManager.getSimCountryIso());


        int phoneType = tManager.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                typA.setText("Type de l'appareil : " + "CDMA");
                break;

            case (TelephonyManager.PHONE_TYPE_GSM):
                typA.setText("Type de l'appareil : " + "GSM");
                break;

            case (TelephonyManager.PHONE_TYPE_NONE):
                typA.setText("Type de l'appareil : " + "NONE");
                break;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        ;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            etatr.setText("Etat du réseau : " + "Connecté à Internet...");
        } else {
            etatr.setText("Etat du réseau : " + "Pas de connexion Internet...");

        }

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo.isConnected()) {
            typr.setText("Tyoe de réseau : " + "Connecté au réseau WIFI");
        } else if (mobileInfo.isConnected()) {
            TelephonyManager manager1 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            int networkType = manager1.getNetworkType();
            if (networkType == 1) {
                typr.setText("Tyoe de réseau : 2G");
            }
            if (networkType == 2) {
                typr.setText("Tyoe de réseau : 2G");
            }
            if (networkType == 4) {
                typr.setText("Tyoe de réseau : 2G");
            }
            if (networkType == 7) {
                typr.setText("Tyoe de réseau : 2G");
            }
            if (networkType == 11) {
                typr.setText("Tyoe de réseau : 2G");
            }
            if (networkType == 3) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 5) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 6) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 8) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 9) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 10) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 12) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 14) {
                typr.setText("Tyoe de réseau : 3G");

            }
            if (networkType == 15) {
                typr.setText("Tyoe de réseau : 3G");

            }

            if (networkType == 13) {
                typr.setText("Tyoe de réseau : 3G");
            }
        } else {
            typr.setText("connexion indisponible");
        }


        operateur.setText("nom de l'operateur : " + tManager.getNetworkOperatorName());




    }
}