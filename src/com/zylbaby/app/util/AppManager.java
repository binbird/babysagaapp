package com.zylbaby.app.util;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

/**
 * 应用程序Activity管理类：主要管理堆栈里的Activity的添加，移除和全部退出。
 * @author  Michael zhu 朱育梁 (zhuyuliang0@126.com)
 * @version 1.0
 * @created 2013-7-25
 */
public class AppManager extends Application
{
	private static AppManager instance;
	private Stack<Activity> activityStack;
	/**
	 * 单一实例
	 */
	public static AppManager getInstance() {
		if(null==instance)
		{
			instance = new AppManager();
		}
		return instance;
	}
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	/**
	 * 获取指定的Activity
	 */
	public Activity getActivity(Activity activity){
		if(activity!=null){
			for(int i=0; i<activityStack.size(); i++){
				if(activity == activityStack.get(i)){
					activity = activityStack.get(i);
				}
			}
			
		}
		return activity;
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls){
		for (Activity activity : activityStack) {
			if(activity.getClass().equals(cls) ){
				finishActivity(activity);
			}
		}
	}
	/**
	 * 结束除thisactivity以外的所有activity
	 */
	public void finishActvity(Activity thisactivity){
		for(Activity activity: activityStack){
			if(activity==null){
				continue;
			}
			if(activity==thisactivity){
				continue;
			}
			activity.finish();
		}
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {	}
	}
	
	
}
