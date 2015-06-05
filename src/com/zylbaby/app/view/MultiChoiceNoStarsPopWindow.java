package com.zylbaby.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;

import com.zylbaby.app.R;
import com.zylbaby.app.adapter.MultiChoicAdapter;
import com.zylbaby.app.adapter.MultiChoicNoStarsAdapter;
import com.zylbaby.app.adapter.Utils;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BabySelectStars;

public class MultiChoiceNoStarsPopWindow extends AbstractChoicePopWindow{
	
	private MultiChoicNoStarsAdapter<BabyBean> mMultiChoicbabyAdapter;
	
	public MultiChoiceNoStarsPopWindow(Context context,View parentView, List<String> list, boolean flag[])
	{
		super(context, parentView, list);
		
		
	}
	public MultiChoiceNoStarsPopWindow(Context context,View parentView, List<BabyBean> list, boolean flag[],int k)
	{
		super(context, parentView, list,k);
		
		initDataBaby(flag,list);
	}
	
	
	private List<BabyBean> babylist;
	
	
	protected void initDataBaby(boolean flag[],List<BabyBean> l) {
		// TODO Auto-generated method stub
		
		mMultiChoicbabyAdapter= new MultiChoicNoStarsAdapter<BabyBean>(mContext, l, flag, R.drawable.selector_checkbox1);
		
		mListView.setAdapter(mMultiChoicbabyAdapter);

		mListView.setOnItemClickListener(mMultiChoicbabyAdapter);
		
		
		Utils.setListViewHeightBasedOnChildren(mListView);

	}


	public boolean[] getSelectItem()
	{
		//return mMultiChoicAdapter.getSelectItem();
		return mMultiChoicbabyAdapter.getSelectItem();
	}
	
//	public ArrayList<BabySelectStars> getStars(){
//    	return MultiChoicNoStarsAdapter.getStars();
//    }
//	
//	public HashMap getSelectStars(){
//		return MultiChoicNoStarsAdapter.getSelectStars();
//	}
	
}
