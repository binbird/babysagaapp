package com.zylbaby.app.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.photo.select.BasePhotoActivity;
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
import com.zylbaby.app.util.Bimp;
import com.zylbaby.app.util.DimensionUtility;
import com.zylbaby.app.util.FileUtils;
import com.zylbaby.app.util.ImageItem;
import com.zylbaby.app.util.PublicWay;
import com.zylbaby.app.util.Res;
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
public class CopyOfAddWandrActivity extends BasePhotoActivity {
	

	
	
	private String diaryId="0";
	private String StateId="0";
	// 显示日期控件按键
	private ImageButton add_start_date_btn;
	private ImageButton add_stop_date_btn;
	private Button diarytype_showbtn;
	private Button babystate_showbtn;
	
	private ImageView diarytype_img;
	private TextView diarytype_name;
	private ImageView babystate_img;
	
	private EditText add_wandr_name_edit;
	private EditText add_wandr_name1_edit;
	
	private Spinner wandr_types_list;
	private Spinner wandr_show_list;
	
	private EditText add_wandr_beizhu_edit;
	
	// 时间控件
	private TimePickerDialog timePickerDialog;
	// 获取时间
	private Calendar calendar;
	
    //返回按键
    private ImageView add_baby_back_btn;
    
    //上传图片
    private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap ;
    
    /*** 
        * 使用照相机拍照获取图片 
        */  
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;  
    /*** 
       * 使用相册中的图片 
       */  
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    private Uri photoUri;
    /**获取到的图片路径*/
	private String picPath;
	private Intent lastIntent ;
	/***
	 * 从Intent获取图片路径的KEY
	 */
	public static final String KEY_PHOTO_PATH = "photo_path";
	
