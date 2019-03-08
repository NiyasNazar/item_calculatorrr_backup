package ebitza.itemcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ebitza.itemcalculator.Db_Helper.DBManager;

public class LOGIN extends AppCompatActivity {
    EditText username,password;
    Button login;
DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbManager=new DBManager(getApplicationContext());
        dbManager.open();
        username=(EditText)findViewById(R.id.edusername);
        password=(EditText)findViewById(R.id.edpass);
        login=(Button)findViewById(R.id.activate) ;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( dbManager.checkUser(username.getText().toString(),password.getText().toString())){
                   Intent is=new Intent(getApplicationContext(),MainActivity.class);
                   startActivity(is);
                   finish();
                   final SharedPreferences reader = getSharedPreferences("activation", Context.MODE_PRIVATE);
                   final SharedPreferences.Editor editor = reader.edit();
                   editor.putBoolean("is_first", true);
                   editor.apply();

                }


            }
        });
    }
}
