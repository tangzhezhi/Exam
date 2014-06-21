package org.tang.exam.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.R;
import org.tang.exam.activity.ChatActivity;
import org.tang.exam.activity.MainActivity;
import org.tang.exam.activity.NoticeActivity;
import org.tang.exam.activity.PostNoticeActivity;
import org.tang.exam.common.AppConfig;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.AppConstant.NoticeType;
import org.tang.exam.db.ChatMsgDBAdapter;
import org.tang.exam.db.NoticeDBAdapter;
import org.tang.exam.entity.ChatMsgEntity;
import org.tang.exam.entity.Notice;
import org.tang.exam.rest.push.ChatMsgDTO;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.utils.PushUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下： 
 *  0 - Success
 *  10001 - Network Problem
 *  30600 - Internal Server Error
 *  30601 - Method Not Allowed 
 *  30602 - Request Params Not Valid
 *  30603 - Authentication Failed 
 *  30604 - Quota Use Up Payment Required 
 *  30605 - Data Required Not Found 
 *  30606 - Request Time Expires Timeout 
 *  30607 - Channel Token Timeout 
 *  30608 - Bind Relation Not Found 
 *  30609 - Bind Number Too Many
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class PushMessageReceiver extends FrontiaPushMessageReceiver {
	  /** TAG to Log */
    public static final String TAG = PushMessageReceiver.class
            .getSimpleName();
    
    private String  pushUserId;
    private String pushChannelId;
    
    public String getPushUserId() {
		return pushUserId;
	}

	public void setPushUserId(String pushUserId) {
		this.pushUserId = pushUserId;
	}

	public String getPushChannelId() {
		return pushChannelId;
	}

	public void setPushChannelId(String pushChannelId) {
		this.pushChannelId = pushChannelId;
	}

	/**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     * 
     * @param context
     *            BroadcastReceiver的执行Context
     * @param errorCode
     *            绑定接口返回值，0 - 成功
     * @param appid
     *            应用id。errorCode非0时为null
     * @param userId
     *            应用user id。errorCode非0时为null
     * @param channelId
     *            应用channel id。errorCode非0时为null
     * @param requestId
     *            向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
            String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
        SharedPreferences preferences = context.getSharedPreferences(PushUtils.PREFERENCES_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (errorCode == 0) {
            PushUtils.setBind(context, true);
            pushUserId = userId;
            pushChannelId = channelId;
            //保存组件中的值
            editor.putString("pushUserId", pushUserId);
            editor.putString("pushChannelId", pushChannelId);
            editor.putString("bind_flag", "ok");
            //提交保存的结果
            editor.commit();
        }
        else{
            editor.putString("pushUserId", null);
            editor.putString("pushChannelId", null);
            editor.putString("bind_flag", null);
            //提交保存的结果
            editor.commit();
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     * 
     * @param context
     *            上下文
     * @param message
     *            推送的消息
     * @param customContentString
     *            自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
            String customContentString) {
    
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);
        
        try {
			JSONObject rootObj = new JSONObject(message);
			if(rootObj.getString("sessionKey")!=null && !rootObj.getString("sessionKey").equals("")){
				if(AppConstant.notice_msg==rootObj.getInt("msgFlag")){
			        try {
			        	receivePushNoticeContent(context, rootObj.getString("response"));
					} catch (JsonSyntaxException e) {
						Log.e(TAG, "解析消息异常：："+e);
						e.printStackTrace();
					}
					
				}
				else if(AppConstant.chat_msg==rootObj.getInt("msgFlag")){
			        try {
			        	receivePushContent(context, rootObj.getString("response"));
					} catch (JsonSyntaxException e) {
						Log.e(TAG, "解析消息异常：："+e);
						e.printStackTrace();
					}
				}
				
			}
			
		} catch (JSONException e1) {
			Log.e(TAG, "解析消息异常：："+e1);
			e1.printStackTrace();
		}
        
    }

    /**
     * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
     * 
     * @param context
     *            上下文
     * @param title
     *            推送的通知的标题
     * @param description
     *            推送的通知的描述
     * @param customContentString
     *            自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
            String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!TextUtils.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param successTags
     *            设置成功的tag
     * @param failTags
     *            设置失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param successTags
     *            成功删除的tag
     * @param failTags
     *            删除失败的tag
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
            List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示列举tag成功；非0表示失败。
     * @param tags
     *            当前应用设置的所有tag。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
            String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     * 
     * @param context
     *            上下文
     * @param errorCode
     *            错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId
     *            分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        // 解绑定成功，设置未绑定flag，
        if (errorCode == 0) {
            PushUtils.setBind(context, false);
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {
        Log.d(TAG, "updateContent");
        String logText = "" + PushUtils.logStringCache;

        if (!logText.equals("")) {
            logText += "\n";
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
        logText += sDateFormat.format(new Date()) + ": ";
        logText += content;

        PushUtils.logStringCache = logText;
        
        Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(PushUtils.EXTRA_MESSAGE, content);
        intent.putExtra("pushUserId", pushUserId);
        intent.putExtra("pushChannelId", pushChannelId);
        context.getApplicationContext().startActivity(intent);
    }
    
    
    private void receivePushContent(Context context, String content) {
        Intent intent = new Intent();
        Long unReadCount = 0L;
        if(AppConfig.isTopActivity(context,ChatActivity.class.getName())){
        	 Log.d(TAG, "ChatActivity.class.getName()包名-----------"+ChatActivity.class.getName());
        	 intent.setClass(context.getApplicationContext(), ChatActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             intent.putExtra(PushUtils.EXTRA_MESSAGE, content);
             context.getApplicationContext().startActivity(intent);
        }
        else{
	    	Gson gson = new Gson();
	        ChatMsgDTO c = gson.fromJson(content, new TypeToken<ChatMsgDTO>() {}.getType());
			
		    ChatMsgEntity entity = new ChatMsgEntity();
			entity.setCreateTime(c.getCreateTime());
			entity.setFromUserName(c.getFromUserName());
			entity.setToUserName(c.getToUserName());
			entity.setMsgText(c.getContent());
			entity.setFromUserId(c.getFromUserId());
			entity.setToUserId(c.getToUserId());
			entity.setMsgType(c.getMsgType());
			
			ArrayList<ChatMsgEntity> list = new  ArrayList<ChatMsgEntity>();
        	list.add(entity);
        	ChatMsgDBAdapter cDBAdapter = new ChatMsgDBAdapter();
    		try {
    			cDBAdapter.open();
    			cDBAdapter.addChatMsgEntity(list);
    			unReadCount = cDBAdapter.getChatMsgUnReadCount();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PushUtils.EXTRA_MESSAGE, content);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		finally{
    			cDBAdapter.close();
    		}
    		
    		if(AppConfig.isTopActivity(context,MainActivity.class.getName())){
    			 intent.putExtra(PushUtils.UNREAD_COUNT, unReadCount);
    			 intent.setAction(PushUtils.ACTION_UNREAD_COUNT);
    			 context.sendOrderedBroadcast(intent, null);
    		}
    		
        }
       
    }
    
    
    private void receivePushNoticeContent(Context context, String contentmsg) {
    	Gson gson = new Gson();
    	Notice notice = gson.fromJson(contentmsg, new TypeToken<Notice>() {}.getType());
    	
		ArrayList<Notice> alist = new ArrayList<Notice>();
		alist.add(notice);
		NoticeDBAdapter dbAdapter = new NoticeDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addServiceNotice(alist);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
    	
    	
    	String content = "您收到" +1 + "条公告："+notice.getContent();

		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = org.tang.exam.R.drawable.notification_icon;
		notification.tickerText = "收到新消息";
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;

		Intent contentIntent = new Intent(context, NoticeActivity.class);
		if(String.valueOf(NoticeType.SYSTEM).equals(notice.getType())){
			contentIntent.setAction(PushUtils.ACTION_NOTIFICATION_ENTRY);
		}
		else{
			contentIntent.setAction(PushUtils.ACTION_NOTIFICATION_ORG_ENTRY);
		}
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0);

		notification.contentIntent = pendingIntent;
		notification.contentView = new RemoteViews(context.getPackageName(),
				R.layout.notification_custom_builder);
		notification.contentView.setImageViewResource(R.id.notification_icon, R.drawable.icon);
		notification.contentView.setTextViewText(R.id.notification_title, "移动考勤");
		notification.contentView.setTextViewText(R.id.notification_content, content);
		notification.contentView.setTextViewText(R.id.notification_time, DateTimeUtil.getShortTime());

		manager.notify(AppConstant.INTERNAL_MESSAGE_ID, notification);
		
    }
    
    
}
