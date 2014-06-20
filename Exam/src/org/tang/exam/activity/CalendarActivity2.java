package org.tang.exam.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.tang.exam.R;
import org.tang.exam.adapter.CalendarAdapter;
import org.tang.exam.adapter.TabsAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.fragments.CalendarFragment;
import org.tang.exam.fragments.VideoFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 日历显示activity
 * 
 *
 */
public class CalendarActivity2 extends BaseActionBarActivity  {
	private static final String TAG = "CalendarActivity2";
	TabsAdapter mTabsAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(11);
		setContentView(mViewPager);
		
		/**
		 * 工具栏--导航tab模式
		 */
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.back));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
		
		//在导航上添加上公告页面--ViewPager
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText(getString(R.string.day_calendar)), CalendarFragment.class, null);
	}
	
	/**
	 * 菜单被分为如下三种，选项菜单（OptionsMenu）、上下文菜单（ContextMenu）和子菜单（SubMenu）
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
}