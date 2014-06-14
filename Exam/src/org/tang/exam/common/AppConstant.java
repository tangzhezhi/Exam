package org.tang.exam.common;

public class AppConstant {
//	public static final String BASE_URL = "http://113.247.250.200:11111/springmvc/";
	public static final String BASE_URL = "http://192.168.2.116:11111/springmvc/";
//	public static final String BASE_URL = "http://192.168.1.103:8080/springmvc/";
	public static final int DEFAULT_PAGESIZE = 15;
	
	public static final int INTERNAL_MESSAGE_ID = 2;
	
	public static final class UserType {
		public static final int SYSTEM = 0;
		public static final int STUDENT = 1;
		public static final int PARENT = 2;
		public static final int TEACHER = 3;
	}

	public static final class Ack {
		public static final int SUCCESS = 0;
		public static final int NOT_FOUND = 100;
	}
	
	public static final class Sex {
		public static final int FEMALE = 1;
		public static final int MALE = 0;
	}
	
	public static final class NoticeQueryType {
		public static final int QUERY_ALL = 0;
		public static final int QUERY_SCHOOL = 1;
		public static final int QUERY_CLASS = 2;
		public static final int QUERY_SOMEONE = 3;
	}
	
	public static final class NoticeType {
		public static final int SYSTEM = 1;
		public static final int ORG= 2;
	}
	
	public static final class HomeworkQueryType {
		public static final int QUERY_CLASS = 1;
		public static final int QUERY_SOMEONE = 2;
	}
	
	public static final class MessageType {
		public static final int NORMAL = 1;
		public static final int NOTICE = 2;
		public static final int HOMEWORK = 3;
		public static final int DAILY = 4;
	}
	
	public static final class MessageObjectType {
		public static final int PERSONAL = 0;
		public static final int STUDENT = 1;
		public static final int PARENT = 2;
		public static final int TEACHER = 3;
		public static final int ALL = 4;
	}
	
	public static final class MessageGroupType {
		public static final int PERSONAL = 0;
		public static final int SCHOOL = 1;
		public static final int CLASS = 2;
	}
	
	public static final class MessageSendStatus {
		public static final int SENDING = 1;
		public static final int SUCCESS = 2;
		public static final int FAILED = 3;
	}
	
	
	
	public static final int login_success = 1001; 
	
	public static final int login_fail = 8001; 
	
	public static final int attendance_success = 1002; 
	
	public static final int attendance_fail = 8002; 
	
	public static final int attendance_upload_success = 1003; 
	
	public static final int attendance_upload_fail = 8003; 
	
	public static final int contact_query_success = 1004; 
	
	public static final int contact_query_fail = 8004; 
	
	public static final int pushInfo_upload_success = 1005; 
	
	public static final int pushInfo_upload_fail = 8005; 
	
	public static final int chat_msg_send_success = 1006; 
	
	public static final int chat_msg_send_fail = 8006; 
	
	public static final int notice_query_success = 1007; 
	
	public static final int notice_query_fail = 8007; 
	
	public static final int notice_add_success = 1008; 
	
	public static final int notice_add_fail = 8008; 
	
	public static final int notice_msg = 1009; 
	
	public static final int chat_msg = 1010; 
	
	public static final int video_query_success = 1011; 
	
	public static final int video_query_fail = 8011; 
	
	
}
