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
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private  TextView phNum,calltype , callDate , callOperator , callDuration ;

    private Button callbtn;
    private LinearLayout historique;
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
        historique=findViewById(R.id.callDetails);
        calltype = findViewById(R.id.tvcalltype);
        callDuration = findViewById(R.id.showDurationCall);
        callDate = findViewById(R.id.showCallDate);
        callOperator = findViewById(R.id.tvcalloperator);
        phNum = findViewById(R.id.phonenumbertv);
        getCallDetailsBtn = findViewById(R.id.getCalldetalisbtn);
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); //pour accéder aux informations sur  les dervices de téléphonie de l'appareil
        listener = new ListenToPhoneState();
        tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); //surveiller les changements d'états de téléphonie spécifiques sur l'appareil
        callsRV = findViewById(R.id.callsRV);

        requestPermission(Manifest.permission.READ_CALL_LOG, 2); //demande la permission pour accéder au répertoire des appels

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
                            @SuppressLint("Range")
                            @Override
                            public void run() {
                                ArrayList<CallData> calls = new ArrayList<>();
                                while (res.moveToNext()) {
                                     calls.add(new CallData(
                                            res.getString(res.getColumnIndex("number")),
                                            res.getString(res.getColumnIndex("operator")),
                                            res.getString(res.getColumnIndex("duration")),
                                            res.getString(res.getColumnIndex("type")),
                                            res.getString(res.getColumnIndex("date"))));
                                }
                                callsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                callsRV.setItemAnimator(new DefaultItemAnimator());
                                adapter = new adapter(calls);
                                callsRV.setAdapter(adapter);

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



    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int code) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, code);
    }


    private void saveCallDetails() {
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int n = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

        if (managedCursor.moveToFirst()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDat = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDat));
            String calDuration = managedCursor.getString(duration);
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
         CallData   c = new CallData(phNumber, name, calDuration, dir, callDayTime.toString());

            Toast.makeText(this, c.toString(), Toast.LENGTH_SHORT).show();
                phNum.setText("numero : "+phNumber);
                callDate.setText("date : "+callDayTime);
                calltype.setText("type :"+dir);
            callDuration.setText("duration :"+calDuration);
            historique.setVisibility(View.VISIBLE);
            dbHelper = new DBHelper(getApplicationContext());
            dbHelper.saveCall(c);
        }
        managedCursor.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(listener.callEnded){
            listener.callEnded = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(listener.callEnded){
            saveCallDetails();
            historique.setVisibility(View.VISIBLE);
        }
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

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull adapter.MyViewHolder holder, int position) {
            if (position > 0) {
                if(calls.get(position)!=null){
                    holder.name.setText(calls.get(position).getName());
                    holder.duration.setText(calls.get(position).getDuration());
                    holder.number.setText(calls.get(position).getNumber());
                    holder.date.setText(calls.get(position).getDate());

                }

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




