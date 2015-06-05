package com.zylbaby.app.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.zylbaby.app.R;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.view.DialogDiaryType.ItemClickListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class DialogState extends Dialog{

	Context context;
	String[] babystatearray=AppConstant.babystatetext_array;
	private int[] babystateioc_array=AppConstant.babystateioc_array;
	public DialogState(Context context) {
		super(context);
		this.context=context;
	}
	
	private GridView gridview;
	public  StateImgDialogListener mylistener=null;
	
	

	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.babystate_gridview);
          
        gridview = (GridView) findViewById(R.id.gridview);  
  
        // 生成动态数组，并且转入数据  
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
        for (int i = 0; i < babystatearray.length; i++) {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("ItemImage", babystateioc_array[i]);// 添加图像资源的ID  
            //map.put("ItemText", "NO." + String.valueOf(i));// 按序号做ItemText  
            map.put("ItemText",babystatearray[i].toString());
            lstImageItem.add(map);  
        }  
        // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
        SimpleAdapter saImageItems = new SimpleAdapter(context, // 没什么解释  
                lstImageItem,// 数据来源  
                R.layout.babystate_griditem,// night_item的XML实现  
                // 动态数组与ImageItem对应的子项  
                new String[] { "ItemImage", "ItemText" },  
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] { R.id.ItemImage, R.id.ItemText });  
        // 添加并且显示  
        gridview.setAdapter(saImageItems);  
        // 添加消息处理  
        gridview.setOnItemClickListener(new ItemClickListener());  
    }  
  
    // 当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件  
    class ItemClickListener implements OnItemClickListener {  
        public void onItemClick(AdapterView<?> arg0,// The AdapterView where the click happened  
                View arg1,// The view within the AdapterView that was clicked  
                int arg2,// The position of the view in the adapter  
                long arg3// The row id of the item that was clicked  
        ) {  
            
        	if(DialogState.this.mylistener!=null){
				try
				{
					// 在本例中arg2=arg3  
		            @SuppressWarnings("unchecked")  
		            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
		            // 显示所选Item的ItemText  
		            setTitle((String) item.get("ItemText"));  
		            DialogState.this.mylistener.ReturnListenerValue("" ,arg2);
		            
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
        	
        	
        }  
    }  


}
