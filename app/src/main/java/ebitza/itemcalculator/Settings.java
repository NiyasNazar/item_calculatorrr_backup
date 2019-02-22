package ebitza.itemcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import ebitza.itemcalculator.Db_Helper.DBManager;

public class Settings extends AppCompatActivity {
SharedPreferences prefs1;
DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        dbManager=new DBManager(getApplicationContext());
        dbManager.open();
        String a=dbManager.getwarningstatus();
        Log.i("ww",a);
        prefs1 = getSharedPreferences("warning", MODE_PRIVATE);

        final Switch simpleSwitch = (Switch) findViewById(R.id.switches);

if (a.equals("Yes")){
    simpleSwitch.setChecked(true);
}else if(a.equals("No")){
    simpleSwitch.setChecked(false);
}

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (simpleSwitch.isChecked()){
                    String   stat = simpleSwitch.getTextOn().toString();
dbManager.updatewarning(stat,"1");


                }else{
                    String   stat = simpleSwitch.getTextOff().toString();
                    dbManager.updatewarning(stat,"1");

                    Toast.makeText(getApplicationContext(),stat.toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });



    }
    @Override
    public void onBackPressed() {

        Intent is=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(is);
        finish();
    }
}




