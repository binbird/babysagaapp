package com.zylbaby.app.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;





import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.loveplusplus.demo.image.T;
import com.teacher.app.ui.CompleteScheduleActivity;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.net.URLs;
import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.DatePickSelect;
import com.zylbaby.app.view.KCalendar;
import com.zylbaby.app.view.KCalendar.OnCalendarClickListener;
import com.zylbaby.app.view.KCalendar.OnCalendarDateChangedListener;

public class WandrFragment extends Fragment {
	String date = null;
	
//	private CalendarPickerView calendar;
	Button addbtn;
	Button showdatebtn;
	TextView viewdate;
	//CalendarPickerView calendar;
	private PullToRefreshListView mPullRefreshListView;  
	private List<DiaryDto> diarylist=new ArrayList<DiaryDto>();
	private List<DiaryDto> showdiarylist=new ArrayList<DiaryDto>();
	public HttpPost httpRequest=null;
	public HttpResponse httpResponse; 
	
	int year, monthOfYear, dayOfMonth;
	
	private MainActivity parentActivity;
	
	private AdapterContextMenuInfo selectedMenuInfo = null; 
	private void Init(){
      
	Calendar calendar = Calendar.getInstance();
      year = calendar.get(Calendar.YEAR);
      monthOfYear = calendar.get(Calendar.MONTH);
      dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

	}
	private void getData(){
		try {
			date=parentActivity.GetDateTxt();
			LoadData(0,date);
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
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment_wandr, null);
		parentActivity = (MainActivity) getActivity();
		Init();
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

	    final Calendar lastYear = Calendar.getInstance();
	    lastYear.add(Calendar.YEAR, -1);
		
		viewdate=(TextView) view.findViewById(R.id.view_date);
		addbtn = (Button) view.findViewById(R.id.btn_new_diary);
		showdatebtn=(Button) view.findViewById(R.id.date_show_btn);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		
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
				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		final ListView actualListView = mPullRefreshListView.getRefreshableView();   
		
		
		
		
		
		
		try {
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
//			String date=sdf.format(new java.util.Date());  
//			viewdate.setText(date);
			
			date=parentActivity.GetDateTxt();
			LoadData(0,date);
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
		addbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),AddWandrActivity.class);
				

				
				startActivity(intent);

			}
		});
		
		actualListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getActivity(), "长按事件", Toast.LENGTH_LONG).show();
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
		
//		
//		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				Toast.makeText(getActivity(), String.valueOf(arg2), 3000).show();
//				//ttt();
//
//				
//				
//				try {
//					LoadDiaryPicData(arg3);
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		
		//展示日历
		showdatebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(getActivity() ,ShowCalendar.class);
				intent.putExtra("tag", "2");
				startActivityForResult(intent, 1234);

			}
		});
		return view;
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
					Toast.makeText(getActivity(),
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
					Toast.makeText(getActivity(),
							"查看--"+String.valueOf(id),
							Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}

				return super.onContextItemSelected(item);

			}


	
	
	
	@Override
	


	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "返回结果", Toast.LENGTH_LONG).show();
		
		if(requestCode >= 0){  
            String s=data.getStringExtra("selectdate");
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            try {
            	viewdate.setText(s);
				LoadData(0,s);
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
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void LoadData(final int Id,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(getActivity(), "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					diarylist=(List<DiaryDto>) msg.obj;
					System.out.println(diarylist.toString());
					showdiarylist.clear();
					for(int k=0;k<diarylist.size();k++){
						if(diarylist.get(k).getDiarytypeId()!=100)
							showdiarylist.add(diarylist.get(k));
							//diarylist.remove(diarylist.get(k));
					}
					
					BabyDiaryAdapter myAdpter =new BabyDiaryAdapter(getActivity(),showdiarylist);
					//myAdpter.onListener(this);
					mPullRefreshListView.setAdapter(myAdpter);
					diarylist.clear();
					diarylist=null;
					
					//listView.setAdapter(new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, mDataSourceList));
					
				} else if (msg.what == -1) {
					
					Toast.makeText(getActivity(), "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) getActivity().getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("day", Day);
					
     
					

//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("day", Day);

		            ArrayList<DiaryDto> listdaydiary = ac.getDayDiary(paramMap);
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
	
	public void DelData(final int Id) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(getActivity(), "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					
					Toast.makeText(getActivity(), "删除数据成功", Toast.LENGTH_SHORT).show();
					getData();
				} else if (msg.what == -1) {
					
					Toast.makeText(getActivity(), "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) getActivity().getApplication();
					
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
	

	
	
	private void ttt() {
		Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
		//图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra("images", T.imageUrls);
		startActivity(intent);
	}
	
	public void LoadDiaryPicData(final long index) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(getActivity(), "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {

					Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
					//图片url,为了演示这里使用常量，一般从数据库中或网络中获取
					String[] picdata=(String[])msg.obj;

					
					intent.putExtra("images", picdata);
					DiaryDto dto=	showdiarylist.get((int)index);
					intent.putExtra("diarycontent", dto.getDiarycontext());
					startActivity(intent);
					
				} else if (msg.what == -1) {
					
					Toast.makeText(getActivity(), "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) getActivity().getApplication();
					
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

	
}