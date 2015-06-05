package com.baby.common.ui;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loveplusplus.demo.image.ImagePagerActivity;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter;
import com.zylbaby.app.adapter.HelpListAdapter;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.global.AppConstant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HelpListActivity extends Activity {

	private PullToRefreshListView mPullRefreshListView;
	private ArrayList<String> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_list_activity);
		mPullRefreshListView=(PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		final ListView actualListView = mPullRefreshListView.getRefreshableView(); 
		list= new ArrayList<String>();
		for (String s : AppConstant.help_title) {
			list.add(s);
		}
		
		
		HelpListAdapter myAdpter =new HelpListAdapter(HelpListActivity.this,list);
		//myAdpter.onListener(this);
		mPullRefreshListView.setAdapter(myAdpter);
		
		
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				LoadHelpPicData(arg3);
				//ImagePagerActivity
			}
		});
	}

	private void LoadHelpPicData(long arg3){
		Intent intent = new Intent(HelpListActivity.this, HelpPicShowActivity.class);
		intent.putExtra("images", AppConstant.help_pic[(int) arg3]);
		startActivity(intent);
	}
}
