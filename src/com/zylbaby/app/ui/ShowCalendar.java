package com.zylbaby.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.TimeTag;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.view.KCalendar;
import com.zylbaby.app.view.KCalendar.OnCalendarClickListener;
import com.zylbaby.app.view.KCalendar.OnCalendarDateChangedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCalendar extends Activity {
	String date=null;
	public List<String> list = new ArrayList<String>(); //设置标记列表
	private KCalendar calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int month1=0;
		setContentView(R.layout.activitywindow_calendar);
		final TextView popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		calendar=(KCalendar) findViewById(R.id.popupwindow_calendar);
		
				
		Button popupwindow_calendar_bt_enter = (Button)findViewById(R.id.popupwindow_calendar_bt_enter); 

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");

		if (null != date) {

			int years = Integer.parseInt(date.substring(0,
					date.indexOf("-")));
			int month = Integer.parseInt(date.substring(
					date.indexOf("-") + 1, date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);	
			month1=month;
		}
		
		
		String temp=String.format("%02d", calendar.getCalendarMonth());
		
		
		ListTimeTag(String.valueOf(temp));
		//calendar.addMarks(list, 1);
		

		//ListTimeTag(String.valueOf(calendar.getCalendarMonth()));
		
		
		//监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));
				
				if (calendar.getCalendarMonth() - month == 1//跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();
					
				} else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();
					
				} else {
					calendar.removeAllBgColor(); 
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;//最后返回给全局 date
				}
			}
		});

		//监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month
						.setText(year + "年" + month + "月");

				String temp=String.format("%02d", month);
				ListTimeTag(String.valueOf(temp));
				//calendar.addMarks(list, 1);
				
				
				
			}
		});
		
		//上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.lastMonth();
					}

				});
		
		//下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.nextMonth();
					}
				});
		
		//关闭窗口
		popupwindow_calendar_bt_enter
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						Toast.makeText(ShowCalendar.this, date, Toast.LENGTH_LONG).show();
						
						Intent intent = new Intent(); 
	                    Bundle bundle = new Bundle(); 
	                    bundle.putString("selectdate", date); 
	                    intent.putExtras(bundle); 
	                    
	                    setResult(Activity.RESULT_OK, intent); 

						
						finish();
						//dismiss();
					}
				});
		
	}
	
	ArrayList<TimeTag> listtime;
	
	private void ListTimeTag(final String month){
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(ShowCalendar.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					listtime=(ArrayList<TimeTag>) msg.obj;
					for(int i=0;i<listtime.size();i++){
						list.add(listtime.get(i).getTime());
					}
					listtime.clear();
					calendar.addMarks(list, 1);
					
					
				} else if (msg.what == -1) {
					
					//Toast.makeText(ShowCalendar.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) ShowCalendar.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("MonthNum", month);


		            ArrayList<TimeTag> listdaydiary = ac.getTimeTag(paramMap);
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
