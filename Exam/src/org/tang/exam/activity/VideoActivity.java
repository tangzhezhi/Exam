package org.tang.exam.activity;

import org.tang.exam.R;
import org.tang.exam.adapter.TabsAdapter;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.fragments.AskFeeFragment;
import org.tang.exam.fragments.NoticeOrgFragment;
import org.tang.exam.fragments.NoticeSystemFragment;
import org.tang.exam.fragments.TaskDistributeFragment;
import org.tang.exam.fragments.TaskWeeklyFragment;
import org.tang.exam.fragments.VideoFragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * 视频
 * @author lenovo
 *
 */
public class VideoActivity extends BaseActionBarActivity {
	private static final String TAG = "VideoActivity";
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
		mTabsAdapter.addTab(bar.newTab().setText(getString(R.string.video_resource)), VideoFragment.class, null);
	}
	
	/**
	 * 菜单被分为如下三种，选项菜单（OptionsMenu）、上下文菜单（ContextMenu）和子菜单（SubMenu）
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
}