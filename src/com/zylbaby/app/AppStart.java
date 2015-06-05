package com.zylbaby.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.ui.LoginActivity;
import com.zylbaby.app.util.UIHelper;

/**
 * 应用程序启动类：显示欢迎界面并且跳转到主界面
 * @author  Michael zhu 朱育梁 (zhuyuliang0@126.com)
 * @version 1.0
 * @created 2013-7-25
 */
public class AppStart extends Activity {

	private AppContext appContext;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 作用是将xml定义的一个布局找出来并且隐藏起来，并没有显示出来
		final View view = View.inflate(this, R.layout.start_activity, null);
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        //渐变展示启动屏
      	//窗口的动画效果，比如淡入淡出等.
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f); //fromAlpha：开始时刻的透明度，取0~1，toAlpha：结束时刻的透明度。
        aa.setDuration(3000); //设置动画持续时间
        view.setAnimation(aa);
        aa.setAnimationListener(new AnimationListener()  
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}	
		});
    }

	
	 /**
     * 跳转到...
     */
    private void redirectTo(){   
    	appContext = (AppContext) getApplication();
    	//判断用户是否登录
    	if (appContext.isLogin()) {
			/**
			 * 跳转到主页页面
			 * 
			 * @param activity
			 */
    		
    		if(appContext.getBabyData().equals("0")){
    			UIHelper.showAddBaby(AppStart.this,"0");
    		}else{
    			UIHelper.showhomepage(this);
    		}
		} else {
			UIHelper.showLogin(this);
		}
        finish();
    }
}