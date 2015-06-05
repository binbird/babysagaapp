package com.zylbaby.app.ui;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.photo.select.BasePhotoActivity;
import com.photo.select.Bimp;
import com.photo.select.PhotoActivity;
import com.photo.select.BasePhotoActivity.PopupWindows;
import com.why.photoaibum.PhotoAlbumActivity;
import com.why.photoaibum.adapter.MyImgAdapter;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.GridAdapter;
import com.zylbaby.app.bean.WandrBean;
import com.zylbaby.app.global.AppContext;

import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.DateTimeDialog;
import com.zylbaby.app.util.DimensionUtility;
import com.zylbaby.app.util.FileUtils;
import com.zylbaby.app.util.ImageItem;
import com.zylbaby.app.util.PublicWay;
import com.zylbaby.app.util.Res;
import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.util.UIHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddLifeActivity extends BasePhotoActivity implements OnClickListener{
	
	private EditText title;
	private EditText content;
	private Button save_btn;
	private ImageView back_btn;
	
	
	private EditText add_wandr_date_edit;
	private ImageButton add_select_date_btn;
	
	
	private GridView noScrollgridview;
	private GridAdapter adapter;

	public static Bitmap bimap ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_life_activity);
		
		back_btn=(ImageView) findViewById(R.id.add_left_back_btn);
		save_btn=(Button) findViewById(R.id.add_life_save_btn);
		title=(EditText) findViewById(R.id.add_lift_title_edit);
		content=(EditText) findViewById(R.id.add_lift_beizhu_edit);
		noScrollgridview=(GridView) findViewById(R.id.noScrollgridview);
		
		add_select_date_btn=(ImageButton) findViewById(R.id.add_select_date_btn);
		add_wandr_date_edit=(EditText) findViewById(R.id.add_wandr_date_edit);

		add_wandr_date_edit.setText(StringUtils.GetDate());
		
		back_btn.setOnClickListener(this);
		save_btn.setOnClickListener(this);
		
		add_select_date_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DateTimeDialog dialog= new DateTimeDialog();
				dialog.showDate(AddLifeActivity.this, add_wandr_date_edit);
			}
		});
		//底部图片选择器
		Init();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
		case SELECT_PIC_BY_TACK_PHOTO:
			if (Bimp.drr.size() < 9 ) {
				File f = new File(FILE_PIC_SCREENSHOT,localTempImageFileName);
				Bimp.drr.add(f.getPath());
			}
			break;
		}
		
	}
	
	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//adapter = new GridAdapter(this);
		adapter=new GridAdapter(AddLifeActivity.this,handler);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(AddLifeActivity.this, noScrollgridview,"life");
				} else {
					Intent intent = new Intent(AddLifeActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					//intent.putExtra("activityname", "life");
					startActivity(intent);
				}
			}
		});
	
}
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		default:
		case R.id.add_left_back_btn:
			finish();
			break;
		case R.id.add_life_save_btn:
			
			Toast.makeText(AddLifeActivity.this, "保存生活片段", 3000).show();
			
			List<String> list = new ArrayList<String>();				
			for (int i = 0; i < Bimp.drr.size(); i++) {
//				String Str = Bimp.drr.get(i).substring( 
//						Bimp.drr.get(i).lastIndexOf("/") + 1,
//						Bimp.drr.get(i).lastIndexOf("."));
				String Str=Bimp.drr.get(i);
				//list.add(FileUtils.SDPATH+Str+".JPEG");				
				list.add(Str);
			}
			// 高清的压缩图片全部就在  list 路径里面了
			// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
			// 完成上传服务器后 .........
			
			showDialog(SIGN);
			ShowPd();
			SaveBabyLife();
			//FileUtils.deleteDir();
			
			
			
			
			break;
		
		}
		
	}
	
	
	private void SaveBabyLife(){
		WandrBean wandrbean = new WandrBean();
		
		wandrbean.setTimeEnd("");
		
		wandrbean.setDiaryType("100"); //设置作息类型Id
		wandrbean.setTitle(title.getText().toString());
		wandrbean.setContent(content.getText().toString());

		wandrbean.setByUser("0");
		wandrbean.setImportant("0");
		wandrbean.setOpen("0");
		wandrbean.setToUser("0");
		
		List<File> files = new ArrayList<File>();
//		for(int i=0; i<getpiclist.size(); i++){
//			File f=new File(getpiclist.get(i));
//			files.add(f);
//		}
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String Str = Bimp.drr.get(i).substring( 
					Bimp.drr.get(i).lastIndexOf("/") + 1,
					Bimp.drr.get(i).lastIndexOf("."));
			//File f=new File(FileUtils.SDPATH+Str+".JPEG");
			File f=new File(Bimp.drr.get(i));
			files.add(f);
			
			//list.add(FileUtils.SDPATH+Str+".JPEG");				
		}
		
		
		wandrbean.setUploadPicFiles(files);
