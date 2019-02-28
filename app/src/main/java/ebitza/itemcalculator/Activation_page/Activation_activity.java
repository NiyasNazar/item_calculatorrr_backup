package ebitza.itemcalculator.Activation_page;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ebitza.itemcalculator.MainActivity;
import ebitza.itemcalculator.MyPreferences;
import ebitza.itemcalculator.R;
import ebitza.itemcalculator.Test;

public class Activation_activity extends AppCompatActivity {
    String finalDateString;
    String strbody;
    ProgressDialog progress;
    String TAG="s";
    Handler mHandler;
    String  current_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_activity);

        isSMSPermissionGranted();


   progress = new ProgressDialog(Activation_activity.this){
        @Override
        public void onBackPressed() {
            //progress.dismiss();
            Activation_activity.this.finish();

        }};


        progress.setTitle("Item Calculator Activation");
        progress.setMessage("Waiting For Activation...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
// To dismiss the dialog
       // progress.dismiss();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        /*System.out.println(formatter.format(date));*/

        Log.i("times",formatter.format(date));

    current_time=formatter.format(date);
       // Check_message(current_time);
        final Handler handler = new Handler();
        final int delay = 3000; //milliseconds

    /* handler.postDelayed(new Runnable(){
            public void run(){
               // Check_message(current_time);
                handler.postDelayed(this, delay);

*/
        Check_message(current_time);

    /*if (Build.VERSION.SDK_INT>=23){
        isSMSPermissionGranted();
    }else{
        Check_message(current_time);
    }*/

if (isSMSPermissionGranted()) {
/*    Check_message(current_time);*/
    Log.i("currentdate",current_time);
}
        Log.i("currentdate",current_time);
       // Log.i("final",finalDateString);
        Toast.makeText(getApplicationContext(),"hcurrentdateai"+current_time,Toast.LENGTH_SHORT).show();
     Toast.makeText(getApplicationContext(),"final"+finalDateString,Toast.LENGTH_SHORT).show();

/*if (!strbody.equals("")) {
   // check(current_time, strbody);
}*/
        }








 /*   private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Toast.makeText(getApplicationContext(),"in runnable",Toast.LENGTH_SHORT).show();

            Activation_activity.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };*/



/*919048765852*/

    private void Check_message(String current_time) {
        StringBuilder smsBuilder = new StringBuilder();
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getContentResolver().query(uri, projection, "address='+919048765852'", null, "date asc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
 strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);
                    Log.i("str",strAddress);
                    Log.i("str",strbody);
                    //Log.i("str",strAddress);

                  //  Toast.makeText(getApplicationContext(),""+strAddress,Toast.LENGTH_SHORT).show();

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(longDate + ", ");
                    smsBuilder.append(int_Type);
                    smsBuilder.append(" ]\n\n");

                    DateFormat formatters = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(longDate);
             finalDateString = formatters.format(calendar.getTime());
             check(current_time,strbody);
                  //  Toast.makeText(getApplicationContext(),smsBuilder.toString(),Toast.LENGTH_SHORT).show();


                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }







    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

             String[] msg=  message.split(":");
             msg[1]=msg[1].trim();
             String ms=msg[1];


                /*String msg=message.replaceAll("\\s+","");*/

                final String sender = intent.getStringExtra("Sender");


                Toast.makeText(getApplicationContext(),"te"+ms,Toast.LENGTH_SHORT).show();
                Log.i("sss",ms);
                finalDateString = intent.getStringExtra("finalDateString");
                Log.i("sss",finalDateString);
                Toast.makeText(getApplicationContext(),"te"+finalDateString,Toast.LENGTH_SHORT).show();
                if (ms.equals("ACTIVATEIC")){
                check(current_time,ms);}else{
                    Log.i("sss","notthatone");

                }
            }
        }
    };


    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void check(String current_time, String strbodys) {

        if (current_time.equals(finalDateString)){
            Log.i("prefernce","loaddate");
           // Log.i("strbodys",strbodys);

            if (strbodys.equals("ACTIVATEIC")) {
                // handler.removeCallbacks(m_Runnable);
                Intent is = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(is);
                finish();

                final SharedPreferences reader = getSharedPreferences("activation", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = reader.edit();
                editor.putBoolean("is_first", true);
                editor.apply();
                progress.dismiss();
            }else{
                Log.i("prefernce","loadp");
                final SharedPreferences reader = getSharedPreferences("activation", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = reader.edit();
                editor.putBoolean("is_first", false);
                editor.apply();

            }
        }else{
            Log.i("prefernce","loaddateelse");
            //doTheAutoRefresh();

        }




    }
    private boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted2");
                return true;
            } else {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted2");
            return true;
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
           // Check_message(current_time);
                    //resume tasks needing this permission
                   /* Intent is=new Intent(getApplicationContext(), Activation_activity.class);
                    startActivity(is);
                    finish();*/
                    //downloadPdfFile();
                }else{
                    // progress.dismiss();
                }
                break;

            case 3:

                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // SharePdfFile();
                } else {
                    // progress.dismiss();

                }
                break;
        }
    }



}
