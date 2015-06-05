package com.zylbaby.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zylbaby.app.R;
import com.zylbaby.app.adapter.MultiChoicAdapter;
import com.zylbaby.app.adapter.Utils;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BabySelectStars;

import android.content.Context;
import android.view.View;



public class MultiChoicePopWindow extends AbstractChoicePopWindow{

	
	private MultiChoicAdapter<String> mMultiChoicAdapter;
	private MultiChoicAdapter<BabyBean> mMultiChoicbabyAdapter;
	
	public MultiChoicePopWindow(Context context,View parentView, List<String> list, boolean flag[])
	{
		super(context, parentView, list);
		
		initData(flag);
	}
	public MultiChoicePopWindow(Context context,View parentView, List<BabyBean> list, boolean flag[],int k)
	{
		super(context, parentView, list,k);
		
		initDataBaby(flag,list);
	}
	
	
	private List<BabyBean> babylist;
	protected void initData(boolean flag[]) {
		// TODO Auto-generated method stub
		mMultiChoicAdapter = new MultiChoicAdapter<String>(mContext, mList, flag, R.drawable.selector_checkbox1);
		mListView.setAdapter(mMultiChoicAdapter);
		mListView.setOnItemClickListener(mMultiChoicAdapter);   
		
		
		Utils.setListViewHeightBasedOnChildren(mListView);

	}
	
	protected void initDataBaby(boolean flag[],List<BabyBean> l) {
		// TODO Auto-generated method stub
		
		mMultiChoicbabyAdapter= new MultiChoicAdapter<BabyBean>(mContext, l, flag, R.drawable.selector_checkbox1);
		
		mListView.setAdapter(mMultiChoicbabyAdapter);

		mListView.setOnItemClickListener(mMultiChoicbabyAdapter);
		
		
		Utils.setListViewHeightBasedOnChildren(mListView);

	}


	public boolean[] getSelectItem()
	{
		//return mMultiChoicAdapter.getSelectItem();
		return mMultiChoicbabyAdapter.getSelectItem();
	}
	
	public ArrayList<BabySelectStars> getStars(){
    	return mMultiChoicbabyAdapter.getStars();
    }
	
	public HashMap getSelectStars(){
		return mMultiChoicbabyAdapter.getSelectStars();
	}
	
	
	
	
}
