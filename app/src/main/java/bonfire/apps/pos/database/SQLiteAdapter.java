package bonfire.apps.pos.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bonfire.apps.pos.modules.DataModule;

import static java.lang.Integer.parseInt;

public class SQLiteAdapter extends SQLiteOpenHelper {

    private Context context;
    public int totalqnty = 0;
    public double totalAmt = 0;
    public static final int DATABASE_VERSION = 1;
    //database name
    public static final String DATABASE_NAME = "DB";
    //table names
    public static final String TABLE_NAME = "ITEMS";
    String prod_id, qty, price, product_remark, combo_prd_id, ccatId, getCombo;
    String minusQty;
    int pQ, mQ, reQ;
    double pric, reAm;

    //table colums names
    public static final String ITEMNAME = "ItemName"; //items name;
    public static final String ID = "ID"; //item id
    public static final String ITEMPRICE = "ItemPrice"; // item price
    public static final String ITEMIMAGE = "ItemImage"; //item image
    public static final String ITEMQTY = "QTY"; //item qty
    public static final String ITEMAMOUNT = "TotalAmount";
    public static final String ITEMID = "ItemId";
    public static final String ITEMIMGI = "ItemImageInt";
    public static final String ITEMINST = "ItemInst";
    public static final String ITEMREM = "ItemRemarks";
    public static final String ISCOMBOITEM = "IsComboItem";
    public static final String TABLE_COMBOCAT = "COMBOCAT";
    public static final String TABLE_COMBOP = "COMBOP";
    public static final String CCATID = "CCATID";
    public static final String CCATEGORYNAME = "CATEGORYNAME";
    public static final String FITEMS = "FITEMS";
    public static final String COMBOPRODUCTID = "COMBOPRODUCTID";
    public static final String COMBOPRODUCTNAME = "COMBOPRODUCTNAME";
    public static final String FCOMBO = "FCOMBO";
    public static final String SNO = "SNO";
    public static final String TABLEID = "TABLEID";
    public static final String ID_KTD = "ID_KTD";
    public static final String ID_KT = "ID_KT";
    public static final String WAITER_NAME = "WAITER_NAME";
    public static final String ITEMSNAME = "ITEMSNAME";
    public static final String TABLENO = "TABLENO";
    public static final String NOTIFY = "NOTIFY";
    public static final String TABLE_NOTIFY = "TABLENOTIFY";
    public static final String KOT_DATE = "KOT_DATE";
    public static final String INSTR_NAME = "INSTR_NAME";
    public static final String TABLEINSTR = "TABLEINTSR";

    SQLiteDatabase db = getWritableDatabase();


    public SQLiteAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String query = " CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ITEMID + " INTEGER ,"
                    + ITEMNAME + " VARCHAR(1000) , "
                    + ITEMIMAGE + " VARCHAR(1000) , "
                    + ITEMIMGI + " INTEGER ,"
                    + ITEMPRICE + " DOUBLE , "
                    + ITEMQTY + " INTEGER  ,"
                    + ITEMAMOUNT + " DOUBLE ,"
                    + ITEMINST + " VARCHAR(1000) , "
                    + ITEMREM + " VARCHAR(1000) ,"
                    + ISCOMBOITEM + " VARCHAR(100) , "
                    + SNO + " VARCHAR(100))";


            //   + " FOREIGN KEY ( " + FITEMS + ") REFERENCES " + TABLE_NAME + " ( " + ID + ") )";  //INCASE OF FORRIGN KEY IMPORTANCE

            //        + " FOREIGN KEY ( " + FCOMBO + ") REFERENCES " + TABLE_COMBOCAT + " ( " + ID + ") )";  //INCASE OF FORRIGN KEY IMPORTANCE


            db.execSQL(query);


            String ffquery = " CREATE TABLE IF NOT EXISTS " + TABLE_COMBOP + " ( "
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COMBOPRODUCTID + " INTEGER, "
                    + COMBOPRODUCTNAME + " VARCHAR(999) , "
                    + FCOMBO + " VARCHAR(1000) , "
                    + ITEMID + " INTEGER , "
                    + SNO + " VARCHAR(100)"
                    + " , "
                    // + " " ;
                    + TABLEID + " VARCHAR(100))";
            db.execSQL(ffquery);

            String fquery = " CREATE TABLE IF NOT EXISTS " + TABLE_COMBOCAT + " ( "
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + CCATID + " INTEGER ,"
                    + CCATEGORYNAME + " VARCHAR(999) , "
                    + FITEMS + " INTEGER "
                    + " ) ";

