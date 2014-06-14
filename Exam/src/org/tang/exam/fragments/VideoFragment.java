/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */
package org.tang.exam.fragments;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.activity.MapActivity;
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
import org.tang.exam.utils.PushUtils;
import org.tang.exam.view.DropDownListView;
import org.tang.exam.view.DropDownListView.OnDropDownListener;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

public class VideoFragment  extends Fragment implements OnItemClickListener{
	private static final String TAG = "VideoFragment";
	private View mView;
	private VideoListAdapter mAdapter;
	private DropDownListView lvVideoList;
	private ArrayList<Video> videoList = new ArrayList<Video>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	 @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);
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
		mView = inflater.inflate(R.layout.fragment_video_list, container, false);
		
		if(PushUtils.ACTION_NOTIFICATION_VIDEO.equals(getActivity().getIntent().getAction())){
			refreshServicePushVideo();
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
		videoList.clear();
		initVideoList();
		lvVideoList = (DropDownListView) mView.findViewById(R.id.lv_video_list);
		mAdapter = new VideoListAdapter(mView.getContext(), videoList);
		lvVideoList.setAdapter(mAdapter);
		lvVideoList.setOnItemClickListener(this);
		lvVideoList.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Log.d(TAG, "下拉点击");
				refreshVideoList();
			}});
		
		mAdapter.notifyDataSetChanged();
	}
	
	
	public void refreshVideoList() {
		UserCache userCache = UserCache.getInstance();
		QueryVideoReq reqData = new QueryVideoReq();
		
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
						lvVideoList.onDropDownComplete();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						lvVideoList.onDropDownComplete();
						MessageBox.showServerError(mView.getContext());
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}
	
	
	private void checkResponse(String response) {
		try {
			QueryVideoResp respData = new QueryVideoResp(response);
			if (respData.getMsgFlag()==AppConstant.video_query_success) {
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
	
	private void doSuccess(QueryVideoResp respData) {
		videoList.addAll(0, respData.getResponse());
		VideoDBAdapter dbAdapter = new VideoDBAdapter();
		try {
			dbAdapter.open();
			dbAdapter.addVideo(respData.getResponse());
			if(videoList!=null && videoList.size() > 0){
				initData();
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to operate database: " + e);
		} finally {
			dbAdapter.close();
		}
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
	            bundle.putString("videoname", video.getVideoName());
	            bundle.putString("videourl", video.getVideoUrl());
	            intent.putExtra("videoinfo",bundle);
	            getActivity().startActivity(intent);
			break;
		}
	}

	public static VideoFragment newInstance() {
		VideoFragment newFragment = new VideoFragment();
		return newFragment;
	}

	
}
