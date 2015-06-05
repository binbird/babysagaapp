package com.teacher.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.photo.select.BaseSinglePhotoActivity;
import com.photo.select.BaseSinglePhotoSelect;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.TeacherInfoMainAdapter;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.bean.TeacherInfoMainBean;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.global.Regional;
import com.zylbaby.app.ui.AddbabyActivity;
import com.zylbaby.app.util.FileUtils;
import com.zylbaby.app.view.BootstrapCircleThumbnail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateBanjiActivity extends BaseSinglePhotoActivity implements OnClickListener{

	
	private EditText banjiname;
	private EditText schoolname;
	private EditText banjiremark;
	private EditText banjiaddress;
	private EditText banjiid;
	private EditText banjipassword;
	private Button btnsave; 
	private ImageView btnback;
	
	private Spinner province_spinner;
	private Spinner city_spinner;
	private Spinner county_spinner;
	private Regional regional=new Regional();
	private BaseSinglePhotoSelect photoselect=new BaseSinglePhotoSelect(this);
	
	private BanjiDto Banji;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teacher_create_banji_activity);
		banjiname=(EditText) findViewById(R.id.edit_banji_name);
		banjiid=(EditText) findViewById(R.id.edit_banji_id);
		schoolname=(EditText) findViewById(R.id.edit_school_name);
		banjiremark=(EditText) findViewById(R.id.edit_banji_remark);
		btnsave=(Button) findViewById(R.id.edit_banji_savebtn);
		btnback=(ImageView) findViewById(R.id.add_baby_back_btn);
		banjiaddress=(EditText)findViewById(R.id.edit_school_address);
		province_spinner=(Spinner) findViewById(R.id.spinner_province);
		city_spinner=(Spinner) findViewById(R.id.spinner_city);
		county_spinner=(Spinner) findViewById(R.id.spinner_area);
		banjipassword=(EditText) findViewById(R.id.edit_banji_password);
		init();
		initData();
		btnsave.setOnClickListener(this);
		btnback.setOnClickListener(this);
		
	}
	
	private void init(){
		
		tempFile = new File(Environment.getExternalStorageDirectory(),photoselect.getPhotoFileName());
		//添加头像
				add_baby_image = (BootstrapCircleThumbnail) findViewById(R.id.add_baby_image);
				add_baby_image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showDialog();
					}
				});
				

		
		regional.loadSpinner(CreateBanjiActivity.this, province_spinner, city_spinner, county_spinner);
	}
	
	private void initData(){
		
		try {
			GetBanjiInfo();
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
	
	
	public void GetBanjiInfo() throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(CreateBanjiActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					Banji=(BanjiDto) msg.obj;
					banjiname.setText(Banji.getName());
					schoolname.setText(Banji.getSchoool());
					banjiremark.setText(Banji.getDescribe());
					banjiaddress.setText(Banji.getAddress());
					banjiid.setText(Banji.getCode());
					
				} else if (msg.what == -1) {
					
					Toast.makeText(CreateBanjiActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) CreateBanjiActivity.this.getApplication();

					Map<String, Object> paramMap = new HashMap<String, Object>();


		            BanjiDto banjiinfo = ac.getBanjiInfo(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = banjiinfo;
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
	


	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		case R.id.edit_banji_savebtn:
			Toast.makeText(CreateBanjiActivity.this, "保存班级信息", Toast.LENGTH_LONG).show();
			try {
				SaveBanjiInfo();
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
			break;
		case R.id.add_baby_back_btn:
			
			Toast.makeText(CreateBanjiActivity.this, "返回上一个界面", Toast.LENGTH_LONG).show();
			finish();
			break;
		}
		
		
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



	//保存班级信息
	private void SaveBanji(){
		
	}
	
	public void SaveBanjiInfo() throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(CreateBanjiActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					Banji=(BanjiDto) msg.obj;
					banjiname.setText(Banji.getName());
					schoolname.setText(Banji.getSchoool());
					banjiremark.setText(Banji.getDescribe());
					banjipassword.setText(Banji.getPassword());
					
				} else if (msg.what == -1) {
					
					Toast.makeText(CreateBanjiActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) CreateBanjiActivity.this.getApplication();

					Map<String, Object> paramMap = new HashMap<String, Object>();

					paramMap.put("Name", banjiname.getText());
					paramMap.put("School", schoolname.getText());
					paramMap.put("Description", banjiremark.getText());
					paramMap.put("Address", banjiaddress.getText());
					paramMap.put("BanjiId", banjiid.getText());
					paramMap.put("Password", banjipassword.getText());
					
					File file = null;
					if(bitmaptemp != null){
						FileUtils fileutils = new FileUtils(CreateBanjiActivity.this);
						
						try {
							file = fileutils.getSaveFile("imagtemp",bitmaptemp);
						} catch (IOException e) {
							
							e.printStackTrace();
						}

					}else{
						//baby.setImageurl(null);
					}
					Map<String, File> paramMap1 = new HashMap<String, File>();
					
					
					if(file==null){
						paramMap1 = null;
					}else{
						paramMap1.put("HeadImg", file);
					}
					
					
					
					
					
					
		            BanjiDto banjiinfo = ac.saveBanjiInfo(paramMap,paramMap1);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = banjiinfo;
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
