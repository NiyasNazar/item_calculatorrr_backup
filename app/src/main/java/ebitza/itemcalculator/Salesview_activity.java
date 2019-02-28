package ebitza.itemcalculator;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.vipul.hp_hp.library.Layout_to_Image;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.FileHandler;



import ebitza.itemcalculator.Adapter.Adapter_sales_View;
import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_sales;

public class Salesview_activity extends AppCompatActivity {
    Button button_from, button_to, button_view_sales,Backup;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    RelativeLayout relativeLayout;
    Layout_to_Image layout_to_image;
    DatabaseHelper databaseHelper;
    String to, from;
    ListView recordsView;
    RecyclerView recyclerView;
    Adapter_sales_View adapter_sales_view;
    MediaScannerConnection    msConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesview_activity);
        dbManager = new DBManager(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
        dbManager.open();
        recordsView = (ListView) findViewById(R.id.records_view_sales);
        Backup = (Button) findViewById(R.id.btn_sales_export);
     Backup.setEnabled(false);
        //Backup.setBackgroundColor(Color.parseColor("#808080"));
        Backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWholeListViewItemsToBitmap(recordsView);
                Toast.makeText(getApplicationContext(),"Backup Success",Toast.LENGTH_SHORT).show();
            }
        });

        button_view_sales = (Button) findViewById(R.id.btn_sales_view);
        button_from = (Button) findViewById(R.id.btn_pick_date_from);
        button_to = (Button) findViewById(R.id.btn_pick_date_to);
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


                                from = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = (Date) formatter.parse(from);
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

                                to = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = (Date) formatter.parse(to);
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
                if (from == null) {
                    Toast.makeText(getApplicationContext(), "Start Date Required", Toast.LENGTH_LONG).show();

                } else if (to == null) {
                    Toast.makeText(getApplicationContext(), "End Date Required", Toast.LENGTH_LONG).show();

                } else {


                    List<Model_sales> datalist = dbManager.getsalessearch(from, to);
                    adapter_sales_view = new Adapter_sales_View(getApplicationContext(), datalist);
                    int a = adapter_sales_view.getCount();
                    if (a == 0) {
                        Toast.makeText(getApplicationContext(), "No Sales Made Yet", Toast.LENGTH_SHORT).show();
                    } else {

                        recordsView.setAdapter(adapter_sales_view);
                        adapter_sales_view.notifyDataSetChanged();
Backup.setEnabled(true);

                    }

                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent is = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(is);
        finish();
    }
    public void getWholeListViewItemsToBitmap(ListView listview) {
       // ListView listview = p_ListView;
        ListAdapter adapter = listview.getAdapter();
        int itemscount = adapter.getCount();
        int allitemsheight = 0;
        List<Bitmap> bmps = new ArrayList<Bitmap>();
        for (int i = 0; i < itemscount; i++) {
            View childView = adapter.getView(i, null, listview);
            childView.measure(
                    View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            childView.setDrawingCacheEnabled(true);
            childView.buildDrawingCache();
            childView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bmps.add(childView.getDrawingCache());
            allitemsheight += childView.getMeasuredHeight();
        }
        /*listview.setDrawingCacheEnabled(true);
        listview.buildDrawingCache();*/
        Bitmap bigbitmap = Bitmap.createBitmap(listview.getMeasuredWidth(), allitemsheight,
                Bitmap.Config.ARGB_8888);
        Canvas bigcanvas = new Canvas(bigbitmap);
        Paint paint = new Paint();
        int iHeight = 0;
        for (int i = 0; i < bmps.size(); i++) {
            Bitmap bmp = bmps.get(i);
            bigcanvas.drawBitmap(bmp, 0, iHeight, paint);
            iHeight += bmp.getHeight();
            bmp.recycle();
            bmp = null;
        }
        savePhoto(bigbitmap);
       // storeImage(bigbitmap, "Test.jpg");
    }
    /**
     * Convert the bitmap into image and save it into the sdcard.
     *
     * @param imageData
     *      -Bitmap image.
     * @param filename
     *      -Name of the image.
     * @return
     */
    public boolean storeImage(Bitmap imageData, String filename) {
        // get path to external storage (SD card)
        File sdIconStorageDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/SALESBACKUP/");
        // create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();
        try {
            String filePath = sdIconStorageDir.toString() + File.separator + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            Toast.makeText(getApplicationContext(), "Image Saved at----" + filePath, Toast.LENGTH_LONG).show();
            // choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void savePhoto(Bitmap bmp)
    {

        File imageFileFolder = new File(Environment.getExternalStorageDirectory(),"Rotate");

        FileOutputStream out = null;
        Calendar c = Calendar.getInstance();
        String date = fromInt(c.get(Calendar.MONTH))
                + fromInt(c.get(Calendar.DAY_OF_MONTH))
                + fromInt(c.get(Calendar.YEAR))
                + fromInt(c.get(Calendar.HOUR_OF_DAY))
                + fromInt(c.get(Calendar.MINUTE))
                + fromInt(c.get(Calendar.SECOND));
        File  imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
        try
        {
            out = new FileOutputStream(imageFileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            scanPhoto(imageFileName.toString());
            out = null;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public String fromInt(int val)
    {
        return String.valueOf(val);
    }


    public void scanPhoto(final String imageFileName)
    {
        msConn = new MediaScannerConnection(Salesview_activity.this,new MediaScannerConnection.MediaScannerConnectionClient()
        {
            public void onMediaScannerConnected()
            {
                msConn.scanFile(imageFileName, null);
                Log.i("Utility","connection established");
            }
            public void onScanCompleted(String path, Uri uri)
            {
                msConn.disconnect();
                Log.i(" Utility","scan completed");
            }
        });
        msConn.connect();
    }

}

