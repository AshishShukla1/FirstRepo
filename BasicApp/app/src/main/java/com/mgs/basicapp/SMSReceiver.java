package com.mgs.basicapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by MGS1747 on 18-04-2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null != context && intent != null) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from="";
            String msgBody="";

            Log.d("smsreceiver ===", "-------"+intent.getAction());


            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    for (int i = 0; i < msgs.length; i++) {

                        SmsMessage currentMsg = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = currentMsg.getOriginatingAddress();
                        msgBody = currentMsg.getMessageBody();

                        if(msg_from.equals("+917977927294")){
                            Main3Activity.setOtp(msgBody);
                        }
                    }
//                    activity=(Activity) context;

                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }

            }
        }else{
            Log.d("ELSE  caught", " else");

        }
    }
}
