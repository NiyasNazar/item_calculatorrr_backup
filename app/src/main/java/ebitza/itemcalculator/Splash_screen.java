package ebitza.itemcalculator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import ebitza.itemcalculator.Activation_page.Activation_activity;
import ebitza.itemcalculator.Db_Helper.DBManager;

public class Splash_screen extends AppCompatActivity {
    String TAG="TAG";
    DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbManager=new DBManager(getApplicationContext());
        dbManager.open();
        dbManager.Createlogin();
        dbManager.inserttlogin();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final SharedPreferences reader = getSharedPreferences("activation", Context.MODE_PRIVATE);
                final boolean activated = reader.getBoolean("is_first", false);


                boolean isFirstTime = MyPreferences.isFirst(Splash_screen.this);
                if (isFirstTime) {
                    dbManager.CreateDynamicTablesWarning();
                    dbManager.insertstatus("Yes");
                    if (Build.VERSION.SDK_INT >= 26) {
                        Toast.makeText(getApplicationContext(),"oreo",Toast.LENGTH_SHORT).show();
             Log.i("version","oreo");
                        isSMSPermissionGranted();

                    } else if(Build.VERSION.SDK_INT>=23) {
                        Toast.makeText(getApplicationContext(),"notoreo",Toast.LENGTH_SHORT).show();

                        Log.i("version","notoreo");
                        Toast.makeText(getApplicationContext(),"notoreo",Toast.LENGTH_SHORT).show();
                   requestSmsPermission();
//requestSmsPermissions();


                    }else{
                        Toast.makeText(getApplicationContext(),"noversion",Toast.LENGTH_SHORT).show();

                        Log.i("version","noversion");

                        Intent is = new Intent(getApplicationContext(), Activation_activity.class);
                        startActivity(is);
                        finish();
                    }

                } else {
                    if (activated) {
                        Log.i("through", "through");


                        Intent is = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(is);
                        finish();
                    } else {
                        Log.i("notactivated", "notactivated");
                        Intent is = new Intent(getApplicationContext(), Activation_activity.class);
                        startActivity(is);
                        finish();
                    }

                }
            }

        }, 2000);
    }

    private void requestSmsPermission() {

        String permission = Manifest.permission.READ_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 3);
        }



    }
    private void requestSmsPermissions() {

        String permission = Manifest.permission.SEND_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 9);
        }



    }






    private boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted2");
                return true;
            } else {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 2);
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
                    //resume tasks needing this permission
                    Intent is=new Intent(getApplicationContext(), Activation_activity.class);
                    startActivity(is);
                    finish();
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
                        Intent is=new Intent(getApplicationContext(), Activation_activity.class);
                        startActivity(is);
                        finish();
                    } else {
                        // progress.dismiss();

                }
                break;

            case 9:

                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    // SharePdfFile();
                    Intent is=new Intent(getApplicationContext(), Activation_activity.class);
                    startActivity(is);
                    finish();
                } else {
                    // progress.dismiss();

                }
                break;
        }
    }





}