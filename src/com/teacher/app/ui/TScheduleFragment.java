package com.teacher.app.ui;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.adapter.ScheduleAdapter;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.global.AppContext;

import com.zylbaby.app.ui.ShowCalendar;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;
import com.zylbaby.app.view.BanjiScheduleOnItemDeleteClick;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class TScheduleFragment extends Fragment implements BanjiAdpterOnItemClick,BanjiScheduleOnItemDeleteClick  {

String date = null;
	
//	private CalendarPickerView calendar;
	Button addbtn;
	Button showdatebtn;
	TextView viewdate;
	//CalendarPickerView calendar;
	//private static ListView schedulelistView=null;
	private PullToRefreshListView mPullRefreshListView; 
	private List<ScheduleDto> schedulelist=new ArrayList<ScheduleDto>();
	public HttpPost httpRequest=null;
	public HttpResponse httpResponse; 
	
	int year, monthOfYear, dayOfMonth;
	private void Init(){
      
		Calendar calendar = Calendar.getInstance();
      year = calendar.get(Calendar.YEAR);
      monthOfYear = calendar.get(Calendar.MONTH);
      dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

	}
	
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacher_main_fragment_schedule, null);
		Init();
		final Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

	    final Calendar lastYear = Calendar.getInstance();
	    lastYear.add(Calendar.YEAR, -1);
		
		viewdate=(TextView) view.findViewById(R.id.view_date);
		addbtn = (Button) view.findViewById(R.id.new_schedule);
		showdatebtn=(Button) view.findViewById(R.id.date_show_btn);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		//schedulelistView=(ListView) view.findViewById(R.id.schedulelistView);

		final ListView actualListView = mPullRefreshListView.getRefreshableView();  
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				getData("");
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
						menu.add(0, 0, 0, "日程完成");
						menu.add(0, 1, 0, "删除");
						menu.add(0, 2, 0, "查看");
						selectedMenuInfo = (AdapterContextMenuInfo) menuInfo;
						
					}
				});
				
				return false;
			}
		});
		
		
		
		
		getData("");
		addbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(),AddScheduleActivity.class);
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
	private void getData(String s){
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			String date="";
			if(s==""){
				date=sdf.format(new java.util.Date());
			}else
			{
				date=s;
			}
			viewdate.setText(date);
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
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "返回结果", Toast.LENGTH_LONG).show();
		
		if(requestCode >= 0){  
            String s=data.getStringExtra("selectdate");
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            viewdate.setText(s);
            getData(s);
        } 
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void LoadData(final int Id,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(getActivity(), "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					schedulelist=(List<ScheduleDto>) msg.obj;
					ScheduleAdapter myAdpter =new ScheduleAdapter(getActivity(),schedulelist,2);
					mPullRefreshListView.setAdapter(myAdpter);
					myAdpter.onListener(TScheduleFragment.this); // 必须有的 ********
					myAdpter.onDelListener(TScheduleFragment.this); // 必须有的 ********
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
					


		            ArrayList<ScheduleDto> listdaydiary = ac.getDaySchedule(paramMap);
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
	
	
	private AdapterContextMenuInfo selectedMenuInfo = null; 
	

	
	
	
	// 长按菜单响应函数
		public boolean onContextItemSelected(MenuItem item) {
			
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		    if(info == null){
		           info = selectedMenuInfo;
		       }
			
			int MID = (int) info.id;// 这里的info.id对应的就是数据库中_id的值
			String name=schedulelist.get(MID).getEntrytime().toString();
			switch (item.getItemId()) {
			case 0:
				// 添加操作
				Toast.makeText(getActivity(),
						"日程完成--"+String.valueOf(name),
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(getActivity() ,CompleteScheduleActivity.class);
				
				intent.putExtra("tag", "12");
				intent.putExtra("Id", schedulelist.get(MID).getId());

				startActivity(intent);
				break;

			case 1:
				Toast.makeText(getActivity(),
						"删除--"+String.valueOf(name),
						Toast.LENGTH_SHORT).show();
				break;

			case 2:
				Toast.makeText(getActivity(),
						"查看--"+String.valueOf(name),
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

			return super.onContextItemSelected(item);

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
//				mListItems.addFirst("Added after refresh...");
//				mAdapter.notifyDataSetChanged();

				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();

				super.onPostExecute(result);
			}
		}

		@Override
		public void onAdpterClick(int which, int postion) {
			int babyid=schedulelist.get(postion).getId();
			switch (which) {
			case R.id.over_btn:
				Toast.makeText(getActivity(), "完成的日程Id："+String.valueOf(postion), Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(getActivity() ,CompleteScheduleActivity.class);
				
				intent.putExtra("tag", "12");
				intent.putExtra("Id", schedulelist.get(postion).getId());

				startActivity(intent);
				
				break;
			
			
			default:
				break;
			}
			
		}


		@Override
		public void onAdpterDeleteClick(int which, int postion) {
			int babyid=schedulelist.get(postion).getId();
			switch (which) {
			case R.id.del_btn:
				Toast.makeText(getActivity(), "删除的日程Id："+String.valueOf(postion), Toast.LENGTH_SHORT).show();
				
//				Intent intent = new Intent(getActivity() ,CompleteScheduleActivity.class);
//				
//				intent.putExtra("tag", "12");
//				intent.putExtra("Id", schedulelist.get(postion).getId());
//
//				startActivity(intent);
				
				break;
			
			
			default:
				break;
			}
			
		}
	
	
}
