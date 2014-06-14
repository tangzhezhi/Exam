/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */

package org.tang.exam.rest.video;

import java.util.HashMap;

import org.tang.exam.common.AppConstant;
import org.tang.exam.rest.BaseRequest;

public class QueryVideoReq extends BaseRequest {
	
		private java.lang.String videoname;
		private java.lang.String createtime;
		private String page;
		private String rows;
		
		public java.lang.String getVideoname() {
			return this.videoname;
		}
		
		public void setVideoname(java.lang.String videoname) {
			this.videoname = videoname;
		}
		public java.lang.String getCreatetime() {
			return this.createtime;
		}
		
		public void setCreatetime(java.lang.String createtime) {
			this.createtime = createtime;
		}

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public String getRows() {
			return rows;
		}

		public void setRows(String rows) {
			this.rows = rows;
		}

	@Override
	public String getPath() {
		return AppConstant.BASE_URL + "mobile/queryVideo";
	}

	@Override
	public HashMap<String, String> toParamsMap() {
		HashMap<String, String> paramsHashMap = new HashMap<String, String>();
			if(this.videoname!=null && !("").equals(this.videoname)){
				paramsHashMap.put("videoname", this.videoname);
			}
			if(this.createtime!=null && !("").equals(this.createtime)){
				paramsHashMap.put("createtime", this.createtime);
			}
	    	paramsHashMap.put("page", this.page==null?"1":this.page);
	    	paramsHashMap.put("rows", this.rows==null?"20":this.rows);
		return paramsHashMap;
	}

}
