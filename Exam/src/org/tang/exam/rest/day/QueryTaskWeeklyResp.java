package org.tang.exam.rest.day;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.tang.exam.common.AppConstant;
import org.tang.exam.entity.TaskWeekly;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryTaskWeeklyResp  {
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

	private ArrayList<TaskWeekly> response;
	
	public ArrayList<TaskWeekly> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<TaskWeekly> response) {
		this.response = response;
	}

	public QueryTaskWeeklyResp(String jsonStr) throws JSONException {
		parseResponseData(jsonStr);
	}
	
	@SuppressWarnings("unchecked")
	private void parseResponseData(String jsonStr) throws JSONException {
		Gson json = new Gson();
		QueryTaskWeeklyResp d =  json.fromJson(jsonStr, QueryTaskWeeklyResp.class);

		if(d!=null && d.getMsgFlag()==AppConstant.taskweekly_query_success){
			this.msgFlag = d.getMsgFlag();
			this.sessionKey = d.getSessionKey();
			this.response = (ArrayList<TaskWeekly>) json.fromJson(json.toJson(d.getResponse()),  
					new TypeToken<List<TaskWeekly>>() {}.getType());
		}

	}
	

}
