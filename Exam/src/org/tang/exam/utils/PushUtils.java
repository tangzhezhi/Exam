package org.tang.exam.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * 百度推送工具类
 * @author lenovo
 *
 */
public class PushUtils {
	 public static final String TAG = "PushDemoActivity";
		public static final String ACTION_BIND_SUCCESS = "bind_success";
		public static final String PUSH_CHANNELID = "pushChannelId";
		public static final String ACTION_NOTIFICATION_ENTRY = "notification_entry";
		
	    public static final String RESPONSE_METHOD = "method";
	    public static final String RESPONSE_CONTENT = "content";
	    public static final String RESPONSE_ERRCODE = "errcode";
	    public static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	    public static final String EXTRA_ACCESS_TOKEN = "access_token";
	    public static final String EXTRA_MESSAGE = "message";
	    public static final String UNREAD_COUNT = "unreadCount"; 
	    public static final String READ_COUNT = "readCount"; 
	    public static final String ACTION_READ_COUNT = "org.tang.exam.service.PushMessageReceiver.readCount"; 
	    public static final String ACTION_UNREAD_COUNT = "org.tang.exam.service.PushMessageReceiver.unreadCount"; 
	    public static final String ACTION_UPDATE_COUNT = "org.tang.exam.service.PushMessageReceiver.updateCount"; 
	    
	    public static final String ACTION_NOTIFICATION_ORG_ENTRY = "notification_org_entry";
	    public static final String ACTION_NOTIFICATION_VIDEO = "notification_video_entry";
	    
	    public static final String PREFERENCES_NAME="push_info";
	    public static String logStringCache = "";

	    // 获取ApiKey
	    public static String getMetaValue(Context context, String metaKey) {
	        Bundle metaData = null;
	        String apiKey = null;
	        if (context == null || metaKey == null) {
	            return null;
	        }
	        try {
	            ApplicationInfo ai = context.getPackageManager()
	                    .getApplicationInfo(context.getPackageName(),
	                            PackageManager.GET_META_DATA);
	            if (null != ai) {
	                metaData = ai.metaData;
	            }
	            if (null != metaData) {
	                apiKey = metaData.getString(metaKey);
	            }
	        } catch (NameNotFoundException e) {

	        }
	        return apiKey;
	    }

	    // 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	    public static boolean hasBind(Context context) {
	        SharedPreferences sp = PreferenceManager
	                .getDefaultSharedPreferences(context);
	        String flag = sp.getString("bind_flag", "");
	        if ("ok".equalsIgnoreCase(flag)) {
	            return true;
	        }
	        return false;
	    }

	    public static void setBind(Context context, boolean flag) {
	        String flagStr = "not";
	        if (flag) {
	            flagStr = "ok";
	        }
	        SharedPreferences sp = PreferenceManager
	                .getDefaultSharedPreferences(context);
	        Editor editor = sp.edit();
	        editor.putString("bind_flag", flagStr);
	        editor.commit();
	    }

	    public static List<String> getTagsList(String originalText) {
	        if (originalText == null || originalText.equals("")) {
	            return null;
	        }
	        List<String> tags = new ArrayList<String>();
	        int indexOfComma = originalText.indexOf(',');
	        String tag;
	        while (indexOfComma != -1) {
	            tag = originalText.substring(0, indexOfComma);
	            tags.add(tag);

	            originalText = originalText.substring(indexOfComma + 1);
	            indexOfComma = originalText.indexOf(',');
	        }

	        tags.add(originalText);
	        return tags;
	    }

	    public static String getLogText(Context context) {
	        SharedPreferences sp = PreferenceManager
	                .getDefaultSharedPreferences(context);
	        return sp.getString("log_text", "");
	    }

	    public static void setLogText(Context context, String text) {
	        SharedPreferences sp = PreferenceManager
	                .getDefaultSharedPreferences(context);
	        Editor editor = sp.edit();
	        editor.putString("log_text", text);
	        editor.commit();
	    }
	
}
