package org.tang.exam.rest.day;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class QueryTaskWeeklyReq extends BaseRequest {
	
	private String type;
	private String orgId;
	private String createDate;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/queryTaskWeekly";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("type", this.type);
		paramsHashMap.put("orgId", this.orgId);
		paramsHashMap.put("createDate", this.createDate);
		return paramsHashMap;
	}

}
