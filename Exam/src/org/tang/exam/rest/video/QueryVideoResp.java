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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryVideoResp  {
	private int msgFlag;
	private String sessionKey;
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
		Gson json = new Gson();
		QueryVideoResp d =  json.fromJson(jsonStr, QueryVideoResp.class);

		if(d!=null && d.getMsgFlag()==AppConstant.video_query_success){
			this.msgFlag = d.getMsgFlag();
			this.sessionKey = d.getSessionKey();
			this.response = (ArrayList<Video>) json.fromJson(json.toJson(d.getResponse()),  
					new TypeToken<List<Video>>() {}.getType());
		}

	}
	

}
