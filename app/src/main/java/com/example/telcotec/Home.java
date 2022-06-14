package com.example.telcotec;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Home extends AppCompatActivity {
        private LinearLayout call ;
        private  LinearLayout ftp;
        private LinearLayout stream;
        private LinearLayout navigation;
        private  LinearLayout info;
        private LinearLayout infoBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

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
                Toast.makeText(getApplicationContext(),"info clicked",Toast.LENGTH_LONG).show();
            }
        });
    }
}