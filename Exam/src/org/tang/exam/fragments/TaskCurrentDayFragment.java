package org.tang.exam.fragments;

import java.util.ArrayList;
import java.util.UUID;

import org.tang.exam.R;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.TaskCurrentDayDBAdapter;
import org.tang.exam.entity.TaskCurrentDay;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.utils.DateTimeUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TaskCurrentDayFragment  extends Fragment {
	private static final String TAG = "TaskCurrentDayFragment";
	private View mView;
	private String selectDate;
	private EditText etTitle;
	private Button btnSubmit;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.menu, menu);
//         MenuItem menuItem = menu.findItem(R.id.action_menu);  
//         menuItem.setTitle("写周报");
     }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:
 			getActivity().finish();
 			break;
		}
		return true;
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_post_task_currentday, container, false);
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
		
		etTitle = (EditText) mView.findViewById(R.id.et_task_current_day_title);
		btnSubmit = (Button) mView.findViewById(R.id.btn_current_day_add);
		
		UserCache userCache = UserCache.getInstance();
		final UserInfo userInfo = userCache.getUserInfo();
		
		btnSubmit.setOnClickListener(new OnClickListener() {  
	          @Override  
	          public void onClick(View v) {  
	      		TaskCurrentDayDBAdapter dbAdapter = new TaskCurrentDayDBAdapter();
	    		try {
	    			ArrayList<TaskCurrentDay> list = new ArrayList<TaskCurrentDay>();
	    			
	    			TaskCurrentDay td = new TaskCurrentDay();
	    			td.setId(UUID.randomUUID().toString());
	    			td.setAuthorId(userInfo.getUserId());
	    			td.setAuthorName(userInfo.getUserName());
	    			td.setCreateDate(DateTimeUtil.getYmd());
	    			td.setCreateTime(DateTimeUtil.getCompactTime());
	    			td.setOrgId(userInfo.getOrgId());
	    			td.setRemindDate(selectDate);
	    			td.setRemindTime(DateTimeUtil.getShortTime());
	    			td.setState("0");
	    			td.setType("1");
	    			td.setTitle(etTitle.getText().toString());
	    			list.add(td);
	    			dbAdapter.open();
	    			dbAdapter.addTaskCurrentDay(list);
	    			Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
	    		} catch (Exception e) {
	    			Log.e(TAG, "Failed to operate database: " + e);
	    			Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
	    		} finally {
	    			dbAdapter.close();
	    		}
	          }

        } );
		
		
	}
	

	public static TaskCurrentDayFragment newInstance() {
		TaskCurrentDayFragment newFragment = new TaskCurrentDayFragment();
		return newFragment;
	}

	
}
