package cn.flyexp.mvc.mine;

import android.content.ContentValues;
import android.database.Cursor;

import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.util.DatabaseHelper;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/3.
 */
public class MineModel {

    public void insertMine(MyInfoResponse.MyInfoResponseData data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_MINE);
        ContentValues contentValues = null;
        contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(data));
        contentValues.put(DatabaseHelper.KEY_DATE, data.getCreated_at());
        DatabaseHelper.insertData(DatabaseHelper.TABLE_MINE, contentValues);
    }

    public MyInfoResponse.MyInfoResponseData getLocalMine() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_MINE);
        if (cursor == null) {
            return null;
        }
        MyInfoResponse.MyInfoResponseData responseData = null;
        if (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            MyInfoResponse.MyInfoResponseData myInfoResponseData = GsonUtil.fromJson(data, MyInfoResponse.MyInfoResponseData.class);
            responseData = myInfoResponseData;
        }
        return responseData;
    }

    public String getMineLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_MINE);
    }

}
