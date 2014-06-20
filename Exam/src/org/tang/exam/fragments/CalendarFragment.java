/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */
package org.tang.exam.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.tang.exam.R;
import org.tang.exam.adapter.CalendarAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CalendarFragment  extends Fragment  {
	private static final String TAG = "CalendarFragment";
	
	private GestureDetector gestureDetector = null;
	
	 private GestureDetectorCompat mDetector;   
	
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView topText = null;
	private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private Bundle bd=null;//发送参数
	private Bundle bun=null;//接收参数
	private String ruzhuTime;
	private String lidianTime;
	private String state="";
	
	private View mView;
	
	
	public CalendarFragment() {
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
		setHasOptionsMenu(true);
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		 menu.add(0, menu.FIRST, menu.FIRST, "今天");
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.day_task, menu);
     }
	 
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
         Log.d(TAG, "点击了。。。"+item.getItemId());
		switch (item.getItemId()) {
		case android.R.id.home:
 			getActivity().finish();
 			break;
	    case Menu.FIRST:
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
        	calV = new CalendarAdapter(getActivity(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	        gridView.setAdapter(calV);
	        addTextToTopTextView(topText);
	        gvFlag++;
        	break;
		}
		return true;
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.calendar, container, false);
		return mView;
	}
	
	
	private void initView(){
		bd=new Bundle();//out
		bun=getActivity().getIntent().getExtras();//in
		
		
		  if(bun!=null&&bun.getString("state").equals("ruzhu"))
          {
          	state=bun.getString("state");
          	Log.d(TAG,"%%%%%%"+state);
          }else if(bun!=null&&bun.getString("state").equals("lidian")){
          	
          	state=bun.getString("state");
          	Log.d(TAG,"|||||||||||"+state);
          }
		
		mDetector = new GestureDetectorCompat(getActivity(),new MyOnGestureListener());  
        calV = new CalendarAdapter(getActivity(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
        addGridView();
        gridView.setAdapter(calV);
        
		topText = (TextView) getActivity().findViewById(R.id.tv_month);
		addTextToTopTextView(topText);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		initView();
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
		
		gridView =(GridView)getActivity().findViewById(R.id.gridview);

		gridView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mDetector.onTouchEvent(event);
			}
		});           
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				  //点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				  int startPosition = calV.getStartPositon();
				  int endPosition = calV.getEndPosition();
				  if(startPosition <= position+7  && position <= endPosition-7){
					  String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
					  //String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];  //这一天的阴历
	                  String scheduleYear = calV.getShowYear();
	                  String scheduleMonth = calV.getShowMonth();
//	                  Toast.makeText(CalendarActivity.this, scheduleYear+"-"+scheduleMonth+"-"+scheduleDay, 2000).show();
		              ruzhuTime=scheduleMonth+"月"+scheduleDay+"日";	                  
	                  lidianTime=scheduleMonth+"月"+scheduleDay+"日";       
	                Intent intent=new Intent();
	                if(state.equals("ruzhu"))
	                {
	                	
	                	bd.putString("ruzhu", ruzhuTime);
	                	Log.d(TAG,"ruzhuuuuuu"+bd.getString("ruzhu"));
	                }else if(state.equals("lidian")){
	                	
	                	bd.putString("lidian", lidianTime);
	                }
	                }
				  }
			
		});
	}
	
	
	
	public static CalendarFragment newInstance() {
		CalendarFragment newFragment = new CalendarFragment();
		return newFragment;
	}
	
	
	   class MyOnGestureListener extends SimpleOnGestureListener {
	        @Override
	        public boolean onSingleTapUp(MotionEvent e) {
	            return false;
	        }

	        @Override
	        public void onLongPress(MotionEvent e) {
	        }

	        @Override
	        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	            return false;
	        }

	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            
	            Log.d(TAG, "e1.getX()::"+e1.getX()+"::e2.getX()"+e2.getX()+"::::"+(e1.getX() - e2.getX()));
	            
	            
	        	int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
	    		if (e1.getX() - e2.getX() > 120) {
	                //像左滑动
	    			addGridView();   //添加一个gridView
	    			jumpMonth++;     //下一个月
	    			
	    			calV = new CalendarAdapter(getActivity(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	    	        gridView.setAdapter(calV);
	    	        addTextToTopTextView(topText);
	    	        gvFlag++;
	    	
	    			return true;
	    		} else if (e1.getX() - e2.getX() < -120) {
	                //向右滑动
	    			addGridView();   //添加一个gridView
	    			jumpMonth--;     //上一个月
	    			
	    			calV = new CalendarAdapter(getActivity(),getResources(),jumpMonth,jumpYear,year_c,month_c,day_c);
	    	        gridView.setAdapter(calV);
	    	        gvFlag++;
	    	        addTextToTopTextView(topText);

	    			return true;
	    		}
	    		return false; 
	            
	        }

	        @Override
	        public void onShowPress(MotionEvent e) {
	        }

	        @Override
	        public boolean onDown(MotionEvent e) {
	            return false;
	        }

	        @Override
	        public boolean onDoubleTap(MotionEvent e) {
	            return false;
	        }

	        @Override
	        public boolean onDoubleTapEvent(MotionEvent e) {
	            return false;
	        }

	        @Override
	        public boolean onSingleTapConfirmed(MotionEvent e) {
	            return false;
	        }
	    }

	
}
