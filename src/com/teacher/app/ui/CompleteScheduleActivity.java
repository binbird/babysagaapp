package com.teacher.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;


import com.photo.select.BasePhotoActivity;
import com.photo.select.Bimp;
import com.photo.select.FileUtils;
import com.photo.select.PhotoActivity;

import com.why.photoaibum.adapter.MyImgAdapter;
import com.zylbaby.app.R;

import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BabySelectStars;
import com.zylbaby.app.bean.ScheduleCompleteBean;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.ui.AddWandrActivity;
import com.zylbaby.app.util.ArrayUtils;
import com.zylbaby.app.util.DateTimeDialog;
import com.zylbaby.app.util.DimensionUtility;

import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.view.MultiChoicePopWindow;
import com.zylbaby.app.view.SingleChoicePopWindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CompleteScheduleActivity  extends BasePhotoActivity{
	
	
	private String Id;
	private Button select_babys;
	private ImageButton add_start_date_btn;
	private ImageButton add_stop_date_btn;
	private EditText add_wandr_name_edit;
	private EditText add_wandr_name1_edit;
	private ImageView add_wandr_back_btn;
	private TextView selectbabyid;
	private Button btn_save;
	private EditText add_wandr_beizhu_edit;
	private int scheduleId;
	private ImageView scheduletype;

	 
	 
	 
	 	private GridView noScrollgridview;
		private GridAdapter adapter;
		private View parentView;
		private PopupWindow pop = null;
		private LinearLayout ll_popup;
		public static Bitmap bimap ;
		public DateTimeDialog datetimedialog= new DateTimeDialog();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teacher_complete_schedule);
		add_start_date_btn = (ImageButton) findViewById(R.id.add_start_date_btn);
		add_stop_date_btn = (ImageButton) findViewById(R.id.add_stop_date_btn);
		add_wandr_name_edit = (EditText) findViewById(R.id.add_wandr_name_edit);
		add_wandr_name1_edit = (EditText) findViewById(R.id.add_wandr_name1_edit);
		//add_wandr_back_btn=(ImageView)findViewById(R.id.add_baby_back_btn);
		selectbabyid=(TextView)findViewById(R.id.selectbabyid);
		scheduletype=(ImageView)findViewById(R.id.diarytype_img);
		btn_save=(Button)findViewById(R.id.add_wandr_save_btn);
		add_wandr_back_btn=(ImageView) findViewById(R.id.add_wandr_back_btn);
		add_wandr_beizhu_edit=(EditText) findViewById(R.id.add_wandr_beizhu_edit);
		Bundle bundle = this.getIntent().getExtras();  
		          
		       /*获取Bundle中的数据，注意类型和key*/  
		int k = bundle.getInt("Id");
		scheduleId=k;
		        
		try {
			LoadScheduleById(String.valueOf(k)); //加载作息内容
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
		
		
		Init();
		initData1();	
		
		select_babys=(Button) findViewById(R.id.select_babys);
		//多选学生
		select_babys.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMultiChoiceWindow();
				
			}
		});
		
		add_start_date_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				datetimedialog.showTime(CompleteScheduleActivity.this, add_wandr_name_edit);
				
			}
		});
		add_stop_date_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				datetimedialog.showTime(CompleteScheduleActivity.this, add_wandr_name1_edit);
				
			}
		});
		
		add_wandr_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
		//保存
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ScheduleCompleteBean bean= new ScheduleCompleteBean();
				bean.setBabyid(selBabyId);
				bean.setId(scheduleId);
				bean.setContent(add_wandr_beizhu_edit.getText().toString());
				bean.setStarttime(add_wandr_name_edit.getText().toString());
				bean.setEndtime(add_wandr_name1_edit.getText().toString());
				bean.setPiclist(" ");
				bean.setRank("3");
				
				
				
				
				List<File> files = new ArrayList<File>();
				
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring( 
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					
					File f=new File(Bimp.drr.get(i));
					files.add(f);
									
				}
				
				
				bean.setUploadfile(files);//上传文件赋值
				getpiclist.clear();
				try {
					SaveCompleteData(bean);
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
		});
		
		//回退
