package com.zylbaby.app.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zylbaby.app.R;
import com.zylbaby.app.bean.User;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.net.ApiClient;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.UIHelper;

/**
 * 注册布局界面
 * 
 * @author Micheal zhu 朱育梁 （zhuyuliang0@126.com）
 * @version 1.0
 * @created 2014-3-6
 */
public class RegisterActivity extends Activity {

	private ImageView register_back_btn; // 返回按键
	private EditText register_user_name_edit; // 用户名
	private EditText register_pass_edit; // 用户密码
	private EditText register_pass1_edit; // 用户密码确定
	private EditText register_mail_edit; // 用户密码确定
	private Button register_save_btn; // 注册
	
	private RadioGroup radiogrop;
	private int Shenfen = 1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_new_activity);
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);

		// 初始化
		init();
		// 加载数据
		initData();

	}

	// 初始化
	private void init() {
		register_back_btn = (ImageView) findViewById(R.id.register_back_btn);
		register_user_name_edit = (EditText) findViewById(R.id.register_user_name_edit);
		register_pass_edit = (EditText) findViewById(R.id.register_pass_edit);
		register_pass1_edit = (EditText) findViewById(R.id.register_pass1_edit);
		register_save_btn = (Button) findViewById(R.id.register_save_btn);
		register_mail_edit= (EditText) findViewById(R.id.register_mail_edit);
		radiogrop = (RadioGroup) findViewById(R.id.radiogroup1);
	}

	// 加载数据
	private void initData() {
		register_back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		register_save_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (register_user_name_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(RegisterActivity.this, "用户名不能为空！");
				} else if (register_user_name_edit.getText().toString()
						.length() < 6) {
					UIHelper.ToastMessage(RegisterActivity.this, "用户名不能小于6位字符");
				} else if (register_pass_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(RegisterActivity.this, "密码不能为空！");
				} else if (register_pass_edit.getText().toString().length() < 6) {
					UIHelper.ToastMessage(RegisterActivity.this, "密码不能小于6位字符");
				} else if (!register_pass_edit.getText().toString()
						.equals(register_pass1_edit.getText().toString())) {
					UIHelper.ToastMessage(RegisterActivity.this,
							"确认密码不正确，请重新输入！");
				} else if(register_mail_edit.getText().toString().equals("")){
					UIHelper.ToastMessage(RegisterActivity.this,
							"邮箱不能为空！");
				}else if(!isEmail(register_mail_edit.getText().toString())){
					UIHelper.ToastMessage(RegisterActivity.this,
							"邮箱格式不正确！");
				}
				else {
					register(register_user_name_edit.getText().toString(),
							register_pass_edit.getText().toString(), true);
				}
			}
		});
		radiogrop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.Parents){
					Shenfen = 1;
				}
				if(checkedId == R.id.teacher){
					Shenfen = 2;
				}
			}
		});

	}
	
    //判断email格式是否正确

	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	// 注册验证
	private void register(final String account, final String pwd,
			final boolean isRememberMe) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					User user = (User) msg.obj;
					if (user != null) {
						// 清空原先cookie
						ApiClient.cleanCookie();
						// 发送通知广播
						// UIHelper.sendBroadCast(Login_activity.this,
						// user.getNotice());
						// 提示登陆成功
						UIHelper.ToastMessage(RegisterActivity.this, "注册成功");
						UIHelper.showLogin(RegisterActivity.this);
						finish();
					}
				} else if (msg.what == 0) {
					UIHelper.ToastMessage(RegisterActivity.this, "注册错误"
							+ msg.obj);
				} else if (msg.what == -1) {
					((AppException) msg.obj).makeToast(RegisterActivity.this);
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
					paramMap.put("Shenfen",  Shenfen);
					paramMap.put("EMail",  register_mail_edit.getText());
					User user = ac.RegisterVerify(paramMap);
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

}
