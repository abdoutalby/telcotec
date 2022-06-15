package com.example.telcotec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Navigation extends AppCompatActivity {
private WebView webView ;
private Button btn ;
private EditText url ;
private TextView tvDate ;
private  TextView tvHeure;
private  TextView tvtchargement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        webView = findViewById(R.id.webView);
        btn = findViewById(R.id.getUrlbtn);
        url = findViewById(R.id.ETurl);
        tvDate = findViewById(R.id.tvdate);
        tvHeure = findViewById(R.id.tvheure);
        tvtchargement = findViewById(R.id.tvtchargement);
        btn.setOnClickListener(new View.OnClickListener() {
            long pageLoadStartTime=0;
            @Override
            public void onClick(View view) {
                webView.setWebViewClient(new WebViewClient(){


                    private void onPageStarted(WebView view, String url) {
                        pageLoadStartTime = System.currentTimeMillis();
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        long pageLoadTime = System.currentTimeMillis() - pageLoadStartTime;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            Date today = new Date();

                        tvtchargement.setText("Temps de chargement : "+  ((pageLoadTime)/ 1000) % 60);

                        tvDate.setText( "Date : "+  formatter.format(today));

                            Calendar rightNow = Calendar.getInstance();
                            int hour = rightNow.get(Calendar.HOUR_OF_DAY);
                            int min = rightNow.get(Calendar.MINUTE);
                        tvHeure.setText("Heure : " +String.valueOf(hour)+":" +String.valueOf(min));
                        webView.setVisibility(View.VISIBLE);
                    } }
                });
                webView.loadUrl(url.getText().toString());

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);

            }
        });
    }
}