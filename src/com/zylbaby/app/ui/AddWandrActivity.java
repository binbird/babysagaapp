package com.zylbaby.app.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.photo.select.BasePhotoActivity;
import com.photo.select.Bimp;
import com.photo.select.FileUtils;
import com.photo.select.PhotoActivity;
import com.photo.select.BasePhotoActivity.GridAdapter;
import com.photo.select.BasePhotoActivity.PopupWindows;
import com.teacher.app.ui.CompleteScheduleActivity;
import com.why.photoaibum.PhotoAlbumActivity;
import com.why.photoaibum.adapter.MyImgAdapter;
import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.WandrBean;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.AppManager;

import com.zylbaby.app.util.DateTimeDialog;
import com.zylbaby.app.util.DimensionUtility;

import com.zylbaby.app.util.ImageItem;
import com.zylbaby.app.util.PublicWay;
import com.zylbaby.app.util.Res;
import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.DialogDiaryType;
import com.zylbaby.app.view.DialogState;
import com.zylbaby.app.view.ImgDialogListener;
import com.zylbaby.app.view.StateImgDialogListener;

/**
 * 添加作息布局界面
 * 
 * @author  姜斌 （binbird@qq.com）
 * @version 1.0
 * @created 2014-3-6
 */
public class AddWandrActivity extends BasePhotoActivity {
	

	
	
	private String diaryId="0";
	private String StateId="0";
	// 显示日期控件按键
	private ImageButton add_start_date_btn;
	private ImageButton add_stop_date_btn;
	private ImageButton add_select_date_btn;
	private Button diarytype_showbtn;
	private Button babystate_showbtn;
	
	private ImageView diarytype_img;
	private TextView diarytype_name;
	private ImageView babystate_img;
	
	private EditText add_wandr_name_edit;
	private EditText add_wandr_name1_edit;
	
	private EditText add_wandr_date_edit;
	
	private Spinner wandr_types_list;
	private Spinner wandr_show_list;
	
	private EditText add_wandr_beizhu_edit;
	
	// 时间控件
	private TimePickerDialog timePickerDialog;
	private DatePickerDialog datePickerDialog;
	// 获取时间
	private Calendar calendar;
	//星星评分
	private RatingBar ratingbar;
    //返回按键
    private ImageView add_baby_back_btn;
    
    //上传图片
    private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap ;
    
	private RelativeLayout imgselect ;
	
