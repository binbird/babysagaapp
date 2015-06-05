package com.zylbaby.app.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import com.zylbaby.app.R;

import com.zylbaby.app.adapter.InfoItemAdapter;

import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.InfoItem;

import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppException;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class AddInfoActivity extends Activity{

	private int touser;
	private int infofrom;
	
	private EditText text;
	private Button btnsend;
	private PullToRefreshListView mPullRefreshListView;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_info_activity_new);
		//text=(EditText) findViewById(R.id.editText1);
		Init();
		ListInfoItem(0,touser);
		btnsend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				InfoItem item= new InfoItem();
				item.setByuser(touser);//接口会设置值
				item.setTouser(touser);//发送给谁消息
				item.setContent(text.getText().toString());
				if(text.getText().toString()!=""){
					SendInfo(item);
				}else
				{
					Toast.makeText(AddInfoActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		final ListView actualListView = mPullRefreshListView.getRefreshableView();  
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				ListInfoItem(0,touser);
				new GetDataTask().execute();  
				//mPullRefreshListView.onRefreshComplete();  
				
			}  

        });
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//Toast.makeText(AddInfoActivity.this, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		
		if (!(actualListView).isStackFromBottom()) {
			actualListView.setStackFromBottom(false);
		}
		actualListView.setStackFromBottom(true);
		
	}
	
	
	private void Init(){
		Intent i = getIntent();
		touser = i.getIntExtra("touser", 0);  
		infofrom=i.getIntExtra("infofrom", 1);
		text=(EditText) findViewById(R.id.editText1);
		btnsend=(Button) findViewById(R.id.button1);
		
	}
	
	
	
	// 登录验证
	private void SendInfo(final InfoItem item) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					
					
				} else if (msg.what == 0) {
//					pd.dismiss();
//					UIHelper.ToastMessage(AddbabyActivity.this,
//						   "添加失败" + msg.obj);
					Toast.makeText(AddInfoActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
					text.setText("");
					//判断是教师的列表，还是班级的列表
					ListInfoItem(0,touser);
				} else if (msg.what == -1) {
//					pd.dismiss();
//					((AppException) msg.obj).makeToast(AddbabyActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					InfoItem info=item;
					AppContext ac = (AppContext) getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					
					paramMap.put("FromUser", info.getByuser());
					paramMap.put("ToUser", info.getTouser());
					paramMap.put("Content", info.getContent());
					paramMap.put("InfoType",0);
					
					
					int k=ac.addInfo(paramMap,infofrom);
					
					if (k==0) {
						msg.what = 0;// 成功

					} else {
						ac.cleanLoginInfo();// 清除登录信息
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
	
	private List<InfoItem> listinfoitem;
	
	private void ListInfoItem(final int byuser,final int touser){
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(AddInfoActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					listinfoitem=(List<InfoItem>) msg.obj;
					
//					AppContext ac = (AppContext) getApplication();
//					int k=0;
//					try {
//						BabyBean user=ac.getBabyInfo();
//						k=user.getId();
//					} catch (AppException e) {
//						k=0;
//					}
					
					InfoItemAdapter myAdpter =new InfoItemAdapter(AddInfoActivity.this,listinfoitem,touser);
					
					mPullRefreshListView.setAdapter(myAdpter);
					
					//listView.setAdapter(new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, mDataSourceList));
					
				} else if (msg.what == -1) {
					
					Toast.makeText(AddInfoActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) AddInfoActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("ToUser", touser);
					
     
					

//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("day", Day);

		            ArrayList<InfoItem> infoitemlist = ac.getInfoItemList(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = infoitemlist;
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
