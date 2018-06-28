package eazi.com.eazi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static  int database_version  = 1;
    private final Context context;
    public String Messages =" CREATE TABLE `Messages` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "From_User TEXT, To_User TEXT, Message_Text TEXT,Date TEXT,isMine TEXT); ";
    public String Users =" CREATE TABLE `Users` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "PhoneNumber TEXT unique, Me TEXT); ";

    private static final String DELETE_Messages = "DROP TABLE IF EXISTS Messages" ;
    private static final String DELETE_Users = "DROP TABLE IF EXISTS Users" ;

    public DataBaseHelper(Context context) {
        super(context, "eazy_db",null, database_version);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Messages);
        db.execSQL(Users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_Messages);
        db.execSQL(DELETE_Users);
        onCreate(db);
    }


    public boolean putMessages(DataBaseHelper dbh,String From_NodeID,
                                     String To_Node_ID, String Message_Text,String Date , String Time){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 To_Node_ID " + To_Node_ID);

        ContentValues cv = new ContentValues();
        cv.put("From_User", From_NodeID);
        cv.put("To_User", To_Node_ID);
        cv.put("Message_Text", Message_Text);
        cv.put("Date", Date);
        cv.put("isMine", Time);
        sq.insert("Messages", null, cv);
        return true;

    }

    public boolean putUsers(DataBaseHelper dbh,String PhoneNumber){
        if(!PhoneNumber.contains("@eazi.ai")) {
            PhoneNumber = PhoneNumber+"@eazi.ai";
        }
        SQLiteDatabase sq=dbh.getWritableDatabase();
        System.out.println("DataBaseHelper123 To_Node_ID " + PhoneNumber);
        String Me = CommonMethods.getSharedPrefValue(context, Constants.user_name);

        ContentValues cv = new ContentValues();
        cv.put("PhoneNumber", PhoneNumber);
        cv.put("Me", Me);
        long insert = sq.insert("Users", null, cv);
        System.out.println("DataBaseHelper123 putUsers " + insert);

        return true;

    }

    public List<eazi.com.eazi.database.Messages> getMessages(String user) {
        String from = CommonMethods.getSharedPrefValue(context, Constants.user_name);
        List<eazi.com.eazi.database.Messages> dataListList = new ArrayList<eazi.com.eazi.database.Messages>();
        String selectQuery = "SELECT * FROM Messages Where To_User = '"+user+"' AND From_User = '"+from+"'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getDateRate " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                eazi.com.eazi.database.Messages msg = new Messages();
                msg.setID(cursor.getString(0));
                msg.setFrom(cursor.getString(1));
                msg.setTo(cursor.getString(2));
                msg.setMessage_Text(cursor.getString(3));
                msg.setDate(cursor.getString(4));
                msg.setISMine(cursor.getString(5));
                System.out.println("DataBaseHelper123 getDateRate " + cursor.getString(2));
                dataListList.add(msg);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }

    public List<Users> getUsers() {
        String from = CommonMethods.getSharedPrefValue(context, Constants.user_name);

        List<Users> dataListList = new ArrayList<Users>();
        String selectQuery = "SELECT * FROM Users Where Me = '"+from+"'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getUsers " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Users msg = new Users();
                msg.setName(cursor.getString(0));
                msg.setPhoneNumber(cursor.getString(1));
                System.out.println("DataBaseHelper123 getUsers " + cursor.getString(2));
                dataListList.add(msg);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }


    public void deleteFrominbox (String id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "Delete from Inbox_Message where  Id = "+id ;
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

}
