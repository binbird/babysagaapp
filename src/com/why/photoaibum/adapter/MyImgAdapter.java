package com.why.photoaibum.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.zylbaby.app.R;
import com.zylbaby.app.util.Bimp;
import com.zylbaby.app.util.ImageUtils;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyImgAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<String> paths;
	public MyImgAdapter(Context c,ArrayList<String> ps ){
		context=c;
		paths=ps;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return paths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private Bitmap bit;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		MyView tag;
		View v = LayoutInflater.from(context).inflate(
				R.layout.grid_img_item, null);
	    
		tag = new MyView();
		tag.imageView = (ImageView) v.findViewById(R.id.imageView1);
		v.setTag(tag);
		
		
	    
	   // String imagepath1 = Environment.getExternalStorageDirectory() + "/abc.jpg";
	    String imagepath=paths.get(position);
	    //bit = BitmapFactory.decodeFile(imagepath);
	    //Bitmap newbit= ImageUtils.scaleCenterCrop(bit,210,210);
	    //Bitmap newbit1=null;
	    //newbit1=ThumbnailUtils.extractThumbnail(bit,100,100);
//	    try {
//			newbit1 = Bimp.revitionImageSize(imagepath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    //Bitmap newbit=ImageUtils.getCircleBitmap(bit,100,100);
//	    
		//tag.imageView.setImageBitmap(newbit);
		tag.imageView.setImageDrawable(ImageUtils.resizeImage2(imagepath, 100, 100));
//	    tag.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	    
	    return v;
	}
	
	class MyView {
		ImageView imageView;
		

		}

}
