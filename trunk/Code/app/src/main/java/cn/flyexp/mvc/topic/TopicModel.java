package cn.flyexp.mvc.topic;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.util.DatabaseHelper;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/3.
 */
public class TopicModel {

    public void insertTopic(ArrayList<TopicResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_TOPIC);
        ContentValues contentValues = null;
        for (TopicResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_ID, responseData.getTid());
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_TOPIC,contentValues);
        }
    }

    public ArrayList<TopicResponseData> getLocalTopic() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_TOPIC);
        if (cursor == null) {
            return null;
        }
        ArrayList<TopicResponseData> responseData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            TopicResponseData topicResponseData = GsonUtil.fromJson(data, TopicResponseData.class);
            responseData.add(topicResponseData);
        }
        return responseData;
    }

    public String getTopicLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_TOPIC);
    }

    public void insertMyTopic(ArrayList<TopicResponseData> data) {
        DatabaseHelper.deleteAllData(DatabaseHelper.TABLE_MYTOPIC);
        ContentValues contentValues = null;
        for (TopicResponseData responseData : data) {
            contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.KEY_ID, responseData.getTid());
            contentValues.put(DatabaseHelper.KEY_DATA, GsonUtil.toJson(responseData));
            contentValues.put(DatabaseHelper.KEY_DATE, responseData.getCreated_at());
            DatabaseHelper.insertData(DatabaseHelper.TABLE_MYTOPIC,contentValues);
        }
    }

    public ArrayList<TopicResponseData> getLocalMyTopic() {
        Cursor cursor = DatabaseHelper.queryAllData(DatabaseHelper.TABLE_MYTOPIC);
        if (cursor == null) {
            return null;
        }
        ArrayList<TopicResponseData> responseData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATA));
            TopicResponseData topicResponseData = GsonUtil.fromJson(data, TopicResponseData.class);
            responseData.add(topicResponseData);
        }
        return responseData;
    }

    public String getMyTopicLastTime() {
        return DatabaseHelper.getLastTime(DatabaseHelper.TABLE_MYTOPIC);
    }
}
