package com.zylbaby.app.ui;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.ImgLoader;
import com.zylbaby.app.util.NetWorkHelper;
import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.util.UpdateManager;

/**
 * 主页显示菜单栏布局界面
 * @author Micheal zhu 朱育梁 （zhuyuliang0@126.com）
 * @version 1.0
 * @created 2013-7-30
 */
public class MainActivity extends FragmentActivity implements OnClickListener {
	
	//菜单标示
	public static String TAB_TAG_HOME = "home";
	public static String TAB_TAG_WANDR = "wandr";
	public static String TAB_TAG_INFO = "info";
	public static String TAB_TAG_LIFT = "lift";
	public static String TAB_TAG_MORE = "more";
	
	//字体颜色
	static final int COLOR1 = Color.parseColor("#000000");
	static final int COLOR2 = Color.parseColor("#33b5e5");
	
	//图片图标
	ImageView mBut1, mBut2, mBut3, mBut4;
	//菜单文字
	TextView mCateText1, mCateText2, mCateText3, mCateText4;
	LinearLayout channel1;
	LinearLayout channel2;
	LinearLayout channel3;
	LinearLayout channel4;
	
	//菜单Fragment——Intent
	WandrFragment mWandrfragment;
	InfoFragment  mInfofragment;
	LifeFragment  mLifefragment;
	MoreFragment  mMorefragment;
	
	//默认选择菜单标示
	int mCurTabId = R.id.channel1;

	// Animation
	private Animation left_in, left_out;
	private Animation right_in, right_out;
	
	FrameLayout content_frame;
	public  FragmentManager mFragMgr;
    public  FragmentTransaction trans;
    Fragment currentFragment = null;
    
    // 退出程序按键时间标示
  	private long exitTime =0;
  	
  	private int tag = 0;
  	
  	// 定义一个ProgressDialog
 	private ProgressDialog pd;
 	// 设置标记
 	final int SIGN = 0x11;
 	
 	protected ImageLoader imageLoader = ImageLoader.getInstance();
 	//DisplayImageOptions options;
 	private ImgLoader imgloader = new ImgLoader();
 	
 	private ImageView BabyImage;
 	private TextView titleText;
 	
