package com.zylbaby.app.util;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * 日期时间对话框
 * @author binbird
 *
 */
public class DateTimeDialog {

	private Calendar calendar;
	private TimePickerDialog timePickerDialog;
	private DatePickerDialog datePickerDialog;
	/**
	 * 显示日期选择对话框
	 * @param context
	 * @param showdate
	 */
	public void showDate(Context context,final EditText showdate) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		
		DatePickerDialog.OnDateSetListener onDateSetListener= new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
				
				showdate.setText(arg1 + "-" + (arg2+1) + "-" + arg3);
			}
		};
		

		calendar = Calendar.getInstance();
		//calendar.setTimeInMillis(System.currentTimeMillis());
		
		Date mydate=new Date(); //获取当前日期Date对象
		calendar.setTime(mydate);////为Calendar对象设置时间为当前日期
//		datePickerDialog = new DatePickerDialog(this, onDateSetListener,
//				calendar.get(Calendar.DAY_OF_MONTH),
//				calendar.get(Calendar.MINUTE), true);,
		datePickerDialog=new DatePickerDialog(context, onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));		
		
		datePickerDialog.show();
	}
	/**
	 * 选择时间对话框
	 * @param context
	 * @param showtime
	 */
	public void showTime(Context context,final EditText showtime) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
			
					showtime.setText(arg1 + ":" + arg2);
				
			}

		};
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePickerDialog = new TimePickerDialog(context, onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}

}
