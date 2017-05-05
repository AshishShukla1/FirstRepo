package com.mgs.basicapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    static EditText autoRead;
    SMSReceiver smsReceiver;
    SmsManager smsManager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        autoRead=(EditText)findViewById(R.id.autoReadEditText);

        smsManager=SmsManager.getDefault();


        try {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

//                    smsManager.sendTextMessage("8291982388", null, "123459", null, null);
                }
            }, 2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(getIntent().getData()!=null)
            Toast.makeText(getApplicationContext()," Main3Activity "+getIntent().getData().toString(),Toast.LENGTH_LONG).show();
    }


    public void checkIllegalArgumentException(String resp){
        try {

            String[] data = resp.split("\\|");
            Bundle bundle = new Bundle();
            bundle.putString("pgMeTrnRefNo", data[0]);
            bundle.putString("yblRefId", data[1]);

        }catch (IllegalArgumentException e) {

            String[] data = resp.split("\\|");
            Bundle bundle = new Bundle();
            bundle.putString("pgMeTrnRefNo", data[0]);
            bundle.putString("yblRefId", data[1]);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i=new Intent(Main3Activity.this,SimpleActivity.class);
        Bundle b=new Bundle();
        b.putString("First","1");
        i.putExtras(b);

        checkIllegalArgumentException("");

        //startActivity(i);
    }

    public static void setOtp(String otp){
        autoRead.setText("");
        autoRead.setText(otp.trim());
    }
}
