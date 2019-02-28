package ebitza.itemcalculator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IncomingSms  extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";

        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                sms_str += "\r\nMessage: ";
                sms_str += smsm[i].getMessageBody().toString();
                sms_str+= "\r\n";

                String Sender = smsm[i].getOriginatingAddress();
               long  a =smsm[i].getTimestampMillis();
                DateFormat formatters = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(a);
              String  finalDateString = formatters.format(calendar.getTime());
                //Check here sender is yours
                Log.i("smss", sms_str);
                if (Sender.equals("+919048765852")) {
/*
                    +919048765852
*/
                        Intent smsIntent = new Intent("otp");
                        smsIntent.putExtra("message", sms_str);
                        smsIntent.putExtra("Sender", Sender);
                        smsIntent.putExtra("finalDateString", finalDateString);
                        Log.i("sms", sms_str);
                        Log.i("sms", Sender);
                        Log.i("sms", finalDateString);

                        LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

                }else{
                    Log.i("sms","not that sender");
                }

            }
        }
    }
}