 	private ImageView ShowDate;
 	private TextView showdatetxt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);
		
		//网络连接判断
        if(!NetWorkHelper.isNetworkAvailable(MainActivity.this)){
        	Toast.makeText(MainActivity.this, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
        }
        
		prepareAnim();
		prepareIntent();
		prepareView();
		prepareFragment();
		
		LoadBabyInfo();
//		prepareActionbar();
//		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.ic_launcher)
//		.showImageForEmptyUri(R.drawable.ic_launcher)
//		.showImageOnFail(R.drawable.ic_launcher)
//		.cacheInMemory()
//		.cacheOnDisc()
//		.displayer(new RoundedBitmapDisplayer(20))
//		.build();
		
	}
	
	public String GetDateTxt(){
		String s=(String) showdatetxt.getText();
		if(s.isEmpty() ||s.equals("")){
			s=StringUtils.GetDate();
		}
		return s; 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//加载作息列表
		String s=StringUtils.GetDate();
		s=data.getStringExtra("selectdate");
		try {
			if(data!=null){
			s=data.getStringExtra("selectdate");
			}
			
			showdatetxt.setText(s);
			if(currentFragment.equals(mWandrfragment)){
			mWandrfragment.LoadData(0, s);
			}
			if(currentFragment.equals(mLifefragment)){
			mLifefragment.LoadData(0, s);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
//		int index = requestCode>>16;
//		if (index != 0) {
//		index--;
//		if (mFragMgr.mActive == null || index < 0 || index >= mFragments.mActive.size()) {
//			//Log.w(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
//			
//			return;
//		}
//		
//		Fragment frag = mFragMgr.mActive.get(index);
//		if (frag == null) {
//		//Log.w(TAG, "Activity result no fragment exists for index: 0x"+ Integer.toHexString(requestCode));
//		
//		}
//		frag.onActivityResult(requestCode&0xffff, resultCode, data);
//		return;
//		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void prepareAnim() {
		left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
		left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);
		right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
		right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
	}

	private void prepareView() {
		mBut1 = (ImageView) findViewById(R.id.imageView1);
		mBut2 = (ImageView) findViewById(R.id.imageView2);
		mBut3 = (ImageView) findViewById(R.id.imageView3);
		mBut4 = (ImageView) findViewById(R.id.imageView4);
		findViewById(R.id.channel1).setOnClickListener(this);
		findViewById(R.id.channel2).setOnClickListener(this);
		findViewById(R.id.channel3).setOnClickListener(this);
		findViewById(R.id.channel4).setOnClickListener(this);
		mCateText1 = (TextView) findViewById(R.id.textView1);
		mCateText2 = (TextView) findViewById(R.id.textView2);
		mCateText3 = (TextView) findViewById(R.id.textView3);
		mCateText4 = (TextView) findViewById(R.id.textView4);
		channel1 = (LinearLayout) findViewById(R.id.channel1);
		channel2 = (LinearLayout) findViewById(R.id.channel2);
		channel3 = (LinearLayout) findViewById(R.id.channel3);
		channel4 = (LinearLayout) findViewById(R.id.channel4);
		content_frame = (FrameLayout)  findViewById(R.id.content_frame);
		BabyImage = (ImageView) findViewById(R.id.BabyImage);
		titleText = (TextView) findViewById(R.id.titleText);
		ShowDate=(ImageView) findViewById(R.id.show_date);
		showdatetxt=(TextView) findViewById(R.id.show_datetxt);
		
		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");  
        Date  curDate= new Date(System.currentTimeMillis());//获取当前时间 
        String str=formatter.format(curDate);  
		
		showdatetxt.setText(str);
		ShowDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this ,ShowCalendar.class);
				intent.putExtra("tag", "2");
				startActivityForResult(intent, 1234);
				
			}
		});
	}

	private void prepareIntent() {
		mWandrfragment = new WandrFragment();
		mInfofragment  = new InfoFragment();
		mLifefragment  = new LifeFragment();
		mMorefragment  = new MoreFragment();
	}
	
	private void prepareFragment(){
		mFragMgr = getSupportFragmentManager();
        trans = mFragMgr.beginTransaction(); 
        currentFragment = mWandrfragment;
        trans.add(R.id.content_frame,mWandrfragment,TAB_TAG_HOME);
        trans.addToBackStack(TAB_TAG_HOME);
        trans.commit(); 
	}
	
	
		
	private void prepareActionbar(BabyBean babybean){
		
		titleText.setText("宝贝:"+babybean.getBnickname());
		// 家长儿童头像图标，也可应用图标
		Drawable cacheImage = imgloader.loadDrawable(babybean.getImageurlfile(),
				new ImgLoader.ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						// TODO Auto-generated method stub
						BabyImage.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			BabyImage.setImageDrawable(cacheImage);
		}
		BabyImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "账户操作", Toast.LENGTH_LONG).show();
				//--------切换到更多设置-----------
