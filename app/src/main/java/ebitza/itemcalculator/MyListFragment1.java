package ebitza.itemcalculator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_category;

public class MyListFragment1 extends Fragment {







    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    ListView lv;
    int defaultPositon = 0;
    int justIgnoreId = 0;
DatabaseHelper databaseHelper;
    List<Model_category> labels;
    final String[] from = new String[] {DatabaseHelper._ID,
            DatabaseHelper.SUBJECT, DatabaseHelper.DESC };
    private ArrayList<String> Id = new ArrayList<String>();
    private ArrayList<String> Name = new ArrayList<String>();
String datas;
    final int[] to = new int[] { R.id.title};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(getActivity());
        dbManager.open();
        databaseHelper=new DatabaseHelper(getActivity());
        db=databaseHelper.getReadableDatabase();
        Log.i("read","readingdbs");
        //ListView listView=(ListView)view.findViewById(R.id.mylist);

     // lv.setSelection(0);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfragment1, container, false);
        Bundle bundle=getArguments();


        lv=(ListView) view.findViewById(R.id.lista);
     labels = dbManager.getAllLabels();
        final CustomAdapter dataAdapter=new CustomAdapter(getActivity(),labels);
       /* final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),R.layout.listview_item_category, labels);
      lv.  setAdapter(dataAdapter);*/
       lv.setAdapter(dataAdapter);
        if (getArguments()!=null) {
            datas = getArguments().getString("message1");
          //  Loadfirstposition(datas);

           // Log.i("String",datas);
        }
        if (datas!=null){
            Loadfirstposition(datas);


            Log.i("String","emptynot");
        }else if(!dataAdapter.isEmpty()){
            Log.i("String","loadeddef");
            String value = labels.get(0).getCategory_name();
           Loadfirstposition(value);
            lv.setItemChecked(defaultPositon, true);
        }




        if (!dataAdapter.isEmpty()) {
          //  Log.i("String","loadeddef");
            String value = labels.get(0).getCategory_name();
         //   Loadfirstposition(value);
          //  lv.setItemChecked(defaultPositon, true);
        }



       /* if(bundle !=null){
            datas=bundle.getString("message");
           // Log.i("test",datas);



               Loadfirstposition(datas);
            }*/



        /*if (getArguments()!=null){
            String strtext = getArguments().getString("str");
            Loadfirstposition(strtext);
        }*//*else if(!dataAdapter.isEmpty()) {
            String value = labels.get(0).getCategory_name();
            Loadfirstposition(value);
            lv.setItemChecked(defaultPositon, true);
        }*/

/*
if(!dataAdapter.isEmpty()) {
    String value = labels.get(0).getCategory_name();
    Loadfirstposition(value);
    lv.setItemChecked(defaultPositon, true);
}*/
  /*  lv.setItemChecked(defaultPositon, true);
        lv.performItemClick(lv.getSelectedView(), defaultPositon, justIgnoreId);*/



        ImageView add_category_btn = (ImageView) view.findViewById(R.id.addcat);
        add_category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent is = new Intent(getActivity(), Addcategory.class);
                startActivity(is);
                getActivity().finish();

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                final String data=labels.get(pos).getCategory_name();
                final String data2=data.replaceAll("\\s+","");
                final String catid=labels.get(pos).getCategory_id();
                final String[] options = {"Edit", "Remove"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pick an Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("Edit".equals(options[which])){




                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View alertLayout = inflater.inflate(R.layout.edt_single_cat, null);
                            final EditText etUsername = alertLayout.findViewById(R.id.et_username);
etUsername.setText(data);


                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Edit");
                            // this is set the view from XML inside AlertDialog
                            alert.setView(alertLayout);
                            alert.setCancelable(false);
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                                }
                            });

                            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final ProgressDialog dialogs = new ProgressDialog(getActivity());
                                    dialogs.setMessage("Please wait.....");
                                    dialogs.show();
                                    long delayInMillis = 3000;
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {

                                    String updated_data=etUsername.getText().toString();

dbManager.updatecategory(catid,updated_data);

dbManager.CreateDynamicTables2(updated_data,data);

                                            Intent is=new Intent(getActivity(), MainActivity.class);
                                          startActivity(is);
                                       getActivity().finish();
                                            dialogs.dismiss();
                                        }
                                    }, delayInMillis);


                                    Toast.makeText(getActivity(), "Updated Succesfully", Toast.LENGTH_SHORT).show();




                                }
                            });

                            AlertDialog dialogs = alert.create();
                            dialogs.show();










                        }else if("Remove".equals(options[which])){

                            final ProgressDialog dialogs = new ProgressDialog(getActivity());
                            dialogs.setMessage("Please wait.....");
                            dialogs.show();
                            long delayInMillis = 3000;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {



                                    dbManager.deletcategoty(catid,data2);

                                    Intent is=new Intent(getActivity(), MainActivity.class);
                                    startActivity(is);
                                    getActivity().finish();
                                    dialogs.dismiss();
                                }
                            }, delayInMillis);


                            Toast.makeText(getActivity(), "Deleted  Succesfully", Toast.LENGTH_SHORT).show();










                        }


                    }
                });
                builder.show();
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String data=labels.get(position).getCategory_name();
                Bundle bundle=new Bundle();
                String data1=data.replaceAll("\\s+","");
                bundle.putString("message", data1);
                Fragment2  myListFragment1=new Fragment2();
                myListFragment1.setArguments(bundle);
                getActivity().  getSupportFragmentManager().beginTransaction().replace(R.id.fragment2,myListFragment1).commit();

            }
        });


        return view;
    }


    private void Loadfirstposition(String value) {
     //   lv.setItemChecked(defaultPositon, true);
        Bundle bundle=new Bundle();
        bundle.putString("message", value);
        Fragment2  myListFragment1=new Fragment2();
        myListFragment1.setArguments(bundle);
        getActivity().  getSupportFragmentManager().beginTransaction().replace(R.id.fragment2,myListFragment1).commit();




    }








}
