package cn.flyexp.mvc.campus;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.NoResponse;
import cn.flyexp.util.DatabaseHelper;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/9/26.
 */
public class CampusModel {

    public void insertAd(ArrayList<AdResponse.AdResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_AD);
        ContentValues contentValues = null;
        for (AdResponse.AdResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_AD, contentValues);
        }
    }

    public ArrayList<AdResponse.AdResponseData> getLocalAd() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_AD);
        if (cursor == null) {
            return null;
        }
        ArrayList<AdResponse.AdResponseData> responseData = new ArrayList<AdResponse.AdResponseData>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            AdResponse.AdResponseData adResponseData = GsonUtil.fromJson(data, AdResponse.AdResponseData.class);
            responseData.add(adResponseData);
        }
        return responseData;
    }

    public String getAdLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_AD);
    }

    public void insertNo(ArrayList<NoResponse.NoResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_NO);
        ContentValues contentValues = null;
        for (NoResponse.NoResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_NO, contentValues);
        }
    }

    public ArrayList<NoResponse.NoResponseData> getLocalNo() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_NO);
        if (cursor == null) {
            return null;
        }
        ArrayList<NoResponse.NoResponseData> responseData = new ArrayList<NoResponse.NoResponseData>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            NoResponse.NoResponseData adResponseData = GsonUtil.fromJson(data, NoResponse.NoResponseData.class);
            responseData.add(adResponseData);
        }
        return responseData;
    }

    public String getNoLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_NO);
    }

    public void insertHotAct(ArrayList<AssnActivityResponse.AssnActivityResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_ASSN_HOTACT);
        ContentValues contentValues = null;
        for (AssnActivityResponse.AssnActivityResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_ASSN_ACT, contentValues);
        }
    }

    public ArrayList<AssnActivityResponse.AssnActivityResponseData> getLocalHotAct() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_ASSN_HOTACT);
        if (cursor == null) {
            return null;
        }
        ArrayList<AssnActivityResponse.AssnActivityResponseData> responseData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            AssnActivityResponse.AssnActivityResponseData assnActivityResponseData = GsonUtil.fromJson(data, AssnActivityResponse.AssnActivityResponseData.class);
            responseData.add(assnActivityResponseData);
        }
        return responseData;
    }

    public String getHotActLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_ASSN_HOTACT);
    }
}
