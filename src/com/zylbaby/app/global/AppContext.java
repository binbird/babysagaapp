package com.zylbaby.app.global;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
//import android.webkit.CacheManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BabyDiaryByClass;
import com.zylbaby.app.bean.BanjiBabyAndInOutBean;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.InfoItem;
import com.zylbaby.app.bean.InfoMainBean;
import com.zylbaby.app.bean.ScheduleCompleteBean;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.bean.TeacherInfoMainBean;
import com.zylbaby.app.bean.TimeTag;
import com.zylbaby.app.bean.User;
import com.zylbaby.app.bean.WandrBean;
import com.zylbaby.app.net.ApiClient;
import com.zylbaby.app.net.ApiClientDiary;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.CrashHandler;
import com.zylbaby.app.util.MethodsCompat;
import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.util.UIHelper;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppContext extends Application { // application系统组件，像server一样，在android程序启动时系统会创建一个application对象，用来存储一些系统信息
	
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static final int PAGE_SIZE = 20;// 默认分页大小
	private static final int CACHE_TIME = 60 * 60000;// 缓存失效时间

	private boolean login = false; // 登录状态
	private String loginUid = null; // 登录用户的id
	private String loginUname = null; // 登录用户的id
	private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	
	
	
	
	
	// 登录的错误处理
	private Handler unLoginHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				UIHelper.ToastMessage(AppContext.this,
						getString(R.string.msg_login_error));
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		// 注册App异常崩溃处理器
		
		 CrashHandler crashHandler = CrashHandler.getInstance();  
	        crashHandler.init(getApplicationContext());  
		
//		Thread.setDefaultUncaughtExceptionHandler(AppException
//				.getAppExceptionHandler());
	       initImageLoader(getApplicationContext());
	}
	
	public static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config= new ImageLoaderConfiguration
				.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(1) // default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(cacheDir)) // default
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(100)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
		
	}
	
	/**
	 * 获取baby是否为空
	 * 
	 * @return
	 */
	public String getBabyData(){
		return getProperty("babydata");
	}
	/**
	 * 设置baby是否为空
	 * 
	 * @return
	 */
	public void setBabyData(String babydata){
		setProperty("babydata",babydata);
	}

	/**
	 * 用户是否登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return login;
	}

	/**
	 * 获取登录用户id
	 * 
	 * @return
	 */
	public String getLoginUid() {
		return this.loginUid;
	}

	/**
	 * 获取登录用户name
	 * 
	 * @return
	 */
	public String getLoginUname() {
		return this.loginUname;
	}

	/**
	 * 用户注销
	 */
	public void Logout() {
		ApiClient.cleanCookie();
		this.cleanCookie();
		cleanLoginInfo();
		this.login = false;
		this.loginUid = null;
	}

	/**
	 * 保存登录信息
	 * 
	 * @param user
	 */
	public void saveLoginInfo(final User user, String account) {
		this.loginUid = user.getUid();
		this.loginUname = account;
		this.login = true;
		setProperties(new Properties() {
			{
				setProperty("user.uid", String.valueOf(user.getUid()));
				setProperty("user.name", user.getUname());
			}
		});
		if(user.getIsfirst()==0){
			setBabyData("0");
		}else{
			setBabyData("1");
		}
	}
	
	/**
	 * 保存宝贝信息
	 * 
	 * @param user
	 */
	public void saveBabyInfo(final BabyBean babybean) {
		setProperties(new Properties() {
			{
				setProperty("baby.BabyName", babybean.getBname());
				setProperty("baby.Nickname", babybean.getBnickname());
				setProperty("baby.Sex", babybean.getBsex());
				setProperty("baby.Birthday", babybean.getBbirth());
				setProperty("baby.Country", babybean.getBnativeplace1());
				setProperty("baby.Province", babybean.getBnativeplace2());
				setProperty("baby.City", babybean.getBnativeplace3());
				setProperty("baby.BloodType", babybean.getBblood());
				setProperty("baby.Introduction", babybean.getBbeizhu());
				setProperty("baby.HeadImg", babybean.getImageurlfile());
			}
		});
	}

	/**
	 * 清除登录信息
	 */
	public void cleanLoginInfo() {
		this.loginUid = null;
		this.login = false;
		removeProperty("user.uid");
	}

	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public User getLoginInfo() {
		User lu = new User();
		lu.setUid(getProperty("user.uid"));
		lu.setUname(getProperty("user.name"));
		return lu;
	}

	/**
	 * 用户登录验证
	 * 
	 * @param account
	 * @param pwd
	 * @return User
	 * @throws AppException
	 */
	public User RegisterVerify(Map<String, Object> paramMap) throws AppException {
		return ApiClient.Register(this, paramMap);
	}
	
	/**
	 * 用户注册
	 * 
	 * @param account
	 * @param pwd
	 * @return User
	 * @throws AppException
	 */
	public User loginVerify(Map<String, Object> paramMap) throws AppException {
		return ApiClient.login(this, paramMap);
	}
	
	/**
	 * 用户注册
	 * 
	 * @param paramMap
	 * @param file
	 * @return BabyBean
	 * @throws AppException
	 */
	public BabyBean addBabyInfo(Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		return ApiClient.addBabyInfo(this, paramMap,file);
	}
	
	/**
	 * 发送信息
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public int addInfo(Map<String, Object> paramMap,int infofrom) throws AppException{
		return ApiClient.AddInfo(this, paramMap,infofrom);
	}
	
	public int addInfoAll(Map<String, Object> paramMap,int infofrom) throws AppException{
		return ApiClient.AddInfoAll(this, paramMap,infofrom);
	}
	
	
	/**
	 * 获取作息
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<DiaryDto> getDayDiary(Map<String, Object> paramMap) throws AppException {
		return ApiClient.getDayDiary(this, paramMap);
	}
	
	
	public BabyDiaryByClass getTeacherDayDiary(Map<String, Object> paramMap) throws AppException {
		return ApiClient.getTeacherDayDiary(this, paramMap);
	}
	
	public int delDiary(Map<String, Object> paramMap) throws AppException {
		return ApiClient.delDiary(this, paramMap);
	}
	
	/**
	 * 获得班级的信息
	 * @return
	 * @throws AppException
	 */
	public BanjiDto getBanjiInfo(Map<String, Object> paramMap) throws AppException{
		return ApiClient.getBanjiInfo(this, paramMap);
	}
	
	public BanjiDto saveBanjiInfo(Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		return ApiClient.saveBanjiInfo(this, paramMap,file);
	}
	
	
	/**
	 * 获取消息记录
	 * @return
	 * @throws AppException
	 */
	public ArrayList<InfoItem> getInfoItemList(Map<String, Object> paramMap) throws AppException {
		return ApiClient.getInfoItemList(this, paramMap);
	}
	/**
	 * 教师账户获得消息记录
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<InfoItem> getTeacherInfoItemList(Map<String, Object> paramMap) throws AppException {
		return ApiClient.getTeacherInfoItemList(this, paramMap);
	}
	
	/**
	 * 获得消息头列表
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<InfoMainBean> getInfoMain(Map<String, Object> paramMap) throws AppException{
		return ApiClient.getInfoMain(this, paramMap);
		
				
	}
	/**
	 * 获得教师消息头列表
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<TeacherInfoMainBean> getTeacherInfoMain(Map<String, Object> paramMap) throws AppException{
		return ApiClient.getTeacherInfoMain(this, paramMap);
		
				
	}
	
	
	
	
	/**
	 * 获得当日日程
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<ScheduleDto> getDaySchedule(Map<String,Object> paramMap) throws AppException{
		return ApiClient.getDaySchedule(this, paramMap);
	}

	/**
	 * 获得班级宝贝列表
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<BabyBean> getBanjibabyList(Map<String,Object> paramMap) throws AppException{
		return ApiClient.getBanjibabyList(this, paramMap);
	}
	/**
	 * 班级学生列表带有入园离校时间
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<BanjiBabyAndInOutBean> getBanjibabyListAndInOut(Map<String,Object> paramMap) throws AppException{
		return ApiClient.getBanjibabyListAndInOut(this, paramMap);
	}
	
	
	
	/**
	 * 保存日程完成状态
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public int saveScheduleComplete(Map<String,Object> paramMap) throws AppException{
		return ApiClient.saveScheduleComplete(this,paramMap);
	}
	
	
	public int saveScheduleComplete(Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		return ApiClient.saveScheduleComplete(this, paramMap,file);
	}
	/**
	 * 按照名称查询班级
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<BanjiDto> getBanji(Map<String, Object> paramMap) throws AppException {
		return ApiClient.getBanjiList(this, paramMap);
	}
	/**
	 * 按照学生Id 查询班级所在班级列表
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ArrayList<BanjiDto> getBanjiByBabyId(Map<String, Object> paramMap) throws AppException {
		return ApiClient.getBanjiListByBabyId(this, paramMap);
	}
	
	
	/**
	 * 加入班级
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public int JoinBanji(Map<String, Object> paramMap)throws AppException{
		return ApiClient.joinBanji(this, paramMap);
		
		
	}
	/**
	 * 获取一个日程Id
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public ScheduleDto getScheduleById (Map<String, Object> paramMap)throws AppException{
		return ApiClient.getScheduleById(this, paramMap);
	}
	
	
	
	
	/**
	 * 添加作息
	 * 
	 * @param paramMap
	 * @param file
	 * @return BabyBean
	 * @throws AppException
	 */
	public WandrBean addwandrInfo(Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		return ApiClient.addWandrInfo(this, paramMap,file);
	}
	
	
	
	public WandrBean addlifeInfo(Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		return ApiClient.addLifeInfo(this, paramMap,file);
	}
	
	public int  UploadErrorFile(Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		return ApiClient.uploadErrorFile(this, paramMap,file);
	}
	
	/**
	 * 到校离校时间
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public int InOutSchoolTime(Map<String,Object> paramMap) throws AppException{
		return ApiClient.InOutSchoolTime(this,paramMap);
	}
	
	/**
	 * 增加日程JSON
	 * @param paramMap
	 * @param file
	 * @throws AppException
	 */
	public int addSchedule(Map<String, Object> paramMap) throws AppException {
		return ApiClient.addSchedulejson(this, paramMap);
	}
	
	/**
	 * 获取宝贝信息
	 * 
	 * @return Babybean
	 * @throws AppException
	 */
	public BabyBean getBabyInfo() throws AppException {
		return ApiClient.getBabyInfo(this);
	}
	
	
	/**
	 * 获得宝贝个人信息
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public BabyBean getPersonBabyInfo(Map<String, Object> paramMap) throws AppException{
		return ApiClient.getBabyPersonInfo(this, paramMap);
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 获取App唯一标识
	 * 
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	/**
	 * 是否加载显示图片
	 * 
	 * @return
	 */
	public boolean isLoadImage() {
		String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
		// 默认是加载的
		if (StringUtils.isEmpty(perf_loadimage))
			return true;
		else
			return StringUtils.toBool(perf_loadimage);
	}

	/**
	 * 设置是否加载图片
	 * 
	 * @param b
	 */
	public void setConfigLoadimage(boolean b) {
		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}

	/**
	 * 清除保存的缓存
	 */
	public void cleanCookie() {
		removeProperty(AppConfig.CONF_COOKIE);
	}

	/**
	 * 判断缓存数据是否可读
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isReadDataCache(String cachefile) {
		return readObject(cachefile) != null;
	}

	/**
	 * 判断缓存是否存在
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 判断缓存是否失效
	 * 
	 * @param cachefile
	 * @return
	 */
	public boolean isCacheDataFailure(String cachefile) {
		boolean failure = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if (!data.exists())
			failure = true;
		return failure;
	}

	/**
	 * 清除app缓存
	 */
	public void clearAppCache() {
		// 清除webview缓存
//		File file = CacheManager.getCacheFileBaseDir();
//		if (file != null && file.exists() && file.isDirectory()) {
//			for (File item : file.listFiles()) {
//				item.delete();
//			}
//			file.delete();
//		}
//		deleteDatabase("webview.db");
//		deleteDatabase("webview.db-shm");
//		deleteDatabase("webview.db-wal");
//		deleteDatabase("webviewCache.db");
//		deleteDatabase("webviewCache.db-shm");
//		deleteDatabase("webviewCache.db-wal");
//		// 清除数据缓存
//		clearCacheFolder(getFilesDir(), System.currentTimeMillis());
//		clearCacheFolder(getCacheDir(), System.currentTimeMillis());
//		// 2.2版本才有将应用缓存转移到sd卡的功能
//		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
//			clearCacheFolder(MethodsCompat.getExternalCacheDir(this),
//					System.currentTimeMillis());
//		}
//		// 清除编辑器保存的临时内容
//		Properties props = getProperties();
//		for (Object key : props.keySet()) {
//			String _key = key.toString();
//			if (_key.startsWith("temp"))
//				removeProperty(_key);
//		}
	}

	/**
	 * 清除缓存目录
	 * 
	 * @param dir
	 *            目录
	 * @param numDays
	 *            当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	/**
	 * 将对象保存到内存缓存中
	 * 
	 * @param key
	 * @param value
	 */
	public void setMemCache(String key, Object value) {
		memCacheRegion.put(key, value);
	}

	/**
	 * 从内存缓存中获取对象
	 * 
	 * @param key
	 * @return
	 */
	public Object getMemCache(String key) {
		return memCacheRegion.get(key);
	}

	/**
	 * 保存磁盘缓存
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 获取磁盘缓存数据
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getDiskCache(String key) throws IOException {
		FileInputStream fis = null;
		try {
			fis = openFileInput("cache_" + key + ".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 保存对象
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 读取对象
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	public void setProperty(String key, String value) {
		AppConfig.getAppConfig(this).set(key, value);
	}

	public String getProperty(String key) {
		return AppConfig.getAppConfig(this).get(key);
	}

	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}
/**
 * 根据Diary Id 获取图片列表
 * @param paramMap
 * @return
 */
	public String[] getDiaryPicList(Map<String, Object> paramMap) {
		
		return ApiClient.getdiarypiclist(this, paramMap);
		
	}

	public ArrayList<ScheduleDto> getBabyClassSchedule(Map<String, Object> paramMap) {
		return ApiClient.getBabyClassSchedule(this, paramMap);
	}
	
	public ArrayList<TimeTag> getTimeTag(Map<String, Object> paramMap) {
		return ApiClientDiary.getTimeTagList(this, paramMap);
		}
}
