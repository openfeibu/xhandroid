package cn.flyexp.mvc.assn;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.util.DatabaseHelper;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/3.
 */
public class AssnModel {

    public void insertAct(ArrayList<AssnActivityResponse.AssnActivityResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_ASSN_ACT);
        ContentValues contentValues = null;
        for (AssnActivityResponse.AssnActivityResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_ASSN_ACT,contentValues);
        }
    }

    public ArrayList<AssnActivityResponse.AssnActivityResponseData> getLocalAct() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_ASSN_ACT);
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




    public String getActLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_ASSN_ACT);
    }

}
