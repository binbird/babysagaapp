package com.zylbaby.app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.adapter.BanjiAdapter;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SearchBanjiListActivity extends Activity implements BanjiAdpterOnItemClick{
	
	ArrayList<BanjiDto> list=null;
	ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.return_searchbanji_activity);
		listview=(ListView) findViewById(R.id.searchbanji_listview);
		
		Intent intent=getIntent();
		String banjiname=intent.getStringExtra("banjiname").toString();
		String banjiid=intent.getStringExtra("banjiId").toString();
		
		
		try {
			LoadData(banjiname,banjiid);
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
		
		//Toast.makeText(this, banjiname, Toast.LENGTH_LONG).show();
	}
	
	
	

	public void LoadData(final String banjiname,final String banjiid) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(SearchBanjiListActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					list=(ArrayList<BanjiDto>) msg.obj;

					BanjiAdapter myAdpter =new BanjiAdapter(SearchBanjiListActivity.this,list);
					myAdpter.onListener(SearchBanjiListActivity.this);

					listview.setAdapter(myAdpter);
				} else if (msg.what == -1) {
					
					Toast.makeText(SearchBanjiListActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) SearchBanjiListActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("ClassName", banjiname);
					paramMap.put("ClassCode", banjiid);
					

		            ArrayList<BanjiDto> listbanji = ac.getBanji(paramMap);
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




	public void JoinBanji(final int banjiid,final String password) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					Toast.makeText(SearchBanjiListActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					Toast.makeText(SearchBanjiListActivity.this, "成功加入班级", Toast.LENGTH_SHORT).show();
				} else if (msg.what == -1) {
					Toast.makeText(SearchBanjiListActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}else if(msg.what==-1003){
					Toast.makeText(SearchBanjiListActivity.this, "班级密码输入错误", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) SearchBanjiListActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("ClassId", banjiid);
					paramMap.put("Password", password);
					

		           int k = ac.JoinBanji(paramMap);
					// 发出HTTP request
		           	msg.what=k;
		           
		            
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
	public void onAdpterClick(int which, final int postion) {
		switch (which) {
		case R.id.banji_joinbtn:
			
			//PerSon person = arrayPerson.get(postion);
			//person.setAge(person.getAge()+1);
			//myAdpter.notifyDataSetChanged();这里如果点击速度过快的话，getView重绘时会使数据加载混乱，所以不能用myAdpter.notifyDataSetChanged();
			//应该写一个独立的方法 局部刷新
			
			
			final EditText inputServer = new EditText(this);
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("输入班级密码").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
	                .setNegativeButton("取消", null);
	        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	            	inputServer.getText().toString();
	            	
	            	
	            	//----------------------------
	            	
	            	
	            	String str_password=inputServer.getText().toString();
	    			int k=list.get(postion).getId();
	    			Integer i= new Integer(k);
	    			Toast.makeText(SearchBanjiListActivity.this, "班级Id："+i.toString(), Toast.LENGTH_LONG).show();
	    			
	    			try {
	    				JoinBanji(k,str_password);
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
	            	//----------------------------
	            	
	            	
	            	
	             }
	        });
	        builder.show();
			
			
			
	        
			break;
		default:
			break;
		}
		
	}
	

	

}
