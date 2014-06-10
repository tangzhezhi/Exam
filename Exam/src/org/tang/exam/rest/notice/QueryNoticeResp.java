package org.tang.exam.rest.notice;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.tang.exam.common.AppConstant;
import org.tang.exam.entity.Notice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryNoticeResp  {
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

	private ArrayList<Notice> response;
	
	public ArrayList<Notice> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<Notice> response) {
		this.response = response;
	}

	public QueryNoticeResp(String jsonStr) throws JSONException {
		parseResponseData(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	private void parseResponseData(String jsonStr) throws JSONException {
		Gson json = new Gson();
		QueryNoticeResp d =  json.fromJson(jsonStr, QueryNoticeResp.class);

		if(d!=null && d.getMsgFlag()==AppConstant.notice_query_success){
			this.msgFlag = d.getMsgFlag();
			this.sessionKey = d.getSessionKey();
			this.response = (ArrayList<Notice>) json.fromJson(json.toJson(d.getResponse()),  
					new TypeToken<List<Notice>>() {}.getType());
		}

	}
	

}
