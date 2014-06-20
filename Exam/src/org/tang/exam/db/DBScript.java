package org.tang.exam.db;

public final class DBScript {
    
	public static final String CREATE_TABLE_NOTICE = 
    		"CREATE TABLE Notice " + 
            "( " + 
            	"id text not null primary key , " +
            	"orgId text , " + 
                "authorId text, " + 
                "authorName text , " + 
                "type text , " + 
                "title text , " +
                "content text , " +
                "createTime text  " +
            "); ";
   
    public static final String CREATE_TABLE_HOMEWORK = 
    		"CREATE TABLE Homework " + 
            "( " + 
            	"cacheUserId text not null, " +
                "authorId text not null, " + 
                "authorName text not null, " + 
                "subject text not null, " + 
                "classId text not null, " +
                "content text not null, " +
                "createTime text not null " +
            "); ";
	
    public static final String CREATE_TABLE_DAILY = 
    		"CREATE TABLE Daily " +  
    		"( " + 
    			"cacheUserId text not null, " + 
            	"authorId text not null, " + 
	            "authorName text not null, " + 
	            "receiverId text not null, " + 
	            "receiverName text not null, " +
	            "content text not null, " +
	            "createTime text not null " +
	        "); ";
    
    public static final String CREATE_TABLE_STUDENT_ROSTER = 
    		"CREATE TABLE StudentRoster " + 
            "( " + 
            	"cacheUserId text not null, " +
                "userId text not null, " + 
                "userName text not null, " + 
                "schoolId text not null, " +
                "studentNo text not null, " +
                "sex integer not null, " +
                "phone text, " +
                "picUrl text, " +
                "classId text not null, " +
                "className text not null " +
            "); ";
    
    public static final String CREATE_TABLE_PARENT_ROSTER = 
    		"CREATE TABLE ParentRoster " + 
            "( " + 
            	"cacheUserId text not null, " +
                "userId text not null, " + 
                "userName text not null, " + 
                "sex integer not null, " +
                "phone text, " +
                "picUrl text, " +
                "childUserId text not null, " +
                "childName text not null, " +
                "schoolId text not null, " +
                "classId text not null, " +
                "className text not null " +
            "); ";
	
    public static final String CREATE_TABLE_TEACHER_ROSTER = 
    		"CREATE TABLE TeacherRoster " + 
            "( " + 
            	"cacheUserId text not null, " +
                "userId text not null, " + 
                "userName text not null, " + 
                "teacherId text not null, " + 
                "sex integer text not null, " +
                "phone text, " +
                "picUrl text, " +
                "role text, " +
                "schoolId text text not null, " +
                "classId text text not null, " +
                "className text text not null " +
            "); ";
    
    public static final String CREATE_TABLE_MESSAGE = 
    		"CREATE TABLE Message " + 
            "( " + 
            	"msgId integer not null primary key autoincrement, " + 
                "msgType integer not null, " + 
                "senderId text not null, " +
                "receiverId text, " +
                "objectType integer, " + 
                "groupType integer, " + 
                "content text not null, " +
                "outFlag integer not null, " +
                "createTime text not null, " +
                "readStatus integer not null " +
            "); ";
    
    public static final String CREATE_TABLE_SMS = 
    		"CREATE TABLE SMS " + 
            "( " + 
            	"cacheUserId text not null, " +
            	"smsId integer not null primary key autoincrement, " + 
                "userList text, " + 
                "content text not null, " +
                "createTime text not null " +
            "); ";
    
    
    public static final String CREATE_TABLE_ATTENDANCE = 
    		"CREATE TABLE Attendance " + 
            "( " + 
        		"id text not null primary key , " + 
            	"userId text not null, " +
                "address text, " + 
                "gps text not null, " +
                "createTime text not null, " +
                "latitude text not null, " +
                "longitude text not null " +
            "); ";
    
    
    public static final String CREATE_TABLE_ATTENDANCE_GRAPH = 
    		"CREATE TABLE AttendanceGraph " + 
            "( " + 
        		"id text not null primary key , " + 
            	"userId text not null, " +
                "address text, " + 
                "gps text not null, " +
                "createTime text not null, " +
                "latitude text not null, " +
                "longitude text not null, " +
                "photoUrl text not null " +
            "); ";
    
    