    private Uri photoUri;
    /**获取到的图片路径*/
	private String picPath;
	private Intent lastIntent ;
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	public DateTimeDialog datetimedialog= new DateTimeDialog();
	private Button add_wandr_save_btn;
    
    
//    // 定义一个ProgressDialog
// 	private ProgressDialog pd;
// 	// 设置标记
// 	final int SIGN = 0x11;
 	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.add_wandr_activity, null);
		setContentView(parentView);
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);
		
		// 初始化
		Init();
		init1();
		// 加载数据
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	public void Init() {
		
		imgselect=(RelativeLayout) findViewById(R.id.add_wandr_item6);
		/*
		noScrollgridview = (GridView) findViewById(R.id.add_wandr_uploadimage_list);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//adapter = new GridAdapter(this);
		adapter=new GridAdapter(AddWandrActivity.this,handler);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(AddWandrActivity.this, noScrollgridview,"");
				} else {
					Intent intent = new Intent(AddWandrActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		*/
		
	}
	// 初始化
	private void init1() {
		
		//返回按键
		add_baby_back_btn = (ImageView) findViewById(R.id.add_wandr_back_btn);
		add_baby_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					finish();
			}
		});
		
		add_select_date_btn=(ImageButton) findViewById(R.id.add_select_date_btn);
		
		add_wandr_date_edit=(EditText) findViewById(R.id.add_wandr_date_edit);
		add_wandr_date_edit.setText(StringUtils.GetDate());
		add_start_date_btn = (ImageButton) findViewById(R.id.add_start_date_btn);
		add_stop_date_btn = (ImageButton) findViewById(R.id.add_stop_date_btn);
		
		add_wandr_name_edit = (EditText) findViewById(R.id.add_wandr_name_edit);
		add_wandr_name1_edit = (EditText) findViewById(R.id.add_wandr_name1_edit);
		add_wandr_beizhu_edit = (EditText) findViewById(R.id.add_wandr_beizhu_edit);
		diarytype_showbtn=(Button)findViewById(R.id.diarytype_dialog_showbtn);
		babystate_showbtn=(Button) findViewById(R.id.btn_showstate);
		diarytype_img =(ImageView) findViewById(R.id.diarytype_img);
		babystate_img=(ImageView) findViewById(R.id.img_showstate);
		diarytype_name= (TextView) findViewById(R.id.diarytype_name);
		ratingbar=(RatingBar) findViewById(R.id.ratingBar1);
		//		add_wandr_name_edit.set
		
		
		
		//作息类别对话框
		diarytype_showbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final DialogDiaryType Dlg=new DialogDiaryType(AddWandrActivity.this,AppConstant.diarytype_array);
				//对话框中的返回事件
				
				Dlg.mylistener= new ImgDialogListener() {
					
					@Override
					public void ReturnListenerValue(String text,int posid) {
						// TODO Auto-generated method stub
						
						diaryId=String.valueOf(AppConstant.diarytypeid_array[posid]); 
						diarytype_img.setImageResource(AppConstant.diaryioc_array[posid]);
						diarytype_name.setText(AppConstant.diarytype_array[posid]);
						//Toast.makeText(AddWandrActivity.this, "作息类型Id："+diaryId, Toast.LENGTH_LONG).show();
						Dlg.dismiss();
					}
				};
				Dlg.show();
				
			}
		});
		//宝贝表现
		babystate_showbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final DialogState Dlg= new DialogState(AddWandrActivity.this);

				Dlg.mylistener=new StateImgDialogListener() {
					
					@Override
					public void ReturnListenerValue(String text, int posid) {
						babystate_img.setImageResource(AppConstant.babystateioc_array[posid]);
						StateId=String.valueOf(AppConstant.babtstateid_array[posid]);
						Dlg.dismiss();
						
					}
				};
				Dlg.show();
			}
		});
		//开始时间
		add_start_date_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datetimedialog.showTime(AddWandrActivity.this, add_wandr_name_edit);
			}
		});
		//结束时间
		add_stop_date_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datetimedialog.showTime(AddWandrActivity.this, add_wandr_name1_edit);
			}
		});
		
		
		add_select_date_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				datetimedialog.showDate(AddWandrActivity.this, add_wandr_date_edit);
			}
		});
		
		noScrollgridview = (GridView) findViewById(R.id.add_wandr_uploadimage_list);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter=new GridAdapter(AddWandrActivity.this,handler);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(AddWandrActivity.this, noScrollgridview,"diary");
				} else {
					Intent intent = new Intent(AddWandrActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					
					startActivity(intent);
				}
			}
		});
		
		
		add_wandr_save_btn = (Button) findViewById(R.id.add_wandr_save_btn);
		add_wandr_save_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 判断是否为空
				if (add_wandr_name_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(AddWandrActivity.this, "任务开始时间不能为空！");
				} else {
					showDialog(SIGN);
					ShowPd();
//					pd.setOnKeyListener(new OnKeyListener() {
//						@Override
//						public boolean onKey(DialogInterface dialog, int keyCode,
//								KeyEvent event) {
//							boolean onkey = false;
//							int keyCode1 = event.getKeyCode();
//							if (keyCode1 == KeyEvent.KEYCODE_BACK
//									&& event.getAction() != KeyEvent.ACTION_UP) {
//								if (event.getRepeatCount() == 0) {
//									pd.dismiss();
//									pd.cancel();
//									onkey = true;
//								}
//							}
//							return onkey;
//						}
//					});
					
					WandrBean wandrbean = new WandrBean();
					wandrbean.setTimeBegin(add_wandr_name_edit.getText().toString());
					if(add_wandr_name1_edit.getText().toString().equals("")){
						wandrbean.setTimeEnd(add_wandr_name_edit.getText().toString());
					}
					wandrbean.setTimeEnd(add_wandr_name1_edit.getText().toString());
					wandrbean.setDiaryRemark(add_wandr_beizhu_edit.getText().toString());
					wandrbean.setDiaryType(diaryId); //设置作息类型Id
					
					//wandrbean.setRank(StateId);
					wandrbean.setRank(String.valueOf((int)ratingbar.getRating()));
					wandrbean.setByUser("0");
					wandrbean.setImportant("0");
					wandrbean.setOpen("0");
					wandrbean.setToUser("0");
					
					List<File> files = new ArrayList<File>();
					
					for (int i = 0; i < Bimp.drr.size(); i++) {
						String Str = Bimp.drr.get(i).substring( 
								Bimp.drr.get(i).lastIndexOf("/") + 1,
								Bimp.drr.get(i).lastIndexOf("."));
						
						File f=new File(Bimp.drr.get(i));
						files.add(f);
										
					}
					wandrbean.setUploadPicFiles(files);
					
					AddWandrNet(wandrbean);
					
					Bimp.drr.clear();
					//Bimp.bmp.clear();
					ClearBimp();
					
				}
			}
		});
		
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
		LayoutParams p=(LayoutParams)imgselect.getLayoutParams();
		int h=p.height;
		int w=p.width;
		p.height=p.height*2;
