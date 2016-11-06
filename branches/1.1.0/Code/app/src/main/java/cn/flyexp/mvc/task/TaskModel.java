package cn.flyexp.mvc.task;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import cn.flyexp.entity.MyOrderResponse;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.DatabaseHelper;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/3.
 */
public class TaskModel {

    public void insertTask(ArrayList<OrderResponse.OrderResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_TASK);
        ContentValues contentValues = null;
        for (OrderResponse.OrderResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_ID, responseData.getOid());
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_TASK,contentValues);
        }
    }

    public ArrayList<OrderResponse.OrderResponseData> getLocalTask() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_TASK);
        if (cursor == null) {
            return null;
        }
        ArrayList<OrderResponse.OrderResponseData> responseData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            OrderResponse.OrderResponseData orderResponseData = GsonUtil.fromJson(data, OrderResponse.OrderResponseData.class);
            responseData.add(orderResponseData);
        }
        return responseData;
    }

    public String getTaskLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_TASK);
    }

    public void insertMyTask(ArrayList<MyTaskResponse.MyTaskResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_MYTASK);
        ContentValues contentValues = null;
        for (MyTaskResponse.MyTaskResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_ID, responseData.getOid());
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_MYTASK,contentValues);
        }
    }

    public ArrayList<MyTaskResponse.MyTaskResponseData> getLocalMyTask() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_MYTASK);
        if (cursor == null) {
            return null;
        }
        ArrayList<MyTaskResponse.MyTaskResponseData> responseData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            MyTaskResponse.MyTaskResponseData orderResponseData = GsonUtil.fromJson(data, MyTaskResponse.MyTaskResponseData.class);
            responseData.add(orderResponseData);
        }
        return responseData;
    }

    public String getMyTaskLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_MYTASK);
    }


    public void insertMyOrder(ArrayList<MyOrderResponse.MyOrderResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_MYORDER);
        ContentValues contentValues = null;
        for (MyOrderResponse.MyOrderResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_ID, responseData.getOid());
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_MYORDER,contentValues);
        }
    }

    public ArrayList<MyOrderResponse.MyOrderResponseData> getLocalMyOrder() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_MYORDER);
        if (cursor == null) {
            return null;
        }
        ArrayList<MyOrderResponse.MyOrderResponseData > responseData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            MyOrderResponse.MyOrderResponseData myOrderResponseData = GsonUtil.fromJson(data, MyOrderResponse.MyOrderResponseData.class);
            responseData.add(myOrderResponseData);
        }
        return responseData;
    }

    public String getMyOrderLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_MYORDER);
    }
}
