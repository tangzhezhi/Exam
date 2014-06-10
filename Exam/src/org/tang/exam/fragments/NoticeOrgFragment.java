package org.tang.exam.fragments;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.activity.PostNoticeActivity;
import org.tang.exam.adapter.NoticeListAdapter;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.AppConstant.NoticeType;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.NoticeDBAdapter;
import org.tang.exam.entity.Notice;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.notice.QueryNoticeReq;
import org.tang.exam.rest.notice.QueryNoticeResp;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.utils.PushUtils;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;


public class NoticeOrgFragment  extends Fragment implements OnItemClickListener{
	private static final String TAG = "NoticeOrgFragment";
	private View mView;
	private NoticeListAdapter mAdapter;
	private DropDownListView lvNoticeList;
	private ArrayList<Notice> noticeList = new ArrayList<Notice>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.notice, menu);
     }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
         
		switch (item.getItemId()) {
		case R.id.action_notice:
			Intent startMain = new Intent();
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startMain.setClass(getActivity(), PostNoticeActivity.class);
			startActivity(startMain);
			break;
		case android.R.id.home:
 			getActivity().finish();
 			break;
		}
		return true;
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_notice_list, container, false);
		
		if(PushUtils.ACTION_NOTIFICATION_ORG_ENTRY.equals(getActivity().getIntent().getAction())){
			refreshServicePushNotice();
		}
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
		super.onDestroy();
	}
	
	
	private void initData() {
		noticeList.clear();
		initNoticeList();
		lvNoticeList = (DropDownListView) mView.findViewById(R.id.lv_notice_list);
		mAdapter = new NoticeListAdapter(mView.getContext(), noticeList);
		lvNoticeList.setAdapter(mAdapter);
		lvNoticeList.setOnItemClickListener(this);
		lvNoticeList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Log.d(TAG, "下拉点击");
				refreshNoticeList();
			}});
		
		mAdapter.notifyDataSetChanged();
	}
	
	
	public void refreshNoticeList() {
		UserCache userCache = UserCache.getInstance();
		QueryNoticeReq reqData = new QueryNoticeReq();
		reqData.setType(String.valueOf(NoticeType.ORG));
		reqData.setOrgId(userCache.getUserInfo().getOrgId());
		String createTime = "";
		
		if (noticeList.size() > 0) {
			createTime = noticeList.get(0).getCreateTime();
		}
		reqData.setCreateTime(createTime);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvNoticeList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvNoticeList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkResponse(String response) {
		try {
			QueryNoticeResp respData = new QueryNoticeResp(response);
			if (respData.getMsgFlag()==AppConstant.notice_query_success) {
				doSuccess(respData);
			} 
			else{
				Toast.makeText(getActivity(), "解析错误", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryNoticeResp respData) {
		noticeList.addAll(0, respData.getResponse());
		NoticeDBAdapter dbAdapter = new NoticeDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addServiceNotice(respData.getResponse());
			if(noticeList!=null && noticeList.size() > 0){
				initData();
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	private void refreshServicePushNotice(){
		initData();
	}
	
	
	
	private void initNoticeList() {
		NoticeDBAdapter dbAdapter = new NoticeDBAdapter();
		try {
			dbAdapter.open();
			noticeList.addAll(dbAdapter.getNoticeOrg());
			if(noticeList!=null && noticeList.size() > 0){
				lvNoticeList.setSecondPositionVisible();
				lvNoticeList.onDropDownComplete();
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long arg3) {
		UserCache userCache = UserCache.getInstance();
		switch (parent.getId()) {
			case R.id.lv_notice_list:
	            
			break;
		}
	}

	public static NoticeOrgFragment newInstance() {
		NoticeOrgFragment newFragment = new NoticeOrgFragment();
		return newFragment;
	}

	
}
