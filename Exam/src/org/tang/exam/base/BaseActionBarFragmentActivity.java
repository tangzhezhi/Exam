package org.tang.exam.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class BaseActionBarFragmentActivity extends ActionBarActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(BaseActionBarFragmentActivity.this);  
        Thread.setDefaultUncaughtExceptionHandler(new MyCustomExceptionHandler(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