            db.execSQL(fquery);

            String iQuery = " CREATE TABLE IF NOT EXISTS " + TABLEINSTR + "(" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    INSTR_NAME + " VARCHAR(999) )";
            db.execSQL(iQuery);

            String notification = " CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFY + " ( "
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ID_KT + " VARCHAR(1000) , "
                    + ID_KTD + " VARCHAR(1000) , "
                    + WAITER_NAME + " VARCHAR(1000) , "
                    + TABLENO + " VARCHAR(100) , "
                    + ITEMSNAME + " VARCHAR(100) , "
                    + KOT_DATE + " VARCHAR(100) , "
                    + NOTIFY + " INTEGER DEFAULT 0 "
                    + " ) ";
            db.execSQL(notification);
        }catch (Exception e){
            Log.e("CREATE TABLE" , e.toString());
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = " DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        String fquery = " DROP TABLE IF EXISTS " + TABLE_COMBOP + ";";
        String ffquery = " DROP TABLE IF EXISTS " + TABLE_COMBOCAT + ";";
        String notification = " DROP TABLE IF EXISTS " + TABLE_NOTIFY + ";";
        db.execSQL(query);
        db.execSQL(fquery);
        db.execSQL(ffquery);
        db.execSQL(notification);
        onCreate(db);

    }



    public void insert(String name, int image, double price, String id, String isComboItem) {

        ContentValues cv = new ContentValues();
        cv.put(ITEMNAME, name);
        cv.put(ITEMIMGI, image);
        cv.put(ITEMPRICE, price);
        cv.put(ITEMQTY, 1);
        cv.put(ITEMID, id);
        cv.put(ITEMAMOUNT, 1 * price);
        cv.put(ISCOMBOITEM, isComboItem);
        this.db.insert(TABLE_NAME, null, cv);

    }

