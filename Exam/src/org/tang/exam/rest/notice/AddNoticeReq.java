package org.tang.exam.rest.notice;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.common.AppConstant.NoticeType;
import org.tang.exam.entity.Notice;
import org.tang.exam.rest.BaseRequest;

public class AddNoticeReq extends BaseRequest {
	
	private Notice notice;

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/addNotice";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		paramsHashMap.put("id", notice.getId());
		paramsHashMap.put("orgId", notice.getOrgId());
		paramsHashMap.put("createTime", notice.getCreateTime());
		paramsHashMap.put("authorId", notice.getAuthorId());
		paramsHashMap.put("authorName", notice.getAuthorName());
		paramsHashMap.put("title", notice.getTitle());
		paramsHashMap.put("content", notice.getContent());
		paramsHashMap.put("type", String.valueOf(NoticeType.ORG));
		return paramsHashMap;
	}

}
