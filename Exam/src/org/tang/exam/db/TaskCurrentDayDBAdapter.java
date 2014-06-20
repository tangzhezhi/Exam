/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */

package org.tang.exam.db;
import java.util.ArrayList;
import org.tang.exam.entity.TaskCurrentDay;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


public class TaskCurrentDayDBAdapter extends DBAdapter {
	private static final String TAG = "TaskCurrentDayDBAdapter";
	private int MAX_NUMBER = 20;
	
	
	public ArrayList<TaskCurrentDay> getTaskCurrentDay(String userId,String date) {
		String where = String.format(" authorId = '%s' and remindDate = '%s'  ", 
				userId,date);
		return getTaskCurrentDayByWhere(where);
	}
	
	
	private ArrayList<TaskCurrentDay> getTaskCurrentDayByWhere(String where) {
		ArrayList<TaskCurrentDay> list = new ArrayList<TaskCurrentDay>();

		String orderBy = "remindTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("TaskCurrentDay", new String[] {	
				"id",
		    	"orgId",
		    	"authorId",
		    	"authorName",
		    	"type",
		    	"title",
		    	"state",
		    	"createTime",
		    	"createDate",
		    	"remindType",
		    	"remindDate",
		    	"remindTime"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchTaskCurrentDay(result));
			} while (result.moveToNext());
		}
		return list;
	}
	
	
	private TaskCurrentDay fetchTaskCurrentDay(Cursor result) {
		TaskCurrentDay video = new TaskCurrentDay();
    	video.setId(result.getString(result.getColumnIndex("id")));
    	video.setOrgId(result.getString(result.getColumnIndex("orgId")));
    	video.setAuthorId(result.getString(result.getColumnIndex("authorId")));
    	video.setAuthorName(result.getString(result.getColumnIndex("authorName")));
    	video.setType(result.getString(result.getColumnIndex("type")));
    	video.setTitle(result.getString(result.getColumnIndex("title")));
    	video.setState(result.getString(result.getColumnIndex("state")));
    	video.setCreateTime(result.getString(result.getColumnIndex("createTime")));
    	video.setCreateDate(result.getString(result.getColumnIndex("createDate")));
    	video.setRemindType(result.getString(result.getColumnIndex("remindType")));
    	video.setRemindDate(result.getString(result.getColumnIndex("remindDate")));
    	video.setRemindTime(result.getString(result.getColumnIndex("remindTime")));
		return video;
	}
	
	public void addTaskCurrentDay(ArrayList<TaskCurrentDay> list) throws SQLException {
		
		for (TaskCurrentDay video : list) {
			ContentValues values = new ContentValues();
			values.put("id", video.getId());
			values.put("orgId", video.getOrgId());
			values.put("authorId", video.getAuthorId());
			values.put("authorName", video.getAuthorName());
			values.put("type", video.getType());
			values.put("title", video.getTitle());
			values.put("state", video.getState());
			values.put("createTime", video.getCreateTime());
			values.put("createDate", video.getCreateDate());
			values.put("remindType", video.getRemindType());
			values.put("remindDate", video.getRemindDate());
			values.put("remindTime", video.getRemindTime());
			
			try {
					getDb().insertOrThrow("TaskCurrentDay", null, values);
			} catch (Exception e) {
				Log.e(TAG, "插入错误"+e);
			}
		}
	}
	
}