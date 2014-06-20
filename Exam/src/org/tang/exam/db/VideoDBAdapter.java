/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */

package org.tang.exam.db;

import java.util.ArrayList;
import java.util.List;

import org.tang.exam.entity.Video;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


public class VideoDBAdapter extends DBAdapter {
	private static final String TAG = "VideoDBAdapter";
	private int MAX_NUMBER = 20;
	
	
	private List<Video> getVideoExist(String title,String createTime) {
		String where = String.format(" videoname = '%s'   and videourl =  '%s' ", 
				title,createTime);
		return getVideoByWhere(where);
	}
	
	public ArrayList<Video> getVideo() {
		String where = "";
		return getVideoByWhere(where);
	}
	
	
	
	private ArrayList<Video> getVideoByWhere(String where) {
		ArrayList<Video> list = new ArrayList<Video>();

		String orderBy = "createTime DESC";
		String limit = String.valueOf(MAX_NUMBER);

		Cursor result = getDb().query("Video", new String[] {	
				"videoid",
		    	"videoname",
		    	"videourl",
		    	"userid",
		    	"createtime",
		    	"clicknum",
		    	"videoduration",
		    	"videorecommend",
		    	"videosubject",
		    	"videotime",
		    	"videopic"},
				where, null, null, null, orderBy, limit);
		if (result.moveToFirst()) {
			do {
				list.add(fetchVideo(result));
			} while (result.moveToNext());
		}
		return list;
	}
	
	
	
	private Video fetchVideo(Cursor result) {
		Video video = new Video();
    	video.setVideoid(result.getString(result.getColumnIndex("videoid")));
    	video.setVideoName(result.getString(result.getColumnIndex("videoname")));
    	video.setVideoUrl(result.getString(result.getColumnIndex("videourl")));
    	video.setUserid(result.getString(result.getColumnIndex("userid")));
    	video.setCreatetime(result.getString(result.getColumnIndex("createtime")));
    	video.setClickNum(result.getString(result.getColumnIndex("clicknum")));
    	video.setVideoDuration(result.getString(result.getColumnIndex("videoduration")));
    	video.setVideoRecommend(result.getString(result.getColumnIndex("videorecommend")));
    	video.setVideoSubject(result.getString(result.getColumnIndex("videosubject")));
    	video.setVideoTime(result.getString(result.getColumnIndex("videotime")));
    	video.setVideopic(result.getString(result.getColumnIndex("videopic")));
		return video;
	}
	
	public void addVideo(ArrayList<Video> list) throws SQLException {
		
		for (Video video : list) {
			ContentValues values = new ContentValues();
			values.put("videoid", video.getVideoid());
	    	values.put("videoname", video.getVideoName());
	    	values.put("videourl", video.getVideoUrl());
	    	values.put("userid", video.getUserid());
	    	values.put("createtime", video.getCreatetime());
	    	values.put("clicknum", video.getClickNum());
	    	values.put("videoduration", video.getVideoDuration());
	    	values.put("videorecommend", video.getVideoRecommend());
	    	values.put("videosubject", video.getVideoSubject());
	    	values.put("videotime", video.getVideoTime());
	    	values.put("videopic", video.getVideopic());
			
			try {
				List<Video> lh = this.getVideoExist(video.getVideoName(),video.getVideoUrl());
				if(lh.size()==0){
					getDb().insertOrThrow("Video", null, values);
				}
			} catch (Exception e) {
				Log.e(TAG, "插入错误"+e);
			}
		}
	}
	
	
	// 查询记录的总数  
    public int getCount() {  
        String sql = "select count(*) from Video";  
        Cursor c = getDb().rawQuery(sql, null);  
        c.moveToFirst();  
        int length = c.getInt(0);  
        c.close();  
        return length;  
    }  
	
    
    /** 
     * 分页查询 
     *  
     * @param currentPage 当前页 
     * @param pageSize 每页显示的记录 
     * @return 当前页的记录 
     */  
    public ArrayList<Video> getAllItems(int currentPage, int pageSize) {  
        int firstResult = (currentPage - 1) * pageSize;  
        int maxResult = currentPage * pageSize;  
        String sql = "select * from Video limit ?,?";  
        Cursor mCursor = getDb().rawQuery(  
                sql,  
                new String[] { String.valueOf(firstResult),  
                        String.valueOf(maxResult) });  
        ArrayList<Video> items = new ArrayList<Video>();  
        int columnCount  = mCursor.getColumnCount();  
        while (mCursor.moveToNext()) {
        	items.add(fetchVideo(mCursor));
        }  
        return items;  
    } 
	
}