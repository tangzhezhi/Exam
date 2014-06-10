package org.tang.exam.base;

import java.util.ArrayList;

import android.app.Activity;

public class MyApplication extends BaseApplication {
	private static MyApplication mInstance = null;
	// 存放activity的集合  
    private ArrayList<Activity> list = new ArrayList<Activity>();  

	private int newsCount = 0;
	
	public int getNewsCount() {
		return newsCount;
	}

	public void plusNewsCount() {
		newsCount++;
	}
	
	public void clearNewsCount() {
		newsCount = 0;
	}

//	public static synchronized MyApplication getInstance() {
//		return mInstance;
//	}
	
	/** 
     * 利用单例模式获取MyAppalication实例 
     *  
     * @return 
     */  
    public static MyApplication getInstance() {  
        if (null == mInstance) {  
        	mInstance = new MyApplication();  
        }  
        return mInstance;  
    }  
	

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}
	
	
	 /** 
     * 添加activity到list集合 
     *  
     * @param activity 
     */  
    public void addActivity(Activity activity) {  
        list.add(activity);  
  
    }  
  
    /** 
     * 退出集合所有的activity 
     */  
    public void exit() {  
        try {  
            for (Activity activity : list) {  
                activity.finish();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            System.exit(0);  
        }  
    }  
  
    @Override  
    public void onLowMemory() {  
        super.onLowMemory();  
        System.gc();  
    }  
	
	
}
