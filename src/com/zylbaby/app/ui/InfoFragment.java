package com.zylbaby.app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyLifeAdapter;
import com.zylbaby.app.adapter.InfoMainAdapter;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.InfoMainBean;
import com.zylbaby.app.global.AppContext;

public class InfoFragment extends Fragment {

	ArrayList<InfoMainBean> maininfolist;
	private PullToRefreshListView mPullRefreshListView;  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main_fragment_info, null);
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
		
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Toast.makeText(getActivity(), String.valueOf(arg2), 3000).show();
				Toast.makeText(getActivity(), String.valueOf(arg3)+"查看详细", Toast.LENGTH_LONG).show();
 
				Intent intent = new Intent(getActivity(), AddInfoActivity.class);
				
				InfoMainBean dto=	maininfolist.get((int)arg3);
				intent.putExtra("touser", dto.getClassid());
				
				startActivity(intent);
			
			}
		});
		if (!(actualListView).isStackFromBottom()) {
			actualListView.setStackFromBottom(false);
		}
		actualListView.setStackFromBottom(true);

		
		return view;
		
	}
	
	private void getData(){
		try {
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
//			String date=sdf.format(new java.util.Date());  
//			viewdate.setText(date);
			//date=parentActivity.GetDateTxt();
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
					maininfolist=(ArrayList<InfoMainBean>) msg.obj;
					//lifediarylist.clear();
//					for(int k=0;k<diarylist.size();k++){
//						if(diarylist.get(k).getDiarytypeId()==100)
//							lifediarylist.add(diarylist.get(k));
//					}
					
					
					InfoMainAdapter myAdpter =new InfoMainAdapter(getActivity(),maininfolist);
					//myAdpter.onListener(this);
//					DiarylistView.setAdapter(myAdpter);
					
					mPullRefreshListView.setAdapter(myAdpter);
					
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
					//paramMap.put("day", Day);
					
 
//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("day", Day);

		            ArrayList<InfoMainBean> listinfomain = ac.getInfoMain(paramMap);
					// 发出HTTP request
		            String res="200";
		            if (res.equals("200")) {
						msg.what = 0;// 成功
						msg.obj = listinfomain;
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