/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */
package org.tang.exam.rest.video;
import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.entity.Video;
import org.tang.exam.rest.BaseRequest;

public class AddVideoReq extends BaseRequest {
	
	private Video video;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/addVideo";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
		
    		paramsHashMap.put("videoid", video.getVideoid());
    		paramsHashMap.put("videoname", video.getVideoName());
    		paramsHashMap.put("videourl", video.getVideoUrl());
    		paramsHashMap.put("userid", video.getUserid());
    		paramsHashMap.put("createtime", video.getCreatetime());
    		paramsHashMap.put("clicknum", video.getClickNum());
    		paramsHashMap.put("videoduration", video.getVideoDuration());
    		paramsHashMap.put("videorecommend", video.getVideoRecommend());
    		paramsHashMap.put("videosubject", video.getVideoSubject());
    		paramsHashMap.put("videotime", video.getVideoTime());
    		paramsHashMap.put("videopic", video.getVideopic());
		return paramsHashMap;
	}

}