//		add_wandr_back_btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//				
//			}
//		});
		
		
		

		
		
		
		super.onCreate(savedInstanceState);
	}
	private ArrayAdapter<String> m_adapter; 
	private ArrayList<String> getpiclist=new ArrayList<String>();

	/**
	 * popwindows 学生多选
	 * 
	 */
	public void showMultiChoiceWindow() {
		mMultiChoicePopWindow.show(true);

	}
	
	private final static int COUNT = 3;
	private List<String> mSingleDataList;
	private List<BabyBean> mMultiDataList;
	private View mRootView;
	private Context mContext;
	private SingleChoicePopWindow mSingleChoicePopWindow;
	private MultiChoicePopWindow mMultiChoicePopWindow;

	public void initData1() {
		mContext = this;
		mRootView=findViewById(R.id.complete_wandr_view);
		mMultiDataList = new ArrayList<BabyBean>();
		
		try {
			LoadData(1,"");
			
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
		
		
		

		//initDialog(booleans);

	}
	/**
	 * 初始话 popwindows
	 * @param booleans
	 */
	private String selBabyId;
	private String starsStr;
	private String selBabyName;
	public void initPopWindow(boolean[] booleans) {

		
		mMultiChoicePopWindow = new MultiChoicePopWindow(this, mRootView,
				mMultiDataList, booleans,1);
		mMultiChoicePopWindow.setTitle("班级学生");
		mMultiChoicePopWindow.setOnOKButtonListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
				//ArrayList<BabySelectStars> liststars=mMultiChoicePopWindow.getStars();
				HashMap liststars=mMultiChoicePopWindow.getSelectStars();
				
				int size = liststars.size();
				StringBuffer stringBuffer = new StringBuffer();
				StringBuffer stringBuffer1 = new StringBuffer();
				StringBuffer stringBuffer2 = new StringBuffer();
				
				//遍历HashMap的另一个方法  
		        Set<Entry<Integer, Integer>> sets = liststars.entrySet();  
		        for(Entry<Integer, Integer> entry : sets) {  
		            int k=entry.getKey();
		        	//System.out.print(String.valueOf(k) + ", ");  
		        	stringBuffer2.append(mMultiDataList.get(k).getBname()+", ");
		            //System.out.println(entry.getValue());  
		        	stringBuffer1.append(entry.getValue()+",");
		        	stringBuffer.append(mMultiDataList.get(k).getId()+",");
		        } 
								
				
				selBabyId=stringBuffer.toString();
				starsStr=stringBuffer1.toString();
				selBabyName=stringBuffer2.toString();
				
				Toast.makeText(mContext,
						"selItems = " + stringBuffer1.toString()+"||"+stringBuffer.toString(),
						Toast.LENGTH_SHORT).show();
				selectbabyid.setText(selBabyName);
			}
		});
	}

	
	
	
	
	
	
	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	//private static final int TAKE_PICTURE = 0x000001;

//	public void photo() {
//		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		startActivityForResult(openCameraIntent, TAKE_PICTURE);
//	}
	
	

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
	
	
	
	public void LoadData(final int Id,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(CompleteScheduleActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					
					mMultiDataList=(List<BabyBean>) msg.obj;
					boolean booleans[] = new boolean[COUNT * 5];
					initPopWindow(booleans);
				} else if (msg.what == -1) {
					
					Toast.makeText(CompleteScheduleActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) CompleteScheduleActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					
//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("day", Day);

		            ArrayList<BabyBean> listdaydiary = ac.getBanjibabyList(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = listdaydiary;
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
	
	
	
	
	public void SaveCompleteData(final ScheduleCompleteBean bean) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(CompleteScheduleActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					Toast.makeText(CompleteScheduleActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
					ClearBimp();
					Bimp.drr.clear();
					Bimp.max = 0;
					FileUtils.deleteDir();
					
					
				} else if (msg.what == -1) {
					
					Toast.makeText(CompleteScheduleActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) CompleteScheduleActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("Id", bean.getId());
					paramMap.put("StartTime", bean.getStarttime());
					paramMap.put("EndTime", bean.getEndtime());
					paramMap.put("Rand", bean.getRank());
					paramMap.put("Content", bean.getContent());
					paramMap.put("BabyId", bean.getBabyid());
					paramMap.put("PicList", bean.getPiclist());
					paramMap.put("Stars", starsStr);
					
					
					Map<String, File> paramMap1 = new HashMap<String, File>();
					if(bean.getUploadfile().size()==0){
							paramMap1 = null;
					}else{
						for(int i=0 ; i<bean.getUploadfile().size(); i++){
							System.out.println("UploadPic"+i);
							paramMap1.put("CompleteUploadPic"+i,bean.getUploadfile().get(i));
						}
					}
					


		            int k = ac.saveScheduleComplete(paramMap,paramMap1);
					// 发出HTTP request
		            
		            if (k==0) {
						msg.what = 0;// 成功
						msg.obj = "修改成功";
//						ClearBimp();
//						Bimp.drr.clear();
//						Bimp.max = 0;
//						FileUtils.deleteDir();
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
	
	
	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.add_wandr_uploadimage_list);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//adapter = new GridAdapter(this);
		adapter=new GridAdapter(CompleteScheduleActivity.this,handler);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					//传入标示，返回那个Activity
					new PopupWindows(CompleteScheduleActivity.this, noScrollgridview,"completeschedule");
				} else {
					Intent intent = new Intent(CompleteScheduleActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		
	}
	
	
	
	
	public void LoadScheduleById(final String Id) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(CompleteScheduleActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					ScheduleDto dto=(ScheduleDto)msg.obj;
					add_wandr_name_edit.setText(dto.getStarttime());
					add_wandr_name1_edit.setText(dto.getEndtime());
					add_wandr_beizhu_edit.setText(dto.getSchedulecontext());
					
					int k=ArrayUtils.GetPosition(AppConstant.diarytypeid_array, StringUtils.toInt(dto.getScheduletypeid()));
					scheduletype.setImageResource(AppConstant.diaryioc_array[k]);
					
					
				} else if (msg.what == -1) {
					
					Toast.makeText(CompleteScheduleActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) CompleteScheduleActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					
//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("Id", Id);
		            paramMap.put("Id", Id);
		            ScheduleDto schedule = ac.getScheduleById(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = schedule;
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
