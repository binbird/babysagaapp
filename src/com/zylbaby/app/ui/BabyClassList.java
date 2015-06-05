package com.zylbaby.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyClassListAdapter;
import com.zylbaby.app.adapter.BanjiAdapter;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class BabyClassList extends Activity implements BanjiAdpterOnItemClick{
	
	private ImageView backbtn;
	ArrayList<BanjiDto> list=null;
	ListView listview;
	
	private PullToRefreshListView pull_refresh_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baby_inclass_list);
		init();
		LoadData(0);
	}
	
	private void init(){
		backbtn=(ImageView) findViewById(R.id.back_btn);
		pull_refresh_list=(PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		backbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		final ListView actualListView = pull_refresh_list.getRefreshableView();
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int k=list.get(Integer.parseInt(String.valueOf(arg3))).getId();
				Integer i= new Integer(k);
				Toast.makeText(BabyClassList.this, "班级Id："+i.toString(), Toast.LENGTH_LONG).show();

				Intent intent = new Intent(BabyClassList.this ,ClassScheduleActivity.class);
				intent.putExtra("classid", i);
				startActivityForResult(intent, 1234);
				
			}
		});
		
	}
	
	private void LoadData(final int id){
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(BabyClassList.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					list=(ArrayList<BanjiDto>) msg.obj;

					BabyClassListAdapter myAdpter =new BabyClassListAdapter(BabyClassList.this,list);
					//myAdpter.onListener(BabyClassList.this);

					pull_refresh_list.setAdapter(myAdpter);
				} else if (msg.what == -1) {
					
					Toast.makeText(BabyClassList.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) BabyClassList.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("babyid", id);
					
		            ArrayList<BanjiDto> listbanji = ac.getBanjiByBabyId(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = listbanji;
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
	public void onAdpterClick(int which, int postion) {
		
		int k=list.get(postion).getId();
		Integer i= new Integer(k);
		Toast.makeText(BabyClassList.this, "班级Id："+i.toString(), Toast.LENGTH_LONG).show();

		Intent intent = new Intent(BabyClassList.this ,ClassScheduleActivity.class);
		intent.putExtra("classid", i);
		startActivityForResult(intent, 1234);
	}

	
}
