package com.example.telcotec;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.telcotec.utils.DBHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class Ftp extends AppCompatActivity {

    private LinearLayout downloadbtn;
    private LinearLayout uploadbtn ;
    private EditText ftpadrs ;
    private  EditText ftpport ;
    private EditText ftplogin ;
    private  EditText ftppwd ;
    private Button connectbtn;

    private TextView debut ;
    final String host = "196.203.251.15";
    final int port = 21;
    final String username = "telcotec";
    final String password = "k42bn57n";
    private  TextView res ;
    FTPClient ftpClient;

    private DBHelper dbHelper;
     @RequiresApi(api = Build.VERSION_CODES.O)
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);

        downloadbtn = findViewById(R.id.ftpdownloadbtn);
        connectbtn = findViewById(R.id.ftpconnecter);
        uploadbtn = findViewById(R.id.ftpupladbtn);
        ftpadrs = findViewById(R.id.ftpAdress);
        ftpadrs.setText(host);
        ftpport = findViewById(R.id.ftpPort);
        ftpport.setText(String.valueOf(port));
        ftplogin = findViewById(R.id.ftplogin);
        ftplogin.setText(username);
        ftppwd = findViewById(R.id.ftppwd);
        ftppwd.setText(password);
        res = findViewById(R.id.ftpres);
        debut = findViewById(R.id.ftpdd);
        debut.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss")));
         dbHelper = new DBHelper(getApplicationContext());

          ftpClient = new FTPClient();
         connectbtn.setOnClickListener(  view->{
                 try {
                     FtpConnect();
                     Toast.makeText(getApplicationContext(), "    connected to ftp  ", Toast.LENGTH_SHORT).show();

                 } catch (IOException e) {
                     Toast.makeText(getApplicationContext(), "error while connecting ", Toast.LENGTH_SHORT).show();
                     e.printStackTrace();
                 }

         } );

        uploadbtn.setOnClickListener(view -> {

            ftpUpload();
            res.setVisibility(View.VISIBLE);

        });

        downloadbtn.setOnClickListener(View ->{
             ftpDownload();
            res.setVisibility(View.VISIBLE);
        });
    }

    private void ftpDownload() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {

                    String fosTxt = "this is a test to upload a file for the application ";
                    fos = openFileOutput("upload.txt", MODE_PRIVATE);
                    fos.write(fosTxt.getBytes(StandardCharsets.UTF_8));

                    if (ftpClient.isConnected()) {
                        FileOutputStream fis = null;
                        try {
                            String filename = getFilesDir() + "/upload.txt";
                            fis = new FileOutputStream(filename);
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

                            ftpClient.retrieveFile("uploadTest.txt", fis);
                            fis.close();

                            handler.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void run() {
                                    String rest = ftpClient.getReplyString();
                                    Toast.makeText(getApplicationContext(), "downloaded successfully", Toast.LENGTH_SHORT).show();
                                    res.setText("Download : "+ rest);

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String rest = ftpClient.getReplyString();
                            Toast.makeText(getApplicationContext(), "error in file creating ", Toast.LENGTH_SHORT).show();
                            res.setText(rest.toString());

                            dbHelper.saveftp(debut.getText().toString(),rest);
                        }
                    });
                }
            }
        });
      }



    private void ftpUpload() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {

                    String fosTxt = "this is a test to upload a file for the application ";
                    fos = openFileOutput("upload.txt", MODE_PRIVATE);
                    fos.write(fosTxt.getBytes(StandardCharsets.UTF_8));

                    if (ftpClient.isConnected()) {
                        FileInputStream fis = null;
                        try {
                            String filename = getFilesDir() + "/upload.txt";
                            fis = new FileInputStream(filename);
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
                            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);

                            ftpClient.storeFile("uploadTest.txt", fis);
                             fis.close();

                            handler.post(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void run() {
                                    String rest = ftpClient.getReplyString();
                                    Toast.makeText(getApplicationContext(), "Uploaded successfully ", Toast.LENGTH_SHORT).show();
                                    res.setText("Upload : "+rest);
                                    dbHelper.saveftp(debut.getText().toString(),rest);

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String rest = ftpClient.getReplyString();
                            Toast.makeText(getApplicationContext(), "error in file creating ", Toast.LENGTH_SHORT).show();
                              res.setText(rest.toString());
                        }
                    });
                }
            }
        });
    }



    private void FtpConnect() throws IOException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    requestPermission(Manifest.permission.INTERNET , 1);
                    if(checkPermission(Manifest.permission.INTERNET)){
                        ftpClient.connect(host , port);
                        ftpClient.login(username , password);
                }
                    else{
                        Toast.makeText(getApplicationContext(), "please allow internet ", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });

    }



    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
    private  void requestPermission(String permission , int code ){
        ActivityCompat.requestPermissions(this, new String[]{permission}, code);
    }

}