package com.teacher.app.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.InfoItem;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.view.MultiChoiceNoStarsPopWindow;
import com.zylbaby.app.view.MultiChoicePopWindow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TAddInfoAllActivity extends Activity {
	
	
	
	private final static int COUNT = 3;
	private Button btn_selectbaby;
	private Button btn_sendinfo;
	private EditText info;
	
	private List<BabyBean> mMultiDataList;
	private View mRootView;
	private Context mContext;
	private TextView selectbabyid;
	
	
	private TextView selectbabyname;
	private MultiChoiceNoStarsPopWindow mMultiChoicePopWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_add_info_all_activity);
		Init();
	}
	
	private void Init(){
		
		mContext = this;
		mRootView=findViewById(R.id.add_wandr_view);
		
		mMultiDataList = new ArrayList<BabyBean>();
		
		try {
			LoadData(1,"");
			
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
		
		selectbabyid=(TextView) findViewById(R.id.selectbaby_id);
		selectbabyname=(TextView) findViewById(R.id.selectbaby_name);
		btn_selectbaby=(Button) findViewById(R.id.select_baby);
		btn_sendinfo=(Button) findViewById(R.id.send_info);
		info=(EditText) findViewById(R.id.send_content);
		
		btn_selectbaby.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				showMultiChoiceWindow();
			}
		});
		
		btn_sendinfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(info.getText().toString()==""){
				Toast.makeText(TAddInfoAllActivity.this, "输入内容", Toast.LENGTH_SHORT).show();
				return;
				}
				SendInfo(selectbabyid.getText().toString(),info.getText().toString());
			}
		});
	}
	public void showMultiChoiceWindow() {
		mMultiChoicePopWindow.show(true);

	}
	
/**
	 * 初始话 popwindows
	 * @param booleans
	 */
	private String selBabyId;
	private String starsStr;
	private String selBabyName;
	public void initPopWindow(boolean[] booleans) {

		
		mMultiChoicePopWindow = new MultiChoiceNoStarsPopWindow(this, mRootView,
				mMultiDataList, booleans,1);
		mMultiChoicePopWindow.setTitle("班级学生");
		mMultiChoicePopWindow.setOnOKButtonListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
				int size = selItems.length;
				StringBuffer stringBuffer = new StringBuffer();
				StringBuffer stringBuffer1 = new StringBuffer();
				
				for (int i = 0; i < size; i++) {
					if (selItems[i]) {
						stringBuffer.append(mMultiDataList.get(i).getId() + ",");
						stringBuffer1.append(mMultiDataList.get(i).getBname() + ",");
						
					}

				}
				selBabyId=stringBuffer.toString();
				
				
				Toast.makeText(mContext,
						"selItems = " + stringBuffer1.toString(),
						Toast.LENGTH_SHORT).show();
				selectbabyid.setText(selBabyId);
				selectbabyname.setText(stringBuffer1.toString());
			}
		});
	}

	
	
	
	public void LoadData(final int Id,final String Day) throws ClientProtocolException, IOException, JSONException{
		
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				  
				if (msg.what == 1) {
					
					Toast.makeText(TAddInfoAllActivity.this, "获取数据失败0", Toast.LENGTH_SHORT).show();
				} else if (msg.what == 0) {
					
					mMultiDataList=(List<BabyBean>) msg.obj;
					boolean booleans[] = new boolean[COUNT * 5];
					initPopWindow(booleans);
				} else if (msg.what == -1) {
					
					Toast.makeText(TAddInfoAllActivity.this, "获取数据失败-1", Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					/*建立HttpPost连接*/   
					
					AppContext ac = (AppContext) TAddInfoAllActivity.this.getApplication();
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					
//					JSONObject jsonObject = new JSONObject();
//		            jsonObject.put("day", Day);

		            ArrayList<BabyBean> listdaydiary = ac.getBanjibabyList(paramMap);
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
	
	

	
	// 发送群消息
	private void SendInfo(final String toUserId,final String content) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					
					
				} else if (msg.what == 0) {
//					pd.dismiss();
//					UIHelper.ToastMessage(AddbabyActivity.this,
//						   "添加失败" + msg.obj);
					Toast.makeText(TAddInfoAllActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
					info.setText("");
					//判断是教师的列表，还是班级的列表
					//ListInfoItem(0,touser);
				} else if (msg.what == -1) {
//					pd.dismiss();
//					((AppException) msg.obj).makeToast(AddbabyActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				//InfoItem info=item;
				AppContext ac = (AppContext) getApplication();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				
//				paramMap.put("FromUser", info.getByuser());
//				paramMap.put("ToUser", info.getTouser());
//				paramMap.put("Content", info.getContent());
				
				paramMap.put("ToBabyIdStr", toUserId);
				paramMap.put("Content", content);
				paramMap.put("InfoType",2);

				
				
				int k=-1;
				try {
					k = ac.addInfoAll(paramMap,3);
				} catch (AppException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (k==0) {
					msg.what = 0;// 成功

				} else {
					//ac.cleanLoginInfo();// 清除登录信息
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	


}
