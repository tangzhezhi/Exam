package org.tang.exam.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import org.tang.exam.R;
import org.tang.exam.base.BaseActionBarActivity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlayVideoActivity  extends BaseActionBarActivity implements OnInfoListener, OnBufferingUpdateListener {

	  /**
	   * TODO: Set the path variable to a streaming video URL or a local media file
	   * path.
	   */
	  private String path = "http://pl.youku.com/playlist/m3u8?ts=1394676342&keyframe=0&vid=XNjU4MTc0Mjky&type=mp4";
	  private Uri uri;
	  private VideoView mVideoView;
	  private ProgressBar pb;
	  private TextView downloadRateView, loadRateView;

	  @Override
	  public void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    if (!LibsChecker.checkVitamioLibs(this))
	      return;
	    setContentView(R.layout.videobuffer);
	    
		/**
		 * 工具栏--导航tab模式
		 */
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.back));
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//显示在顶部
		bar.setDisplayHomeAsUpEnabled(true);
	    
	    mVideoView = (VideoView) findViewById(R.id.buffer);
	    pb = (ProgressBar) findViewById(R.id.probar);

	    downloadRateView = (TextView) findViewById(R.id.download_rate);
	    loadRateView = (TextView) findViewById(R.id.load_rate);
	    if (path == "") {
	      // Tell the user to provide a media file URL/path.
	      Toast.makeText(
	    		  PlayVideoActivity.this,
	          "Please edit VideoBuffer Activity, and set path"
	              + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
	      return;
	    } else {
	      /*
	       * Alternatively,for streaming media you can use
	       * mVideoView.setVideoURI(Uri.parse(URLstring));
	       */
	      uri = Uri.parse(path);
	      mVideoView.setVideoURI(uri);
	      mVideoView.setMediaController(new MediaController(this));
	      mVideoView.requestFocus();
	      mVideoView.setOnInfoListener(this);
	      mVideoView.setOnBufferingUpdateListener(this);
	      mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
	        @Override
	        public void onPrepared(MediaPlayer mediaPlayer) {
	          // optional need Vitamio 4.0
	          mediaPlayer.setPlaybackSpeed(1.0f);
	        }
	      });
	    }

	  }

	  @Override
	  public boolean onInfo(MediaPlayer mp, int what, int extra) {
	    switch (what) {
	    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
	      if (mVideoView.isPlaying()) {
	        mVideoView.pause();
	        pb.setVisibility(View.VISIBLE);
	        downloadRateView.setText("");
	        loadRateView.setText("");
	        downloadRateView.setVisibility(View.VISIBLE);
	        loadRateView.setVisibility(View.VISIBLE);

	      }
	      break;
	    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
	      mVideoView.start();
	      pb.setVisibility(View.GONE);
	      downloadRateView.setVisibility(View.GONE);
	      loadRateView.setVisibility(View.GONE);
	      break;
	    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
	      downloadRateView.setText("" + extra + "kb/s" + "  ");
	      break;
	    }
	    return true;
	  }

	  @Override
	  public void onBufferingUpdate(MediaPlayer mp, int percent) {
	    loadRateView.setText(percent + "%");
	  }
	  
	  
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			return true;
		}
	  
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);
			switch (item.getItemId()) {
			case android.R.id.home:
	 			this.finish();
	 			break;
			}
			return true;
		}

	}