    public void insert(String kotid, String kotdetailid, String itemname, String tableno, String waitername, String kot_date) {
        ContentValues cv = new ContentValues();
        cv.put(ID_KT, kotid);
        cv.put(ID_KTD, kotdetailid);
        cv.put(ITEMSNAME, itemname);
        cv.put(TABLENO, tableno);
        cv.put(WAITER_NAME, waitername);
        cv.put(KOT_DATE,kot_date);
        try {
            this.db.insert(TABLE_NOTIFY, null, cv);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //FOR COMBO ITEMS
    public void insert(String name, int image, double price, String id, String isComboItem, String sno) {

        ContentValues cv = new ContentValues();
        cv.put(ITEMNAME, name);
        cv.put(ITEMIMGI, image);
        cv.put(ITEMPRICE, price);
        cv.put(ITEMQTY, 1);
        cv.put(ITEMID, id);
        cv.put(ITEMAMOUNT, 1 * price);
        cv.put(ISCOMBOITEM, isComboItem);
        cv.put(SNO, sno);
        this.db.insert(TABLE_NAME, null, cv);

    }

    public void insertComboCat(String catid, String catname, String itemId) {

        ContentValues cv = new ContentValues();
        cv.put(CCATID, catid);
        cv.put(CCATEGORYNAME, catname);
        cv.put(FITEMS, itemId);
        this.db.insert(TABLE_COMBOCAT, null, cv);
    }

   /* public void insertComboCatList( DataModule dm){
        String fquery = " CREATE TABLE IF NOT EXISTS " + TABLE_COMBOCAT + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CCATID + " INTEGER ,"
                + CCATEGORYNAME + " VARCHAR(999) , "
                + FITEMS + " INTEGER "
                + " ) ";

        db.execSQL(fquery);
        ContentValues cv = new ContentValues();
        cv.put(CCATID, dm.getCCatId() );
        cv.put(CCATEGORYNAME,dm.getCCategoryName());
        cv.put(FITEMS, dm.getItemID());
        this.db.insert(TABLE_COMBOCAT, null, cv);
    }*/


    public String insertComboProd(String pId, String pName, String catId, String itemId, String sno) {


        ContentValues cv = new ContentValues();
        cv.put(COMBOPRODUCTID, pId);
        cv.put(COMBOPRODUCTNAME, pName);
        cv.put(FCOMBO, catId);
        cv.put(ITEMID, itemId);
        cv.put(SNO, sno);
        this.db.insert(TABLE_COMBOP, null, cv);
        return " Selected ";
    }

    /* public void insertComboProList( DataModule dm){
         String ffquery = " CREATE TABLE IF NOT EXISTS " + TABLE_COMBOP + " ( "
                 + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                 + COMBOPRODUCTID + " INTEGER, "
                 + COMBOPRODUCTNAME + " VARCHAR(999) , "
                 + FCOMBO + "INTEGER )";
         db.execSQL(ffquery);

         ContentValues cv = new ContentValues();
         cv.put(COMBOPRODUCTID, dm.getCombo_product_id());
         cv.put(COMBOPRODUCTNAME, dm.getCombo_product_name());
         cv.put(FCOMBO,dm.getCCatId());
         this.db.insert(TABLE_COMBOP, null, cv);
         }
 */


    public void delete(String id) {

/*
        if (getIsComboItem(id).matches("True")) {
            String f;
            try {
                Cursor cus = this.db.rawQuery(" SELECT " + CCATID + " FROM " + TABLE_COMBOCAT + " WHERE " + FITEMS + " = ? ", new String[]{id});
                if (cus.moveToFirst()) {
                    do {
                        f = cus.getString(cus.getColumnIndex(CCATID));
                        Log.e("ccatid", f);
                 */
/*   Cursor cusr = this.db.rawQuery(" SELECT " + COMBOPRODUCTNAME + " FROM " + TABLE_COMBOP + " WHERE " + FCOMBO + " = ? ", new String[]{id});
                    if (cusr.moveToFirst()) {
                        do {
                            f = cusr.getString(cusr.getColumnIndex(COMBOPRODUCTNAME));
                            Log.e("ccatid2", f);

                            this.db.delete(TABLE_COMBOP, FCOMBO + " = ? ", new String[]{f});

                        } while (cusr.moveToNext());
                    }*//*


                        this.db.delete(TABLE_COMBOP, FCOMBO + " = ? ", new String[]{f});


                    }
                    while (cus.moveToNext());

                }


            } catch (Exception e) {
                Log.e("Error", "Sqlite Error");
            }
        }
        if (getIsComboItem(id).matches("True")) {

            this.db.delete(TABLE_COMBOCAT, FITEMS + " = ? ", new String[]{id});
        }
*/


        this.db.delete(TABLE_NAME, ITEMID + " = ?", new String[]{id});


    }

    public int getITEMid(String itemid, String sno) {
        String ids = " ";
        int a = 0;
        Cursor getId =
                db.rawQuery(" SELECT " + ID + " FROM " + TABLE_NAME + " WHERE "
                        + ITEMID + " = ? AND " + SNO + " = ?", new String[]{itemid, sno});

        if (getId.moveToNext()) {
            do {
                a = getId.getInt(getId.getColumnIndex(ID));
                //  ids = getId.getString(getId.getColumnIndex(ID));
            } while (getId.moveToNext());
        }
        return a;
        //  return ids;
    }

    public void insertInstruction(String remarks){
        Cursor checkRemarks = db.rawQuery(" SELECT * FROM " + TABLEINSTR + " WHERE " + INSTR_NAME + " = ? ",
                new String[]{remarks});
       if (checkRemarks.getCount() > 0) {
           if (checkRemarks.moveToNext()) {
               do {
                   Log.e("Remarks Exists", checkRemarks.getString(checkRemarks.getColumnIndex(INSTR_NAME)));
               } while (checkRemarks.moveToNext());
           }
       }else
       {
           ContentValues cv = new ContentValues();
           cv.put(INSTR_NAME, remarks);
           db.insert(TABLEINSTR,null,cv);
           Log.e("Item inserted", remarks);
       }



    }

    public List<String> getInstr(){
        List<String> dmr = new ArrayList<String>();
        Cursor c= db.rawQuery(" SELECT * FROM " + TABLEINSTR + " ORDER BY " + INSTR_NAME + " ASC ", null);
        if (c.moveToNext()){
            do {
                dmr.add(c.getString((c.getColumnIndex(INSTR_NAME))));

            }while(c.moveToNext());
        }
        return dmr;
    }
    public String check_KotD_ID(String kotdetailid) {
        String num = "";
        Cursor getID =
                db.rawQuery(" SELECT " + ID_KTD + " FROM " + TABLE_NOTIFY +
                        " WHERE " + ID_KTD + " = ? ", new String[]{kotdetailid});
        if (getID.moveToNext()) {
            do {
              return num = getID.getString(getID.getColumnIndex(ID_KTD));
            } while (getID.moveToNext());
        }
        return num = "nope";
    }


    public void delete(String id, String dbid) {
        try {
            if (getIsComboItem(id).matches("True")) {
                String f;
                try {
                    Cursor cus = this.db.rawQuery(" SELECT " + ID
                            + " FROM " + TABLE_COMBOP
                            + " WHERE " + ITEMID + " = ? AND "
                            + ID + " =  ?", new String[]{id, dbid});
                    if (cus.moveToFirst()) {
                        do {
                            f = cus.getString(cus.getColumnIndex(ID));
                            Log.e("ccatid", f);
                 /*   Cursor cusr = this.db.rawQuery(" SELECT " + COMBOPRODUCTNAME + " FROM " + TABLE_COMBOP + " WHERE " + FCOMBO + " = ? ", new String[]{id});
                    if (cusr.moveToFirst()) {
                        do {
                            f = cusr.getString(cusr.getColumnIndex(COMBOPRODUCTNAME));
                            Log.e("ccatid2", f);

                            this.db.delete(TABLE_COMBOP, FCOMBO + " = ? ", new String[]{f});

                        } while (cusr.moveToNext());
                    }*/

                            this.db.delete(TABLE_COMBOP, ID + " = ? ", new String[]{f});
                            Log.e("DDDELETEDDDDDDDDD", f);


                        }
                        while (cus.moveToNext());

                    }


                } catch (Exception e) {
                    Log.e("Error", "Sqlite Error");
                }
            }
            if (getIsComboItem(id).matches("True")) {

                this.db.delete(TABLE_COMBOCAT, FITEMS + " = ? ", new String[]{id});
            }


            this.db.delete(TABLE_NAME, ITEMID + " = ? AND " + ID + " = ? ", new String[]{id, dbid});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String qty, String id) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMQTY, qty);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }

    public void update(String kotDetailID) {
        ContentValues cv = new ContentValues();
        cv.put(NOTIFY, 1);
        this.db.update(TABLE_NOTIFY, cv, ID_KTD + " = ? ", new String[]{kotDetailID});
    }

    public Cursor getData(String id) {
        String query = " SELECT * FROM " + TABLE_NAME + " WHERE " + ITEMID + " = '" + id + "'";
        return this.db.rawQuery(query, null);
    }

    public Cursor getAllData() {
        return this.db.rawQuery(" SELECT * FROM "
                + TABLE_NAME + " ORDER BY " + ID + " DESC , "
                + ITEMQTY + " DESC", null);
    }

    public Cursor getComboData() {
        return this.db.rawQuery(" SELECT * FROM "
                + TABLE_COMBOCAT + " ORDER BY "
                + FITEMS + " ASC ", null);
    }

    public Cursor getProductCombo() {
        return this.db.rawQuery(" SELECT * FROM "
                + TABLE_COMBOP + " ORDER BY "
                + FCOMBO + " ASC ", null);
    }

    public Cursor getNotification() {
        return this.db.rawQuery("SELECT * FROM " + TABLE_NOTIFY
                + " WHERE " + NOTIFY + " = 0 "
                + " ORDER BY " + ID + "  DESC ", null);
    }
    public Cursor getNotificationR() {
        return this.db.rawQuery("SELECT * FROM " + TABLE_NOTIFY
                + " WHERE " + NOTIFY + " = 1 "
                + " ORDER BY " + ID + "  DESC ", null);
    }
    public Cursor getProductCombo(String itemId) {
        return this.db.rawQuery(" SELECT * FROM " + TABLE_COMBOP + " WHERE "
                + ITEMID + " = ?  ORDER BY " + FCOMBO + " ASC ", new String[]{itemId});
    }

    public Cursor getProductCombo(String itemId, String sno) {

        return this.db.rawQuery(" SELECT * FROM "
                + TABLE_COMBOP + " WHERE "
                + ITEMID + " = ? AND "
                + SNO + " = ? ORDER BY "
                + FCOMBO + " ASC ", new String[]{itemId, sno});


    }

    @SuppressLint("Recycle")
    public boolean checkExists(String id) {
        @SuppressLint("Recycle") Cursor cur = this.db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE " + ITEMID +
                " = '" + id + " ' ", null);
        return cur.getCount() > 0;
    }

