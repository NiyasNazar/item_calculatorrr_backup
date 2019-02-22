package ebitza.itemcalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_category;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    DatabaseHelper controldb;
    SQLiteDatabase db;
    private ArrayList<String> Id = new ArrayList<String>();
    private List<Model_category> Name ;

    public CustomAdapter(Context  context,List<Model_category> Name){


        this.mContext = context;
        this.Name = Name;

    }
    @Override
    public int getCount() {
        return Name.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final    viewHolder holder;
        controldb =new DatabaseHelper(mContext);
        LayoutInflater layoutInflater;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_view_record, null);
            holder = new viewHolder();


            holder.name = (TextView) convertView.findViewById(R.id.titles);

            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        holder.name.setText(Name.get(position).getCategory_name());
        Log.i("names",Name.get(position).getCategory_name());


        return convertView;
    }
    public class viewHolder {

        TextView name;


    }
}