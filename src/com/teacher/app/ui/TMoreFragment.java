package com.teacher.app.ui;

import com.zylbaby.app.R;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.util.UpdateManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;



public class TMoreFragment  extends Fragment{
View view = null;
	
	private RelativeLayout babymanage_item1;
	private RelativeLayout updatesoft_item2;
	private RelativeLayout about_item3;
	private RelativeLayout searchclass_item;
	private Button outbtn;
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.teacher_main_fragment_more, null);
		initView();
		initData();
		return view;
	}
	
	private void initView(){
		babymanage_item1 = (RelativeLayout) view.findViewById(R.id.babymanage_item1);
		updatesoft_item2 = (RelativeLayout) view.findViewById(R.id.updatesoft_item2);
		about_item3 = (RelativeLayout) view.findViewById(R.id.about_item3);
		searchclass_item=(RelativeLayout)view.findViewById(R.id.searchclass_item3);
		outbtn=(Button) view.findViewById(R.id.login_or_out);
	}
	
	private void initData(){
		babymanage_item1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//UIHelper.showhomepage(this);
				UIHelper.showTeacherInfo(getActivity(), "1");
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
				UIHelper.showCreateClass(getActivity(), "102");
				
			}
		});
		
		outbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AppContext appContext = (AppContext)getActivity().getApplication();
				appContext.Logout();
				AppManager.getInstance().AppExit(getActivity());
				getActivity().finish();
				
			}
		});
	}
}
