package org.tang.exam.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.tang.exam.R;
import org.tang.exam.adapter.CommonPagerAdapter;
import org.tang.exam.base.BaseActionBarFragmentActivity;
import org.tang.exam.base.MyApplication;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.fragments.ContactFragment;
import org.tang.exam.fragments.IndexFragment;
import org.tang.exam.rest.RequestController;
import org.tang.exam.utils.PushUtils;
import org.tang.exam.view.BadgeView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends BaseActionBarFragmentActivity implements OnPageChangeListener {
	private static final String TAG = "MainActivity";
	private ArrayList<Fragment> mFragmentsList;
	private CommonPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private BadgeView mBadge = null;
	private TextView mMessageItem = null;
	private RelativeLayout mIndexView = null;
	private RelativeLayout mContactView = null;
	private static String pushUserId="";
	private static String pushChannelId="";
	private RefreshUnreadReceiver mReceiver = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabWidget();
		initPagerView();
		initPushService();
		handleIntent(getIntent());
		registerMyReceiver();
		Log.d(TAG, "onCreate：：");
	}

	private void initPushService() {
		// Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
		// 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this, "api_key")
		// 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
			Log.d("YYY", "before start work at " + Calendar.getInstance().getTimeInMillis());
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY, 
					PushUtils.getMetaValue(MainActivity.this, "api_key"));
			Log.d("YYY", "after start work at " + Calendar.getInstance().getTimeInMillis());
			// Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
