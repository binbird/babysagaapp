package com.zylbaby.app.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

public class HeadManager {
	Context context;
	private ImageView datebtn;
    private TextView showdatetxt;
    
    private TextView titletxt;
    private ImageView picview;
    
    public HeadManager(ImageView btn,TextView datetxt,
    		TextView title,ImageView pic)
    {
    	
    	datebtn=btn;
    	showdatetxt=datetxt;
    	titletxt=title;
    	picview=pic;
    	
    }
	
}
