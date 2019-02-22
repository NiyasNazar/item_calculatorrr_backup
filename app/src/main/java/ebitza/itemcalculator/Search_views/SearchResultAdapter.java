package ebitza.itemcalculator.Search_views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.R;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<SearchResult> mItemList;
   private Context mc;
    String a;
    private DBManager dbManager;

    public SearchResultAdapter(List<SearchResult> mItemList,Context mc) {
        this.mItemList = mItemList;
        this.mc=mc;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.addproduct_from_search, parent, false);
        return new SearchResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SearchResultViewHolder holder, int position) {
        SearchResult result = mItemList.get(position);
        holder.mTitleTextView.setText(result.getTitle());
        holder.mDescriptionTextView.setText(result.getItem_Quantity());
        holder.mprice.setText(result.getItem_price());

        Log.i("resultA",result.getTitle());
        byte imgs[]=result.get_img();
        if (imgs==null){

        }else{
            Bitmap bmp = BitmapFactory.decodeByteArray(imgs, 0, imgs.length);

            holder.imv.setImageBitmap(bmp);
            Log.i("checkimagssse", Arrays.toString(imgs));
            //   holder.imv.setImageBitmap(convertToBitmap(movie.get_img()));
        }
      holder.implus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a= Integer.parseInt(holder.selected_quantity.getText().toString());
                a=a+1;
                holder. selected_quantity.setText(""+a);

            }
        });


      holder.  imminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int a= Integer.parseInt(holder.selected_quantity.getText().toString());
                if (a>0) {
                    a = a - 1;
                    holder. selected_quantity.setText("" + a);
                }


            }
        });




    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView selected_quantity;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mprice;
        Button add_product;
        ImageView imv,implus,imminus;
        CardView cardView;
        public SearchResultViewHolder(final View itemView) {
            super(itemView);
            dbManager = new DBManager(mc);
            dbManager.open();
            mTitleTextView = (TextView) itemView.findViewById(R.id.dialog_item_name);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.itemq);
            mprice = (TextView) itemView.findViewById(R.id.dialog_item_price);
            selected_quantity=itemView.findViewById(R.id.tvqua);
            imv=(ImageView)itemView.findViewById(R.id.dialog_imv);
     implus=itemView.findViewById(R.id.imvplus);
            imminus=itemView.findViewById(R.id.imvminus);
           /* implus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a= Integer.parseInt(selected_quantity.getText().toString());
                    a=a+1;
                    selected_quantity.setText(""+a);

                }
            });*/

        /*   imminus=itemView.findViewById(R.id.imvminus);
            imminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int a= Integer.parseInt(selected_quantity.getText().toString());
                    if (a>0) {
                        a = a - 1;
                        selected_quantity.setText("" + a);
                    }


                }
            });
*/

            add_product=(Button)itemView.findViewById(R.id.btn_alert_dialog_confirm);
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();

                    final String itemid=mItemList.get(position).getItem_id();
                    final String itemname=mItemList.get(position).getTitle();
                    final String itemprice=mItemList.get(position).getItem_price();
                    final String itemquantity=mItemList.get(position).getItem_Quantity();

                    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


                    String pp=mItemList.get(position).getItem_price();
                    a=selected_quantity.getText().toString();
                    if (a.equals("0")){
                        Toast.makeText(mc,"Quantity must be greater than zero",Toast.LENGTH_SHORT).show();
                    }else {
                        int q=Integer.parseInt(a);
                        int p=Integer.parseInt(pp);
                        int tot=p*q;
                        String total=String.valueOf(tot);
                        dbManager.additemsforbilling(itemname,itemprice,a,total,date);

          AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                       // AlertDialog.Builder      builder = new AlertDialog.Builder(itemView.getContext(),R.style.dialogTheme);
                        builder.setMessage("Successfully Added")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        selected_quantity.setText("0");
                                    }
                                });
                        AlertDialog alert = builder.create();
                      //  alert.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);

                        alert.show();



                    }
                }
            });

        }
    }



    public void addAll(Collection<SearchResult> items) {
        int currentItemCount = mItemList.size();
        mItemList.addAll(items);
        notifyItemRangeInserted(currentItemCount, items.size());
    }

    public void addAll(int position, Collection<SearchResult> items) {
        int currentItemCount = mItemList.size();
        if(position > currentItemCount)
            throw new IndexOutOfBoundsException();
        else
            mItemList.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public void replaceWith(Collection<SearchResult> items) {
        replaceWith(items, false);
    }

    public void clear() {
        int itemCount = mItemList.size();
        mItemList.clear();
        notifyItemRangeRemoved(0, itemCount);
    }


    public void replaceWith(Collection<SearchResult> items, boolean cleanToReplace) {
        if(cleanToReplace) {
            clear();
            addAll(items);
        } else {
            int oldCount = mItemList.size();
            int newCount = items.size();
            int delCount = oldCount - newCount;
            mItemList.clear();
            mItemList.addAll(items);
            if(delCount > 0) {
                notifyItemRangeChanged(0, newCount);
                notifyItemRangeRemoved(newCount, delCount);
            } else if(delCount < 0) {
                notifyItemRangeChanged(0, oldCount);
                notifyItemRangeInserted(oldCount, - delCount);
            } else {
                notifyItemRangeChanged(0, newCount);
            }
        }
    }
}