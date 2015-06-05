package com.zylbaby.app.view;

import java.util.List;

import com.zylbaby.app.R;
import com.zylbaby.app.adapter.SingleChoicAdapter;
import com.zylbaby.app.adapter.Utils;



import android.content.Context;
import android.view.View;

public class SingleChoicePopWindow extends AbstractChoicePopWindow {

	private SingleChoicAdapter<String> mSingleChoicAdapter;

	public SingleChoicePopWindow(Context context, View parentView,
			List<String> list) {
		super(context, parentView, list);

		initData();
	}

	protected void initData() {
		mSingleChoicAdapter = new SingleChoicAdapter<String>(mContext, mList,
				R.drawable.selector_checkbox2);

		mListView.setAdapter(mSingleChoicAdapter);
		mListView.setOnItemClickListener(mSingleChoicAdapter);

		Utils.setListViewHeightBasedOnChildren(mListView);

	}

	public int getSelectItem() {
		return mSingleChoicAdapter.getSelectItem();
	}

}