    public static final String CREATE_TABLE_USERINFO = 
    		"CREATE TABLE userinfo " + 
            "( " + 
        		"userId text not null primary key , " + 
            	"userName text , " +
                "sex integer, " + 
                "phone text , " +
                "picUrl text , " +
                "pinYin text , " +
                "initial text , " +
                "userType text , " +
                "orgId text , " +
                "departId text  " +
            "); ";
    
    
    public static final String CREATE_TABLE_CHATMSG = 
    		"CREATE TABLE ChatMsg " + 
            "( " + 
        		"id text not null primary key , " + 
            	"fromUserId text , " +
            	"toUserId text , " +
                "fromUserName text, " + 
                "toUserName text , " +
                "createTime text , " +
                "msgtext text,  " +
                "msgType text,  " +
                "msgState text  " +
            "); ";
    
    
  	public static final String CREATE_TABLE_VIDEO = 
      		"CREATE TABLE Video " + 
              "( " + 
      				"videoid text not null ,"+
      				"videoname text  ,"+
      				"videourl text ,"+
      				"userid text  ,"+
      				"createtime text  ,"+
      				"clicknum text  ,"+
      				"videoduration text  ,"+
      				"videorecommend text  ,"+
      				"videosubject text ,"+
      				"videotime text  ,"+
      				"videopic text  "+
              "); ";
    
  	
  	public static final String CREATE_TABLE_TASKCURRENTDAY = 
      		"CREATE TABLE TaskCurrentDay " + 
              "( " + 
      				"id text not null ,"+
      				"orgId text  ,"+
      				"authorId text ,"+
      				"authorName text  ,"+
      				"type text  ,"+
      				"title text  ,"+
      				"state text  ,"+
      				"createTime text  ,"+
      				"createDate text ,"+
      				"remindType text  ,"+
      				"remindDate text , "+
      				"remindTime text  "+
              "); ";
  	
  	
  	
    public static final String DROP_TABLE_NOTICE = "DROP TABLE IF EXISTS Notice; ";
    public static final String DROP_TABLE_HOMEWORK = "DROP TABLE IF EXISTS Homework; ";
    public static final String DROP_TABLE_DAILY = "DROP TABLE IF EXISTS Daily; ";
    public static final String DROP_TABLE_STUDENT_ROSTER = "DROP TABLE IF EXISTS StudentRoster; ";
    public static final String DROP_TABLE_PARENT_ROSTER = "DROP TABLE IF EXISTS ParentRoster; ";
    public static final String DROP_TABLE_TEACHER_ROSTER = "DROP TABLE IF EXISTS TeacherRoster; ";
    public static final String DROP_TABLE_MESSAGE = "DROP TABLE IF EXISTS Message; ";
    public static final String DROP_TABLE_SMS = "DROP TABLE IF EXISTS SMS; ";
    public static final String DROP_TABLE_ATTENDANCE = "DROP TABLE IF EXISTS Attendance; ";
    public static final String DROP_TABLE_ATTENDANCE_GRAPH = "DROP TABLE IF EXISTS AttendanceGraph; ";
    public static final String DROP_TABLE_USERINFO = "DROP TABLE IF EXISTS userinfo; ";
    public static final String DROP_TABLE_CHATMSG  = "DROP TABLE IF EXISTS ChatMsg; ";
    public static final String DROP_TABLE_Video= "DROP TABLE IF EXISTS Video; ";
    public static final String DROP_TABLE_TASKCURRENTDAY= "DROP TABLE IF EXISTS TaskCurrentDay; ";
}
