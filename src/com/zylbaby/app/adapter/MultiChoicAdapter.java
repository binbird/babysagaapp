package com.zylbaby.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BabySelectStars;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;



public class MultiChoicAdapter<T> extends BaseAdapter  implements OnItemClickListener{

	
		//MultiChoicAdpterOnRatingBarClick myAdpterOnclick;
	 	private Context mContext;   
	    private List<T> mObjects = new ArrayList<T>();
	    private int mCheckBoxResourceID = 0;
	    private boolean mBoolean[] = null;
	    private String[] babystars=null;
	    private ArrayList<BabySelectStars> babyselectstars=new ArrayList<BabySelectStars>();
	   
	    private HashMap babyselectstars1= new HashMap();
	    private LayoutInflater mInflater;

	    public ArrayList<BabySelectStars> getStars(){
	    	return babyselectstars;
	    }
	    public HashMap getSelectStars(){
	    	return babyselectstars1;
	    }
	    
	    public MultiChoicAdapter(Context context, int checkBoxResourceId) {
	        init(context, checkBoxResourceId);
	    }
	    
	    public MultiChoicAdapter(Context context,  List<T> objects, boolean[] flag, int checkBoxResourceId) {
	        init(context, checkBoxResourceId);
	        if (objects != null)
	    	{
	        	mObjects = objects;
	        	mBoolean = flag;
	    	}

	    }

	    private void init(Context context, int checkBoResourceId) {
	        mContext = context;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        mCheckBoxResourceID = checkBoResourceId;
	    }
	    
	    public void refreshData(List<T> objects,  boolean[] flag)
	    {
	    	if (objects != null)
	    	{
	    		mObjects = objects;
	    		mBoolean = flag;
	    	}
	    }
	    
	  
	    public void setSelectItem( boolean[] flag)
	    {
	    	if (flag != null)
	    	{
	    		mBoolean = flag;
	    		notifyDataSetChanged();
	    	}
	    
	    }
	    

	    public boolean[] getSelectItem()
	    {
	    	return mBoolean;
	    }

	    public void clear() {
	         mObjects.clear();
	         notifyDataSetChanged();
	    }

	    
	    public int getCount() {
	        return mObjects.size();
	    }

	    public T getItem(int position) {
	        return mObjects.get(position);
	    }

	    public int getPosition(T item) {
	        return mObjects.indexOf(item);
	    }


	    public long getItemId(int position) {
	        return position;
	    }

	     
	    
	    public View getView(final int position, View convertView, ViewGroup parent) {   
		
	    	 final ViewHolder viewHolder;
	    	 
	    	 
		     if (convertView == null) {
		    	 convertView = mInflater.inflate(R.layout.choice_list_item_layout_2, null);
		         
		    	 viewHolder = new ViewHolder();
		         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
		         viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkBox);
		         viewHolder.mCheckBox.setFocusable(false);
		         viewHolder.mratingbar=(RatingBar) convertView.findViewById(R.id.ratingBar);
		         viewHolder.mratingbar.setFocusable(false);
		         viewHolder.mratingbar.setStepSize(1);
		         viewHolder.mratingbar.setMax(5);
		         convertView.setTag(viewHolder);
		         
		         if (mCheckBoxResourceID != 0)
		         {
		        	 viewHolder.mCheckBox.setButtonDrawable(mCheckBoxResourceID);
		         }

		     } else {
		         
		    	 viewHolder = (ViewHolder) convertView.getTag();
		         
		     }
		   
		     viewHolder.mCheckBox.setChecked(mBoolean[position]);

		     final int fpostion = position;	
		     viewHolder.mratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
				
				@Override
				public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
					
					int k=position;
					//修改
					//babyselectstars.add(new BabySelectStars(k,(int) arg1));
					if(babyselectstars1.get(k)==null){
						babyselectstars1.put(k, arg1);
					}else
					{
						babyselectstars1.remove(k);
						babyselectstars1.put(k, arg1);
					}
					
					Toast.makeText(mContext, String.valueOf(k)+"---"+String.valueOf(arg1), Toast.LENGTH_LONG).show();
					
					
				}
			});
		     	 
		     T item = getItem(position);
			 if (item instanceof CharSequence) {
			        viewHolder.mTextView.setText((CharSequence)item);
			 } else if(item instanceof BabyBean){
				 viewHolder.mTextView.setText(((BabyBean)item).getBname().toString());
				 
			 }
		
		     return convertView;
	    }

	    public static class ViewHolder
	    {
	    	public TextView mTextView;
	    	public CheckBox mCheckBox;
	    	public RatingBar mratingbar;
	    }

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			mBoolean[position] = !mBoolean[position];
			notifyDataSetChanged();
		} 
		
		
		
}
