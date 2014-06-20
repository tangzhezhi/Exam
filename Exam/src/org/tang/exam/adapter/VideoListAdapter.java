/*
 * Powered By tangzezhi
 * Since 2013 - 2014
 */

package org.tang.exam.adapter;

import java.util.ArrayList;

import org.tang.exam.R;
import org.tang.exam.base.MyBaseAdatper;
import org.tang.exam.entity.Video;
import org.tang.exam.rest.ImageCacheManager;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends MyBaseAdatper {
	private static final String TAG = "VideoListAdapter";
	private LayoutInflater mInflater;
	private ArrayList<Video> mVideoList;

	public VideoListAdapter(Context context, ArrayList<Video> videoList) {
		super();
		mVideoList = videoList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mVideoList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mVideoList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_video, null);
			holder = new ViewHolder();
	    	holder.tvVideoname = (TextView) convertView.findViewById(R.id.tv_videoname);
	    	holder.tvVideopic = (ImageView) convertView.findViewById(R.id.iv_video_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Video video = mVideoList.get(pos);
		if (video != null) {
	    		holder.tvVideoname.setText(video.getVideoName());
	    		ImageListener listener = ImageLoader.getImageListener(holder.tvVideopic,
						R.drawable.avatar_default_normal, R.drawable.avatar_default_normal);
	    		
	    		if(video.getVideopic()==null){
	    			video.setVideopic("");
	    		}
	    		
				try {
					Bitmap bm = ImageCacheManager.getInstance().getImageLoader().get(video.getVideopic(), listener).getBitmap();
					holder.tvVideopic.setImageBitmap(bm);
				} catch (Exception e) {
					Log.e(TAG, ""+e);
				}
		}

		return convertView;
	}

	public class ViewHolder {
		private TextView  tvVideoname;
		private ImageView  tvVideopic;
	}
}
