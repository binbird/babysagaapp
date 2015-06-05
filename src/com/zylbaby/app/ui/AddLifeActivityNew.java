package com.zylbaby.app.ui;

import java.util.ArrayList;
import java.util.List;

import com.photo.select.BasePhotoActivity;
import com.photo.select.Bimp;
import com.photo.select.FileUtils;
import com.photo.select.PhotoActivity;

import com.zylbaby.app.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AddLifeActivityNew extends BasePhotoActivity{
	
	private GridView noScrollgridview;
	//private GridAdapter adapter;
	private TextView activity_selectimg_send;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_life_activity_new);
		Init();
	}
	
	
	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//adapter = new GridAdapter(this);
		adapter=new GridAdapter(AddLifeActivityNew.this,handler);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(AddLifeActivityNew.this, noScrollgridview,"");
				} else {
					Intent intent = new Intent(AddLifeActivityNew.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				List<String> list = new ArrayList<String>();				
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring( 
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH+Str+".JPEG");				
				}
				// 高清的压缩图片全部就在  list 路径里面了
				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
				// 完成上传服务器后 .........
				FileUtils.deleteDir();
			}
		});
	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		}
	}



}
