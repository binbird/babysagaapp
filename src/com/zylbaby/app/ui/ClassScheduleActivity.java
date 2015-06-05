package com.zylbaby.app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.teacher.app.ui.TScheduleFragment;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.ScheduleAdapter;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ClassScheduleActivity extends Activity {

	private int id;
	private PullToRefreshListView mPullRefreshListView; 
	private List<ScheduleDto> schedulelist=new ArrayList<ScheduleDto>();
	private ImageView back_btn;
	private ImageView ShowDate;
	private TextView showdatetxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baby_preschedule_list);

		Bundle bundle = this.getIntent().getExtras();  
        /*获取Bundle中的数据，注意类型和key*/  
		int k = bundle.getInt("classid");
		
		id=k;
		Init();
		showdatetxt.setText(StringUtils.GetDate());
		try {
			LoadData(id,StringUtils.GetDate());
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
		mPullRefreshListView=(PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		back_btn=(ImageView) findViewById(R.id.BabyImage);
		ShowDate=(ImageView) findViewById(R.id.show_date);
		showdatetxt=(TextView) findViewById(R.id.show_datetxt);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		ShowDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ClassScheduleActivity.this ,ShowCalendar.class);
				intent.putExtra("tag", "5");
				startActivityForResult(intent, 1234);
				
			}
		});
	}
	
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//加载作息列表
		String s=data.getStringExtra("selectdate");
		try {
			showdatetxt.setText(s);
			LoadData(id,s);
			
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


		super.onActivityResult(requestCode, resultCode, data);
	}
	

	
	
	public void LoadData(final int Id,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(ClassScheduleActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					schedulelist=(List<ScheduleDto>) msg.obj;
					ScheduleAdapter myAdpter =new ScheduleAdapter(ClassScheduleActivity.this,schedulelist,1);
					mPullRefreshListView.setAdapter(myAdpter);
					//myAdpter.onListener(ClassScheduleActivity.this); // 必须有的 ********
				} else if (msg.what == -1) {
					
					Toast.makeText(ClassScheduleActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) ClassScheduleActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("day", Day);
					paramMap.put("classid",Id);
					


		            ArrayList<ScheduleDto> listdaydiary = ac.getBabyClassSchedule(paramMap);
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
	

}
