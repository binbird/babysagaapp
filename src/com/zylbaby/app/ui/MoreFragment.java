package com.zylbaby.app.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zylbaby.app.R;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.util.UpdateManager;

public class MoreFragment extends Fragment {
	private SharedPreferences sp;
	private Editor editor;
	View view = null;
	
	private RelativeLayout babymanage_item1;
	private RelativeLayout updatesoft_item2;
	private RelativeLayout about_item3;
	private RelativeLayout searchclass_item;
	private RelativeLayout listclass_item;
	private Button outbtn;
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_fragment_more, null);
		initView();
		initData();
		return view;
	}
	
	private void initView(){
		babymanage_item1 = (RelativeLayout) view.findViewById(R.id.babymanage_item1);
		updatesoft_item2 = (RelativeLayout) view.findViewById(R.id.updatesoft_item2);
		about_item3 = (RelativeLayout) view.findViewById(R.id.about_item3);
		searchclass_item=(RelativeLayout)view.findViewById(R.id.searchclass_item3);
		listclass_item=(RelativeLayout) view.findViewById(R.id.listclass_item3);
		outbtn=(Button)view.findViewById(R.id.login_or_out);
	}
	
	private void initData(){
		
		outbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AppContext appContext = (AppContext)getActivity().getApplication();
				//------------测试第一次登陆-------------
				
				sp = getActivity().getSharedPreferences("users", getActivity().MODE_PRIVATE);
				editor=sp.edit();
				editor.putString("first", "no");
				editor.commit();
				
				//------------测试第一次登陆-------------
				appContext.Logout();
				AppManager.getInstance().AppExit(getActivity());
				getActivity().finish();
				
			}
		});
		babymanage_item1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//UIHelper.showhomepage(this);
				UIHelper.showAddBaby(getActivity(), "1");
			}
		});
		updatesoft_item2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 //判断更新
		        UpdateManager.getUpdateManager().checkAppUpdate(getActivity(), true);
			}
		});
		about_item3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		searchclass_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.showSearchClass(getActivity(), "101");
				
			}
		});
		listclass_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),BabyClassList.class);
				intent.putExtra("tag", "102");
				getActivity().startActivity(intent);
				
			}
		});
	}
	
}