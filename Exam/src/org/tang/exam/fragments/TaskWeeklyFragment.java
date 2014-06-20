package org.tang.exam.fragments;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.activity.PostTaskWeeklyActivity;
import org.tang.exam.adapter.TaskWeeklyListAdapter;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.AppConstant.DayTaskStatus;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.TaskWeekly;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.day.QueryTaskWeeklyReq;
import org.tang.exam.rest.day.QueryTaskWeeklyResp;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
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


public class TaskWeeklyFragment  extends Fragment implements OnItemClickListener{
	private static final String TAG = "TaskWeeklyFragment";
	private View mView;
	private TaskWeeklyListAdapter mAdapter;
	private DropDownListView lvTaskWeeklyList;
	private ArrayList<TaskWeekly> taskWeeklyList = new ArrayList<TaskWeekly>();
	private String selectDate;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.menu, menu);
         MenuItem menuItem = menu.findItem(R.id.action_menu);  
         menuItem.setTitle("写周报");
     }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
         
		switch (item.getItemId()) {
		case R.id.action_menu:
			Intent startMain = new Intent();
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startMain.setClass(getActivity(), PostTaskWeeklyActivity.class);
			startMain.putExtra("org.tang.exam.activity.PostTaskWeeklyActivity.selectDate", selectDate);
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
		selectDate =  getActivity().getIntent().getStringExtra("org.tang.exam.activity.CalendarActivity.selectDate");
		Log.d(TAG, "selectDate:::::"+selectDate);
		if(null==selectDate||("").equals(selectDate)){
			selectDate = DateTimeUtil.getYmd();
		}
		
		taskWeeklyList.clear();
		lvTaskWeeklyList = (DropDownListView) mView.findViewById(R.id.lv_notice_list);
		mAdapter = new TaskWeeklyListAdapter(mView.getContext(), taskWeeklyList);
		lvTaskWeeklyList.setAdapter(mAdapter);
		lvTaskWeeklyList.setOnItemClickListener(this);
		lvTaskWeeklyList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Log.d(TAG, "下拉点击");
				refreshTaskWeeklyList();
			}});
		
		
		refreshTaskWeeklyList();
		
	}
	
	
	public void refreshTaskWeeklyList() {
		UserCache userCache = UserCache.getInstance();
		QueryTaskWeeklyReq reqData = new QueryTaskWeeklyReq();
		reqData.setType(String.valueOf(DayTaskStatus.task_weekly));
		reqData.setOrgId(userCache.getUserInfo().getOrgId());
		String createDate = selectDate;
		reqData.setCreateDate(createDate);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
						lvTaskWeeklyList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvTaskWeeklyList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkResponse(String response) {
		try {
			QueryTaskWeeklyResp respData = new QueryTaskWeeklyResp(response);
			if (respData.getMsgFlag()==AppConstant.taskweekly_query_success) {
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
	
	private void doSuccess(QueryTaskWeeklyResp respData) {
		Log.v(TAG, "respData: " + respData);
		taskWeeklyList.clear();
		taskWeeklyList.addAll(respData.getResponse());
		mAdapter.notifyDataSetChanged();
		lvTaskWeeklyList.setSecondPositionVisible();
		lvTaskWeeklyList.onDropDownComplete();
		
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long arg3) {
		UserCache userCache = UserCache.getInstance();
		switch (parent.getId()) {
			case R.id.lv_notice_list:
	            
			break;
		}
	}

	public static TaskWeeklyFragment newInstance() {
		TaskWeeklyFragment newFragment = new TaskWeeklyFragment();
		return newFragment;
	}

	
}