//		for(int i=0; i<Bimp.tempSelectBitmap.size(); i++){
//			System.out.println("uploadPicFiles"+i);
//			files.add(Bimp.tempSelectBitmap.get(i).getFile());
//		}
//		for(int i=0; i<getpiclist.size(); i++){
//			File f=new File(getpiclist.get(i));
//			files.add(f);
//		}
		wandrbean.setUploadPicFiles(files);
		
		AddLifeNet(wandrbean);
	}
	
	private void AddLifeNet(final WandrBean life){
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					WandrBean user = (WandrBean) msg.obj;
					pd.dismiss();
					if (user != null) {
						UIHelper.ToastMessage(AddLifeActivity.this,
								"添加成功");
						//保存成功后，清除图片
						Bimp.bmp.clear();
						Bimp.drr.clear();
						Bimp.max = 0;
						FileUtils.deleteDir();
						
						//保存成功后，清除图片
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
						String date=sdf.format(new java.util.Date());  
//						Intent intent = new Intent(); 

						Intent intent = new Intent(); 
	                    Bundle bundle = new Bundle(); 
	                    bundle.putString("selectdate", date); 
	                    intent.putExtras(bundle); 
	                    
	                    setResult(Activity.RESULT_OK, intent); 
						
						finish();
						
					}
				} else if (msg.what == 0) {
					pd.dismiss();
					UIHelper.ToastMessage(AddLifeActivity.this,
						   "添加失败" + msg.obj);
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(AddLifeActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext) getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("TimeBegin","" );
					paramMap.put("TimeEnd", "");
					paramMap.put("DiaryType", life.getDiaryType());
					paramMap.put("DiaryRemark", "");
					paramMap.put("Important", life.getImportant());
					paramMap.put("Open", life.getOpen());
					paramMap.put("Content", life.getContent());
					paramMap.put("title", life.getTitle());
					paramMap.put("ByUser", life.getByUser());
					paramMap.put("ToUser", life.getToUser());
					paramMap.put("RankStr", life.getRank());
					paramMap.put("DiaryDate", add_wandr_date_edit.getText().toString());
					Map<String, File> paramMap1 = new HashMap<String, File>();
					if(life.getUploadPicFiles().size()==0){
							paramMap1 = null;
					}else{
						for(int i=0 ; i<life.getUploadPicFiles().size(); i++){
							//System.out.println("UploadPic"+i);
							paramMap1.put("UploadPic"+i,life.getUploadPicFiles().get(i));
						}
					}
					WandrBean baby = ac.addlifeInfo(paramMap, paramMap1);
					String res = baby.getStatusCode();
					if (res.equals("200")) {
						msg.what = 1;// 成功
						msg.obj = baby;
					} else {
						ac.cleanLoginInfo();// 清除登录信息
						msg.what = 0;// 失败
						msg.obj = baby.getError();
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
	
	private static final int TAKE_PICTURE = 0x000002;
	
	
	
	}
