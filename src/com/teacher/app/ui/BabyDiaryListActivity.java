package com.teacher.app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.bean.BabyDiaryByClass;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.ui.MainActivity;
import com.zylbaby.app.ui.ShowCalendar;
import com.zylbaby.app.ui.WandrFragment;
import com.zylbaby.app.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class BabyDiaryListActivity  extends FragmentActivity{

	public static String TAB_TAG_HOME = "home";
	
	private ImageView datebtn;
    private TextView showdatetxt;
    
    private TextView titletxt;
    private ImageView picview;
    private Button btn_Add;
    
    private int babyid=0;
    private AdapterContextMenuInfo selectedMenuInfo = null; 
    private PullToRefreshListView mPullRefreshListView;  
    private List<DiaryDto> diarylist=new ArrayList<DiaryDto>();
    private BabyDiaryByClass babydiary=new BabyDiaryByClass();
	private List<DiaryDto> showdiarylist=new ArrayList<DiaryDto>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.teacher_baby_diary_list);
		Init();
		
		
		
		
	}
	private void getData(){
		try {
			String date;
			date=BabyDiaryListActivity.this.GetDateTxt();
			LoadData(0,babyid,date);
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
	private void Init(){
		Intent intent = getIntent();
		babyid=intent.getIntExtra("babyid",0);
		
		datebtn=(ImageView) findViewById(R.id.show_date);
		showdatetxt=(TextView) findViewById(R.id.show_datetxt);
		showdatetxt.setText(StringUtils.GetDate());
		titletxt=(TextView) findViewById(R.id.titleText);
		picview=(ImageView) findViewById(R.id.BabyImage);
		
		btn_Add=(Button) findViewById(R.id.btn_new_diary);
		btn_Add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent= new Intent();
				intent.setClass(BabyDiaryListActivity.this, TAddWandrActivity.class);
				intent.putExtra("babyid", babyid);
				startActivityForResult(intent, 1005);
			}
		});
		
		
		datebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BabyDiaryListActivity.this ,ShowCalendar.class);
				intent.putExtra("tag", "2");
				startActivityForResult(intent, 1234);
				
			}
		});
		
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getData();
				new GetDataTask().execute();  
				//mPullRefreshListView.onRefreshComplete();  
				
			}  

        });
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(BabyDiaryListActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		final ListView actualListView = mPullRefreshListView.getRefreshableView();   
		
		actualListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(BabyDiaryListActivity.this, "长按事件", Toast.LENGTH_LONG).show();
				actualListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
					
					@Override
					public void onCreateContextMenu(ContextMenu menu, View arg1,
							ContextMenuInfo menuInfo) {
						menu.add(0, 1, 0, "删除");
						menu.add(0, 2, 0, "查看");
						selectedMenuInfo = (AdapterContextMenuInfo) menuInfo;
						
					}
				});
				
				return false;
			}
		});
		
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					LoadDiaryPicData(arg3);
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
		
		
		
		
		try {
			String date;
			date=BabyDiaryListActivity.this.GetDateTxt();
			LoadData(0,babyid,date);
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
	
	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {

				
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			    if(info == null){
			           info = selectedMenuInfo;
			       }
				int MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
				int id=showdiarylist.get(MID).getDiaryid();
				switch (item.getItemId()) {
				case 0:

					break;

				case 1:
					Toast.makeText(BabyDiaryListActivity.this,
							"删除--"+String.valueOf(id),
							Toast.LENGTH_SHORT).show();
					try {
						DelData(id);
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

				case 2:
					Toast.makeText(BabyDiaryListActivity.this,
							"查看--"+String.valueOf(id),
							Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}

				return super.onContextItemSelected(item);

			}


	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//加载作息列表
		String s=StringUtils.GetDate();
		
		//s=data.getStringExtra("selectdate");
		if(data!=null){
			s=data.getStringExtra("selectdate");
			showdatetxt.setText(s);
			getData();
		}
		
		


		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public void LoadData(final int Id,final int touser,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(BabyDiaryListActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					
					babydiary=(BabyDiaryByClass) msg.obj;
					diarylist=babydiary.getDiaryList();
					System.out.println(diarylist.toString());
					showdiarylist.clear();
					for(int k=0;k<diarylist.size();k++){
						if(diarylist.get(k).getDiarytypeId()!=100)
							showdiarylist.add(diarylist.get(k));
							//diarylist.remove(diarylist.get(k));
					}
					
					BabyDiaryAdapter myAdpter =new BabyDiaryAdapter(BabyDiaryListActivity.this,showdiarylist);
					//myAdpter.onListener(this);
					mPullRefreshListView.setAdapter(myAdpter);
					diarylist.clear();
					diarylist=null;
					titletxt.setText(babydiary.getBabyName());
					ImageLoader.getInstance().displayImage(babydiary.getBabyPic(), picview, AppConstant.options);
					
					//listView.setAdapter(new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, mDataSourceList));
					
				} else if (msg.what == -1) {
					
					Toast.makeText(BabyDiaryListActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext)BabyDiaryListActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("day", Day);
					paramMap.put("touser",touser );
					
					
     
					

//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("day", Day);

		            //ArrayList<DiaryDto> listdaydiary = ac.getTeacherDayDiary(paramMap);
					
					BabyDiaryByClass babydiary = ac.getTeacherDayDiary(paramMap);
					
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = babydiary;
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
	

	
	
	public String GetDateTxt(){
		String s=(String) showdatetxt.getText();
		if(s.isEmpty() ||s.equals("")){
			s=StringUtils.GetDate();
		}
		return s; 
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
//			mListItems.addFirst("Added after refresh...");
//			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	
	public void LoadDiaryPicData(final long index) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(BabyDiaryListActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {

//					diarylist=(List<DiaryDto>) msg.obj;
//					BabyDiaryAdapter myAdpter =new BabyDiaryAdapter(getActivity(),diarylist);
//					//myAdpter.onListener(this);
//					DiarylistView.setAdapter(myAdpter);
					
					//listView.setAdapter(new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, mDataSourceList));
					Intent intent = new Intent(BabyDiaryListActivity.this, ImagePagerActivity.class);
					//图片url,为了演示这里使用常量，一般从数据库中或网络中获取
					String[] picdata=(String[])msg.obj;
					
					
					//intent.putExtra("images", T.imageUrls);picdata
					intent.putExtra("images", picdata);
					DiaryDto dto=	showdiarylist.get((int)index);
					intent.putExtra("diarycontent", dto.getDiarycontext());
					startActivity(intent);
					
				} else if (msg.what == -1) {
					
					Toast.makeText(BabyDiaryListActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext)BabyDiaryListActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					DiaryDto dto=	showdiarylist.get((int)index);
					
					paramMap.put("Id", dto.getDiaryid());

					
					String[] piclist=ac.getDiaryPicList(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = piclist;
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
	
	
	
	public void DelData(final int Id) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(BabyDiaryListActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					
					Toast.makeText(BabyDiaryListActivity.this, "删除数据成功", Toast.LENGTH_SHORT).show();
					getData();
				} else if (msg.what == -1) {
					
					Toast.makeText(BabyDiaryListActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) BabyDiaryListActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("Id", Id);


		            int k = ac.delDiary(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = k;
					} else {
						//ac.cleanLoginInfo();// 清除登录信息
						msg.what = -1;// 失败
						msg.obj = -1;
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
