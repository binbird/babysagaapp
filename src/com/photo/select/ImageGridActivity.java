package com.photo.select;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import com.photo.select.ImageGridAdapter.TextCallback;
import com.teacher.app.ui.CompleteScheduleActivity;
import com.teacher.app.ui.TAddWandrActivity;
import com.zylbaby.app.R;
import com.zylbaby.app.ui.AddLifeActivity;
import com.zylbaby.app.ui.AddLifeActivityNew;
import com.zylbaby.app.ui.AddWandrActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	// ArrayList<Entity> dataList;//鐢ㄦ潵瑁呰浇鏁版嵁婧愮殑鍒楄〃
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;// 鑷畾涔夌殑閫傞厤鍣�
	AlbumHelper helper;
	Button bt;
	TextView bt_over;
	TextView bt_cancel;
	
	private String fromactivity="";

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", 400).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.photo_activity_image_grid);
		bt_over=(TextView) findViewById(R.id.bt_over);
		bt_cancel=(TextView) findViewById(R.id.bt_cancel);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);
		fromactivity=getIntent().getStringExtra("fromactivity");
		initView();
		bt = (Button) findViewById(R.id.bt);
		bt_over.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				if (Bimp.act_bool) {
					//判断返回那个那个选取的Activity  姜斌
					
					if(fromactivity.equals("life") || "".equals(fromactivity)){
						Intent intent = new Intent(ImageGridActivity.this,
								AddLifeActivity.class);
						startActivity(intent);
					}
					if(fromactivity.equals("diary")){
						Intent intent = new Intent(ImageGridActivity.this,
								AddWandrActivity.class);
						startActivity(intent);
					}
					//返回日程完成activity
					if(fromactivity.equals("completeschedule")){
						Intent intent = new Intent(ImageGridActivity.this,
								CompleteScheduleActivity.class);
						startActivity(intent);
					}
					
					//返回日程完成activity
					if(fromactivity.equals("tadddiary")){
						Intent intent = new Intent(ImageGridActivity.this,
								TAddWandrActivity.class);
						startActivity(intent);
					}
					
					
					
					Bimp.act_bool = false;
				}
				for (int i = 0; i < list.size(); i++) {
					if (Bimp.drr.size() < 9) {
						Bimp.drr.add(list.get(i));
					}
				}
				finish();
			}

		});
	
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
	}

	/**
	 * 鍒濆鍖杤iew瑙嗗浘
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt_over.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// if(dataList.get(position).isSelected()){
				// dataList.get(position).setSelected(false);
				// }else{
				// dataList.get(position).setSelected(true);
				// }
				/**
				 * 閫氱煡閫傞厤鍣紝缁戝畾鐨勬暟鎹彂鐢熶簡鏀瑰彉锛屽簲褰撳埛鏂拌鍥�
				 */
				adapter.notifyDataSetChanged();
			}

		});

	}
}
