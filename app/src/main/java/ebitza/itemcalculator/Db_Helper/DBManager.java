package ebitza.itemcalculator.Db_Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.cryse.widget.persistentsearch.SearchItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ebitza.itemcalculator.Category_model;
import ebitza.itemcalculator.Models.Mode_searchHistory;
import ebitza.itemcalculator.Models.Model_bill;
import ebitza.itemcalculator.Models.Model_category;
import ebitza.itemcalculator.Models.Model_category_item;
import ebitza.itemcalculator.Models.Model_sales;
import ebitza.itemcalculator.Search_views.SearchResult;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        database = dbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void addcategory(String name) {
        database = dbHelper.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.SUBJECT, name);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        String name2 = name.replaceAll("\\s+", "");
        Log.i("spacename",name2);
        CreateDynamicTables(name2);
        database.close();
    }


    public List<Category_model> getAllCategory() {
        List<Category_model> contactList = new ArrayList<Category_model>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category_model category_model = new Category_model();
                category_model.set_id(Integer.parseInt(cursor.getString(0)));
                category_model.set_description(cursor.getString(1));
                category_model.set_subject(cursor.getString(2));
                // Adding contact to list
                contactList.add(category_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public List<Model_category> getAllLabels() {
        List<Model_category> list = new ArrayList<Model_category>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_category model_category = new Model_category();
                model_category.setCategory_id(cursor.getString(0));
                model_category.setCategory_name(cursor.getString(1));
                list.add(model_category);//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public void CreateDynamicTables(String Table_Name) {
        String name2 = Table_Name.replaceAll("\\s+", "");


        String CID = "itemid";
        String DName = "item_name";
        String PName = "item_price";
        String KEY_IMAGE = "key_image";
        String KEY_Quantity = "key_quantity";

        database = dbHelper.getWritableDatabase();
        // database.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        String query = "CREATE TABLE IF NOT EXISTS " + name2 + "(" + CID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DName + " TEXT, " + PName + " TEXT, " + KEY_IMAGE + " TEXT, " + KEY_Quantity + " TEXT);";
        database.execSQL(query);
       /* database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CID, Contact_ID);
        cv.put(DName, Display_Name);
        database.insert(Table_Name, null, cv);*/
        // database.close();
    }

    public void additemstocategory(String tablename, String itemname, String item_price, byte[] photo, String Quantity) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("item_name", itemname);
        cv.put("item_price", item_price);
        cv.put("key_image", photo);
        cv.put("key_quantity", Quantity);
        database.insert(tablename, null, cv);
        database.close();


    }

    public List<Model_category_item> getAllitems(String tablename) {
        String tablename2 = tablename.replaceAll("\\s+", "");

        List<Model_category_item> contactList = new ArrayList<Model_category_item>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + tablename2;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_category_item category_model = new Model_category_item();
                category_model.setItem_id((cursor.getString(0)));
                category_model.setItemname(cursor.getString(1));
                category_model.setItemprice(cursor.getString(2));
                category_model.set_img(cursor.getBlob(3));
                category_model.setItemquantity(cursor.getString(4));
                byte[] aa = cursor.getBlob(3);
                Log.i("checkimage", Arrays.toString(aa));
                // Adding contact to list
                contactList.add(category_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void CreateDynamicTablesforbill() {
        String BID = "billid";
        String DName = "item_name";
        String PName = "item_price";
        String KEY_ITEM_QUANTITY = "itemquantity";
        String KEY_Total = "itemtotal";
        String KEY_Date = "dates";

        database = dbHelper.getWritableDatabase();
        //  database.execSQL("DROP TABLE IF EXISTS " + "BILL");
        String query = "CREATE TABLE IF NOT EXISTS " + "BILL" + "(" + BID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DName + " TEXT, " + PName + " TEXT, " + KEY_ITEM_QUANTITY + " TEXT, " + KEY_Total + " TEXT, " + KEY_Date + " TEXT);";
        database.execSQL(query);
       /* database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CID, Contact_ID);
        cv.put(DName, Display_Name);
        database.insert(Table_Name, null, cv);*/
        database.close();
    }

    public void additemsforbilling(String itemname, String itemrate, String item_quantity, String itemtotal, String date) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("item_name", itemname);
        cv.put("item_price", itemrate);
        cv.put("itemquantity", item_quantity);
        cv.put("itemtotal", itemtotal);
        cv.put("dates", date);
        database.insertWithOnConflict("BILL", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();

    }

    public void updatebilltable(String id, String item_quantity, String itemtotal, String itemname, String itemprice) {
        database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("item_name", itemname);
        cv.put("item_price", itemprice);
        cv.put("itemquantity", item_quantity);
        cv.put("itemtotal", itemtotal);
        database.update("Bill", cv, "item_name" + " = '" + itemname + "'", null);
    }

    public List<Model_bill> get_generate_bill() {
        List<Model_bill> contactList = new ArrayList<Model_bill>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + "BILL" + " Where itemquantity <> 0";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_bill category_model = new Model_bill();
                category_model.setBillid((cursor.getString(0)));
                category_model.setItem(cursor.getString(1));
                category_model.setItem_price(cursor.getString(2));
                category_model.setQuantity(cursor.getString(3));
                category_model.setTotal_amount(cursor.getString(4));

                contactList.add(category_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deletcategoty(String id, String tablename) {


        database = dbHelper.getWritableDatabase();
        database.delete("CATEGORIES", "_id" + "=" + id, null);
        database.execSQL("DROP TABLE IF EXISTS " + tablename);

    }

    public void deleteitems(String id, String tablename) {
        String name2 = tablename.replaceAll("\\s+", "");

        database = dbHelper.getWritableDatabase();
        database.delete(name2, "itemid" + "=" + id, null);
        // database.execSQL("DROP TABLE IF EXISTS " + tablename);
    }

    public void deletetable() {
        database = dbHelper.getWritableDatabase();
        database.execSQL("delete from " + "Bill");


    }

    public void updateitems(String tablename, String id, String itemname, String price, String quantity) {
        String name2 = tablename.replaceAll("\\s+", "");

        database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("item_name", itemname);
        cv.put("item_price", price);

        cv.put("key_quantity", quantity);
        database.update(name2, cv, "itemid=" + id, null);

    }

    public void updatecategory(String id, String catname) {

        database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", catname);


        database.update(DatabaseHelper.TABLE_NAME, cv, "_id=" + id, null);

    }

    public void CreateDynamicTables2(String Table_Name, String old_table_name) {
        String Table_Name2 = Table_Name.replaceAll("\\s+", "");
        String oldTable_Name2 = old_table_name.replaceAll("\\s+", "");

        String CID = "itemid";
        String DName = "item_name";
        String PName = "item_price";
        String KEY_IMAGE = "key_image";
        String KEY_Quantity = "key_quantity";

        database = dbHelper.getWritableDatabase();
        // database.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        String query = "CREATE TABLE IF NOT EXISTS " + Table_Name2 + "(" + CID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DName + " TEXT, " + PName + " TEXT, " + KEY_IMAGE + " TEXT, " + KEY_Quantity + " TEXT);";
        database.execSQL(query);
        String query2 = "INSERT INTO " + Table_Name2 + " SELECT * FROM " + oldTable_Name2;
        database.execSQL(query2);
        database.execSQL("DROP TABLE IF EXISTS " + oldTable_Name2);


    }

    public void CreateDynamicTablesmysales() {
        String BID = "salesid";
        String DName = "item_name";
        String PName = "item_price";
        String KEY_ITEM_QUANTITY = "itemquantity";
        String KEY_Total = "itemtotal";
        String KEY_Date = "dates";

        database = dbHelper.getWritableDatabase();
        //  database.execSQL("DROP TABLE IF EXISTS " + "BILL");
        String query = "CREATE TABLE IF NOT EXISTS " + "MYSALES" + "(" + BID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DName + " TEXT, " + PName + " TEXT, " + KEY_ITEM_QUANTITY + " TEXT, " + KEY_Total + " TEXT, " + KEY_Date + " TEXT);";
        database.execSQL(query);
      /*  String query2 = "INSERT INTO MYSALES SELECT * FROM BILL ";
        database.execSQL(query2);*/
        database.close();
    }

    public List<Model_sales> getsalessearch(String datefrom, String dateto) {
        String KEY_Date = "dates";
        String TBL = "MYSALES";
        List<Model_sales> contactList = new ArrayList<Model_sales>();
        // Select All Query
/*
    String query="SELECT item_name, item_price, itemquantity, itemtotal, dates FROM MYSALES WHERE  dates BETWEEN "+datefrom +" AND " +dateto+ " ORDER BY dates ASC";
*/


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL +
                " WHERE " + KEY_Date +
                " BETWEEN '" + datefrom + "' AND '" + dateto + "'", null);


        //Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model_sales model_sales = new Model_sales();
                model_sales.setSalesid(cursor.getString(0));
                String a = cursor.getString(1);
                Log.i("help", a);
                model_sales.setItem(cursor.getString(1));
                model_sales.setItem_price(cursor.getString(2));
                model_sales.setQuantity(cursor.getString(3));
                model_sales.setTotal_amount(cursor.getString(4));
                model_sales.setDate(cursor.getString(5));

                // Adding contact to list
                contactList.add(model_sales);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;


    }

    public void CreateDynamicTablesWarning() {
        String BID = "Statusid";
        String DName = "Status";

        database = dbHelper.getWritableDatabase();
        //  database.execSQL("DROP TABLE IF EXISTS " + "BILL");
        String query = "CREATE TABLE IF NOT EXISTS " + "WARNING" + "(" + BID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + DName + " TEXT );";
        database.execSQL(query);

       /* database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CID, Contact_ID);
        cv.put(DName, Display_Name);
        database.insert(Table_Name, null, cv);*/
        database.close();
    }


    public void insertstatus(String status) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Status", status);

        database.insertWithOnConflict("WARNING", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        database.close();

    }


    public List<SearchResult> getAllitemsearch(String tablename) {
      //  String names = tablename.replaceAll("\\s+", "");

        CreateDynamicTablesforallproducts();
        ArrayList<String> arrTblNames = new ArrayList<String>();
        List<SearchResult> newResults = new ArrayList<>();
        // Select All Query////////////
        Cursor c = database.rawQuery("SELECT * FROM CATEGORIES ", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("subject")));
                c.moveToNext();
            }
            for (int i=0;i<arrTblNames.size();i++){
                Log.i("ff",arrTblNames.get(i).toString());
                String tablenames=arrTblNames.get(i);
                //String selectQuery = "SELECT  * FROM " + tablenames;

                String name2 = tablenames.replaceAll("\\s+", "");




        /////////////////////////
     //   String selectQuery = "SELECT " + "itemname" + " FROM " + tablenames + " WHERE"  + "itemname" + " LIKE " + " +'J%'+ "  ;;






        SQLiteDatabase db = dbHelper.getWritableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " +name2+ " WHERE item_name LIKE '"+tablename+"%' ",null);
             /* //  Cursor cursor=     db.query(true, tablenames, new String[] {
                               "item_name" },"item_name"  + " LIKE ?",
                        new String[] {"%"+ tablename+ "%" }, null, null, null,
                        null);*/
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                SearchResult category_model = new SearchResult();
                //category_model.setItem_id((cursor.getString(0)));
                category_model.setTitle(cursor.getString(1));
                category_model.setItem_id(cursor.getString(0));
                category_model.setItem_price(cursor.getString(2));
                category_model.set_img(cursor.getBlob(3));
                category_model.setItem_Quantity(cursor.getString(4));
                Log.i("allpro",cursor.getString(1));
                String a=cursor.getString(1);




                //  category_model.setItemprice(cursor.getString(2));
                // category_model.set_img(cursor.getBlob(3));
                // category_model.setItemquantity(cursor.getString(4));
                //  byte[] aa=cursor.getBlob(3);
                //  Log.i("checkimage", Arrays.toString(aa));
                // Adding contact to list
                newResults.add(category_model);
            } while (cursor.moveToNext());
        }
            }


        }

        // return contact list
        return newResults;

    }

    public void CreateDynamicTablesforallproducts() {
        ArrayList<String> arrTblNames = new ArrayList<String>();

   /*     String BID="pid";
        String DName="pname";
        String QName="qname";
        String price="price";
*/
        //  database = dbHelper.getWritableDatabase();
        // String query = "CREATE TABLE IF NOT EXISTS " + "ALLPRODUCTS"+ "(" + BID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DName + " TEXT, " + QName + " TEXT, " + price + " TEXT);";
        // database.execSQL(query);*/
        database = dbHelper.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM CATEGORIES ", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("subject")));
                c.moveToNext();
            }
            for (int i=0;i<arrTblNames.size();i++){
                Log.i("ff",arrTblNames.get(i).toString());
                String tablename=arrTblNames.get(i);
                String selectQuery = "SELECT  * FROM " + tablename;

            }


        }




    }
    public List<SearchItem> getsearchhistory() {
        List<SearchItem> contactList = new ArrayList<SearchItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM  SEARCHHISTORY ORDER BY dates " ;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            String title=    cursor.getString(1);
                String value=  (cursor.getString(1));
                SearchItem category_model = new SearchItem(title,value,  SearchItem.TYPE_SEARCH_ITEM_HISTORY);
                Log.i("search",value);
               // category_model.setTitle((cursor.getString(1)));
               // category_model.setValue(cursor.getString(1));

                contactList.add(category_model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void CreateDynamicTablesforsearchhistory() {
        String BID = "searchid";
        String DName = "searchname";

        String KEY_Date = "dates";

        database = dbHelper.getWritableDatabase();
        //  database.execSQL("DROP TABLE IF EXISTS " + "BILL");
        String query = "CREATE TABLE IF NOT EXISTS " + "SEARCHHISTORY" + "(" + BID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DName + " TEXT," + KEY_Date + " TEXT);";
        database.execSQL(query);
       /* database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CID, Contact_ID);
        cv.put(DName, Display_Name);
        database.insert(Table_Name, null, cv);*/
        database.close();
    }

    public void inserttosearch(String search, String date) {
        database = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("searchname", search);
        cv.put("dates", date);

        database.insert("SEARCHHISTORY", null, cv);
        database.close();


    }
    public List<SearchItem> getAllitemsearchsug(String tablename) {
        String tablenamess = tablename.replaceAll("\\s+", "");

        CreateDynamicTablesforallproducts();
        ArrayList<String> arrTblNames = new ArrayList<String>();
        List<SearchItem> newResults = new ArrayList<>();
        // Select All Query////////////
        Cursor c = database.rawQuery("SELECT * FROM CATEGORIES ", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("subject")));
                c.moveToNext();
            }
            for (int i=0;i<arrTblNames.size();i++){
                Log.i("ff",arrTblNames.get(i).toString());
                String tablenames=arrTblNames.get(i);
                String name2 = tablenames.replaceAll("\\s+", "");

                //String selectQuery = "SELECT  * FROM " + tablenames;





                /////////////////////////
                //   String selectQuery = "SELECT " + "itemname" + " FROM " + tablenames + " WHERE"  + "itemname" + " LIKE " + " +'J%'+ "  ;;






                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " +name2+ " WHERE item_name LIKE '"+tablenamess+"%' ",null);
             /* //  Cursor cursor=     db.query(true, tablenames, new String[] {
                               "item_name" },"item_name"  + " LIKE ?",
                        new String[] {"%"+ tablename+ "%" }, null, null, null,
                        null);*/
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        String a=cursor.getString(1);


                        SearchItem category_model = new SearchItem(a,a,SearchItem.TYPE_SEARCH_ITEM_SUGGESTION);
                        //category_model.setItem_id((cursor.getString(0)));
                        /*category_model.setTitle(cursor.getString(1));*/
                       /* category_model.setItem_id(cursor.getString(0));
                        category_model.setItem_price(cursor.getString(2));
                        category_model.set_img(cursor.getBlob(3));
                        category_model.setItem_Quantity(cursor.getString(4));*/






                        //  category_model.setItemprice(cursor.getString(2));
                        // category_model.set_img(cursor.getBlob(3));
                        // category_model.setItemquantity(cursor.getString(4));
                        //  byte[] aa=cursor.getBlob(3);
                        //  Log.i("checkimage", Arrays.toString(aa));
                        // Adding contact to list
                        newResults.add(category_model);
                    } while (cursor.moveToNext());
                }
            }


        }

        // return contact list
        return newResults;

    }

    public String getwarningstatus() {
        String selection = "";
        // Select All Query
        String selectQuery = "SELECT  * FROM  WARNING  " ;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String id=    cursor.getString(0);
                selection=  (cursor.getString(1));
             Log.i("warningstatus",selection);
            } while (cursor.moveToNext());
        }

        // return contact list
        return selection;
    }

    public void updatewarning(String status,String id) {
        database = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Status", status);
        database.update("WARNING", cv, "Statusid=" + id, null);
      //  database.update("WARNING", cv, "Status" + " = '" + status + "'", null);
        database.close();
    }



    public void insertosales() {
        database = dbHelper.getWritableDatabase();
 String query2 = "INSERT INTO MYSALES SELECT * FROM BILL ";
        database.execSQL(query2);
        database.close();

    }

    public void clearsales() {
        database = dbHelper.getWritableDatabase();
        String sql = "DELETE FROM MYSALES WHERE dates <= date('now','-30 day')";
        database.execSQL(sql);
        database.close();

    }



}