	private Button add_wandr_save_btn;
    
    
    // 定义一个ProgressDialog
 	private ProgressDialog pd;
 	// 设置标记
 	final int SIGN = 0x11;
 	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.add_wandr_activity, null);
		setContentView(parentView);
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);
		// 初始化
		init();
		// 加载数据
		initData();
	}

	// 初始化
	private void init() {
		
		//返回按键
		add_baby_back_btn = (ImageView) findViewById(R.id.add_wandr_back_btn);
		add_baby_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					finish();
			}
		});
		
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
		
		//		add_wandr_name_edit.set
		
		
		
		//作息类别对话框
		diarytype_showbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final DialogDiaryType Dlg=new DialogDiaryType(CopyOfAddWandrActivity.this,AppConstant.diarytype_array);
				//对话框中的返回事件
				
				Dlg.mylistener= new ImgDialogListener() {
					
					@Override
					public void ReturnListenerValue(String text,int posid) {
						// TODO Auto-generated method stub
						
						diaryId=String.valueOf(AppConstant.diarytypeid_array[posid]); 
						diarytype_img.setImageResource(AppConstant.diaryioc_array[posid]);
						diarytype_name.setText(AppConstant.diarytype_array[posid]);
						Toast.makeText(CopyOfAddWandrActivity.this, "作息类型Id："+diaryId, Toast.LENGTH_LONG).show();
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
				final DialogState Dlg= new DialogState(CopyOfAddWandrActivity.this);

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
				showTime(1);
			}
		});
		//结束时间
		add_stop_date_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTime(2);
			}
		});
		
		
		//添加图片
		Res.init(this);
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
		pop = new PopupWindow(CopyOfAddWandrActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(CopyOfAddWandrActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		
		noScrollgridview = (GridView) findViewById(R.id.add_wandr_uploadimage_list);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		setListViewHeightBasedOnChildren(noScrollgridview);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
//					Log.i("ddddddd", "----------");
//					ll_popup.startAnimation(AnimationUtils.loadAnimation(AddWandrActivity.this,R.anim.activity_translate_in));
//					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
					Log.i("ddddddd", "----------");
//					ll_popup.startAnimation(AnimationUtils.loadAnimation(AddScheduleActivity.this,R.anim.activity_translate_in));
//					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
					Intent intent = new Intent(CopyOfAddWandrActivity.this,PhotoAlbumActivity.class);
					Bundle bundle=new Bundle();  
					bundle.putString("position","1");  
		            intent.putExtras(bundle);  
					startActivity(intent);
				} else {
//					Intent intent = new Intent(AddWandrActivity.this,GalleryActivity.class);
//					intent.putExtra("position", "1");
//					intent.putExtra("ID", arg2);
//					startActivity(intent);
					Intent intent = new Intent(CopyOfAddWandrActivity.this,PhotoAlbumActivity.class);
					
					Bundle bundle=new Bundle();  
					bundle.putString("position","1");  
		            intent.putExtras(bundle);  
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
					UIHelper.ToastMessage(CopyOfAddWandrActivity.this, "任务开始时间不能为空！");
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
					
					WandrBean wandrbean = new WandrBean();
					wandrbean.setTimeBegin(add_wandr_name_edit.getText().toString());
					if(add_wandr_name1_edit.getText().toString().equals("")){
						wandrbean.setTimeEnd(add_wandr_name_edit.getText().toString());
					}
					wandrbean.setTimeEnd(add_wandr_name1_edit.getText().toString());
					wandrbean.setDiaryRemark(add_wandr_beizhu_edit.getText().toString());
					wandrbean.setDiaryType(diaryId); //设置作息类型Id
					wandrbean.setRank("好");

					wandrbean.setByUser("0");
					wandrbean.setImportant("0");
					wandrbean.setOpen("0");
					wandrbean.setToUser("0");
					
					List<File> files = new ArrayList<File>();
					
//					for(int i=0; i<Bimp.tempSelectBitmap.size(); i++){
//						System.out.println("uploadPicFiles"+i);
//						files.add(Bimp.tempSelectBitmap.get(i).getFile());
//					}
					for(int i=0; i<getpiclist.size(); i++){
						File f=new File(getpiclist.get(i));
						files.add(f);
					}
					wandrbean.setUploadPicFiles(files);
					
					AddWandrNet(wandrbean);
					
					
					
					
				}
			}
		});
		
	}
	private ArrayAdapter<String> m_adapter; 
	private ArrayList<String> getpiclist=new ArrayList<String>();
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		ArrayList<String> l1=intent.getStringArrayListExtra("piclist");
		getpiclist=l1;
		m_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, l1);  
        //lv1.setAdapter(m_adapter);  
		
		noScrollgridview.setAdapter(new MyImgAdapter(this, l1));
		super.onNewIntent(intent);
		
		
	}
	
	public void setListViewHeightBasedOnChildren(GridView gridview) {
		ListAdapter gridAdapter = gridview.getAdapter();

		if (gridAdapter == null) {
			return;
		}
		int totalHeight = 0;
		double num = gridAdapter.getCount();
		double size=4;
		double bug = num/size;
		double line = Math.ceil(bug); 
		int totalline = (int) line;
		for (int i = 0; i <totalline; i++) {
			totalHeight += DimensionUtility.dip2px(CopyOfAddWandrActivity.this, 55); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = gridview.getLayoutParams();
//		if(totalline == 1){
			params.height = totalHeight;
//		}else{
//			params.height = totalHeight-30*(totalline);
//		}
		// listView.getDividerHeight() 获取子项间分隔符占用的高度
		gridview.setLayoutParams(params);
	}

	// 加载数据
	private void initData() {
		
	}
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					setListViewHeightBasedOnChildren(noScrollgridview);
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
	
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				File file  = FileUtils.saveBitmap(bm, fileName);
				System.out.println("file+=" + file.length());
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.setFile(file);
//				takePhoto.setImagePath(imagePath);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			PublicWay.activityList.clear();
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
						UIHelper.ToastMessage(CopyOfAddWandrActivity.this,
								"添加成功");

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
					UIHelper.ToastMessage(CopyOfAddWandrActivity.this,
						   "添加失败" + msg.obj);
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(CopyOfAddWandrActivity.this);
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
	
	// 加载日期
	private void showTime(final int sign) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				if (sign == 1) {
					add_wandr_name_edit.setText(arg1 + ":" + arg2);
				}else{
					add_wandr_name1_edit.setText(arg1 + ":" + arg2);
				}
			}

		};
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Bimp.tempSelectBitmap.clear();
		super.onDestroy();
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
