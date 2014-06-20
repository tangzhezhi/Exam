package org.tang.exam.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.tang.exam.R;
import org.tang.exam.adapter.CalendarAdapter;
import org.tang.exam.adapter.TaskCurrentDayListAdapter;
import org.tang.exam.adapter.TaskWeeklyListAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.common.AppConstant.DayTaskStatus;
import org.tang.exam.db.NoticeDBAdapter;
import org.tang.exam.db.TaskCurrentDayDBAdapter;
import org.tang.exam.entity.TaskCurrentDay;
import org.tang.exam.entity.TaskWeekly;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.day.QueryTaskWeeklyReq;
import org.tang.exam.rest.day.QueryTaskWeeklyResp;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 日历显示activity
 * 
 *
 */
public class CalendarActivity extends BaseActionBarActivity  implements OnGestureListener {
	private static final String TAG = "CalendarActivity";
	private GestureDetectorCompat gestureDetector = null;
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private String ruzhuTime;
	private String lidianTime;
	
	private ListView lvTaskWeeklyList;
	private ArrayList<TaskWeekly> taskWeeklyList = new ArrayList<TaskWeekly>();
	private TaskWeeklyListAdapter mAdapter;
	private String selectDate;
	
	private ListView lvTaskCurrentDayList;
	private ArrayList<TaskCurrentDay> taskCurrentDayList = new ArrayList<TaskCurrentDay>();
	private TaskCurrentDayListAdapter mAdapter2;
	
	public CalendarActivity() {
		Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    	currentDate = sdf.format(date);  //当期日期
    	year_c = Integer.parseInt(currentDate.split("-")[0]);
    	month_c = Integer.parseInt(currentDate.split("-")[1]);
    	day_c = Integer.parseInt(currentDate.split("-")[2]);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.back));
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
		
		gestureDetector = new GestureDetectorCompat(this,this);
        calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        addGridView();
        gridView.setAdapter(calV);
        
		topText = (TextView) findViewById(R.id.tv_month);
		addTextToTopTextView(topText);
		
		initDayTask();
	}
	
	
	private void initDayTask(){
		UserCache userCache = UserCache.getInstance();
		UserInfo userInfo = userCache.getUserInfo();
//		taskWeeklyList.clear();
//		lvTaskWeeklyList = (ListView) findViewById(R.id.lv_list);
//		mAdapter = new TaskWeeklyListAdapter(this, taskWeeklyList);
//		lvTaskWeeklyList.setAdapter(mAdapter);
//		lvTaskWeeklyList.setOnItemClickListener( new OnItemClickListener(){
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//					Log.d(TAG, "点击了"+arg2);
//			}} );
//		refreshTaskWeeklyList();
		
		if(null==selectDate||("").equals(selectDate)){
			selectDate = DateTimeUtil.getYmd();
		}
		
		TaskCurrentDayDBAdapter dbAdapter = new TaskCurrentDayDBAdapter();
		try {
			dbAdapter.open();
			taskCurrentDayList.clear();
			taskCurrentDayList.addAll(dbAdapter.getTaskCurrentDay(userInfo.getUserId(), selectDate));
			
			Log.e(TAG, "taskCurrentDayList.size: " + taskCurrentDayList.size());
			
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
		
		lvTaskCurrentDayList = (ListView) findViewById(R.id.lv_list);
		mAdapter2 = new TaskCurrentDayListAdapter(this, taskCurrentDayList);
		lvTaskCurrentDayList.setAdapter(mAdapter2);
		mAdapter2.notifyDataSetChanged();
	}
	
	
	public void refreshTaskWeeklyList() {
		UserCache userCache = UserCache.getInstance();
		QueryTaskWeeklyReq reqData = new QueryTaskWeeklyReq();
		reqData.setType(String.valueOf(DayTaskStatus.task_weekly));
		reqData.setOrgId(userCache.getUserInfo().getOrgId());
		
		if(null==selectDate||("").equals(selectDate)){
			selectDate = DateTimeUtil.getYmd();
		}
		Log.v(TAG, "selectDate: " + selectDate);
		String createDate = selectDate;
		reqData.setCreateDate(createDate);

		MyStringRequest req = new MyStringRequest(Method.GET, reqData.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.v(TAG, "Response: " + response);
						checkResponse(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.v(TAG, "error: " + error);
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
				Toast.makeText(this, "解析错误", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			MessageBox.showParserError(this);
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryTaskWeeklyResp respData) {
		Log.v(TAG, "respData: " + respData);
		taskWeeklyList.clear();
		taskWeeklyList.addAll(respData.getResponse());
		mAdapter.notifyDataSetChanged();
	}
	
	
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 120) {
            //像左滑动
			addGridView();   //添加一个gridView
			jumpMonth++;     //下一个月
			
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
	
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
			addGridView();   //添加一个gridView
			jumpMonth--;     //上一个月
			
			calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        gvFlag++;
	        addTextToTopTextView(topText);

			return true;
		}
		return false;
	}
	
	
	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		menu.getItem(0).setTitle("今天");
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, ""+item.getItemId());
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		   case R.id.action_menu:
	        	//跳转到今天
	        	int xMonth = jumpMonth;
	        	int xYear = jumpYear;
	        	int gvFlag =0;
	        	jumpMonth = 0;
	        	jumpYear = 0;
	        	addGridView();   //添加一个gridView
	        	year_c = Integer.parseInt(currentDate.split("-")[0]);
	        	month_c = Integer.parseInt(currentDate.split("-")[1]);
	        	day_c = Integer.parseInt(currentDate.split("-")[2]);
	        	calV = new CalendarAdapter(this,getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
		        gridView.setAdapter(calV);
		        addTextToTopTextView(topText);
		        gvFlag++;

	        	break;
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	//添加头部的年份 闰哪月等信息
	public void addTextToTopTextView(TextView view){
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年").append(
				calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	//添加gridview
	private void addGridView() {
		
		gridView =(GridView)findViewById(R.id.gridview);

		gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return CalendarActivity.this.gestureDetector.onTouchEvent(event);
			}
		});           
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
					Log.d(TAG, "点击了::"+position);
				
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position+7  && position <= endPosition-7){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = (Integer.valueOf(calV.getShowMonth())>10)?calV.getShowMonth():("0"+calV.getShowMonth());
	                  scheduleDay = (Integer.valueOf(scheduleDay)>10)?scheduleDay:("0"+scheduleDay);
	                  
		              ruzhuTime=scheduleMonth+"月"+scheduleDay+"日";	                  
	                  lidianTime=scheduleMonth+"月"+scheduleDay+"日";       
	                  
	                  selectDate = scheduleYear+scheduleMonth+scheduleDay;
	                  
	                  
	                  Log.d(TAG, "点击了:ruzhuTime:"+ruzhuTime);
	                  Log.d(TAG, "点击了:lidianTime:"+lidianTime);
	                  
		                Intent intent=new Intent();
		                intent.setClass(CalendarActivity.this, DayTaskActivity.class);
		                intent.putExtra("org.tang.exam.activity.CalendarActivity.selectDate", selectDate);
		                startActivity(intent);
	                }
				  }
			
		});
	}
	
}