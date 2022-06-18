package com.example.telcotec;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telcotec.utils.CallData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;


public class Call extends AppCompatActivity {


    private TextView operator ;
    private TextView calltype ;
    private TextView serial ;
    private  TextView calllist;
    private EditText phoneNumber ;
    private Button callbtn;
    private Set<CallData> calls  ;


    private RelativeLayout getCallDetailsBtn;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        phoneNumber = findViewById(R.id.phonenumber);
        callbtn= findViewById(R.id.callbtn);
        getCallDetailsBtn = findViewById(R.id.getCalldetalisbtn);
        calllist = findViewById(R.id.calllist);
        operator = findViewById(R.id.phonenumbertv);
        calltype = findViewById(R.id.tvcalltype);

        requestPermission(Manifest.permission.READ_CALL_LOG , 2);
        requestPermission(Manifest.permission.READ_PHONE_STATE , 3);
        requestPermission(Manifest.permission.READ_CONTACTS, 4);

        getCallDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCallDetails();
            }
        });

         callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = phoneNumber.getText().toString();
                Toast.makeText(getApplicationContext(), "calling "+number, Toast.LENGTH_SHORT).show();

                if (!TextUtils.isEmpty(number)) {

                        String call = "tel:" + number;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)) );
                        getCallDetails();

                } else {
                    Toast.makeText(getApplicationContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
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




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callbtn.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                case 2:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                return;
                case 3:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                return;
            case 4:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCallDetails() {

        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        String stringOutput = "";
        Uri uriCallLogs = Uri.parse("content://call_log/calls");
        Cursor cursorCallLogs = getContentResolver().query(uriCallLogs, null,null,null);
        cursorCallLogs.moveToFirst();
        do {

            @SuppressLint("Range") String stringNumber =
                    cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER));
            @SuppressLint("Range") String stringDate = df.format(new Date(
                    cursorCallLogs.getLong(cursorCallLogs.getColumnIndex(CallLog.Calls.DATE))));
            @SuppressLint("Range") String stringDuration =
                    cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.DURATION));
            @SuppressLint("Range") String stringType =
                    cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.TYPE));
                CallData call = new CallData(stringNumber , stringDate,stringDuration,stringType);
            stringOutput = stringOutput + call.toString()
                    + "\n\n";

            calls.add(new CallData(stringNumber , stringDate,stringDuration, stringType));
        }while (cursorCallLogs.moveToNext());

        calllist.setText(stringOutput);
       return stringOutput;

    }



}



