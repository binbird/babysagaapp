package com.zylbaby.app.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.loveplusplus.demo.image.ImagePagerActivity;
import com.loveplusplus.demo.image.T;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.adapter.BabyLifeAdapter;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.global.AppContext;

public class LifeFragment extends Fragment {


	
	
String date = null;
	
//	private CalendarPickerView calendar;
	Button addbtn;
	Button showdatebtn;
	TextView viewdate;
	//CalendarPickerView calendar;
//	private static ListView DiarylistView=null;
	private PullToRefreshListView mPullRefreshListView;  
	
	private List<DiaryDto> diarylist=new ArrayList<DiaryDto>();
	private List<DiaryDto> lifediarylist=new ArrayList<DiaryDto>();
	public HttpPost httpRequest=null;
	public HttpResponse httpResponse; 
	
	int year, monthOfYear, dayOfMonth;
	private AdapterContextMenuInfo selectedMenuInfo = null; 
	private void Init(){
      
	Calendar calendar = Calendar.getInstance();
      year = calendar.get(Calendar.YEAR);
      monthOfYear = calendar.get(Calendar.MONTH);
      dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

	}
	private MainActivity parentActivity;
	private void getData(){
		try {
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
//			String date=sdf.format(new java.util.Date());  
//			viewdate.setText(date);
			date=parentActivity.GetDateTxt();
			LoadData(0,date);
			
			//LoadData(0,date);
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
		View view = inflater.inflate(R.layout.main_fragment_life, null);
		parentActivity = (MainActivity) getActivity();
		Init();
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

	    final Calendar lastYear = Calendar.getInstance();
	    lastYear.add(Calendar.YEAR, -1);
		
		viewdate=(TextView) view.findViewById(R.id.view_date);
		addbtn = (Button) view.findViewById(R.id.add_life_btn);
		showdatebtn=(Button) view.findViewById(R.id.date_show_btn);
		//DiarylistView = (ListView) view.findViewById(R.id.wandr_lists);
		//刷新列表控件
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		final ListView actualListView = mPullRefreshListView.getRefreshableView();   
		getData();
		
		
		
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
				Toast.makeText(getActivity(), String.valueOf(arg2), 3000).show();
				//ttt();
				
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
		
		// 新增生活片段
		addbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 新增生活片段
				Intent intent = new Intent(getActivity(),AddLifeActivity.class);
				//Intent intent = new Intent(getActivity(),AddLifeActivityNew.class);
				
				startActivity(intent);

			}
		});
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
	
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		Toast.makeText(getActivity(), "重新出现", 5000).show();
//	}
	
	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {

		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    if(info == null){
	           info = selectedMenuInfo;
	       }
		int MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
		int id=lifediarylist.get(MID).getDiaryid();
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
					lifediarylist.clear();
					for(int k=0;k<diarylist.size();k++){
						if(diarylist.get(k).getDiarytypeId()==100)
							lifediarylist.add(diarylist.get(k));
					}
					
					
					BabyLifeAdapter myAdpter =new BabyLifeAdapter(getActivity(),lifediarylist);
					//myAdpter.onListener(this);
//					DiarylistView.setAdapter(myAdpter);
					
					mPullRefreshListView.setAdapter(myAdpter);
					
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
					//intent.putExtra("images", T.imageUrls);picdata
					intent.putExtra("images", picdata);
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
					DiaryDto dto=	lifediarylist.get((int)index);
					
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
				Thread.sleep(4000);
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

