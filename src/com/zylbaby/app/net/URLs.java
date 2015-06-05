package com.zylbaby.app.net;

import java.io.Serializable;

/**
 * 接口URL实体类
 * @author Michael zhu 朱育梁 （zhuyuliang0@126.com）
 * @version 1.0
 * @created 2014-3-13
 */
public class URLs implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5944393553435679206L;
	
	public final static String HOST = "www.babysaga.cn"; //10.0.2.2本地地址
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	private final static String URL_SPLITTER = "/";
	
	private final static String URL_API = "";
	
	public final  static String URL_API_HOST = HTTP + HOST + URL_SPLITTER + URL_API;
	
	// 登录验证
	public final static String URL_LOGIN_VALIDATE= URL_API_HOST+"app/service?method=user.login";
	// 注册
	public final static String URL_REGISTER_VALIDATE= URL_API_HOST + "app/service?method=user.register";
	// 修改密码
	public final static String URL_UPDATEPASSWD = URL_API_HOST+ "";
	// 添加baby信息
	public final static String URL_ADDBABYINFO = URL_API_HOST+  "app/service?method=user.UpdateUserInfo";
	// 获取baby信息
	public final static String URL_GETBABYINFO = URL_API_HOST+  "app/service?method=user.GetUserInfo";
	
	// 获取baby 个人信息
	public final static String URL_GETPERSONBABYINFO = URL_API_HOST+  "app/service?method=user.GetBabyInfo";
	
	// 系统更新
	public final static String URL_UPDATASOFT = URL_API_HOST + "app/service?method=SystemApp.Upgrade";
	
	// 添加作息
	public final static String URL_ADDWANDR = URL_API_HOST + "app/service?method=diary.inputdiary";
	//获得作息列表
	public final static String URL_LISTWANDR = URL_API_HOST + "app/service?method=diary.daydiary";
	//教师----获得作息列表
	public final static String URL_TEACHERLISTWANDR = URL_API_HOST + "app/service?method=diary.tdaydiary";
	
	//获得消息记录头
	public final static String URL_LISTINOFMAIN=URL_API_HOST + "app/service?method=diary.infolist";
	//教师----账户获得消息头
	public final static String URL_LISTTEACHERINOFMAIN=URL_API_HOST + "app/service?method=TeacherManage.TeacherInfoListHead";
	
	
	//获得查询班级列表
	public final static String URL_LISTSEARCHBANJI = URL_API_HOST + "app/service?method=user.FindClass";
	
	//获得查询班级列表
	public final static String URL_JOINBANJI = URL_API_HOST + "app/service?method=user.joinClass";
	//获得日期的日程列表
	public final static String URL_LISTSCHEDULEBYDAY=URL_API_HOST + "app/service?method=ClassSchedule.GetListSchedule";
	//增加日程JOSN 方法
	public final static String URL_ADDSCHEDULEJSON=URL_API_HOST + "app/service?method=ClassSchedule.InputScheduleJson";
	//	班级学生列表
	public final static String URL_LISTBANJIBABYLIST= URL_API_HOST + "app/service?method=user.ListClassBaby";
	//班级学生列表 带有入园时间和离校时间
	public final static String URL_LISTBANJIBABYLISTINOUTTIME= URL_API_HOST + "app/service?method=TeacherManage.ListBanjiBabyInAndOut";
	//	获取一个日程ById
	public final static String URL_GETSCHEDULEBYID= URL_API_HOST + "app/service?method=ClassSchedule.GetScheduleById";
	//教师----保存日程完成的情况
	public static final String URL_SAVECOMPLETESCHEDULE = URL_API_HOST + "app/service?method=ClassSchedule.CompleteSchedule";
	//获取作息中图片列表
	public static final String URL_GETDIARYPICLIST = URL_API_HOST + "app/service?method=Diary.GetDiaryPicList";
	//增加消息
	public static final String URL_ADDINFO = URL_API_HOST + "app/service?method=Diary.InputInfo";
	//教师发送消息
	public static final String URL_TEACHERADDINFO = URL_API_HOST + "app/service?method=Diary.TeacherInputInfo";
	//教师群发消息
	public static final String URL_TEACHERADDINFOALL = URL_API_HOST + "app/service?method=TeacherManage.SendInfoAll";
	//获取消息列表
	public static final String URL_GETINFOITEMLIST=URL_API_HOST + "app/service?method=Diary.InfoListItem";
	//教师账户获得消息列表
	public static final String URL_GETTEACHERINFOITEMLIST=URL_API_HOST + "app/service?method=TeacherManage.TInfoListItem";
	//到校离校时间
	public static final String URL_INOUTSCHOOLTIME=URL_API_HOST + "app/service?method=TeacherManage.InOutSchoolTime";
	//获得班级信息
	public static final String URL_GETBANJIINFO=URL_API_HOST + "app/service?method=TeacherManage.GetBanjiInfo";
	//新增修改班级信息
	public static final String URL_EDITBANJIINFO = URL_API_HOST + "app/service?method=TeacherManage.CreateClassPic";
//家长账户所在班级列表
	public static final String URL_LISTSEARCHBANJIBYBABYID = URL_API_HOST + "app/service?method=user.BabyListClass";
	//删除一个作息
	public static String URL_DELDIARY= URL_API_HOST + "app/service?method=Diary.DelDiary";
	
	//根据班级获得日期的日程列表
	public final static String URL_LISTSCHEDULEBYDAYANDCLASS=URL_API_HOST + "app/service?method=ClassSchedule.PrePareSchedule";

	public final static String URL_UPLOADERRORFILE=URL_API_HOST + "app/service?method=SystemApp.ErrorFileUpload";
	//时间标示
	public final static String URL_SETTIMETAGLIST=URL_API_HOST + "app/service?method=Diary.SetDateTag";
}
