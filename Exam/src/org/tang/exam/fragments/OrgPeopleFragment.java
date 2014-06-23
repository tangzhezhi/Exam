package org.tang.exam.fragments;

import java.util.ArrayList;
import java.util.Collections;

import org.tang.exam.R;
import org.tang.exam.activity.ChatActivity;
import org.tang.exam.adapter.UserListAdapter;
import org.tang.exam.adapter.UserListAdapter.ViewHolder;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.ChatMsgDBAdapter;
import org.tang.exam.db.UserInfoDBAdapter;
import org.tang.exam.entity.UserComparator;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.utils.PushUtils;
import org.tang.exam.view.BadgeView;
import org.tang.exam.view.IndexableListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class OrgPeopleFragment  extends Fragment implements OnItemClickListener{
	private static final String TAG = "OrgPeopleFragment";
	private View mView;
	private ArrayList<UserInfo> mUserList = new ArrayList<UserInfo>();
	private IndexableListView lvUserList;
	private UserListAdapter mAdapter;
	private Bundle bundle ;
	private RefreshUnreadReceiver mReceiver = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.action_exit:
//			Intent startMain = new Intent();
//			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startMain.setClass(getActivity(), MainActivity.class);
//			startActivity(startMain);
//			break;
//		}
		return false;
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_user_list, container, false);
		registerMyReceiver();
		return mView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	
    @Override
	public void onStop() {
        super.onStop();
    }
		
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (mReceiver != null) {
			getActivity().unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}
	
	private void registerMyReceiver() {
		if (mReceiver == null) {
			mReceiver = new RefreshUnreadReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.setPriority(1000);
			intentFilter.addAction(PushUtils.ACTION_UNREAD_COUNT);
			intentFilter.addAction(PushUtils.ACTION_READ_COUNT);
			getActivity().registerReceiver(mReceiver, intentFilter);
		}
	}
	
	private void initData() {
		mUserList.clear();
		initUserList();
		initUserUnReadMsgList();
		lvUserList = (IndexableListView) mView.findViewById(R.id.lv_user_list);
		mAdapter = new UserListAdapter(mView.getContext(), mUserList,bundle,UserListAdapter.NORMAL_MODE);
		lvUserList.setAdapter(mAdapter);
		lvUserList.setFastScrollEnabled(true);
		lvUserList.setTextFilterEnabled(true);
		lvUserList.setOnItemClickListener(this);
		mAdapter.notifyDataSetChanged();
	}

	private void initUserList() {
		UserInfoDBAdapter userInfoDBAdapter = new UserInfoDBAdapter();
		UserCache userCache = UserCache.getInstance();

		try {
			userInfoDBAdapter.open();
			mUserList.clear();
			
			mUserList.addAll(userInfoDBAdapter.getUserInfoInOrg(userCache.getUserInfo().getOrgId()));
			
			Collections.sort(mUserList, new UserComparator());
			
			mAdapter.updateListData(mUserList);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			userInfoDBAdapter.close();
		}
	}
	
	
	private void initUserUnReadMsgList() {
		ChatMsgDBAdapter dBAdapter = new ChatMsgDBAdapter();
		try {
			dBAdapter.open();
			ArrayList<String> userIds = new ArrayList<String>();
			for(UserInfo userInfo : mUserList){
				userIds.add(userInfo.getUserId());
			}
			bundle = dBAdapter.getAllPeopleChatMsgUnReadCount(userIds);
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dBAdapter.close();
		}
	}
	
	
	private void updateUnReadMsgCount(String fromUserId,String toUserId){
		ChatMsgDBAdapter cDBAdapter = new ChatMsgDBAdapter();
		try {
			cDBAdapter.open();
			cDBAdapter.updateUnReadMsgCount(fromUserId,toUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			cDBAdapter.close();
		}
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long arg3) {
		UserCache userCache = UserCache.getInstance();
		switch (parent.getId()) {
			case R.id.lv_user_list:
				final ViewHolder vh = (ViewHolder) v.getTag();
				final String unreadcount =vh.getTvUnReadCount().getText().toString();
				
					Intent intent = new Intent(getActivity(), ChatActivity.class);
		            Bundle bundle = new Bundle();
		            bundle.putString("toUserId", mUserList.get(pos).getUserId());
		            bundle.putString("toUserName", mUserList.get(pos).getUserName());
		            bundle.putString("toUserPicUrl", mUserList.get(pos).getPicUrl());
		            intent.putExtra("tag",bundle);
	            
				 	Intent intent2 = new Intent();
		            intent2.putExtra(PushUtils.READ_COUNT, unreadcount);
	   			 	intent2.setAction(PushUtils.ACTION_READ_COUNT);
	   			 	intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   			 	getActivity().sendOrderedBroadcast(intent2, null);
   			 	
		            updateUnReadMsgCount(mUserList.get(pos).getUserId(),userCache.getUserInfo().getUserId());
		            getActivity().startActivity(intent);
	            
			break;
		}
	}

	public static OrgPeopleFragment newInstance() {
		OrgPeopleFragment newFragment = new OrgPeopleFragment();
		return newFragment;
	}

	public void onUserFilter(String query) {
		// TODO Auto-generated method stub
		
	}
	
	
	final class RefreshUnreadReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "[RefreshUnreadReceiver] Receive intent: \r\n" + intent);
			
			if(PushUtils.ACTION_UNREAD_COUNT.equals(intent.getAction())){
				Long unReadCount = intent.getLongExtra(PushUtils.UNREAD_COUNT, 0);
				initData();
//				abortBroadcast();//关闭广播
			}
			else if(PushUtils.ACTION_UPDATE_COUNT.equals(intent.getAction())){
				initData();
				abortBroadcast();//关闭广播
			}

		}
	}
	
}
