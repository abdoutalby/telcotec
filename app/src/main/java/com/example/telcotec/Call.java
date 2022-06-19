package com.example.telcotec;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telcotec.utils.CallData;
import com.example.telcotec.utils.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Call extends AppCompatActivity {

    DBHelper dbHelper;
    private EditText phoneNumber;
    private Button callbtn;

    private RecyclerView callsRV;

    private RelativeLayout getCallDetailsBtn;
    adapter adapter;
    ListenToPhoneState listener;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        phoneNumber = findViewById(R.id.phonenumber);
        callbtn = findViewById(R.id.callbtn);
        getCallDetailsBtn = findViewById(R.id.getCalldetalisbtn);
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
          listener = new ListenToPhoneState();
        tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        callsRV = findViewById(R.id.callsRV);

        requestPermission(Manifest.permission.READ_CALL_LOG, 2);

        getCallDetailsBtn.setOnClickListener(view -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    dbHelper = new DBHelper(getApplicationContext());
                    Cursor res = dbHelper.getCallHistory();
                    if (res.getCount() == 0) {
                        Toast.makeText(Call.this, "no Call saved yet", Toast.LENGTH_SHORT).show();
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<CallData> calls = new ArrayList<>();
                                while (res.moveToNext()) {
                                    calls.add(new CallData(
                                            res.getString(1),
                                            res.getString(2),
                                            res.getString(3),
                                            res.getString(4),
                                            res.getString(5)));
                                }
                                callsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                callsRV.setItemAnimator(new DefaultItemAnimator());
                                adapter = new adapter(calls);
                                callsRV.setAdapter(adapter);

                                Toast.makeText(getApplicationContext(), "clickedd", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        });

        callbtn.setOnClickListener(view -> {
            if (!checkPermission(Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 145);

            }
            String number = phoneNumber.getText().toString();
            Toast.makeText(getApplicationContext(), "calling " + number, Toast.LENGTH_SHORT).show();

            if (!TextUtils.isEmpty(number)) {

                String call = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(call)));

            } else {
                Toast.makeText(getApplicationContext(), "Enter a phone number", Toast.LENGTH_SHORT).show();
            }


        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(listener.callEnded){
            saveCallDetails();
            Toast.makeText(Call.this, "data saved", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int code) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, code);
    }


    private CallData saveCallDetails() {
        CallData c = new CallData("no", "no", "no ", "no", "d");
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int n = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

        if (managedCursor.moveToFirst()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String name = managedCursor.getString(n);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            c = new CallData(phNumber, name, callDuration, dir, callDayTime.toString());
            dbHelper = new DBHelper(getApplicationContext());
            dbHelper.saveCall(c);
        }
        managedCursor.close();
        return c;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
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

    public class adapter extends RecyclerView.Adapter<adapter.MyViewHolder> {
        private ArrayList<CallData> calls;

        public adapter(ArrayList<CallData> calls) {
            this.calls = calls;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView number;
            private TextView date;
            private TextView name;
            private TextView duration;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                number = findViewById(R.id.rvnumber);
                date = findViewById(R.id.rvdate);
                name = findViewById(R.id.rvname);
                duration = findViewById(R.id.rvduration);

            }
        }

        @NonNull
        @Override
        public adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.callitem, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull adapter.MyViewHolder holder, int position) {
            if (position > 0) {
                holder.name.setText(calls.get(position).getName());
                holder.duration.setText(calls.get(position).getDuration());
                holder.number.setText(calls.get(position).getNumber());
                holder.duration.setText(calls.get(position).getType());
            }
        }

        @Override
        public int getItemCount() {
            return calls.size();
        }
    }

    private class ListenToPhoneState extends PhoneStateListener {

        boolean callEnded = false;

        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:


                    if (callEnded) {
                        //you will be here at **STEP 4**
                        //you should stop service again over here
                    } else {
                        //you will be here at **STEP 1**
                        //stop your service over here,
                        //i.e. stopService (new Intent(`your_context`,`CallService.class`));
                        //NOTE: `your_context` with appropriate context and `CallService.class` with appropriate class name of your service which you want to stop.

                    }


                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callEnded = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //you will be here at **STEP 2**

                    break;


                default:
                    break;
            }
        }

    }
}




