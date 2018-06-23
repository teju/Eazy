package eazi.com.eazi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 04/04/18.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    private static  int database_version  = 1;
    private final Context context;
    public String Messages =" CREATE TABLE `Messages` (Id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "From_User TEXT, To_User TEXT, Message_Text TEXT,Date TEXT,isMine TEXT); ";

    private static final String DELETE_Messages = "DROP TABLE IF EXISTS Messages" ;

    public DataBaseHelper(Context context) {
        super(context, "eazy_db",null, database_version);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Messages);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_Messages);
        onCreate(db);
    }


    public boolean putMessages(DataBaseHelper dbh,String From_NodeID,
                                     String To_Node_ID, String Message_Text,String Date , String Time){
        SQLiteDatabase sq=dbh.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("From_User", From_NodeID);
        cv.put("To_User", To_Node_ID);
        cv.put("Message_Text", Message_Text);
        cv.put("Date", Date);
        cv.put("isMine", Time);
        sq.insert("Messages", null, cv);
        return true;

    }

    public List<eazi.com.eazi.database.Messages> getMessages(String user) {
        List<eazi.com.eazi.database.Messages> dataListList = new ArrayList<eazi.com.eazi.database.Messages>();
        String selectQuery = "SELECT * FROM Messages Where To_User = '"+user+"'" ;

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
                System.out.println("DataBaseHelper123 getDateRate " + cursor.getString(1));
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
