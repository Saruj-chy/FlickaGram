package com.sd.spartan.flickagram.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelperTest extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FLICKAGRAM.DB";
    private static final String FLICKAGRAM_TBL = "flickagram_tbl";


    public DatabaseHelperTest(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + FLICKAGRAM_TBL + " (" +
                    "flicka_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id INTEGER," +
                    "owner VARCHAR," +
                    "secret VARCHAR," +
                    "server VARCHAR," +
                    "farm VARCHAR," +
                    "title VARCHAR," +
                    "ispublic VARCHAR," +
                    "datetaken VARCHAR," +
                    "datetakengranularity VARCHAR," +
                    "datetakenunknown VARCHAR," +
                    "url_h VARCHAR," +
                    "height_h VARCHAR," +
                    "width_h VARCHAR);"
            );
        } catch (SQLException ignored) {
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FLICKAGRAM_TBL);
        onCreate(db);
    }


    public void InsertFlickaTbl(String id, String owner, String secret, String server, String farm, String title, String ispublic, String datetaken, String datetakengranularity, String datetakenunknown, String url_h, String height_h, String width_h) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("owner", owner);
        contentValues.put("secret", secret);
        contentValues.put("server", server);
        contentValues.put("farm", farm);
        contentValues.put("title", title);
        contentValues.put("ispublic", ispublic);
        contentValues.put("datetaken", datetaken);
        contentValues.put("datetakengranularity", datetakengranularity);
        contentValues.put("datetakenunknown", datetakenunknown);
        contentValues.put("url_h", url_h);
        contentValues.put("height_h", height_h);
        contentValues.put("width_h", width_h);

        db.insert(FLICKAGRAM_TBL, null, contentValues);
    }



    public Cursor checkTable(String name, String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;

        switch (name) {
            case "all":
                res = db.rawQuery("select * from " + FLICKAGRAM_TBL, null);
                break;
            case "id":
                res = db.rawQuery("select * from " + FLICKAGRAM_TBL + " WHERE id =\"" + id + "\" ", null);
                break;
        }
        return res;
    }

    public void DeleteFlickaTbl() {
        SQLiteDatabase db1 = this.getWritableDatabase();
        db1.delete(FLICKAGRAM_TBL, "", new String[]{});
    }

}