//				switchContent(mMorefragment,true);
//				channel4.setBackgroundResource(R.drawable.main_down_transparent_black);
//				mBut4.setImageResource(R.drawable.main_more);
//				mCateText4.setTextColor(COLOR2);
//				tag = 3;
				MainActivity.this.onClick(channel4);
			}
		});
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-exitTime) >2000){
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}else {
				//退出
				AppManager.getInstance().AppExit(MainActivity.this);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {

		if (mCurTabId == v.getId()) {
			return;
		}
		mBut1.setImageResource(R.drawable.main_wandr_1);
		mBut2.setImageResource(R.drawable.main_info_1);
		mBut3.setImageResource(R.drawable.main_life_1);
		mBut4.setImageResource(R.drawable.main_more_1);
		channel1.setBackgroundResource(R.drawable.transparent);
		channel2.setBackgroundResource(R.drawable.transparent);
		channel3.setBackgroundResource(R.drawable.transparent);
		channel4.setBackgroundResource(R.drawable.transparent);
		mCateText1.setTextColor(COLOR1);
		mCateText2.setTextColor(COLOR1);
		mCateText3.setTextColor(COLOR1);
		mCateText4.setTextColor(COLOR1);
		int checkedId = v.getId();
		final boolean o;
		if (mCurTabId < checkedId)
			o = true;
		else
			o = false;
		switch (checkedId) {
		case R.id.channel1:
			switchContent(mWandrfragment,o);
			mBut1.setImageResource(R.drawable.main_wandr_2);
			channel1.setBackgroundResource(R.drawable.main_down_transparent_black);
			mCateText1.setTextColor(COLOR2);
			tag = 0;
			break;
		case R.id.channel2:
			switchContent(mInfofragment,o);
			channel2.setBackgroundResource(R.drawable.main_down_transparent_black);
			mBut2.setImageResource(R.drawable.main_info_2);
			mCateText2.setTextColor(COLOR2);
			tag = 1;
			break;
		case R.id.channel3:
			switchContent(mLifefragment,o);
			channel3.setBackgroundResource(R.drawable.main_down_transparent_black);
			mBut3.setImageResource(R.drawable.main_life_2);
			mCateText3.setTextColor(COLOR2);
			tag = 2;
			break;
		case R.id.channel4:
			switchContent(mMorefragment,o);
			channel4.setBackgroundResource(R.drawable.main_down_transparent_black);
			mBut4.setImageResource(R.drawable.main_more_2);
			mCateText4.setTextColor(COLOR2);
			tag = 3;
			break;
		default:
			break;
		}
		mCurTabId = checkedId;
	}
	
	// 切换视图
	public void switchContent(Fragment fragment,Boolean o) {
		currentFragment = fragment;
		FragmentTransaction trat = getSupportFragmentManager().beginTransaction();
		if(o){
			trat.setCustomAnimations(R.anim.left_in,R.anim.left_out); 
		}else{
			trat.setCustomAnimations(R.anim.right_in,R.anim.right_out); 
		}
		trat.replace(R.id.content_frame, fragment).commit();
	}
	
	public void LoadBabyInfo(){
		
		showDialog(SIGN);
		pd.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				boolean onkey = false;
				int keyCode1 = event.getKeyCode();
				if (keyCode1 == KeyEvent.KEYCODE_BACK
						&& event.getAction() != KeyEvent.ACTION_UP) {
					if (event.getRepeatCount() == 0) {
						pd.dismiss();
						pd.cancel();
						onkey = true;
					}
				}
				return onkey;
			}
		});
		
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					BabyBean babybean = (BabyBean) msg.obj;
					pd.dismiss();
					if (babybean != null) {
						AppContext ac = (AppContext) getApplication();
						//保存baby信息到Shared
						ac.saveBabyInfo(babybean);
						
						//显示在Actionbar上
						prepareActionbar(babybean);
						
					}
				} else if (msg.what == 0) {
					pd.dismiss();
					UIHelper.ToastMessage(MainActivity.this,msg.obj+"");
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(MainActivity.this);
				}
			}
		};
		new Thread(){
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext) getApplication();
					BabyBean babybean	 = ac.getBabyInfo();
					String res = babybean.getValidate();
					if (res.equals("200")) {
						msg.what = 1;// 成功
						msg.obj = babybean;
					} else {
						msg.what = 0;// 失败
						msg.obj = babybean.getBbeizhu2();
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	// 加载界面
	// 调用onPrepareDialog()方法之后
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		pd.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
		pd.setCancelable(false);
		return pd;
	}

	// 调用showDialog()之后和调用onCreateDialog之前
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		switch (id) {
		case SIGN:
			break;
		}
	}

}