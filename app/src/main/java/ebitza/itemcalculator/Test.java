package ebitza.itemcalculator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test  extends AppCompatActivity {
    SharedPreferences prefs = null;
    Calendar alarmcalendar;
    String current_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("myAppName", MODE_PRIVATE);
        boolean isFirstTime = MyPreferences.isFirst(Test.this);
        if (isFirstTime){
            Toast.makeText(getApplicationContext(),"haifirstime",Toast.LENGTH_SHORT).show();
        }

        setTitle("Add Record");

        setContentView(R.layout.test);
        SimpleDateFormat formatter = new SimpleDateFormat(" yyyy-MM-dd");
        Date date = new Date();
        /*System.out.println(formatter.format(date));*/

        Log.i("times",formatter.format(date));

current_time=formatter.format(date);




        String launchdate=prefs.getString("launch",null);
       // Log.i("timess",launchdate);
        if (current_time.equals(launchdate)){

        }else{
            StringBuilder smsBuilder = new StringBuilder();
            final String SMS_URI_INBOX = "content://sms/inbox";
            final String SMS_URI_ALL = "content://sms/";
            try {
                Uri uri = Uri.parse(SMS_URI_INBOX);
                String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
                Cursor cur = getContentResolver().query(uri, projection, "address='+917012297229'", null, "date asc");
                if (cur.moveToFirst()) {
                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    do {
                        String strAddress = cur.getString(index_Address);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int int_Type = cur.getInt(index_Type);
                        Log.i("str",strAddress);
                        Log.i("str",strbody);
                        //Log.i("str",strAddress);

                        Toast.makeText(getApplicationContext(),""+longDate,Toast.LENGTH_SHORT).show();

                        smsBuilder.append("[ ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(longDate + ", ");
                        smsBuilder.append(int_Type);
                        smsBuilder.append(" ]\n\n");

                        DateFormat formatters = new SimpleDateFormat("dd/MM/yyyy ");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(longDate);
                        String finalDateString = formatters.format(calendar.getTime());
                        Toast.makeText(getApplicationContext(),smsBuilder.toString(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"hai"+finalDateString,Toast.LENGTH_SHORT).show();
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


        Button button = (Button) findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Test.this, Main2Activity.class));
              /*  final MediaPlayer mp = MediaPlayer.create(Test.this, R.raw.testss);
                mp.start();

                AlertDialog.Builder b = new AlertDialog.Builder(Test.this);
                b.setMessage("test6");
                b.setCancelable(false);
                b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                b.setTitle("hi");
                AlertDialog ad = b.create();
                ad.show();*/
               /* StringBuilder smsBuilder = new StringBuilder();
                final String SMS_URI_INBOX = "content://sms/inbox";
                final String SMS_URI_ALL = "content://sms/";
                try {
                    Uri uri = Uri.parse(SMS_URI_INBOX);
                    String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
                    Cursor cur = getContentResolver().query(uri, projection, "address='+917012297229'", null, "date asc");
                    if (cur.moveToFirst()) {
                        int index_Address = cur.getColumnIndex("address");
                        int index_Person = cur.getColumnIndex("person");
                        int index_Body = cur.getColumnIndex("body");
                        int index_Date = cur.getColumnIndex("date");
                        int index_Type = cur.getColumnIndex("type");
                        do {
                            String strAddress = cur.getString(index_Address);
                            int intPerson = cur.getInt(index_Person);
                            String strbody = cur.getString(index_Body);
                            long longDate = cur.getLong(index_Date);
                            int int_Type = cur.getInt(index_Type);
Log.i("str",strAddress);
                            Log.i("str",strbody);
                            //Log.i("str",strAddress);

                            Toast.makeText(getApplicationContext(),""+longDate,Toast.LENGTH_SHORT).show();

                            smsBuilder.append("[ ");
                            smsBuilder.append(strAddress + ", ");
                            smsBuilder.append(intPerson + ", ");
                            smsBuilder.append(strbody + ", ");
                            smsBuilder.append(longDate + ", ");
                            smsBuilder.append(int_Type);
                            smsBuilder.append(" ]\n\n");

                            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy ");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(longDate);
                            String finalDateString = formatter.format(calendar.getTime());
                            Toast.makeText(getApplicationContext(),smsBuilder.toString(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"hai"+finalDateString,Toast.LENGTH_SHORT).show();
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
                }*/


            }

        });
//        android:digits="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"





        /*AlarmManager service = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i,PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        // Start 1 month after boot completed
        cal.add(Calendar.MONTH, 1);
        //
        // Fetch every 1 month
        // InexactRepeating allows Android to optimize the energy consumption
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP ,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY , pending);
        */


        int a = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);















        /*final MediaPlayer mp = MediaPlayer.create(Test.this, R.raw.testss);
        mp.start();*/

    }


  /*  @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Toast.makeText(getApplicationContext(), "firstrun", Toast.LENGTH_SHORT).show();

            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();

  final MediaPlayer mp = MediaPlayer.create(Test.this, R.raw.testss);
            mp.start();
            prefs.edit().putString("launch",current_time).apply();
            Log.i("timesss",prefs.getString("launch",null));
            AlertDialog.Builder b = new AlertDialog.Builder(Test.this);
            b.setMessage("test6");
            b.setCancelable(false);
            b.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            b.setTitle("hi");
            AlertDialog ad = b.create();
            ad.show();
          prefs.edit().putString("lauch",current_time).apply();


    }*/
}
