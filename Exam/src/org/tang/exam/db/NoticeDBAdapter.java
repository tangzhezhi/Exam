package org.tang.exam.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.tang.exam.common.AppConstant.NoticeType;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.Notice;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class NoticeDBAdapter extends DBAdapter {
	private static final String TAG = "NoticeDBAdapter";
	private int MAX_NUMBER = 20;
	private String mUserId="";
	
	
	private List<Notice> getNoticeExist(String title,String type,String createTime) {
		String where = String.format(" title = '%s' and type = '%s'  and createTime =  '%s' ", 
				title,type,createTime);
		return getNoticeByWhere(where);
	}
	
	
	public ArrayList<Notice> getNoticeSystem() {
		String where = String.format(" type = '%s' ", 
				NoticeType.SYSTEM);
		return getNoticeByWhere(where);
	}
	
	public ArrayList<Notice> getNoticeOrg() {
		String where = String.format(" type = '%s' ", 
				NoticeType.ORG);
		return getNoticeByWhere(where);
	}
	
	private ArrayList<Notice> getNoticeByWhere(String where) {
		ArrayList<Notice> list = new ArrayList<Notice>();

		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("Notice", new String[] {"id", "orgId","authorId", "createTime",
				"authorName", "type","title","content"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchNotice(result));
			} while (result.moveToNext());
		}
		return list;
	}
	
	
	
	private Notice fetchNotice(Cursor result) {
		Notice notice = new Notice();
		notice.setId(result.getString(result.getColumnIndex("id")));
		notice.setOrgId(result.getString(result.getColumnIndex("orgId")));
		notice.setAuthorId(result.getString(result.getColumnIndex("authorId")));
		notice.setCreateTime(result.getString(result.getColumnIndex("createTime")));
		notice.setAuthorName(result.getString(result.getColumnIndex("authorName")));
		notice.setType(result.getString(result.getColumnIndex("type")));
		notice.setTitle(result.getString(result.getColumnIndex("title")));
		notice.setContent(result.getString(result.getColumnIndex("content")));
		return notice;
	}
	
	public void addNotice(ArrayList<Notice> list) throws SQLException {
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		for (Notice notice : list) {
			ContentValues values = new ContentValues();
			values.put("id", UUID.randomUUID().toString());
			values.put("authorId", mUserId);
			values.put("authorName", UserCache.getInstance().getUserInfo().getUserName());
			values.put("orgId", UserCache.getInstance().getUserInfo().getOrgId());
			values.put("type", notice.getType());
			values.put("createTime", notice.getCreateTime());
			values.put("title", notice.getTitle());
			values.put("content", notice.getContent());
			
			try {
				List<Notice> lh = this.getNoticeExist(notice.getTitle(),notice.getType(),notice.getCreateTime());
				if(lh.size()==0){
					getDb().insertOrThrow("Notice", null, values);
				}
			} catch (Exception e) {
				Log.e(TAG, "插入错误"+e);
			}
		}
	}
	
	public void addServiceNotice(ArrayList<Notice> list) throws SQLException {
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		for (Notice notice : list) {
			ContentValues values = new ContentValues();
			values.put("id", notice.getId());
			values.put("authorId", notice.getAuthorId());
			values.put("authorName", UserCache.getInstance().getUserInfo().getUserName());
			values.put("orgId", UserCache.getInstance().getUserInfo().getOrgId());
			values.put("type", notice.getType());
			values.put("createTime", notice.getCreateTime());
			values.put("title", notice.getTitle());
			values.put("content", notice.getContent());
			
			try {
				List<Notice> lh = this.getNoticeExist(notice.getTitle(),notice.getType(),notice.getCreateTime());
				if(lh.size()==0){
					getDb().insertOrThrow("Notice", null, values);
				}
			} catch (Exception e) {
				Log.e(TAG, "插入错误"+e);
			}
		}
	}
	
	
}