package org.tang.exam.rest.day;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.common.AppConstant.DayTaskStatus;
import org.tang.exam.entity.TaskWeekly;
import org.tang.exam.rest.BaseRequest;

public class AddTaskWeeklyReq extends BaseRequest {
	
	private TaskWeekly notice;

	public TaskWeekly getTaskWeekly() {
		return notice;
	}

	public void setTaskWeekly(TaskWeekly notice) {
		this.notice = notice;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/addTaskWeekly";
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
		paramsHashMap.put("type", DayTaskStatus.task_weekly);
		paramsHashMap.put("createDate", notice.getCreateDate());
		paramsHashMap.put("receiveId", notice.getReceiverId());
		return paramsHashMap;
	}

}
