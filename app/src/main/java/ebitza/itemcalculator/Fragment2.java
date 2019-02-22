package ebitza.itemcalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import ebitza.itemcalculator.Adapter.Adapter_for_item_list;
import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_category_item;

public class Fragment2 extends Fragment {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    String strtext="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (getArguments()!=null) {
strtext = getArguments().getString("message");
        }

        View view=inflater.inflate(R.layout.fragment2, container, false);
        RecyclerView RCVcaterory_item=(RecyclerView)view.findViewById(R.id.recyclerview_category);
        RCVcaterory_item.setLayoutManager(new GridLayoutManager(getActivity(),2));
        dbManager = new DBManager(getActivity());
        dbManager.open();
        databaseHelper=new DatabaseHelper(getActivity());
        db=databaseHelper.getReadableDatabase();
        String name2 = strtext.replaceAll("\\s+", "");
    Log.i("String",name2);
            List<Model_category_item> datalist = dbManager.getAllitems(name2);
            Adapter_for_item_list adapter = new Adapter_for_item_list(datalist, getActivity(), name2);
            RCVcaterory_item.setAdapter(adapter);
            adapter.notifyDataSetChanged();









        FloatingActionButton fabs=(FloatingActionButton)view.findViewById(R.id.fab);

        fabs.setImageBitmap(textAsBitmap("ADD", 70, Color.WHITE));



        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  dbManager.CreateDynamicTables(strtext,"items","price");
                Intent intent=new Intent(getActivity(),Add_item_to_categories.class);
                intent.putExtra("strtext",strtext);
                startActivity(intent);
                getActivity().finish();

            }
        });

     /*   TextView tv=(TextView)view.findViewById(R.id.fragment2text);
        tv.setText(strtext);*/
        return view;
    }
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

}