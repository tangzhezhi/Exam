package org.tang.exam.db;

import org.tang.exam.base.MyApplication;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.Message;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "exam.db";
	private static final int DATABASE_VERSION = 2;
	private static final int MAX_NUMBER = 50;

	private SQLiteDatabase db;
	private Context mContext;
	private DBHelper dbHelper;
	private String mUserId = "";

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	public DBAdapter(Context context, String userId) {
		this.mContext = context;
		this.mUserId = userId;
		dbHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DBAdapter() {
		this.mContext = MyApplication.getInstance().getApplicationContext();
		this.mUserId = UserCache.getInstance().getUserInfo().getUserId();
		dbHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public void close() {
		db.close();
	}

	public void addMessage(Message message) throws SQLException {
		ContentValues values = new ContentValues();

		values.put("msgType", message.getMsgType());
		values.put("senderId", message.getSenderId());
		values.put("receiverId", message.getReceiverId());
		values.put("objectType", message.getObjectType());
		values.put("groupType", message.getGroupType());
		values.put("content", message.getContent());
		values.put("outFlag", message.getOutFlag());
		values.put("createTime", message.getCreateTime());
		values.put("readStatus", message.getReadStatus());
		db.insertOrThrow("Message", null, values);
	}

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context mContext, String name, CursorFactory factory, int version) {
			super(mContext, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DBScript.CREATE_TABLE_NOTICE);
			db.execSQL(DBScript.CREATE_TABLE_HOMEWORK);
			db.execSQL(DBScript.CREATE_TABLE_DAILY);
			db.execSQL(DBScript.CREATE_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.CREATE_TABLE_MESSAGE);
			db.execSQL(DBScript.CREATE_TABLE_SMS);
			db.execSQL(DBScript.CREATE_TABLE_ATTENDANCE);
			db.execSQL(DBScript.CREATE_TABLE_ATTENDANCE_GRAPH);
			
			db.execSQL(DBScript.CREATE_TABLE_USERINFO);
			db.execSQL(DBScript.CREATE_TABLE_CHATMSG);
			db.execSQL(DBScript.CREATE_TABLE_VIDEO);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading from version " + oldVersion + " to " + newVersion
					+ ", which will destroy all old data");

			db.execSQL(DBScript.DROP_TABLE_NOTICE);
			db.execSQL(DBScript.DROP_TABLE_HOMEWORK);
			db.execSQL(DBScript.DROP_TABLE_DAILY);
			db.execSQL(DBScript.DROP_TABLE_STUDENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_PARENT_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_TEACHER_ROSTER);
			db.execSQL(DBScript.DROP_TABLE_MESSAGE);
			db.execSQL(DBScript.DROP_TABLE_SMS);
			db.execSQL(DBScript.DROP_TABLE_ATTENDANCE);
			db.execSQL(DBScript.DROP_TABLE_ATTENDANCE_GRAPH);
			db.execSQL(DBScript.DROP_TABLE_USERINFO);
			db.execSQL(DBScript.DROP_TABLE_CHATMSG);
			db.execSQL(DBScript.DROP_TABLE_Video);
			onCreate(db);
		}
	}

}