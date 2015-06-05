package com.zylbaby.app.ui;

import com.zylbaby.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SearchBanjiActivity extends Activity{
	
	EditText banjiName;
	EditText schoolName;
	Button searchBtn;
	ImageView btn_back;
	EditText banjiId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_banji_activity);
		
		banjiName=(EditText) findViewById(R.id.banji_name);
		searchBtn=(Button) findViewById(R.id.search_banji_btn);
		banjiId=(EditText) findViewById(R.id.banji_ID);
		btn_back=(ImageView) findViewById(R.id.back_btn);
		
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(SearchBanjiActivity.this, SearchBanjiListActivity.class);
				intent.putExtra("banjiname", banjiName.getText().toString());
				intent.putExtra("banjiId", banjiId.getText().toString());
				intent.putExtra("banjiSchool", banjiName.getText().toString());
				
				startActivity(intent);
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
	}

}
