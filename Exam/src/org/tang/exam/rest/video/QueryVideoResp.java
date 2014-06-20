/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */

package org.tang.exam.rest.video;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.tang.exam.common.AppConstant;
import org.tang.exam.entity.Video;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryVideoResp  {
	private static final String TAG = "QueryVideoResp";
	private int msgFlag;
	private String sessionKey;
	private int pageSize;
	private int pageNo;
	private int total;
	private int totalPage;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(int msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	private ArrayList<Video> response;
	
	public ArrayList<Video> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<Video> response) {
		this.response = response;
	}

	public QueryVideoResp(String jsonStr) throws JSONException {
		parseResponseData(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	private void parseResponseData(String jsonStr) throws JSONException {
		Log.d(TAG, jsonStr);
		Gson json = new Gson();
		QueryVideoResp d =  json.fromJson(jsonStr, QueryVideoResp.class);

		if(d!=null && d.getMsgFlag()==AppConstant.video_query_success){
			this.msgFlag = d.getMsgFlag();
			this.sessionKey = d.getSessionKey();
			this.pageSize = d.getPageSize();
			this.total = d.getTotal();
			this.pageNo = d.getPageNo();
			this.totalPage = d.getTotalPage();
			if(null!=d.getResponse()&& !("").equals(d.getResponse())){
				this.response = (ArrayList<Video>) json.fromJson(json.toJson(d.getResponse()),  
						new TypeToken<List<Video>>() {}.getType());
			}

		}

	}
	

}
