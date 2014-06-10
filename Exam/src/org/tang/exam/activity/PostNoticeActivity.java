package org.tang.exam.activity;

import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.tang.exam.R;
import org.tang.exam.base.BaseActionBarActivity;
import org.tang.exam.common.AppConstant;
import org.tang.exam.common.UserCache;
import org.tang.exam.entity.Notice;
import org.tang.exam.entity.UserInfo;
import org.tang.exam.rest.MyStringRequest;
import org.tang.exam.rest.RequestController;
import org.tang.exam.rest.notice.AddNoticeReq;
import org.tang.exam.utils.DateTimeUtil;
import org.tang.exam.utils.MessageBox;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 消息编辑框
 * @author lenovo
 *
 */
public class PostNoticeActivity extends BaseActionBarActivity  {
	private static final String TAG = "PostNoticeActivity";

	private ProgressDialog prgDialog = null;
	private EditText etTitle;
	private EditText etContent;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_notice);
		initView();
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.post_notice));
		bar.setDisplayHomeAsUpEnabled(true);
	}


	private void initView() {
		prgDialog = new ProgressDialog(PostNoticeActivity.this);
		etTitle = (EditText) findViewById(R.id.et_notice_title);
		etContent = (EditText) findViewById(R.id.et_notice_content);
	}

	private void showProgressDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("正在提交...");
		prgDialog.show();
	}

	private void closeProgressDialog() {
		if ((prgDialog != null) && (prgDialog.isShowing())) {
			prgDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.post_notice, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.notice_action_submit:
			showProgressDialog();
			submitNotice();
			break;
		case android.R.id.home:
			this.finish();
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		RequestController.getInstance().cancelPendingRequests(TAG);
		closeProgressDialog();
	}
	
	
	private void submitNotice(){
		UserInfo userInfo = UserCache.getInstance().getUserInfo();
		AddNoticeReq anotice = new AddNoticeReq();
		Notice notice = new Notice();
		
		notice.setId(UUID.randomUUID().toString());
		notice.setAuthorId(userInfo.getUserId());
		notice.setAuthorName(userInfo.getUserName());
		notice.setTitle(etTitle.getText().toString());
		notice.setContent(etContent.getText().toString());
		notice.setOrgId(userInfo.getOrgId());
		notice.setCreateTime(DateTimeUtil.getCompactTime());
		anotice.setNotice(notice);
		
		MyStringRequest req = new MyStringRequest(Method.POST, anotice.getAllUrl(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						closeProgressDialog() ;
						Log.v(TAG, "公告发布: " + response);
						JSONObject rootObj;
						try {
							rootObj = new JSONObject(response);
							if(rootObj.getString("sessionKey")!=null && !rootObj.getString("sessionKey").equals("")){
								if(AppConstant.notice_add_success==rootObj.getInt("msgFlag")){
									MessageBox.showMessage(PostNoticeActivity.this, "公告发布成功");
								}
								else{
									MessageBox.showMessage(PostNoticeActivity.this, "服务器异常");
								}
								
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
	
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MessageBox.showMessage(PostNoticeActivity.this, "服务器异常");
						closeProgressDialog() ;
					}
				});

		RequestController.getInstance().addToRequestQueue(req, TAG);
	}


}