//		imgselect.setLayoutParams(new RelativeLayout.LayoutParams(w, h*2));
		imgselect.setLayoutParams(p);
		super.onRestart();
	}

		 @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			 
			// ClearBimp();
			 super.onDestroy();
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
				//imgselect.setLayoutParams(new RelativeLayout.LayoutParams(128, 1000));
				
			}
			break;
		}
	}
	
	// 登录验证
	private void AddWandrNet(final WandrBean baby) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					WandrBean user = (WandrBean) msg.obj;
					pd.dismiss();
					if (user != null) {
						UIHelper.ToastMessage(AddWandrActivity.this,
								"添加成功");
						//保存成功后，清除图片
						ClearBimp();
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
					UIHelper.ToastMessage(AddWandrActivity.this,
						   "添加失败" + msg.obj);
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(AddWandrActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext) getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("TimeBegin",baby.getTimeBegin() );
					paramMap.put("TimeEnd", baby.getTimeEnd());
					paramMap.put("DiaryType", baby.getDiaryType());
					paramMap.put("DiaryRemark", baby.getDiaryRemark());
					paramMap.put("Important", baby.getImportant());
					paramMap.put("Open", baby.getOpen());
					paramMap.put("ByUser", baby.getByUser());
					paramMap.put("ToUser", baby.getToUser());
					paramMap.put("RankStr", baby.getRank());
					paramMap.put("DiaryDate", add_wandr_date_edit.getText().toString());
					
					Map<String, File> paramMap1 = new HashMap<String, File>();
					if(baby.getUploadPicFiles().size()==0){
							paramMap1 = null;
					}else{
						for(int i=0 ; i<baby.getUploadPicFiles().size(); i++){
							//System.out.println("UploadPic"+i);
							paramMap1.put("UploadPic"+i,baby.getUploadPicFiles().get(i));
						}
					}
					WandrBean baby = ac.addwandrInfo(paramMap, paramMap1);
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
	//	// 加载界面
//	// 调用onPrepareDialog()方法之后
//	@Override
//	protected Dialog onCreateDialog(int id, Bundle args) {
//		pd = new ProgressDialog(this);
//		pd.setMessage("正在加载......");
//		pd.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
//		pd.setCancelable(false);
//		return pd;
//	}
//	// 调用showDialog()之后和调用onCreateDialog之前
//	@Override
//	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
//		switch (id) {
//		case SIGN:
//			break;
//		}
//	}

}
