package ebitza.itemcalculator;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;

public class Add_item_to_categories extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    Button additem;
    ImageView chooseimageforproduct;
    EditText item_name, item_price,item_quantity;
    private Bitmap bp;
    private byte[] photo;
    ImageView imv;
  String tablename;
    Uri choosenImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_itm_to_category);
        additem = (Button) findViewById(R.id.add_item);
        chooseimageforproduct = (ImageView) findViewById(R.id.add_items);
        item_name = (EditText) findViewById(R.id.edittext_item_name);
        item_price = (EditText) findViewById(R.id.edittext_item_price);
        item_quantity=(EditText)findViewById(R.id.edittext_item_quantity);
        imv=(ImageView)findViewById(R.id.imv);
       tablename = getIntent().getStringExtra("strtext");
        dbManager = new DBManager(this);
        dbManager.open();
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Itemsname = item_name.getText().toString();
                String Itemprice = item_price.getText().toString();
                String Itemquantity=item_quantity.getText().toString();
                if (!Itemsname.matches("[a-zA-Z.? ]*")) {

                    item_name.setError("Special characters not allowed");

                }else  if (!Itemquantity.matches("[0-9a-zA-Z.? ]*")) {

                    item_quantity.setError("Special characters not allowed");

                }else
                if (bp!=null){
                    photo=profileImage(bp);

                    dbManager.additemstocategory(tablename, Itemsname, Itemprice,photo,Itemquantity);
                    item_name.setText("");
                    item_price.setText("");
                    item_quantity.setText("");
                    imv.setImageBitmap(null);
                    imv.setVisibility(View.GONE);
                    chooseimageforproduct.setVisibility(View.VISIBLE);
                    bp=null;
                    choosenImage=null;



                }else{
                    dbManager.additemstocategory(tablename, Itemsname, Itemprice,null,Itemquantity);
                    item_name.setText("");
                    item_price.setText("");
                    item_quantity.setText("");
                    imv.setImageBitmap(null);


                }

                if (photo==null){
                   /* dbManager.additemstocategory(tablename, Itemsname, Itemprice,null,Itemquantity);
                    item_name.setText("");
                    item_price.setText("");
                    item_quantity.setText("");
                    imv.setImageBitmap(null);*/

                }else{






                }


            }
        });
        chooseimageforproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
        bp=null;
        choosenImage=null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                  choosenImage = data.getData();

                    if (choosenImage != null) {

                        bp = decodeUri(choosenImage, 100);
                        imv.setImageBitmap(bp);
                        chooseimageforproduct.setVisibility(View.GONE);
                        imv.setVisibility(View.VISIBLE);
                    }
                }
        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
            }
        catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }
    @Override
    public void onBackPressed() {
        Intent is=new Intent(getApplicationContext(),MainActivity.class);
        is.putExtra("str",tablename);
        startActivity(is);
        finish();

    }
















    }

