package com.example.telcotec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class Ftp extends AppCompatActivity {

    private LinearLayout downloadbtn;
    private LinearLayout uploadbtn ;
    private EditText ftpadrs ;
    private  EditText ftpport ;
    private EditText ftplogin ;
    private  EditText ftppwd ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);
        FTPClient ftpClient = new FTPClient();
        downloadbtn = findViewById(R.id.ftpdownloadbtn);
        uploadbtn = findViewById(R.id.ftpupladbtn);
        ftpadrs = findViewById(R.id.ftpAdress);
        ftpport = findViewById(R.id.ftpPort);
        ftplogin = findViewById(R.id.ftplogin);
        ftppwd = findViewById(R.id.ftppwd);


    }


    public void connectftp() {
        //  Reference: https://stackoverflow.com/a/8761268/6667035
        FTPClient ftp = new FTPClient();
        try {
            //  Reference: https://stackoverflow.com/a/55950845/6667035
            //  The argument of `FTPClient.connect` method is hostname, not URL.
            ftp.connect(ftpadrs.getText().toString(), Integer.parseInt(ftpport.getText().toString()));
            boolean status = ftp.login(ftplogin.getText().toString(), ftppwd.getText().toString());
            if (status) {
                ftp.enterLocalPassiveMode();
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                ftp.sendCommand("OPTS UTF8 ON");
            }
            System.out.println("status : " + ftp.getStatus());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (SocketException en) {
            en.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}