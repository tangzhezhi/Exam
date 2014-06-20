/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */
package org.tang.exam.fragments;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.activity.PlayVideoActivity;
import org.tang.exam.adapter.VideoListAdapter;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.db.VideoDBAdapter;
import org.tang.exam.entity.Video;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.video.QueryVideoReq;
import org.tang.exam.rest.video.QueryVideoResp;
import org.tang.exam.utils.MessageBox;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class VideoFragment  extends Fragment implements OnItemClickListener{
	private static final String TAG = "VideoFragment";
	private View mView;
	private VideoListAdapter mAdapter;
	private DropDownListView lvVideoList;
	private ArrayList<Video> videoList = new ArrayList<Video>();
	
	private int pageSize = 10;
	private int pageNo = 0;
	private int totalPage = 1;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
         inflater.inflate(R.menu.video, menu);
         
         MenuItem searchItem = menu.findItem(R.id.action_video_search);  
         SearchView sv = (SearchView) MenuItemCompat.getActionView(searchItem);  
         sv.setQueryHint(getString(R.string.query_video_name));  
         sv.setIconifiedByDefault(true);  
         sv.setSubmitButtonEnabled(true);
         sv.setOnQueryTextListener(oQueryTextListener);  
     }
	
	 OnQueryTextListener oQueryTextListener = new OnQueryTextListener() {  
		    
		  @Override  
		  public boolean onQueryTextSubmit(String query) {  
		   Log.d(TAG,"onQueryTextSubmit is:"+query);  
			  if(query.length() > 1){
				  queryVideoByName(query);
				  
				  totalPage = 1;
				  pageNo = 0;
						  
				  
			  }
			  else {
				  refreshVideoList();
			  }
		   return true;  
		  }  
		    
		  @Override  
		  public boolean onQueryTextChange(String newText) {  
			  Log.d(TAG,"query string is:"+newText); 
			  if(newText.length() > 1){
				  queryVideoByName(newText);
				  totalPage = 1;
				  pageNo = 0;
			  }
			  else {
				  refreshVideoList();
			  }
			  
		   return true;  
		  }  
	 };  
	 
	 
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 super.onOptionsItemSelected(item);
         Log.d(TAG, "点击了。。。"+item.getItemId());
		switch (item.getItemId()) {
		case android.R.id.home:
 			getActivity().finish();
 			break;
		}
		return true;
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_video_list, container, false);
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
		videoList.clear();
		lvVideoList = (DropDownListView) mView.findViewById(R.id.lv_video_list);
		mAdapter = new VideoListAdapter(mView.getContext(), videoList);
		lvVideoList.setOnItemClickListener(this);
		lvVideoList.setHeaderDefaultText("加载下一页");
		lvVideoList.setFooterDefaultText("加载上一页");
		lvVideoList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Log.d(TAG, "下拉点击");
				refreshVideoList();
			}});
		
		lvVideoList.setOnBottomListener(new OnClickListener() {
			 
			@Override
            public void onClick(View v) {
            	Log.d(TAG, "下拉底部");
            	refreshVideoListDown();
            }
        });
		
		lvVideoList.setAdapter(mAdapter);
		
		refreshVideoList();
	}
	
	
	
	private void queryVideoByName(String name){
		QueryVideoReq reqData = new QueryVideoReq();
			reqData.setPage("1");
			reqData.setRows("10");
			reqData.setVideoname(name);
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
							MessageBox.showServerError(mView.getContext());
						}
					});

			RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	
	
	private void refreshVideoList() {
		UserCache userCache = UserCache.getInstance();
		QueryVideoReq reqData = new QueryVideoReq();
		
		if(pageNo < totalPage){
			String page = String.valueOf(pageNo+1);
			String rows = String.valueOf(pageSize);
			
			reqData.setPage(page);
			reqData.setRows(rows);
			
			String createTime = "0";
			
			if (videoList.size() > 0) {
				createTime = videoList.get(0).getCreatetime();
				reqData.setCreatetime(createTime);
			}

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
							MessageBox.showServerError(mView.getContext());
						}
					});

			RequestController.getInstance().addToRequestQueue(req, TAG);
		}
		else{
			lvVideoList.onDropDownComplete();
			Toast.makeText(getActivity(), "已经到最后一页", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	public void refreshVideoListDown() {
		UserCache userCache = UserCache.getInstance();
		QueryVideoReq reqData = new QueryVideoReq();
		
		if(pageNo > 1){
			String page = String.valueOf(pageNo-1);
			String rows = String.valueOf(pageSize);
			
			reqData.setPage(page);
			reqData.setRows(rows);
			
			String createTime = "0";
			
			if (videoList.size() > 0) {
				createTime = videoList.get(0).getCreatetime();
				reqData.setCreatetime(createTime);
			}

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
							MessageBox.showServerError(mView.getContext());
						}
					});

			RequestController.getInstance().addToRequestQueue(req, TAG);
		}
		else{
			lvVideoList.onDropDownComplete();
			Toast.makeText(getActivity(), "已经到第一页", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	private void checkResponse(String response) {
		try {
			QueryVideoResp respData = new QueryVideoResp(response);
			if (respData.getMsgFlag()==AppConstant.video_query_success) {
				pageSize = respData.getPageSize();
				pageNo = respData.getPageNo();
				totalPage = respData.getTotalPage();
				
				if(null!=respData.getResponse()&&!("").equals(respData)){
					doSuccess(respData);
				}
				
			} 
			else{
				Toast.makeText(getActivity(), "解析错误", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			MessageBox.showParserError(mView.getContext());
			Log.e(TAG, "Failed to parser response data! \r\n" + e);
		}
	}
	
	private void doSuccess(QueryVideoResp respData) {
		Log.v(TAG, "respData: " + respData);
		videoList.clear();
		videoList.addAll(respData.getResponse());
		Log.v(TAG, "videoList: " + videoList.size());
		mAdapter.notifyDataSetChanged();
		lvVideoList.setSecondPositionVisible();
		lvVideoList.onDropDownComplete();
		Log.v(TAG, "lvVideoList: " + lvVideoList.getCount());
	}
	
	private void refreshServicePushVideo(){
		initData();
	}
	
	
	
	private void initVideoList() {
		VideoDBAdapter dbAdapter = new VideoDBAdapter();
		try {
			dbAdapter.open();
			videoList.addAll(dbAdapter.getVideo());
			if(videoList!=null && videoList.size() > 0){
				lvVideoList.setSecondPositionVisible();
				lvVideoList.onDropDownComplete();
				Log.d(TAG, "test");
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
			case R.id.lv_video_list:
				Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
	            Bundle bundle = new Bundle();
	            Video video = videoList.get(pos==0?0:pos-1);
//	            bundle.putString("videoname", video.getVideoName());
//	            bundle.putString("videourl", video.getVideoUrl());
//	            intent.putExtra("videoinfo",bundle);
	            intent.putExtra("videourl", video.getVideoUrl());
	            intent.putExtra("videoname", video.getVideoName());
	            getActivity().startActivity(intent);
			break;
		}
	}

	public static VideoFragment newInstance() {
		VideoFragment newFragment = new VideoFragment();
		return newFragment;
	}

	
}
