package com.zylbaby.app.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.zylbaby.app.R;
import com.zylbaby.app.adapter.DiaryTypeAdapter;
import com.zylbaby.app.global.AppConstant;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class DialogDiaryType extends Dialog {
	
	Context context;
	String[] diaryarray;
//	private int[] diaryioc_array=new int[]{
//			R.drawable.youxi,R.drawable.shuijiao,R.drawable.shiwaihuodong,R.drawable.shineihuodong,
//			R.drawable.lingshi,R.drawable.laodong,R.drawable.chifan,R.drawable.bianbian
//		};
	public DialogDiaryType(Context context) {
		super(context);
		this.context=context;
	}
	public DialogDiaryType(Context context,String[] array) {
		super(context);
		this.context=context;
		diaryarray=array;
	}
	
	private GridView gridview;
	
	//接口变量
	public  ImgDialogListener mylistener=null;
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.diarytype_gridview);
          
        gridview = (GridView) findViewById(R.id.gridview);  
  
        // 生成动态数组，并且转入数据  
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
        for (int i = 0; i < diaryarray.length; i++) {  
            HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("ItemImage", AppConstant.diaryioc_array[i]);// 添加图像资源的ID  
            //map.put("ItemText", "NO." + String.valueOf(i));// 按序号做ItemText  
            map.put("ItemText",diaryarray[i].toString());
            lstImageItem.add(map);  
        }  
        // 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应  
        //重写此处 2015-04-09
        /*
        SimpleAdapter saImageItems = new SimpleAdapter(context, // 没什么解释  
                lstImageItem,// 数据来源  
                R.layout.diarytype_griditem,// night_item的XML实现  
                // 动态数组与ImageItem对应的子项  
                new String[] { "ItemImage", "ItemText" },  
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID  
                new int[] { R.id.ItemImage, R.id.ItemText });  
        // 添加并且显示  
        */
        DiaryTypeAdapter saImageItems= new DiaryTypeAdapter(context,lstImageItem);
        
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
            
        	if(DialogDiaryType.this.mylistener!=null){
				try
				{
					// 在本例中arg2=arg3  
		            @SuppressWarnings("unchecked")  
		            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
		            // 显示所选Item的ItemText  
		            setTitle((String) item.get("ItemText"));  
		            DialogDiaryType.this.mylistener.ReturnListenerValue((String) item.get("ItemText"),arg2);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
        	
        	
        }  
    }  

}
