package com.teacher.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.photo.select.BaseSinglePhotoActivity;
import com.photo.select.BaseSinglePhotoSelect;
import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.global.Regional;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.FileUtils;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.BootstrapCircleThumbnail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TeacherInfoActivity extends BaseSinglePhotoActivity {
	
	private BaseSinglePhotoSelect photoselect=new BaseSinglePhotoSelect(this);
	// 进入标示
	private String tag = "2";

	// 籍贯相关
	private Spinner province_spinner;
	private Spinner city_spinner;
	private Spinner county_spinner;
	 //籍贯
	private Integer provinceId, cityId;
	private String strProvince, strCity, strCounty;
	private Regional regional= new Regional();

    
	
	//出生相关
	// 显示日期控件按键
	private ImageButton add_task_date_btn;
	// 日期控件
	private DatePickerDialog datePickerDialog;
	// 时间控件
	private TimePickerDialog timePickerDialog;
	// 获取时间
	private Calendar calendar;
	

    
    //返回按键
    private ImageView add_baby_back_btn;
    //跳过添加
    private Button titleRightButton;
    
    //宝贝名称
    private EditText add_baby_name_edit;
    //宝贝小名
    private EditText add_baby_name1_edit;
    //出生日期
    private EditText task_time_edit;
    //性别
    private RadioGroup radiogroup1;
    private int sex = 1;
    //血型
    private Spinner blood_types_list;
    //宝贝描述
    private EditText add_baby_beizhu_edit;
    
    //保存宝贝
    private Button add_baby_save_btn;
    

    BabyBean babyinfo;
 	private BabyBean babybean;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teacher_edit_person_activity);
		tempFile = new File(Environment.getExternalStorageDirectory(),photoselect.getPhotoFileName());
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);
		// 初始化
		init();
		// 加载数据
		initData();
		
		//加载省市联动
		regional.loadSpinner(TeacherInfoActivity.this,province_spinner,city_spinner,county_spinner);
		
		//加载教师信息
		try {
			GetBabyInfo();
//			String a3=babyinfo.getBnativeplace3();
//			Regional.setSpinnerItemSelectedByValue(city_spinner, a3);
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
		
	}

	// 初始化
	private void init() {
		
		tag = getIntent().getExtras().getString("tag");
		
		province_spinner = (Spinner) findViewById(R.id.spinner1);
		city_spinner = (Spinner) findViewById(R.id.spinner2);
		county_spinner = (Spinner) findViewById(R.id.spinner3);
		
		
		
		//出生日期
		add_task_date_btn = (ImageButton) findViewById(R.id.add_task_date_btn);
		task_time_edit = (EditText) findViewById(R.id.task_time_edit);
		task_time_edit.setFocusable(false);
		
		add_task_date_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDate(0);
			}
		});
		
		//添加头像
		add_baby_image = (BootstrapCircleThumbnail) findViewById(R.id.add_baby_image);
		add_baby_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		
		//返回按键
		add_baby_back_btn = (ImageView) findViewById(R.id.add_baby_back_btn);
		add_baby_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(tag.equals("0")){
					AppContext appContext = (AppContext) getApplication();
					appContext.Logout();
					UIHelper.showLogin(TeacherInfoActivity.this);
					finish();
				}else{
					finish();
				}
			}
		});
		
		add_baby_name_edit = (EditText) findViewById(R.id.add_baby_name_edit);
		add_baby_name1_edit = (EditText) findViewById(R.id.add_baby_name1_edit);
		radiogroup1 = (RadioGroup) findViewById(R.id.radiogroup1);
		blood_types_list = (Spinner) findViewById(R.id.blood_types_list);
		add_baby_beizhu_edit = (EditText) findViewById(R.id.add_baby_beizhu_edit);
		add_baby_save_btn = (Button) findViewById(R.id.add_baby_save_btn);
		titleRightButton = (Button) findViewById(R.id.titleRightButton);
		
		if(!tag.equals("0")){
			titleRightButton.setVisibility(8);
		}
	}

	// 加载数据
	private void initData() {
		radiogroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.man){
					sex = 0;
				}
				if(checkedId == R.id.woman){
					sex = 1;
				}
			}
		});
		
		titleRightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.showhomepage(TeacherInfoActivity.this);
				finish();
			}
		});
		
		add_baby_save_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (add_baby_name_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(TeacherInfoActivity.this, "baby名字不能为空！");
				} else if (add_baby_name1_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(TeacherInfoActivity.this, "baby昵称不能为空！");
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
					BabyBean baby = new BabyBean();
					baby.setBname(add_baby_name_edit.getText().toString());
					baby.setBnickname(add_baby_name1_edit.getText().toString());
					baby.setBbirth(task_time_edit.getText().toString());
					if(sex == 1){
						baby.setBsex("男");
					}else{
						baby.setBsex("女");
					}
					baby.setBblood(blood_types_list.getSelectedItem().toString());
					baby.setBnativeplace1(province_spinner.getSelectedItem().toString());
					baby.setBnativeplace2(city_spinner.getSelectedItem().toString());
					baby.setBnativeplace3(county_spinner.getSelectedItem().toString());
					baby.setBbeizhu(add_baby_beizhu_edit.getText().toString());
					if(bitmaptemp != null){
						FileUtils fileutils = new FileUtils(TeacherInfoActivity.this);
						File file = null;
						try {
							file = fileutils.getSaveFile("imagtemp",bitmaptemp);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						baby.setImageurl(file);
					}else{
						baby.setImageurl(null);
					}
					updateData(baby);
				}
			}
		});
		
	}
	
	// 登录验证
	private void updateData(final BabyBean baby1) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					BabyBean user = (BabyBean) msg.obj;
					pd.dismiss();
					if (user != null) {
						UIHelper.ToastMessage(TeacherInfoActivity.this,
								"添加成功");
						UIHelper.showhomepage(TeacherInfoActivity.this);
						//finish();
					}
				} else if (msg.what == 0) {
					pd.dismiss();
					UIHelper.ToastMessage(TeacherInfoActivity.this,
						   "添加失败" + msg.obj);
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(TeacherInfoActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					BabyBean baby=baby1;
					AppContext ac = (AppContext) getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					if(baby.getBsex().equals("男")){
						paramMap.put("Sex", "0");
					}else{
						paramMap.put("Sex", "1");
					}
					paramMap.put("BabyName", baby.getBname());
					paramMap.put("Nickname", baby.getBnickname());
					paramMap.put("Birthday", baby.getBbirth());
					paramMap.put("Country", baby.getBnativeplace1());
					paramMap.put("Province", baby.getBnativeplace2());
					paramMap.put("City", baby.getBnativeplace3());
					paramMap.put("BloodType", baby.getBblood());
					paramMap.put("Introduction", baby.getBbeizhu());
					Map<String, File> paramMap1 = new HashMap<String, File>();
					File f=baby.getImageurl();
					
					if(f==null){
						paramMap1 = null;
					}else{
						paramMap1.put("HeadImg", baby.getImageurl());
					}
					BabyBean baby2 = ac.addBabyInfo(paramMap, paramMap1);
					String res = baby2.getValidate();
					if (res.equals("200")) {
						msg.what = 1;// 成功
						msg.obj = baby2;
					} else {
						ac.cleanLoginInfo();// 清除登录信息
						msg.what = 0;// 失败
						msg.obj = baby2.getBbeizhu2();
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
	
	
	// 加载日期
	private void showDate(final int sign) {

		/**
		 * 内部匿名类，实现DatePickerDialog.OnDateSetListener接口，重写onDateSet()方法
		 * 当弹出DatePickerDialog并设置完Date以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onDateSet()方法。
		 */
		DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				showTime(sign, year, monthOfYear, dayOfMonth);
			}
		};
		calendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(this, onDateSetListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();

	}

	// 加载日期
	private void showTime(final int sign, final int year,
			final int monthOfYear, final int dayOfMonth) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				Log.d("test", "" + year + "年" + (monthOfYear + 1) + "月"
						+ dayOfMonth + "日" + arg1 + ":" + arg2);
				if (sign == 0) {
					task_time_edit.setText(year + "年" + (monthOfYear + 1) + "月"
							+ dayOfMonth + "日" + " " + arg1 + ":" + arg2);
				}
			}

		};
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
//			if(data.getExtras() != null)
			startPhotoZoom(Uri.fromFile(tempFile), 150);		
			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null)
				startPhotoZoom(data.getData(), 150);
			break;

		case PHOTO_REQUEST_CUT:
			if (data.getExtras() != null)
				setPicToView(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	public void GetBabyInfo() throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(TeacherInfoActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					babyinfo=(BabyBean) msg.obj;
					

					
					add_baby_name_edit.setText(babyinfo.getBname());
					add_baby_name1_edit.setText(babyinfo.getBnickname());
					task_time_edit.setText(babyinfo.getBbirth());
					int k=Integer.parseInt(babyinfo.getBsex().toString()) ;
					if(k==1){
						radiogroup1.check(R.id.man);
					}else
					{
						radiogroup1.check(R.id.woman);
					}
					
					String a1=babyinfo.getBnativeplace1();
					String a2=babyinfo.getBnativeplace2();
					String a3=babyinfo.getBnativeplace3();
					String blood=babyinfo.getBblood();
					Regional.setProvinceCityItemSelectedByValue(province_spinner, a1,a2,a3);
					
					Regional.setSpinnerItemSelectedByValue(blood_types_list, blood);
					add_baby_beizhu_edit.setText(babyinfo.getBbeizhu());

					setPicToView(babyinfo.getImageurlfile());
				} else if (msg.what == -1) {
					
					Toast.makeText(TeacherInfoActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) TeacherInfoActivity.this.getApplication();
		            BabyBean baby = ac.getBabyInfo();
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = baby;
					} else {
						//ac.cleanLoginInfo();// 清除登录信息
						msg.what = -1;// 失败
						msg.obj = "";
					}

				}
				catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
		

		

	}
	




}
