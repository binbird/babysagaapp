package com.zylbaby.app.util;

import android.content.Context;

/**
 * ��λ֮���ת����
 * @Description: ��λ֮���ת����

 * @File: DimensionUtility.java

 * @Package org.zyl.util

 * @Author Hanyonglu

 * @Date 2012-6-18 ����07:59:22

 * @Version V1.0
 */
public class DimensionUtility {
	/**
	 * ����ֻ�ķֱ��ʴ� dp�ĵ�λת��Ϊ px(����)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ����ֻ�ķֱ��ʴ� px(����)�ĵ�λת��Ϊ dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
