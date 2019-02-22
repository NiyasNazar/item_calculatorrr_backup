package ebitza.itemcalculator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.vipul.hp_hp.library.Layout_to_Image;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ebitza.itemcalculator.Adapter.Adapter_sales_View;
import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_sales;

public class Salesview_activity extends AppCompatActivity {
    Button button_from,button_to,button_view_sales;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    RelativeLayout relativeLayout;
    Layout_to_Image layout_to_image;
    DatabaseHelper databaseHelper;
    String to,from;
  ListView recordsView;
  Adapter_sales_View adapter_sales_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesview_activity);
dbManager=new DBManager(getApplicationContext());
databaseHelper=new DatabaseHelper(getApplicationContext());
dbManager.open();
        recordsView = (ListView) findViewById(R.id.records_view_sales);

        button_view_sales=(Button)findViewById(R.id.btn_sales_view);
        button_from=(Button)findViewById(R.id.btn_pick_date_from);
        button_to=(Button)findViewById(R.id.btn_pick_date_to);
        button_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Salesview_activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                               from=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = (Date)formatter.parse(from);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                               from = newFormat.format(date);


                                button_from.setText(from);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();






            }
        });
        button_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Salesview_activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                to=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;

                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = (Date)formatter.parse(to);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                                to = newFormat.format(date);





                                button_to.setText(to);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();







            }
        });
button_view_sales.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
if (from==null){
    Toast.makeText(getApplicationContext(),"Start Date Required",Toast.LENGTH_LONG).show();

}else if(to==null){
    Toast.makeText(getApplicationContext(),"End Date Required",Toast.LENGTH_LONG).show();

}else{


        List<Model_sales>datalist=dbManager.getsalessearch(from,to);
        adapter_sales_view=new Adapter_sales_View(getApplicationContext(),datalist);
        int a=adapter_sales_view.getCount();
   if(a==0){
       Toast.makeText(getApplicationContext(),"No Sales Made Yet",Toast.LENGTH_SHORT).show();
   }else{
       recordsView.setAdapter(adapter_sales_view);
       adapter_sales_view.notifyDataSetChanged();

   }

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
