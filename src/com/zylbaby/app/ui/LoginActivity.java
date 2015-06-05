package com.zylbaby.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.baby.common.ui.HelpListActivity;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.loveplusplus.demo.image.T;
import com.zylbaby.app.R;
import com.zylbaby.app.bean.User;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.net.ApiClient;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.MultiChoicePopWindow;
import com.zylbaby.app.view.SingleChoicePopWindow;

/**
 * 登录布局界面
 * 
 * @author Micheal zhu 朱育梁 （zhuyuliang0@126.com）
 * @version 1.0
 * @created 2014-3-6
 */
public class LoginActivity extends Activity {
	private SharedPreferences sp;
	private Editor editor;
	private TextView txt_help;
	
	private Button   titleRightButton; // 注册按键
	private EditText login_name_edit; // 用户登录名
	private EditText login_password_edit; // 用户密码
	private Button   settle_accounts; // 登录按键
	private TextView forget_password; // 忘记密码
	private Button testbtn;

	private String curLoginType = ""; // 登录类型

	// 定义一个ProgressDialog
	private ProgressDialog pd;
	// 设置标记
	final int SIGN = 0x11;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_new_activity);
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);
		
		
		
		
		//initData1();
		// 初始化
		init();
		// 加载数据
		initData();
		sp = getSharedPreferences("users", MODE_PRIVATE);
		editor=sp.edit();
		Boolean f=sp.getString("first", "").equals("yes");
		Toast.makeText(getApplicationContext(), String.valueOf(f),Toast.LENGTH_SHORT).show();
		//f=true;
		if (!f) {
			editor.putString("first", "");
			editor.commit();
			startActivity(new Intent(this, GuideActivity.class));
			return;
		}

	}

	// 初始化
	private void init() {
		titleRightButton = (Button) findViewById(R.id.btn_newuser);
		login_name_edit = (EditText) findViewById(R.id.login_name_edit);
		login_password_edit = (EditText) findViewById(R.id.login_password_edit);
		settle_accounts = (Button) findViewById(R.id.btn_login);
		forget_password = (TextView) findViewById(R.id.forget_password);
		txt_help=(TextView) findViewById(R.id.txt_help);
		txt_help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this,HelpListActivity.class);
				startActivity(intent);
				
			}
		});
	}
	/**
	 * popwindows 学生多选
	 * 
	 */
	public void showMultiChoiceWindow() {
		mMultiChoicePopWindow.show(true);

	}
	
	private final static int COUNT = 3;
	private List<String> mSingleDataList;
	private List<String> mMultiDataList;
	private View mRootView;
	private Context mContext;
	private SingleChoicePopWindow mSingleChoicePopWindow;
	private MultiChoicePopWindow mMultiChoicePopWindow;

	public void initData1() {
		mContext = this;
		mRootView=findViewById(R.id.empty_cart_view);
		mSingleDataList = new ArrayList<String>();
		mMultiDataList = new ArrayList<String>();
		boolean booleans[] = new boolean[COUNT * 5];

		for (int i = 0; i < COUNT; i++) {
			String string1 = "geniuseoe2012 -->" + i;
			mSingleDataList.add(string1);
		}

		for (int i = 0; i < COUNT * 2; i++) {
			String string2 = "talent -->" + i;
			mMultiDataList.add(string2);
		}

		initPopWindow(booleans);

		//initDialog(booleans);

	}
	
	
	public void initPopWindow(boolean[] booleans) {
//		mSingleChoicePopWindow = new SingleChoicePopWindow(this, mRootView,
//				mSingleDataList);
//
//		mSingleChoicePopWindow.setTitle("genius single title");
//		mSingleChoicePopWindow.setOnOKButtonListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				int selItem = mSingleChoicePopWindow.getSelectItem();
//				Toast.makeText(mContext, "selItem = " + selItem,
//						Toast.LENGTH_SHORT).show();
//			}
//		});

		mMultiChoicePopWindow = new MultiChoicePopWindow(this, mRootView,
				mMultiDataList, booleans);
		mMultiChoicePopWindow.setTitle("班级学生");
		mMultiChoicePopWindow.setOnOKButtonListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
				int size = selItems.length;
				StringBuffer stringBuffer = new StringBuffer();
				for (int i = 0; i < size; i++) {
					if (selItems[i]) {
						stringBuffer.append(i + " ");
					}

				}
				Toast.makeText(mContext,
						"selItems = " + stringBuffer.toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * popwindows 学生多选
	 */
	
	
	// 加载数据
	private void initData() {
		titleRightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.showRegister(LoginActivity.this);
			}
		});
		// 登陆按键
		settle_accounts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (login_password_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(LoginActivity.this, "用户名不能为空！");
				} else if (login_password_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(LoginActivity.this, "用户密码啊不能为空！");
				} else {
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
					login(login_name_edit.getText().toString(),
							login_password_edit.getText().toString(), true);
				}
			}
		});
	}

	// 登录验证
	private void login(final String account, final String pwd,
			final boolean isRememberMe) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					User user = (User) msg.obj;
					pd.dismiss();
					if (user != null) {
						// 清空原先cookie
						ApiClient.cleanCookie();
						// 发送通知广播
						// UIHelper.sendBroadCast(Login_activity.this,
						// user.getNotice());
						// 提示登陆成功
						UIHelper.ToastMessage(LoginActivity.this,
								R.string.msg_login_success);
						//判断用户是否输入宝贝
						if(user.getIsfirst()==0 && user.getShenfen().equals("B")){
							UIHelper.showAddBaby(LoginActivity.this,"0"); 
							
						}else{
							if(user.getShenfen().equals("B")){
								UIHelper.showhomepage(LoginActivity.this); //显示家长账户主页面
							}else if(user.getShenfen().equals("T")){
								UIHelper.showteacherhomepage(LoginActivity.this); //显示教师账户主页面
							}
						}
						
						
						finish();
					}
				} else if (msg.what == 0) {
					pd.dismiss();
					UIHelper.ToastMessage(LoginActivity.this,
							getString(R.string.msg_login_fail) + msg.obj);
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(LoginActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext) getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("Username", account);
					paramMap.put("Password", pwd);
					User user = ac.loginVerify(paramMap); //登录操作
					String res = user.getUname();
					if (res.equals("200")) {
						ac.saveLoginInfo(user, account);// 保存登录信息
						
						msg.what = 1;// 成功
						msg.obj = user;
					} else {
						ac.cleanLoginInfo();// 清除登录信息
						msg.what = 0;// 失败
						msg.obj = user.getValidate();
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