    public boolean checkExists(String id, String sno) {
        @SuppressLint("Recycle") Cursor cur = this.db.rawQuery(" SELECT * FROM " + TABLE_NAME
                + " WHERE " + ITEMID + " = ? AND " + SNO + " = ?", new String[]{id, sno});
        return cur.getCount() > 0;
    }

    public String checksno(String id) {
        String sno = "";
        @SuppressLint("Recycle") Cursor cur = this.db.rawQuery(
                " SELECT MAX (SNO) AS SNO1 FROM " + TABLE_NAME
                        + " WHERE " + ITEMID + " =? ", new String[]{id});
        if (cur.moveToNext()) {
            do {
                sno = cur.getString(cur.getColumnIndex("SNO1"));
            } while (cur.moveToNext());
        }

        return sno;
    }

    public void updateQtyAuto(String id) {

        String query = " SELECT " + ITEMQTY + " , "
                + ITEMPRICE + " FROM " + TABLE_NAME
                + " WHERE " + ITEMID + " = ' " + id + " ' ";
        //  this.db.rawQuery(query,null);

        /*
         * cusor must select the query firs
         * after the selection of query the cusor must sav value in variable
         * from the variable the data can be extractec
         *
         *
         * in case of update qty we are goin to use it
         * */
        Cursor cus = this.db.rawQuery(query, null);
        if (cus.moveToNext()) {
            do {
                pQ = cus.getInt(cus.getColumnIndex(ITEMQTY));
                pric = cus.getDouble(cus.getColumnIndex(ITEMPRICE));
            } while (cus.moveToNext());
        }
        int tt = pQ + 1;
        double amount = tt * pric;

        ContentValues cv = new ContentValues();
        cv.put(ITEMQTY, tt);
        cv.put(ITEMAMOUNT, amount);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }

