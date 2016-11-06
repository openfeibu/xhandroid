package cn.flyexp.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.flyexp.FBApplication;

/**
 * Created by tanxinye on 2016/9/26.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private static DatabaseHelper databaseHelper;
    public static final String KEY_ID = "id";
    public static final String KEY_DATA = "data";
    public static final String KEY_DATE = "date";

    public static final String TABLE_AD = "ad";
    public static final String TABLE_NO = "no";
    public static final String TABLE_TASK = "task";
    public static final String TABLE_MYORDER = "myorder";
    public static final String TABLE_MYTASK = "mytask";
    public static final String TABLE_TOPIC = "topic";
    public static final String TABLE_MYTOPIC = "mytopic";
    public static final String TABLE_MINE = "mine";
    public static final String TABLE_ASSN_HOTACT = "assn_hotact";
    public static final String TABLE_ASSN_ACT = "assn_act";
    public static final String TABLE_ASSN = "assn";
    public static final String TABLE_MYASSN = "myassn";

    public DatabaseHelper() {
        super(FBApplication.APPLICATION_CONTEXT, FBApplication.APPLICATION_CONTEXT.getPackageName(), null, 1);
        database = getWritableDatabase();
    }

    private static DatabaseHelper getInstance() {
        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper();
                }
            }
        }
        return databaseHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_AD + " (" + KEY_DATA + " varchar(100), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_NO + " (" + KEY_DATA + " varchar(30), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_TASK + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(100), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_MYORDER + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(100), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_MYTASK + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(100), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_TOPIC + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_MYTOPIC + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_MINE + " (" + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(20))");
        db.execSQL("create table " + TABLE_ASSN_HOTACT + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_ASSN_ACT + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_ASSN + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(30))");
        db.execSQL("create table " + TABLE_MYASSN + " (" + KEY_ID + " integer, " + KEY_DATA + " varchar(200), " + KEY_DATE + " varchar(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean _insertData(String tabName, ContentValues contentValues) {
        long l = database.insert(tabName, null, contentValues);
        return l != -1;
    }

    public Cursor _queryAllData(String tabName) {
        Cursor cursor = database.query(tabName, null, null, null, null, null, null, null);
        return cursor;
    }

    public void _deleteDataById(String tabName, int id) {
        database.delete(tabName, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void _deleteAllData(String tabName) {
        database.delete(tabName, null, null);
    }

    public String _getLastTime(String tabName) {
        String lastTime = "";
        Cursor cursor = database.query(tabName, new String[]{KEY_DATE}, null, null, null, null, null, null);
        if (cursor!=null&&cursor.moveToLast()) {
            lastTime = cursor.getString(cursor.getColumnIndex(KEY_DATE));
        }
        return lastTime;
    }

    public static boolean insertData(String tabName, ContentValues contentValues) {
        return getInstance()._insertData(tabName, contentValues);
    }

    public static Cursor queryAllData(String tabName) {
        return getInstance()._queryAllData(tabName);
    }

    public void deleteDataById(String tabName, int id) {
        getInstance()._deleteDataById(tabName, id);
    }

    public static void deleteAllData(String tabName) {
        getInstance()._deleteAllData(tabName);
    }

    public static String getLastTime(String tabName) {
        return getInstance()._getLastTime(tabName);
    }
}
