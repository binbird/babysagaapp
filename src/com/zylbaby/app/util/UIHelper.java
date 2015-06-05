package com.zylbaby.app.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.teacher.app.ui.CompleteScheduleActivity;
import com.teacher.app.ui.CreateBanjiActivity;
import com.teacher.app.ui.TMainActivity;
import com.teacher.app.ui.TeacherInfoActivity;
import com.zylbaby.app.R;
import com.zylbaby.app.ui.AddbabyActivity;

import com.zylbaby.app.ui.LoginActivity;
import com.zylbaby.app.ui.MainActivity;
import com.zylbaby.app.ui.RegisterActivity;
import com.zylbaby.app.ui.SearchBanjiActivity;
import com.zylbaby.app.ui.SelectCalendar;


/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class UIHelper {

	/**
	 * 显示主页面
	 * @param activity
	 */
	public static void showhomepage(Context context)
	{
		Intent intent = new Intent(context,MainActivity.class);
		context.startActivity(intent);
	}
	/**
	 * 显示教师账户主页面
	 * @param context
	 */
	public static void showteacherhomepage(Context context){
		Intent intent = new Intent(context,TMainActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示登录页面
	 * @param activity
	 */
	public static void showLogin(Context context)
	{
		Intent intent = new Intent(context,LoginActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 显示注册页面
	 * @param activity
	 */
	public static void showRegister(Context context)
	{
		Intent intent = new Intent(context,RegisterActivity.class);
		context.startActivity(intent);
	}
	
	/**
	 * 添加宝贝页面
	 * @param activity
	 */
	public static void showAddBaby(Context context,String tag)
	{
		Intent intent = new Intent(context,AddbabyActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}
	/**
	 * 
	 * @param context
	 * @param tag
	 */
	public static void showTeacherInfo(Context context,String tag)
	{
		Intent intent = new Intent(context,TeacherInfoActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}
	/**
	 * 完成日程窗口
	 * @param context
	 * @param tag
	 */
	public static void showCompleteSchedule(Context context,String tag)
	{
		Intent intent = new Intent(context,CompleteScheduleActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}
	
	
	/**
	 * 日历选择
	 * @param context
	 * @param tag
	 */
	public static void showDateSelect(Context context,String tag){
		Intent intent = new Intent(context,SelectCalendar.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}
	/**
	 * 查询班级
	 * @param context
	 * @param tag
	 */
	public static void showSearchClass(Context context,String tag){
		Intent intent = new Intent(context,SearchBanjiActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}
	/**
	 * 建立班级
	 * @param context
	 * @param tag
	 */
	public static void showCreateClass(Context context,String tag){
		Intent intent = new Intent(context,CreateBanjiActivity.class);
		intent.putExtra("tag", tag);
		context.startActivity(intent);
	}
	
	/**
	 * 弹出Toast消息
	 * @param msg
	 */
	public static void ToastMessage(Context cont,String msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,int msg)
	{
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}
	public static void ToastMessage(Context cont,String msg,int time)
	{
		Toast.makeText(cont, msg, time).show();
	}
	
	/**
	 * 发送App异常崩溃报告
	 * @param cont
	 * @param crashReport
	 */
	public static void sendAppCrashReport(final Context cont, final String crashReport)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_error);
		builder.setMessage(R.string.app_error_message);
		builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//发送异常报告
				Intent i = new Intent(Intent.ACTION_SEND);
				//i.setType("text/plain"); //模拟器
				i.setType("message/rfc822") ; //真机
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{"512519202@qq.com"});
				i.putExtra(Intent.EXTRA_SUBJECT,"成长印记 Android客户端 - 错误报告");
				i.putExtra(Intent.EXTRA_TEXT,crashReport);
				cont.startActivity(Intent.createChooser(i, "发送错误报告"));
				//退出
				AppManager.getInstance().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//退出
				AppManager.getInstance().AppExit(cont);
			}
		});
		builder.show();
	}
	
	/**
	 * 退出程序
	 * @param cont
	 */
	public static void Exit(final Context cont)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.app_menu_surelogout);
		builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//退出
				AppManager.getInstance().AppExit(cont);
			}
		});
		builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
}