    public void qtyAutoDedud(String id) {

        String query = " SELECT " + ITEMQTY + " , " + ITEMPRICE + " FROM " + TABLE_NAME + " WHERE " + ITEMID + " = '" + id + "'";
        Cursor cus = this.db.rawQuery(query, null);
        if (cus.moveToNext()) {
            do {
                mQ = cus.getInt(cus.getColumnIndex(ITEMQTY));
                pric = cus.getDouble(cus.getColumnIndex(ITEMPRICE));
            } while (cus.moveToNext());
        }
        int tt = mQ - 1;
        double amount = pric * tt;
        ContentValues cv = new ContentValues();
        cv.put(ITEMAMOUNT, amount);
        cv.put(ITEMQTY, tt);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }


    public void updateQtyAuto(String id, String dbid) {

        //  this.db.rawQuery(query,null);

        /*
         * cusor must select the query firs
         * after the selection of query the cusor must sav value in variable
         * from the variable the data can be extractec
         *
         *
         * in case of update qty we are goin to use it
         * */
        Cursor cus = this.db.rawQuery(" SELECT " + ITEMQTY + " , " + ITEMPRICE + " FROM "
                + TABLE_NAME
                + " WHERE " + ITEMID + " = ? AND "
                + ID + " = ? ", new String[]{id, dbid});
        if (cus.moveToNext()) {
            do {
                pQ = cus.getInt(cus.getColumnIndex(ITEMQTY));
                pric = cus.getDouble(cus.getColumnIndex(ITEMPRICE));
            } while (cus.moveToNext());
        }
        int tt = pQ + 1;
        double amount = tt * pric;

        ContentValues cv = new ContentValues();
        cv.put(ITEMQTY, tt);
        cv.put(ITEMAMOUNT, amount);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }

    public void qtyAutoDedud(String id, String dbid) {

        //  String query = " SELECT " + ITEMQTY + " , " + ITEMPRICE + " FROM " + TABLE_NAME + " WHERE " + ITEMID + " = '" + id + "'";
        Cursor cus = this.db.rawQuery(" SELECT " + ITEMQTY + " , " + ITEMPRICE + " FROM "
                + TABLE_NAME + " WHERE "
                + ITEMID + " = ? AND "
                + ID + " = ? ", new String[]{id, dbid});
        if (cus.moveToNext()) {
            do {
                mQ = cus.getInt(cus.getColumnIndex(ITEMQTY));
                pric = cus.getDouble(cus.getColumnIndex(ITEMPRICE));
            } while (cus.moveToNext());
        }
        int tt = mQ - 1;
        double amount = pric * tt;
        ContentValues cv = new ContentValues();
        cv.put(ITEMAMOUNT, amount);
        cv.put(ITEMQTY, tt);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }


