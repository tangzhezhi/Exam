package org.tang.exam.rest.notice;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class QueryNoticeReq extends BaseRequest {
	
	private String type;
	private String orgId;
	private String createTime;
	
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/queryNotice";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("type", this.type);
		paramsHashMap.put("orgId", this.orgId);
		paramsHashMap.put("createTime", this.createTime);
		return paramsHashMap;
	}

}
