package com.teacher.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BanjiBabyAdapter;
import com.zylbaby.app.adapter.ScheduleAdapter;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BanjiBabyAndInOutBean;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.ui.LoginActivity;
import com.zylbaby.app.ui.SearchBanjiListActivity;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;
import com.zylbaby.app.view.BanjiAdpterOnTimeSelect;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TClassFragment extends Fragment implements BanjiAdpterOnItemClick,BanjiAdpterOnTimeSelect {
	
	
	private List<BanjiBabyAndInOutBean> babylist;
	private GridView babygrid;
	
//	private DisplayImageOptions options;  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.teacher_main_fragment_class, null);
		
		babygrid=(GridView) view.findViewById(R.id.gridview_banjibaby);
		GetData();
		return view;
	}
	
	
	private void GetData(){
		try {
			LoadData(0,"");
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

	public void LoadData(final int Id,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(getActivity(), "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					//schedulelist=(List<ScheduleDto>) msg.obj;
					
					babylist=(List<BanjiBabyAndInOutBean>)msg.obj;
					
					BanjiBabyAdapter myAdpter =new BanjiBabyAdapter(getActivity(),babylist);
					
					//myAdpter.onListener(getActivity());
					
					myAdpter.onListener(TClassFragment.this);
					myAdpter.onTimeListener(TClassFragment.this);
					babygrid.setAdapter(myAdpter);
					
					
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
		            ArrayList<BanjiBabyAndInOutBean> listdaydiary = ac.getBanjibabyListAndInOut(paramMap);
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


	@Override
	public void onAdpterClick(int which, int postion) {
		int babyid=babylist.get(postion).getId();
		switch (which) {
//		case R.id.btn_daoxiao:
//			//Toast.makeText(getActivity(), "到校时间"+String.valueOf(postion), Toast.LENGTH_SHORT).show();
//			
//			Button b=(Button)getActivity().findViewById(R.id.btn_daoxiao);
//			
//			Toast.makeText(getActivity(), "到校时间"+b.getText().toString(), Toast.LENGTH_SHORT).show();
//			showTime(1,b,babyid);
//			
//			
//			break;
//		case R.id.btn_lixiao:
//			
//			Button b1=(Button)getActivity().findViewById(R.id.btn_lixiao);
//			
//			Toast.makeText(getActivity(), "离校时间"+b1.getText().toString(), Toast.LENGTH_SHORT).show();
//			
//			showTime(2,b1,babyid);
//			
//			
//			//AddInOutSchoolTime(babyid,"",2);
//			break;
		case R.id.baby_img:
			Intent intent = new Intent(getActivity(),BabyDiaryListActivity.class);
			intent.putExtra("babyid", babyid);
			startActivity(intent);
			break;
		
		default:
			break;
		}
		
	}
	
	
	private void AddInOutSchoolTime(final int babyid,final String time,final int type) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(getActivity(), "记录成功", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					Toast.makeText(getActivity(), String.valueOf(msg.obj) , Toast.LENGTH_SHORT).show();
				} else if (msg.what == -1) {
					Toast.makeText(getActivity(), String.valueOf(msg.obj) , Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext)getActivity().getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("BabyId",babyid);
					paramMap.put("Time", time);
					paramMap.put("InputType", type);


					int k = ac.InOutSchoolTime(paramMap); //增加日程
					//String res = baby.getStatusCode();
					if (k==0) {
						msg.what = 1;// 成功
					} else {
						//ac.cleanLoginInfo();// 清除登录信息
						msg.what = 0;// 失败
						msg.obj ="新增错误";
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
	
	
	
	
	private TimePickerDialog timePickerDialog;
	private Calendar calendar;
	private void showTime(final int sign,final Button v,final int id) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				v.setText(arg1 + ":" + arg2);
				if (sign == 1) {
					AddInOutSchoolTime(id,v.getText().toString(),1);
				}else{
					AddInOutSchoolTime(id,v.getText().toString(),2);
				}
			}

		};
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}


	@Override
	public void onTimeSelect(int which, int postion, String time) {
		int babyid=babylist.get(postion).getId();
		switch (which) {
		case R.id.btn_daoxiao:
			//Toast.makeText(getActivity(), "到校时间"+String.valueOf(postion), Toast.LENGTH_SHORT).show();
			
			Button b=(Button)getActivity().findViewById(R.id.btn_daoxiao);
			
			Toast.makeText(getActivity(), "到校时间"+b.getText().toString(), Toast.LENGTH_SHORT).show();
//			AddInOutSchoolTime(babyid,time,1);
			
			break;
		case R.id.btn_lixiao:
			
			Button b1=(Button)getActivity().findViewById(R.id.btn_lixiao);
			
			Toast.makeText(getActivity(), "离校时间"+b1.getText().toString(), Toast.LENGTH_SHORT).show();
//			AddInOutSchoolTime(babyid,time,2);
			break;

		
		default:
			break;
		
	}
	}

}
