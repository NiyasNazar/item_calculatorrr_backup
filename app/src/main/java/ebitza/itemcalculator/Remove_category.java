package ebitza.itemcalculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_category;

public class Remove_category extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    String a,as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_category);
        final ProgressDialog pd = new ProgressDialog(Remove_category.this);
        pd.setMessage("Deleting");

        final MaterialSpinner materialSpinner=(MaterialSpinner)findViewById(R.id.spinner_choose_category);
        dbManager=new DBManager(getApplicationContext());
        databaseHelper=new DatabaseHelper(getApplicationContext());
        dbManager.open();
        final List<Model_category>datalist=dbManager.getAllLabels();
        String[] nameList=new String[datalist.size()];
        for(int i=0;i<datalist.size();i++){
            nameList[i]=datalist.get(i).getCategory_name(); //create array of name
        }
        final ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, nameList);
        //materialSpinner.setDrop
        materialSpinner.setAdapter(categoriesAdapter);

        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
            a=datalist.get(position).getCategory_id();
                as=datalist.get(position).getCategory_name();

          Toast.makeText(getApplicationContext(), "Clicked " + item, Toast.LENGTH_LONG).show();
            }
        });
        Button remove=(Button)findViewById(R.id.btn_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (a==null){

                    Toast.makeText(getApplicationContext(), "Choose category " , Toast.LENGTH_LONG).show();

                }else if (as==null){

                  //  Toast.makeText(getApplicationContext(), "Clicked ", Toast.LENGTH_LONG).show();

                }else{
                    pd.show();
                    long delayInMillis = 3000;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            final String data2=as.replaceAll("\\s+","");

                            dbManager.deletcategoty(a,data2);
                           Intent is=new Intent(getApplicationContext(),Remove_category.class);

                           startActivity(is);
                           finish();
                           pd.dismiss();
                        }
                    }, delayInMillis);

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
