package ebitza.itemcalculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import ebitza.itemcalculator.Models.Model_category_item;

public class Remove_items extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    String a,as,a1,as2;
    MaterialSpinner materialSpinner2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_items);

        final ProgressDialog pd = new ProgressDialog(Remove_items.this);
        pd.setMessage("Deleting");

        final MaterialSpinner materialSpinner=(MaterialSpinner)findViewById(R.id.spinner_choose_category);

         materialSpinner2=(MaterialSpinner)findViewById(R.id.spinner_choose_item);
        dbManager=new DBManager(getApplicationContext());
        databaseHelper=new DatabaseHelper(getApplicationContext());
        dbManager.open();
        final List<Model_category> datalist=dbManager.getAllLabels();
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
                load();


            }
        });


        Button remove=(Button)findViewById(R.id.btn_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (a==null) {
                    Toast.makeText(getApplicationContext(), "Choose category ", Toast.LENGTH_LONG).show();
                } else if (as2==null) {
                    Toast.makeText(getApplicationContext(), "Choose Subcategory ", Toast.LENGTH_LONG).show();
                } else {
                    pd.show();
                    long delayInMillis = 3000;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            dbManager.deleteitems(a1, as);
                            Intent is = new Intent(getApplicationContext(), Remove_items.class);

                            startActivity(is);
                            finish();
                            pd.dismiss();
                        }
                    }, delayInMillis);

                }
            }
        });













    }

    private void load() {
        final List<Model_category_item>datalist2=dbManager.getAllitems(as);
        String[] nameList2=new String[datalist2.size()];
        for(int i=0;i<datalist2.size();i++){
            nameList2[i]=datalist2.get(i).getItemname();//create array of name
        }
        final ArrayAdapter<String> categoriesAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, nameList2);
        //materialSpinner.setDrop
        materialSpinner2.setAdapter(categoriesAdapter2);
        materialSpinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                a1=datalist2.get(position).getItem_id();
                as2=datalist2.get(position).getItemname();


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