    public void deleteDatabase() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(query);
        String query1 = "DROP TABLE IF EXISTS " + TABLE_COMBOP + ";";
        db.execSQL(query1);
        String query2 = "DROP TABLE IF EXISTS " + TABLE_COMBOCAT + ";";
        db.execSQL(query2);
    //    String query3 = "DROP TABLE IF EXISTS " + TABLEINSTR + ";" ;
      //  db.execSQL(query3);
        onCreate(db);
    }

    public void deleteNotification(){
        String notification1 = " DROP TABLE IF EXISTS " + TABLE_NOTIFY + ";";
        db.execSQL(notification1);
        String notification = " CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFY + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_KT + " VARCHAR(1000) , "
                + ID_KTD + " VARCHAR(1000) , "
                + WAITER_NAME + " VARCHAR(1000) , "
                + TABLENO + " VARCHAR(100) , "
                + ITEMSNAME + " VARCHAR(100) , "
                + KOT_DATE + " VARCHAR(100) , "
                + NOTIFY + " INTEGER DEFAULT 0 "
                + " ) ";
        db.execSQL(notification);
    }

    public void getCount(){
        try {
            Cursor cus = this.db.rawQuery(" SELECT COUNT( " + ID_KTD + " ) AS CNT FROM " + TABLE_NOTIFY, null);
            if (cus.moveToNext()) {
                do {
                    String ccount = cus.getString(cus.getColumnIndex("CNT"));
                    Log.e("Count", ccount);
                } while (cus.moveToNext());
            }
        }catch (Exception e){
            Log.e("count error" , e.toString());
        }
    }

    public String getCounts(){
        String ccount = "";
        try {
            @SuppressLint("Recycle") Cursor cus = this.db.rawQuery(" SELECT COUNT( " + ID_KTD + " ) AS CNT FROM " + TABLE_NOTIFY, null);
            if (cus.moveToNext()) {

                     ccount = cus.getString(cus.getColumnIndex("CNT"));
                Log.e("Count", ccount);
                     return ccount;

            }
        }catch (Exception e){
            Log.e("count error" , e.toString());
        }
        return ccount;
    }

    public void getCount(String kot_date) {
        try {
            db.delete(TABLE_NOTIFY,  KOT_DATE + " != ? ", new String[]{kot_date});
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Delete kot date", e.toString());
        }
    }

    public List<DataModule> getAllDatas() {
        Cursor cus = getAllData();
        List<DataModule> dataModules = new ArrayList<>();
        if (cus.moveToFirst()) {
            do {
                DataModule m = new DataModule();
                m.setDbid(cus.getInt(cus.getColumnIndex(ID)));
                m.setItemID(cus.getInt(cus.getColumnIndex(ITEMID)));
                m.setItemName(cus.getString(cus.getColumnIndex(ITEMNAME)));
                m.setItemQty(Integer.parseInt(cus.getString(cus.getColumnIndex(ITEMQTY))));
                m.setItemAmount(cus.getDouble(cus.getColumnIndex(ITEMAMOUNT)));
                m.setItemImag(cus.getInt(cus.getColumnIndex(ITEMIMGI)));
                m.setItemPric(cus.getDouble(cus.getColumnIndex(ITEMPRICE)));
                m.setInstruction(cus.getString(cus.getColumnIndex(ITEMINST)));
                m.setRemarks(cus.getString(cus.getColumnIndex(ITEMREM)));
                m.setIsComboItem(cus.getString(cus.getColumnIndex(ISCOMBOITEM)));
                m.setSno(cus.getString(cus.getColumnIndex(SNO)));
                dataModules.add(m);

            } while (cus.moveToNext());
        }
        return dataModules;


    }

    public List<DataModule> getNotificationData(){
        Cursor cus = getNotification();
        List<DataModule> ddm = new ArrayList<>();
        if (cus.moveToFirst()){
            do{
                DataModule dm = new DataModule();
                dm.setKotid(cus.getString(cus.getColumnIndex(ID_KT)));
                dm.setKotdetailid(cus.getString(cus.getColumnIndex(ID_KTD)));
                dm.setTableno(cus.getString(cus.getColumnIndex(TABLENO)));
                dm.setWaitername(cus.getString(cus.getColumnIndex(WAITER_NAME)));
                dm.setNotify(cus.getInt(cus.getColumnIndex(NOTIFY)));
                dm.setItemName(cus.getString(cus.getColumnIndex(ITEMSNAME)));
                dm.setKot_date(cus.getString(cus.getColumnIndex(KOT_DATE)));
                ddm.add(dm);

            }while (cus.moveToNext());
        }
        return  ddm;
    }
    public List<DataModule> getNotificationReady(){
        Cursor cus = getNotificationR();
        List<DataModule> ddm = new ArrayList<>();
        if(cus.moveToFirst()){
            do{
                DataModule dm = new DataModule();
                dm.setDbid(cus.getInt(cus.getColumnIndex(ID)));
                dm.setTableno(cus.getString(cus.getColumnIndex(TABLENO)));
                dm.setWaitername(cus.getString(cus.getColumnIndex(WAITER_NAME)));
                dm.setItemName(cus.getString(cus.getColumnIndex(ITEMSNAME)));
                ddm.add(dm);
            }while (cus.moveToNext());
        }
        return ddm;
    }


    public String checkComboItemEntry(String itemId, String pid, String pname, String ccatids, String sno) {
        String fqty, catid, comid, catidcount, itemcount;
        String message = "";
        int qty, ccatid, cpid, combocount, procount;
       /* Cursor fitem = this.db.rawQuery(" SELECT " + FITEMS + " FROM " +
                TABLE_COMBOCAT + " WHERE " + CCATID + " = ? " , new String[]{ccatids});
        if (fitem.moveToNext()) {
            do {
                itemId = fitem.getString(fitem.getColumnIndex(FITEMS));
*/
        Cursor cCatID = this.db.rawQuery(" SELECT " + ITEMQTY
                + " FROM " + TABLE_NAME
                + " WHERE " + ITEMID + " = ? AND  "
                + SNO + " = ? ", new String[]{itemId, sno});
        if (cCatID.moveToNext()) {
            do {
                fqty = cCatID.getString(cCatID.getColumnIndex(ITEMQTY));
                qty = parseInt(fqty);
               /* Cursor itemID = this.db.rawQuery(" SELECT " + CCATID + "  FROM " + TABLE_COMBOCAT + " WHERE " + FITEMS + " = ?", new String[]{itemId});
                if (itemID.moveToNext()) {
                    do {
                        comid = itemID.getString(itemID.getColumnIndex(CCATID));*/

                try {
                    Cursor countCatId1 = this.db.rawQuery(
                            " SELECT  COUNT (" + COMBOPRODUCTID +
                                    ") AS PCNT FROM " + TABLE_COMBOP
                                    + " WHERE " +
                                    ITEMID + " = ?  AND "
                                    + FCOMBO + " = ? AND "
                                    + SNO + " = ?"
                            , new String[]{itemId, ccatids, sno
                            });

                    if (countCatId1.moveToNext()) {
                        do {
                            catidcount = countCatId1.getString(countCatId1.getColumnIndex("PCNT"));
                            combocount = parseInt(catidcount);

                            if (qty == combocount) {
                                message = " Already Selected";

                            } else {

                                message = insertComboProd(pid, pname, ccatids, itemId, sno);

                            }


                        } while (countCatId1.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } while (cCatID.moveToNext());

        }
      /*      }while (fitem.moveToNext());
        }*/
        return message;


    }

    public List<DataModule> getComboDatas() {
        Cursor cus = getComboData();
        List<DataModule> data = new ArrayList<>();
        if (cus.moveToFirst()) {
            do {
                DataModule m = new DataModule();
                m.setItemID(cus.getInt(cus.getColumnIndex(FITEMS)));
                m.setCCatId(cus.getString(cus.getColumnIndex(CCATID)));
                m.setCCategoryName(cus.getString(cus.getColumnIndex(CCATEGORYNAME)));
                data.add(m);
            } while (cus.moveToNext());
        }
        return data;
    }

    public List<DataModule> getProdcutData() {
        Cursor cus = getProductCombo();
        List<DataModule> data = new ArrayList<>();
        if (cus.moveToFirst()) {
            do {
                DataModule m = new DataModule();
                m.setDbid(cus.getInt(cus.getColumnIndex(ID)));
                m.setItemID(cus.getInt(cus.getColumnIndex(FCOMBO)));
                m.setCombo_product_id(cus.getString(cus.getColumnIndex(COMBOPRODUCTID)));
                m.setCombo_product_name(cus.getString(cus.getColumnIndex(COMBOPRODUCTNAME)));
                data.add(m);
            } while (cus.moveToNext());

        }
        return data;
    }

    public List<DataModule> getProdcutData(String itemID, String sno) {
        Cursor cus;
        List<DataModule> data = new ArrayList<>();
        try {
            cus = getProductCombo(itemID, sno);


            if (cus.moveToFirst()) {
                do {
                    DataModule m = new DataModule();
                    m.setItemID(cus.getInt(cus.getColumnIndex(FCOMBO)));
                    m.setCombo_product_id(cus.getString(cus.getColumnIndex(COMBOPRODUCTID)));
                    m.setCombo_product_name(cus.getString(cus.getColumnIndex(COMBOPRODUCTNAME)));

                    data.add(m);
                } while (cus.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public int getQnty(String id) {
        Cursor cus = this.db.rawQuery(" SELECT  " + ITEMQTY
                + "  FROM " + TABLE_NAME + " WHERE "
                + ITEMID + " = ' " + id + " ' ", null);
        if (cus.moveToFirst()) {
            do {
                reQ = cus.getInt(cus.getColumnIndex(ITEMQTY));


            } while (cus.moveToNext());


        }
        return reQ;

    }

    public int getQnty(String id, String sno) {
        Cursor cus = this.db.rawQuery(" SELECT  " + ITEMQTY + "  FROM " + TABLE_NAME + " WHERE "
                + ITEMID + " = ? AND " + SNO + " =? ", new String[]{id, sno});
        if (cus.moveToFirst()) {
            do {
                reQ = cus.getInt(cus.getColumnIndex(ITEMQTY));


            } while (cus.moveToNext());


        }
        return reQ;

    }

    //for cancel from item select
    public void dedudeComboQty(String id, String sno) {
        int qty = getQnty(id, sno);
        int dbid = getITEMid(id, sno);

        //  String sn = checksno(id);
        if (qty == 1) {
            delete(id, String.valueOf(dbid));
        }
        /*else {
            qtyAutoDedud(id,sno);
            msg = " deduded qty";
        }*/
    }


    public void dedudeComboQty(String id) {
        String msg = " no ";
        int qty = getQnty(id);
        if (qty == 1) {
            delete(id);
            msg = " 1 qty deleted";
        } else {
            qtyAutoDedud(id);
            msg = " deduded qty";
        }
    }

    public double getAmt(String id) {
        Cursor cus = this.db.rawQuery(
                " SELECT  " + ITEMAMOUNT + "  FROM "
                        + TABLE_NAME + " WHERE "
                        + ITEMID + " = ' " + id + " ' "
                , null);
        if (cus.moveToFirst()) {
            do {
                reAm = cus.getDouble(cus.getColumnIndex(ITEMAMOUNT));


            } while (cus.moveToNext());


        }
        return reAm;

    }

    public int getQntyT() {
        Cursor cus = this.db.rawQuery(
                " SELECT  " + ITEMQTY + "  FROM "
                        + TABLE_NAME, null);
        if (cus.moveToFirst()) {
            do {
                totalqnty += cus.getInt(cus.getColumnIndex(ITEMQTY));


            } while (cus.moveToNext());


        }
        return totalqnty;

    }

    public void addInst(String id, String inst) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMINST, inst);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }

    public void addInst(String id, String inst, String dbID) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMINST, inst);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ? AND " + ID + " = ?", new String[]{id, dbID});

    }

    public void addrem(String id, String rem) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMREM, rem);
        this.db.update(TABLE_NAME, cv, ITEMID + " = ?", new String[]{id});

    }

    public double getAmtT() {
        Cursor cus = this.db.rawQuery(" SELECT  " + ITEMAMOUNT + "  FROM " + TABLE_NAME, null);
        if (cus.moveToFirst()) {
            do {
                totalAmt += cus.getInt(cus.getColumnIndex(ITEMAMOUNT));


            } while (cus.moveToNext());


        }
        return totalAmt;

    }

    public String getIsComboItem(String id) {
        Cursor cus = this.db.rawQuery(" SELECT  " + ISCOMBOITEM + "  FROM " + TABLE_NAME + " WHERE " + ITEMID + " = ' " + id + " ' ", null);
        if (cus.moveToFirst()) {
            do {
                getCombo = cus.getString(cus.getColumnIndex(ISCOMBOITEM));


            } while (cus.moveToNext());


        }
        return getCombo;

    }

    public String getIsComboItem() {
        Cursor cus = this.db.rawQuery(" SELECT  " + ISCOMBOITEM + "  FROM " + TABLE_NAME, null);
        if (cus.moveToFirst()) {
            do {
                getCombo = cus.getString(cus.getColumnIndex(ISCOMBOITEM));


            } while (cus.moveToNext());


        }
        return getCombo;

    }
}