//			PushManager.enableLbs(getApplicationContext());
			SharedPreferences preferences = getSharedPreferences(PushUtils.PREFERENCES_NAME, Activity.MODE_PRIVATE);
			pushUserId = preferences.getString("pushUserId", "");
			pushChannelId = preferences.getString("pushChannelId", "");
			
	}

	@Override
	protected void onNewIntent(Intent intent) {
//		if(intent!=null && intent.getExtras()!=null && intent.getExtras().getSerializable(PushUtils.EXTRA_MESSAGE)!=null){
//			Log.d(TAG, "onNewIntent::"+(String) intent.getExtras().getSerializable(PushUtils.EXTRA_MESSAGE));
//			 pushUserId = (String)intent.getExtras().getSerializable("pushUserId"); 
//			 pushChannelId = intent.getStringExtra("pushChannelId");
//			 if(!PushUtils.hasBind(MainActivity.this)){
//				 handleIntent(intent);
//			 }
//		}
		 if(!PushUtils.hasBind(MainActivity.this)){
			 handleIntent(intent);
		 }
	}
	
	
	private void handleIntent(Intent intent) {
		
		Log.d(TAG, "推送绑定状态：："+PushUtils.hasBind(MainActivity.this));
		
		if(PushUtils.hasBind(MainActivity.this)){
			onBindSuccess(pushUserId, pushChannelId);
		}
		else{
			GetPushBindTask dTask = new GetPushBindTask();  
            dTask.execute(10000L);  
            Log.d(TAG, "GetPushBindTask：：");
		}
	}
	
	
	 class GetPushBindTask extends AsyncTask<Long, Void, String>{  
	        //后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型  
	        @Override  
	        protected void onPreExecute() {  
	            //第一个执行方法  
	            super.onPreExecute();  
	        }  
	          
	        @Override  
	        protected String doInBackground(Long... params) {  
	        	try {
					Thread.sleep(params[0]);
					PushManager.startWork(getApplicationContext(),
							PushConstants.LOGIN_TYPE_API_KEY, 
							PushUtils.getMetaValue(MainActivity.this, "api_key"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}  
	            return "";  
	        }  
	  
	        @Override  
	        protected void onProgressUpdate(Void... progress) {  
	            super.onProgressUpdate(progress);  
	        }  
	  
	        @Override  
	        protected void onPostExecute(String result) {  
	            super.onPostExecute(result);  
	        }  
	          
	    }  
	
	
	
	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new RefreshUnreadReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.setPriority(500);
			intentFilter.addAction(PushUtils.ACTION_UNREAD_COUNT);
			intentFilter.addAction(PushUtils.ACTION_READ_COUNT);
			registerReceiver(mReceiver, intentFilter);
		}
	}
	
	private void onBindSuccess(String pushUserId, String pushChannelId) {
		Log.d(TAG, "[onBindSuccess] pushUserId: " + pushUserId);
		Log.d(TAG, "[onBindSuccess] pushChannelId: " + pushChannelId);
		
		RequestParams params = new RequestParams();
		params.put("userId", UserCache.getInstance().getUserInfo().getUserId());
		params.put("pushUserId", pushUserId);
		params.put("pushChannelId", pushChannelId);
		params.put("deviceType", "android");
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.post(AppConstant.BASE_URL + "mobile/addPushInfo", 
				params, new AsyncHttpResponseHandler(){
		    
		    @Override
		    public void onFailure(Throwable error, String content) {
		        super.onFailure(error, content);
		        Toast.makeText(MainActivity.this, "上传推送信息失败！"+content, Toast.LENGTH_LONG).show();
		    }
		    
		    @Override
		    public void onSuccess(int statusCode, String content) {
		    	Log.d(TAG, content);
		        super.onSuccess(statusCode, content);
		    }
		});
		
		
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
	}

	@Override
	public void onStart() {
		super.onStart();
		PushManager.activityStarted(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		PushManager.activityStoped(this);
	}
	
	@Override
	public void onDestroy() {
		PushUtils.setLogText(getApplicationContext(), PushUtils.logStringCache);
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {  
            // return true;//返回真表示返回键被屏蔽掉  
            creatDialog();// 创建弹出的Dialog  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
	
	 /** 
     * 弹出提示退出对话框 
     */  
    private void creatDialog() {  
        new AlertDialog.Builder(this)  
                .setMessage("确定退出?")  
                .setPositiveButton("是",  
                        new DialogInterface.OnClickListener() {  
                            @Override  
                            public void onClick(DialogInterface dialog,  
                                    int which) {  
                            	MyApplication.getInstance().exit(); 
                            }  
                        })  
                .setNegativeButton("否", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                    }  
                }).show();  
    }  
	

	private void initTabWidget() {
		mIndexView = (RelativeLayout) findViewById(R.id.bottombar_index);
		mContactView = (RelativeLayout) findViewById(R.id.bottombar_contact);
		
		mMessageItem = (TextView) findViewById(R.id.tab_item_contact);
		mBadge = new BadgeView(this, mMessageItem);
		mBadge.setBadgeMargin(0);
		mIndexView.setOnClickListener(new TabOnClickListener(0));
		mContactView.setOnClickListener(new TabOnClickListener(1));
	}

	private void initPagerView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_container);
		mFragmentsList = new ArrayList<Fragment>();
		Fragment indexFrag = IndexFragment.newInstance();
		Fragment contactFrag = ContactFragment.newInstance();
		
		mFragmentsList.add(indexFrag);
		mFragmentsList.add(contactFrag);
		mAdapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragmentsList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		setCurrentPage(0);
	}

	final class TabOnClickListener implements OnClickListener {
		private int mIndex = 0;

		public TabOnClickListener(int index) {
			mIndex = index;
		}

		@Override
		public void onClick(View v) {
			setCurrentPage(mIndex);
		}
	}

	private void setCurrentPage(int index) {
		mViewPager.setCurrentItem(index);
		switch (index) {
		case 0:
			selectIndex();
			break;
		case 1:
			selectContact();
			break;
		}
	}

	private void selectIndex() {
		mIndexView.setSelected(true);
		mContactView.setSelected(false);
	}
	
	private void selectContact() {
		mIndexView.setSelected(false);
		mContactView.setSelected(true);
		mBadge.hide();
	}



	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentPage(arg0);
	}
	
	
	
	final class RefreshUnreadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "[RefreshUnreadReceiver] Receive intent: \r\n" + intent);
			
			if(PushUtils.ACTION_UNREAD_COUNT.equals(intent.getAction())){
				Long unreadCount = intent.getLongExtra(PushUtils.UNREAD_COUNT, 0);
				
				if (mBadge != null) {
					if (unreadCount > 0) {
						Log.d(TAG, "未读消息数量：："+unreadCount);
						mBadge.setText(String.valueOf(unreadCount));
						mBadge.show();
					} else {
						mBadge.hide();
					}
				}
			}
			else if(PushUtils.ACTION_READ_COUNT.equals(intent.getAction())){
				String readCount = intent.getStringExtra(PushUtils.READ_COUNT);
				if(readCount==null || ("").equals(readCount)){
					readCount = "0";
				}
				
				Long readCountTemp = Long.valueOf(readCount);
				if (mBadge != null) {
					if (readCountTemp > 0) {
						Log.d(TAG, "已读消息数量：："+readCount);
						Long unread = Long.valueOf(mBadge.getText().toString());
						Long unreadTemp = unread - readCountTemp;
						String unreadT = String.valueOf(unreadTemp);
						if(unreadTemp==0L){
							mBadge.hide();
						}
						else{
							mBadge.setText(unreadT);
							mBadge.show();
						}
					} else {
						mBadge.hide();
					}
				}
				abortBroadcast();//关闭广播
			}

		}
	}

}