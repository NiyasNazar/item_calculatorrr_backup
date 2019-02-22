package ebitza.itemcalculator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ebitza.itemcalculator.Adapter.Bill_adapter;
import ebitza.itemcalculator.Db_Helper.DBManager;
import ebitza.itemcalculator.Db_Helper.DatabaseHelper;
import ebitza.itemcalculator.Models.Model_bill;

import static java.security.AccessController.getContext;

public class Final_bill_view extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;
    SQLiteDatabase db;
    RelativeLayout relativeLayout;
    Layout_to_Image layout_to_image;
    DatabaseHelper databaseHelper;
    Bitmap bitmap;
    LinearLayout linearLayout, eroorLayout;
    RelativeLayout mainLa;
    ImageView imss;
    int total = 0;
    Button sales;
    EditText Enter_balance,etUsername,etEmail;
EditText Viewbalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bill_view);
        linearLayout = (LinearLayout) findViewById(R.id.lltbl);
        TextView tv = (TextView) findViewById(R.id.tvtotalprice);
        sales=(Button)findViewById(R.id.btn_sale);
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*  dbManager.CreateDynamicTablesmysales();*/
            dbManager.insertosales();
                dbManager.deletetable();
                Toast.makeText(getApplicationContext(),"Saved Succesfully",Toast.LENGTH_SHORT).show();
                Intent is=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(is);
                finish();
            }
        });
        final ProgressDialog pd = new ProgressDialog(Final_bill_view.this);
        pd.setMessage("Processing ...");
        relativeLayout = (RelativeLayout) findViewById(R.id.containers);
        mainLa = (RelativeLayout) findViewById(R.id.mainlayout);
        eroorLayout = (LinearLayout) findViewById(R.id.error_layout);
        Button go_back = (Button) findViewById(R.id.btn_go_back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent is = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(is);
                finish();
            }
        });
        // layout_to_image=new Layout_to_Image(Final_bill_view.this,relativeLayout);


        Viewbalance = (EditText) findViewById(R.id.tv_balance);
        Enter_balance = (EditText) findViewById(R.id.ed_enter_balance);
        final int maxLength = 6;
        Enter_balance.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        Enter_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable as) {
                String s=as.toString();
             int bill=0;
                if (!s.equals("")) {
                    try{
                  int a = !s.equals("")?Integer.parseInt(s) : 0;



                            bill = total - a;
                    }catch(NumberFormatException ex){ // handle your exception

                    }
                }
                if (bill < 0) {


                    Viewbalance.setText("" + bill);
                } else {
                    Viewbalance.setText(" " + bill);

                }

            }
        });


        final ListView recordsView = (ListView) findViewById(R.id.records_view);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        dbManager = new DBManager(getApplicationContext());
        dbManager.open();
        List<Model_bill> datalist = dbManager.get_generate_bill();
        if (datalist.size() == 0) {
            eroorLayout.setVisibility(View.VISIBLE);
            mainLa.setVisibility(View.GONE);
        } else {
            mainLa.setVisibility(View.VISIBLE);
            eroorLayout.setVisibility(View.GONE);

        }
        Bill_adapter bill_adapter = new Bill_adapter(getApplicationContext(), datalist);
        for (int i = 0; i < datalist.size(); i++) {

            int totz = Integer.parseInt(datalist.get(i).getTotal_amount());
            total += totz;

        }
        recordsView.setAdapter(bill_adapter);
        EditText totals = (EditText) findViewById(R.id.totalz);

        tv.setText("" + total);
        totals.setText("" + total);


        Button sharebill = (Button) findViewById(R.id.btn_share_bill);
        sharebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
 linearLayout.setVisibility(View.GONE);
                bitmap = ScreenshotUtils.getScreenShot(relativeLayout);
                storeImage(bitmap);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View alertLayout = inflater.inflate(R.layout.add_contacts, null);

          etEmail = alertLayout.findViewById(R.id.et_email);

                AlertDialog.Builder alert = new AlertDialog.Builder(Final_bill_view.this);
                alert.setTitle("");
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       Intent is=new Intent(getApplicationContext(),Final_bill_view.class);
                       startActivity(is);
                       finish();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    String phoneTypeStr="mobile";

                        /*String user = etUsername.getText().toString();
                        String pass = etEmail.getText().toString();
                        if (user.equals("")){
                            etUsername.setError("Name Required");
                        }else if(pass.equals("")){
                            etEmail.setError("Mobile number Required");
                        }else if(!isValid(pass)){
                            etEmail.setError("10 digits Required");
                        }else {
                            Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                            long rowContactId = getRawContactId();
                            insertContactDisplayName(addContactsUri, rowContactId, user);
                            insertContactPhoneNumber(addContactsUri, rowContactId, pass, phoneTypeStr);
                            screen();
                        }*/
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                theButton.setOnClickListener(new CustomListener(dialog));


               /* pd.show();
                linearLayout.setVisibility(View.GONE);
                bitmap = ScreenshotUtils.getScreenShot(relativeLayout);


                pd.dismiss();
                String pack = "com.whatsapp";

                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("image/*");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                whatsappIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                try {
                    startActivityForResult(whatsappIntent, 0);


                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "App not Installed" + ex, Toast.LENGTH_SHORT).show();

                }
                //  onClickApp(pack,bitmap);*/





            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            Intent is = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(is);
            finish();
        }


    }


    @Override
    public void onBackPressed() {
        Intent is=new Intent(getApplicationContext(),MainActivity.class);
      //  is.putExtra("str",tablename);
        startActivity(is);
        finish();

    }
    public void screen(){
        dbManager.insertosales();
        dbManager.deletetable();
                    // pd.show();
               /* linearLayout.setVisibility(View.GONE);
                bitmap = ScreenshotUtils.getScreenShot(relativeLayout);*/


             //   pd.dismiss();
                String pack = "com.whatsapp";

                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("image/*");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                whatsappIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                try {
                    startActivityForResult(whatsappIntent, 0);


                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "App not Installed" + ex, Toast.LENGTH_SHORT).show();

                }
                //  onClickApp(pack,bitmap);*/

    }
    private long getRawContactId()
    {
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }
    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);

        getContentResolver().insert(addContactsUri, contentValues);

    }

    private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String phoneTypeStr)
    {
        // Create a ContentValues object.
        ContentValues contentValues = new ContentValues();

        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);

        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;

        if("home".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
        }else if("mobile".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }else if("work".equalsIgnoreCase(phoneTypeStr))
        {
            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, contentValues);

    }

    public static boolean isValid(String s)
    {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            String phoneTypeStr="mobile";

       //     String user = etUsername.getText().toString();
            String pass = etEmail.getText().toString();
             if(pass.equals("")){
                etEmail.setError("Mobile number Required");
            }else if(!isValid(pass)){
                etEmail.setError("10 digits Required");
            }else {
               /* Uri addContactsUri = ContactsContract.Data.CONTENT_URI;
                long rowContactId = getRawContactId();
                insertContactDisplayName(addContactsUri, rowContactId, user);
                insertContactPhoneNumber(addContactsUri, rowContactId, pass, phoneTypeStr);*/
               // screen();
                openWhatsApp(pass);
            }

        }

        private void openWhatsApp(String smsNumbe ) {
            dbManager.insertosales();
            dbManager.deletetable();
String smsNumber="91"+smsNumbe;

            boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
            if (isWhatsappInstalled) {
                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                Uri bitmapUri = Uri.parse(bitmapPath);
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
                sendIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                startActivityForResult(sendIntent, 0);
            } else {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(getApplicationContext(), "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }
        }

        private boolean whatsappInstalledOrNot(String uri) {
            PackageManager pm = Objects.requireNonNull(getApplicationContext()).getPackageManager();
            boolean app_installed = false;
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                app_installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                app_installed = false;
            }
            return app_installed;
        }




    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("TAG",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }


}


