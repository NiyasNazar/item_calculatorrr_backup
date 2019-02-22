package ebitza.itemcalculator.Adapter;

import android.app.Activity;
import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ebitza.itemcalculator.Models.Model_bill;
import ebitza.itemcalculator.R;

public class    Bill_adapter extends BaseAdapter {
    private Context recordContext;
    private List<Model_bill> recordList;

    public Bill_adapter(Context context, List<Model_bill> records) {
        recordList = records;
        recordContext = context;
    }
    @Override
    public int getCount() {
        return recordList.size();
    }
    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordViewHolder holder;

        if (view ==null){
            LayoutInflater recordInflater = (LayoutInflater)
                    recordContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = recordInflater.inflate(R.layout.item_bill, null);

            holder = new RecordViewHolder();
            holder.ageView = (TextView) view.findViewById(R.id.record_age);
            holder.nameView = (TextView) view.findViewById(R.id.record_name);
            holder.positionView = (TextView) view.findViewById(R.id.record_position);
            holder.addressView = (TextView) view.findViewById(R.id.record_address);
            view.setTag(holder);

        }else {
            holder = (RecordViewHolder) view.getTag();
        }

String a=recordList.get(i).getQuantity();
        if (!a.equals("0")) {


            holder.nameView.setText(recordList.get(i).getItem());
            holder.ageView.setText(recordList.get(i).getQuantity());
            holder.positionView.setText(recordList.get(i).getItem_price());
            holder.addressView.setText(recordList.get(i).getTotal_amount());
        }else{
           // recordList.remove(recordList.get(i));
        }
        return view;
    }
    private static class RecordViewHolder {

        public TextView nameView;
        public TextView positionView;
        public TextView ageView;
        public TextView addressView;
    }
}
