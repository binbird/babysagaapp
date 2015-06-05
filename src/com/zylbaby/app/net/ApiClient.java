package com.zylbaby.app.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.BabyDiaryByClass;
import com.zylbaby.app.bean.BanjiBabyAndInOutBean;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.InfoItem;
import com.zylbaby.app.bean.InfoMainBean;
import com.zylbaby.app.bean.Result;
import com.zylbaby.app.bean.ScheduleCompleteBean;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.bean.TeacherInfoMainBean;
import com.zylbaby.app.bean.Update;
import com.zylbaby.app.bean.User;
import com.zylbaby.app.bean.WandrBean;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.FileAccess;
import com.zylbaby.app.util.ImageUtils;

/**
 * API客户端接口：用于访问网络数据
 * @author  zhuyuliang (zhuyuliang0@126.com)
 * @version 1.0
 * @created 2012-3-21
 */
public class ApiClient {

	// 编码方式
	public static final String UTF_8 = "utf-8";
	// 降序
	public static final String DESC = "descend";
	// 升序
	public static final String ASC = "ascend";
	
	// 连接超时时间
	private final static int TIMEOUT_CONNECTION = 20000;
	// socket连接超时时间
	private final static int TIMEOUT_SOCKET = 20000;
	// 再审时间
	private final static int RETRY_TIME = 3;

	private static String appCookie;
	// 应用用户代理
	private static String appUserAgent;
	// 清理缓存
	public static void cleanCookie() {
		appCookie = "";
	}
	// 获取缓存
	private static String getCookie(AppContext appContext) {
		if(appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}
	// 获取用户平台信息
	private static String getUserAgent(AppContext appContext) {
		if(appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("Micheal zhu");
			ua.append('/'+appContext.getPackageInfo().versionName+'_'+appContext.getPackageInfo().versionCode);//App版本
			ua.append("/Android");//手机系统平台
			ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
			ua.append("/"+android.os.Build.MODEL); //手机型号
			ua.append("/"+appContext.getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}
	// 获取HTTP配置
	private static HttpClient getHttpClient() {        
        HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}	
	// 获取HTTPGEt头配置
	private static GetMethod getHttpGet(String url, String cookie, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection","Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}
	// 获取HTTPPOST头配置
	private static PostMethod getHttpPost(String url, String cookie, String userAgent,String token) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection","Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		httpPost.setRequestHeader("Token", token);
		return httpPost;
	}
	/**
	 * 结合url和参数的请求地址
	 * @param p_url   url地址
	 * @param params  参数列表
	 * @return String 返回添加好数据参数的url地址
	 */
	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}
	
	/**
	 * get请求URL
	 * @param url
	 * @throws AppException 
	 */
	private static InputStream http_get(AppContext appContext, String url) throws AppException {	
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);			
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				break;				
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		
		responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
			try {
				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				if(res.getErrorCode() == 0){
					// 发送登录失败的信息
//					appContext.getUnLoginHandler().sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	/**
	 * 公用post方法
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 * @throws JSONException 
	 */
	public static InputStream _post(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, JSONException {
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		String token = appContext.getLoginUid();
		System.out.println("token" + token);
		if(token ==null){
			token = "";
			System.out.println("token0" + token);
		}
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		String data;
		JSONObject jsonObj = new JSONObject();//pet对象，json形式  
        
		
		//post表单参数处理
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
        if(params != null)
        for(String name : params.keySet()){
        	parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
        	jsonObj.put(name, String.valueOf(params.get(name)));//向pet对象里面添加值  
        	
        }
        System.out.println("json   " + jsonObj.toString());
        if(files != null)
        for(String file : files.keySet()){
        	try {
				parts[i++] = new FilePart(file, files.get(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent,token);	        
		        httpPost.setRequestEntity(new StringRequestEntity(jsonObj.toString()));		        
		        int statusCode = httpClient.executeMethod(httpPost);
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	throw AppException.http(statusCode);
		        }
		        else if(statusCode == HttpStatus.SC_OK) 
		        {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //保存cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
			try {
				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				if(res.getErrorCode() == 0){
//					appContext.getUnLoginHandler().sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	/**
	 * 公用post方法
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 * @throws JSONException 
	 */
	public static InputStream _post2(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, JSONException {
		int kk=0;
		if(files!=null) kk=files.size();
		String[] newFileArray=new String[kk];
		
		
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		String token = appContext.getLoginUid();
		System.out.println("token" + token);
		if(token ==null){
			token = "";
			System.out.println("token0" + token);
		}
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				System.out.println("name="+name + "//" + "params="+ String.valueOf(params
						.get(name)));
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
			}
		int k=0;
		if (files != null)
			
			for (String file : files.keySet()) {
				try {
					//-----------图片压缩另存----------
					
					File f=files.get(file);
					Bitmap m=ImageUtils.getSmallBitmap(f.getPath());
					ImageUtils.saveMyBitmap(String.valueOf(k),m);
					newFileArray[k]="/sdcard/babysaga/newpic/" + String.valueOf(k) + ".jpg";
					File f1=new File(newFileArray[k]);

					parts[i++] = new FilePart(file, f1);
					//-----------
					
					
					//parts[i++] = new FilePart(file, files.get(file));
					
					k++;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

		System.out.println("parts : "+ parts.toString());
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent,token);	        
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));		        
		        int statusCode = httpClient.executeMethod(httpPost);
		        System.out.println("statusCode"+ statusCode);
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	throw AppException.http(statusCode);
		        }
		        else if(statusCode == HttpStatus.SC_OK) 
		        {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //保存cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
			try {
				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				if(res.getErrorCode() == 0){
//					appContext.getUnLoginHandler().sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	
	/**
	 * 公用post方法
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 * @throws JSONException 
	 */
	public static InputStream _post3(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, JSONException {
		int kk=0;
		if(files!=null) kk=files.size();
		String[] newFileArray=new String[kk];
		
		
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		String token = appContext.getLoginUid();
		System.out.println("token" + token);
		if(token ==null){
			token = "";
			System.out.println("token0" + token);
		}
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				System.out.println("name="+name + "//" + "params="+ String.valueOf(params
						.get(name)));
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
			}
		int k=0;
		if (files != null)
			
			for (String file : files.keySet()) {
				try {
					//-----------图片压缩另存----------
					
					File f=files.get(file);
//					Bitmap m=ImageUtils.getSmallBitmap(f.getPath());
//					ImageUtils.saveMyBitmap(String.valueOf(k),m);
//					newFileArray[k]="/sdcard/babysaga/newpic/" + String.valueOf(k) + ".jpg";
					File f1=f;

					parts[i++] = new FilePart(file, f1);
					//-----------
					
					
					//parts[i++] = new FilePart(file, files.get(file));
					
					k++;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

		System.out.println("parts : "+ parts.toString());
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent,token);	        
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));		        
		        int statusCode = httpClient.executeMethod(httpPost);
		        System.out.println("statusCode"+ statusCode);
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	throw AppException.http(statusCode);
		        }
		        else if(statusCode == HttpStatus.SC_OK) 
		        {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //保存cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
			try {
				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				if(res.getErrorCode() == 0){
//					appContext.getUnLoginHandler().sendEmptyMessage(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        return new ByteArrayInputStream(responseBody.getBytes());
	}

	
	
	/*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    public static String submitPostData(String url,Map<String, String> params, String encode) {
    	InputStream inptStream = null;
        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {            
        	URL urls = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)urls.openConnection();
            httpURLConnection.setConnectTimeout(3000);        //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");    //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            
//            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
//            if(response == HttpURLConnection.HTTP_OK) {
                inptStream = httpURLConnection.getInputStream();
                System.out.println(FileAccess.inputStream2String(inptStream));
                                  //处理服务器的响应结果
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dealResponseResult(inptStream);   
    }
    
    /**
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                            .append("=")
                            .append(URLEncoder.encode(entry.getValue(), encode))
                            .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
    
    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     * Author    :   博客园-依旧淡然
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());    
        return resultData;
    }
	
	/**
	 * @throws JSONException 
	 * post请求URL
	 * @param appContext
	 * @param url
	 * @param params
	 * @param files
	 * @return Result
	 * @throws AppException 
	 * @throws IOException 
	 * @throws  
	 */
	private static Result http_post(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, IOException, JSONException {
        return Result.parse(_post(appContext, url, params, files));  
	}	
	
	
	/**
	 * 登录， 自动处理cookie
	 * @param appContext
	 * @param username
	 * @param pwd
	 * @return  User
	 * @throws AppException
	 */
	public static User login(AppContext appContext, Map<String, Object> paramMap) throws AppException {
		try{
			return loginRemoteService(appContext,paramMap);
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
				throw AppException.network(e);
		}
	}
	/**
	 * 发送信息
	 * @param appContext
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public static int AddInfo(AppContext appContext, Map<String, Object> paramMap,int infofrom) throws AppException {
		int k=0;
		try{
			String url="";
			if(infofrom==1){
				url=URLs.URL_ADDINFO;
			}else if(infofrom==2){
				url=URLs.URL_TEACHERADDINFO;
			}
			InputStream ism =  ApiClient._post(appContext, url, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else{
				String s=msg;
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
		}
		
		return k;
	}
	
	
	
	public static int AddInfoAll(AppContext appContext, Map<String, Object> paramMap,int infofrom) throws AppException {
		int k=0;
		try{
			String url=URLs.URL_TEACHERADDINFOALL;
			InputStream ism =  ApiClient._post(appContext, url, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else{
				String s=msg;
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
		}
		
		return k;
	}

	
	/**
	 * 添加宝贝信息
	 * @param appContext
	 * @param paramMap
	 * @param file
	 * @return  BabyBean
	 * @throws AppException
	 */
	public static BabyBean addBabyInfo(AppContext appContext, Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		BabyBean baby = new BabyBean();
		try{
			InputStream ism =  ApiClient._post2(appContext, URLs.URL_ADDBABYINFO, paramMap, file);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				baby.setValidate(StatusCode+"");
//				baby.setBname(UserInfo.getString("BabyName"));
//				baby.setBnickname(UserInfo.getString("Nickname"));
//				baby.setBsex(UserInfo.getString("Sex"));
//				baby.setBbirth(UserInfo.getString("Birthday"));
//				baby.setBnativeplace1(UserInfo.getString("Country"));
//				baby.setBnativeplace2(UserInfo.getString("Province"));
//				baby.setBnativeplace3(UserInfo.getString("City"));
//				baby.setBblood(UserInfo.getString("BloodType"));
//				baby.setBbeizhu(UserInfo.getString("Introduction"));
//				baby.setImageurlfile(UserInfo.getString("HeadImg"));
			}else{
				baby.setBbeizhu2(msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return baby;
	}
	
	
	/**
	 * 获得班级信息
	 * @param appContext
	 * @param paramMap
	 * @return
	 * @throws AppException
	 */
	public static BanjiDto getBanjiInfo(AppContext appContext, Map<String, Object> paramMap)throws AppException {
		BanjiDto k=new BanjiDto();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETBANJIINFO, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			JSONObject BanjiInfo = person.getJSONObject("Banji");
			if (StatusCode == 200) {
				k.setName(BanjiInfo.getString("Name"));
				k.setDescribe(BanjiInfo.getString("Description"));
				k.setSchoool(BanjiInfo.getString("School"));
				k.setAddress(BanjiInfo.getString("Address"));
				k.setCode(BanjiInfo.getString("Code"));
				k.setPassword(BanjiInfo.getString("Password"));
				k.setPic(BanjiInfo.getString("Pic").replace("~", "http://www.babysaga.cn"));
			}else{
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=null;
		}
		
		return k;
	}
	
	
	/**
	 * 添加作息信息
	 * @param appContext
	 * @param paramMap
	 * @param file
	 * @return  BabyBean
	 * @throws AppException
	 */
	public static WandrBean addWandrInfo(AppContext appContext, Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		WandrBean wandr = new WandrBean();
		try{
			InputStream ism =  ApiClient._post2(appContext, URLs.URL_ADDWANDR, paramMap, file);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				wandr.setStatusCode(StatusCode+"");
//				baby.setBname(UserInfo.getString("BabyName"));
//				baby.setBnickname(UserInfo.getString("Nickname"));
//				baby.setBsex(UserInfo.getString("Sex"));
//				baby.setBbirth(UserInfo.getString("Birthday"));
//				baby.setBnativeplace1(UserInfo.getString("Country"));
//				baby.setBnativeplace2(UserInfo.getString("Province"));
//				baby.setBnativeplace3(UserInfo.getString("City"));
//				baby.setBblood(UserInfo.getString("BloodType"));
//				baby.setBbeizhu(UserInfo.getString("Introduction"));
//				baby.setImageurlfile(UserInfo.getString("HeadImg"));
			}else{
				wandr.setError(msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return wandr;
	}
	
	
	
	
	/**
	 * 添加作息信息
	 * @param appContext
	 * @param paramMap
	 * @param file
	 * @return  BabyBean
	 * @throws AppException
	 */
	public static WandrBean addLifeInfo(AppContext appContext, Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		WandrBean wandr = new WandrBean();
		try{
			InputStream ism =  ApiClient._post2(appContext, URLs.URL_ADDWANDR, paramMap, file);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				wandr.setStatusCode(StatusCode+"");
//				baby.setBname(UserInfo.getString("BabyName"));
//				baby.setBnickname(UserInfo.getString("Nickname"));
//				baby.setBsex(UserInfo.getString("Sex"));
//				baby.setBbirth(UserInfo.getString("Birthday"));
//				baby.setBnativeplace1(UserInfo.getString("Country"));
//				baby.setBnativeplace2(UserInfo.getString("Province"));
//				baby.setBnativeplace3(UserInfo.getString("City"));
//				baby.setBblood(UserInfo.getString("BloodType"));
//				baby.setBbeizhu(UserInfo.getString("Introduction"));
//				baby.setImageurlfile(UserInfo.getString("HeadImg"));
			}else{
				wandr.setError(msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return wandr;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static int saveScheduleComplete(AppContext appContext, Map<String, Object> paramMap,Map<String, File> file) throws AppException {
		ScheduleCompleteBean wandr = new ScheduleCompleteBean();
		int returncode=0;
		try{
			InputStream ism =  ApiClient._post2(appContext, URLs.URL_SAVECOMPLETESCHEDULE, paramMap, file);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				
				//wandr.setStatusCode(StatusCode+"");
//				baby.setBname(UserInfo.getString("BabyName"));
//				baby.setBnickname(UserInfo.getString("Nickname"));
//				baby.setBsex(UserInfo.getString("Sex"));
//				baby.setBbirth(UserInfo.getString("Birthday"));
//				baby.setBnativeplace1(UserInfo.getString("Country"));
//				baby.setBnativeplace2(UserInfo.getString("Province"));
//				baby.setBnativeplace3(UserInfo.getString("City"));
//				baby.setBblood(UserInfo.getString("BloodType"));
//				baby.setBbeizhu(UserInfo.getString("Introduction"));
//				baby.setImageurlfile(UserInfo.getString("HeadImg"));
			}else{
				//wandr.setError(msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return returncode;
	}
	
	
	public static int addSchedulejson(AppContext appContext, Map<String, Object> paramMap) throws AppException {
		int k=0;
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_ADDSCHEDULEJSON, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else{
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
		}
		
		return k;
	}
	
	
	
	
	/**
	 * 获得宝贝作息列表
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static ArrayList<DiaryDto> getDayDiary(AppContext appContext,Map<String, Object> paramMap){
		
		ArrayList<DiaryDto> list=new ArrayList<DiaryDto>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTWANDR, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("Diary");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				String DiaryTypeName = item.getString("DiaryTypeName");
				String Title=item.getString("Title");
				String EntryTime = item.getString("EntryTime");
				int diarytypeid=item.getInt("DiaryTypeId");
				int rank=item.getInt("Rank");
				int piccount=item.getInt("PicCount");

				
				JSONArray piclist=item.getJSONArray("PicList");
				
				ArrayList<String> arraypiclist = new ArrayList<String>();
				for(int k=0;k<piclist.length();k++){
					//JSONObject item1=piclist.getJSONObject(k);
					String s=piclist.getString(k);
					arraypiclist.add(s.replace("~", "http://www.babysaga.cn"));
				}
				
				
				DiaryDto dto= new DiaryDto();
				dto.setPicList(arraypiclist);
				if(diarytypeid==100)
					dto.setDiaryTitle(Title);
				else
					dto.setDiaryTitle(DiaryTypeName);
				dto.setEntrytime(EntryTime);
				dto.setRank(rank);
				dto.setDiarycontext(item.getString("Content"));
				dto.setDiarytypeId(diarytypeid);
				dto.setDiaryid(id);
				dto.setPiccount(piccount);
				list.add(dto);
			}
			}else{
				//baby.setBbeizhu2(msg);
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;

		
	}
	
	
	public static BabyDiaryByClass getTeacherDayDiary(AppContext appContext,Map<String, Object> paramMap){
		
		ArrayList<DiaryDto> list=new ArrayList<DiaryDto>();
		BabyDiaryByClass RBabyDiaryinClass=new BabyDiaryByClass();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_TEACHERLISTWANDR, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			String Name=person.getString("BabyName");
			String Pic=person.getString("BabyPic");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				
				
				JSONArray jsonDiary = jsonObject.getJSONArray("Diary");
				for (int i = 0; i < jsonDiary.length(); i++) {
					JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
					int id = item.getInt("Id"); // 获取对象对应的值
					String DiaryTypeName = item.getString("DiaryTypeName");
					String Title=item.getString("Title");
					String EntryTime = item.getString("EntryTime");
					int diarytypeid=item.getInt("DiaryTypeId");
					int rank=item.getInt("Rank");
					int piccount=item.getInt("PicCount");

				
					JSONArray piclist=item.getJSONArray("PicList");
				
					ArrayList<String> arraypiclist = new ArrayList<String>();
					for(int k=0;k<piclist.length();k++){
					//JSONObject item1=piclist.getJSONObject(k);
						String s=piclist.getString(k);
						arraypiclist.add(s.replace("~", "http://www.babysaga.cn"));
					}
					DiaryDto dto= new DiaryDto();
					dto.setPicList(arraypiclist);
					if(diarytypeid==100)
						dto.setDiaryTitle(Title);
					else
						dto.setDiaryTitle(DiaryTypeName);
					dto.setEntrytime(EntryTime);
					dto.setRank(rank);
					dto.setDiarycontext(item.getString("Content"));
					dto.setDiarytypeId(diarytypeid);
					dto.setDiaryid(id);
					dto.setPiccount(piccount);
					list.add(dto);

			}
				RBabyDiaryinClass.setDiaryList(list);
				RBabyDiaryinClass.setBabyName(Name);
				RBabyDiaryinClass.setBabyPic(Pic);
				
			}else{
				//baby.setBbeizhu2(msg);
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return RBabyDiaryinClass;

		
	}
	
	
	
	public static ArrayList<InfoMainBean> getInfoMain(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<InfoMainBean> list=new ArrayList<InfoMainBean>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTINOFMAIN, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("listinfo");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
//				int id = item.getInt("Id"); // 获取对象对应的值
//				String DiaryTypeName = item.getString("DiaryTypeName");
				String Name=item.getString("Name");
//				String EntryTime = item.getString("EntryTime");
//				int diarytypeid=item.getInt("DiaryTypeId");
				int count=item.getInt("Count");
//				int piccount=item.getInt("PicCount");

				
//				JSONArray piclist=item.getJSONArray("PicList");
//				
//				ArrayList<String> arraypiclist = new ArrayList<String>();
//				for(int k=0;k<piclist.length();k++){
//					//JSONObject item1=piclist.getJSONObject(k);
//					String s=piclist.getString(k);
//					arraypiclist.add(s.replace("~", "http://www.babysaga.cn"));
//				}
				
				
				InfoMainBean dto= new InfoMainBean();
				dto.setName(Name);
				dto.setCount(count);
				dto.setClassid(item.getInt("ClassId"));
				dto.setContent(item.getString("Content"));
				dto.setInfotime(item.getString("Dtime"));
				dto.setMainpic(item.getString("ImgUrl"));
				list.add(dto);
			}
			}else{
				//baby.setBbeizhu2(msg);
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	
	
	/**
	 * 教师账户获取信息列表头
	 * @param appContext
	 * @param paramMap
	 * @return
	 */

	public static ArrayList<TeacherInfoMainBean> getTeacherInfoMain(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<TeacherInfoMainBean> list=new ArrayList<TeacherInfoMainBean>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTTEACHERINOFMAIN, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			

			if (StatusCode == 200) {

				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("infoheadlist");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象

				String Name=item.getString("Name");

				int count=item.getInt("Count");
				String img=item.getString("Babyimg");

				
				
				TeacherInfoMainBean dto= new TeacherInfoMainBean();
				dto.setName(Name);
				dto.setCount(count);
				dto.setBabyimg(img);
				dto.setContent(item.getString("Msg"));
				dto.setInfotime(item.getString("Time"));
				dto.setBabyid(item.getInt("BabyId"));
				list.add(dto);
			}
			}else{
				//baby.setBbeizhu2(msg);
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	

	
	
	
	
	
	/**
	 * 获得当天的日程
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	
	public static ArrayList<ScheduleDto> getDaySchedule(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<ScheduleDto> list=new ArrayList<ScheduleDto>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTSCHEDULEBYDAY, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("ScheduleList");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				//String DiaryTypeName = item.getString("DiaryTypeName");
				String EntryTime = item.getString("EntryTime");
				int diarytypeid=item.getInt("DiaryType");

				ScheduleDto dto= new ScheduleDto();
				//dto.setScheduletitle(DiaryTypeName);
				dto.setId(id);
				dto.setEntrytime(EntryTime);
				dto.setSchedulecontext(item.getString("Content"));
				dto.setScheduletypeid(String.valueOf(diarytypeid));
						
				list.add(dto);
			}
			}else{
				
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	
	/**
	 * 获取班级学生列表
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static ArrayList<BabyBean> getBanjibabyList(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<BabyBean> list=new ArrayList<BabyBean>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTBANJIBABYLIST, paramMap,null);
			
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("BabyList");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				//String DiaryTypeName = item.getString("DiaryTypeName");
				String name = item.getString("Nickname");
				String imgpath=item.getString("HeadImgpath");
				String imgfile=item.getString("HeadImg");
				imgpath=imgpath.replace("~", "http://www.babysaga.cn");
				BabyBean dto= new BabyBean();
				//dto.setScheduletitle(DiaryTypeName);
				dto.setBname(name);
				dto.setImageurlfile(imgpath+imgfile);
				dto.setId(id);
				
						
				list.add(dto);
			}
			}else{
				
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	
	/**
	 * 获得班级学生列表，带有入园和离校时间
	 * @param appContext
	 * @param paramMap
	 * @return
	 */

	public static ArrayList<BanjiBabyAndInOutBean> getBanjibabyListAndInOut(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<BanjiBabyAndInOutBean> list=new ArrayList<BanjiBabyAndInOutBean>();
		try{
			
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTBANJIBABYLISTINOUTTIME, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("list");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Babyid"); // 获取对象对应的值
				//String DiaryTypeName = item.getString("DiaryTypeName");
				String name = item.getString("Babyname");
				String img=item.getString("Img");
				String intime=item.getString("Intime");
				String outtime=item.getString("Outtime");

				BanjiBabyAndInOutBean dto= new BanjiBabyAndInOutBean();
				//dto.setScheduletitle(DiaryTypeName);
				dto.setName(name);
				dto.setImg(img);
				dto.setId(id);
				dto.setIntime(intime);
				dto.setOuttime(outtime);
				
						
				list.add(dto);
			}
			}else{
				
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	

	
	
	public static ScheduleDto getScheduleById (AppContext appContext,Map<String, Object> paramMap){
		ArrayList<ScheduleDto> list=new ArrayList<ScheduleDto>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETSCHEDULEBYID, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			String schedulestr=person.getString("schedule");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(schedulestr);
				int id = jsonObject.getInt("Id");
				String entrytime = jsonObject.getString("EntryTime");
				String starttime=jsonObject.getString("StartTime");
				String endtime=jsonObject.getString("EndTime");
				ScheduleDto dto= new ScheduleDto();
				dto.setId(id);
				dto.setEntrytime(entrytime);
				dto.setStarttime(starttime);
				dto.setEndtime(endtime);
				dto.setSchedulecontext(jsonObject.getString("Content"));
				dto.setScheduletypeid(String.valueOf(jsonObject.getInt("DiaryType")));
				return dto;
				
			}else{
				
				String s="";
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			return null;
		}
		
		
	
	
	
	}
	
	
	
	
	
	
	
	
	/**
	 * 获得班级列表
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	
	public static ArrayList<BanjiDto> getBanjiList(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<BanjiDto> list=new ArrayList<BanjiDto>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTSEARCHBANJI, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonBanji = jsonObject.getJSONArray("ClassList");
				for (int i = 0; i < jsonBanji.length(); i++) {
				JSONObject item = jsonBanji.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				String banjiName = item.getString("Name");
				String banjiDescribe = item.getString("Description");
				

				BanjiDto dto= new BanjiDto();
				dto.setName(banjiName);
				dto.setDescribe(banjiDescribe);
				
				dto.setId(id);
				//图片赋值
				list.add(dto);
			}
			}else{
				//baby.setBbeizhu2(msg);
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
		
	}
	

	
	public static ArrayList<BanjiDto> getBanjiListByBabyId(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<BanjiDto> list=new ArrayList<BanjiDto>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTSEARCHBANJIBYBABYID, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonBanji = jsonObject.getJSONArray("ClassList");
				for (int i = 0; i < jsonBanji.length(); i++) {
				JSONObject item = jsonBanji.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				String banjiName = item.getString("Name");
				//String banjiDescribe = item.getString("Description"); 暂时没有班级描述
				

				BanjiDto dto= new BanjiDto();
				dto.setName(banjiName);
				//dto.setDescribe(banjiDescribe);
				
				dto.setId(id);
				//图片赋值
				list.add(dto);
			}
			}else{
				//baby.setBbeizhu2(msg);
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
		
	}
	

	
	/**
	 * 加入班级
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static int joinBanji(AppContext appContext,Map<String, Object> paramMap){
		
		int k=0;
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_JOINBANJI, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else if(StatusCode==-1003){
				k=-1003;
				String s=msg;
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
		}
		
		return k;
	}
	/**
	 * 到校离校时间
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static int InOutSchoolTime(AppContext appContext,Map<String, Object> paramMap){
		
		int k=0;
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_INOUTSCHOOLTIME, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else{
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
		}
		
		return k;
	}
	

	
	
	
	/**
	 * 获取宝贝信息
	 * @param appContext
	 * @return Babybean
	 * @throws AppException
	 */
	public static BabyBean getBabyInfo(AppContext appContext) throws AppException {
		BabyBean baby = new BabyBean();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETBABYINFO, null, null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				baby.setValidate(StatusCode+"");
				baby.setBname(UserInfo.getString("BabyName"));
				baby.setBnickname(UserInfo.getString("Nickname"));
				baby.setBsex(UserInfo.getString("Sex"));
				baby.setBbirth(UserInfo.getString("Birthday"));
				baby.setBnativeplace1(UserInfo.getString("Country"));
				baby.setBnativeplace2(UserInfo.getString("Province"));
				baby.setBnativeplace3(UserInfo.getString("City"));
				baby.setBblood(UserInfo.getString("BloodType"));
				baby.setBbeizhu(UserInfo.getString("Introduction"));
				baby.setImageurlfile(UserInfo.getString("HeadImg"));
			}else{
				baby.setBbeizhu2(msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return baby;
	}
	
	
	
	
	/**
	 * 获取宝贝个人信息
	 * @param appContext
	 * @return Babybean
	 * @throws AppException
	 */
	public static BabyBean getBabyPersonInfo(AppContext appContext,Map<String, Object> paramMap) throws AppException {
		BabyBean baby = new BabyBean();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETPERSONBABYINFO, null, null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				baby.setValidate(StatusCode+"");
				baby.setBname(UserInfo.getString("BabyName"));
				baby.setBnickname(UserInfo.getString("Nickname"));
				baby.setBsex(UserInfo.getString("Sex"));
				baby.setBbirth(UserInfo.getString("Birthday"));
				baby.setBnativeplace1(UserInfo.getString("Country"));
				baby.setBnativeplace2(UserInfo.getString("Province"));
				baby.setBnativeplace3(UserInfo.getString("City"));
				baby.setBblood(UserInfo.getString("BloodType"));
				baby.setBbeizhu(UserInfo.getString("Introduction"));
				baby.setImageurlfile(UserInfo.getString("HeadImg"));
			}else{
				baby.setBbeizhu2(msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return baby;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 注册， 自动处理cookie
	 * @param appContext
	 * @param username
	 * @param pwd
	 * @return  User
	 * @throws AppException
	 */
	public static User Register(AppContext appContext, Map<String, Object> paramMap) throws AppException {
		try{
			return RegisterRemoteService(appContext,paramMap);
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
				throw AppException.network(e);
		}
	}
	
	/**
	 * 修改密码， 自动处理cookie
	 * @param appContext
	 * @param username
	 * @param pwd
	 * @return  User
	 * @throws AppException
	 */
	public static User updateuserpasswd(AppContext appContext, Map<String, Object> paramMap) throws AppException {
		try{
			return updatepasswdRemoteService(appContext,paramMap);
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
				throw AppException.network(e);
		}
	}
	
	/**
	 * 获取Struts Http 登录请求信息
	 * @param name
	 * @param password
	 * @return User
	 */
	public static User loginRemoteService(AppContext appContext,Map<String, Object> paramMap) {
		User user = new User();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LOGIN_VALIDATE, paramMap, null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			String token = person.getString("Token");
			int isFirst = person.getInt("IsFirst");
			String shenfen=person.getString("Shenfen"); //获得身份
			System.out.println("token" + token);
			if (StatusCode == 200) {
				user.setUid(token);
				user.setIsfirst(isFirst);
				user.setShenfen(shenfen);
			}else{
				user.setValidate(msg);
			}
			user.setUname(StatusCode+"");
		} catch(Exception e) {
			e.setStackTrace(null);
		}
		return user;
	}
	
	/**
	 * 获取Struts Http 注册请求信息
	 * @param name
	 * @param password
	 * @return User
	 */
	public static User RegisterRemoteService(AppContext appContext,Map<String, Object> paramMap) {
		User user = new User();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_REGISTER_VALIDATE, paramMap, null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			user.setValidate(msg);
			user.setUname(StatusCode+"");
		} catch(Exception e) {
			e.setStackTrace(null);
		}
		return user;
	}
	
	
	/**
	 * 获取Struts Http 注册请求信息
	 * @param name
	 * @param password
	 * @return User
	 */
	public static User updatepasswdRemoteService(AppContext appContext,Map<String, Object> paramMap) {
		User user = new User();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_UPDATEPASSWD, paramMap, null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			String msg1 = person.getString("Token");
			user.setValidate(msg);
			user.setUname(StatusCode+"");
			user.setUid(msg1);
		} catch(Exception e) {
			e.setStackTrace(null);
		}
		return user;
	}
	
	
	/**
	 * 获取网络图片
	 * @param url
	 * @return Bitmap
	 */
	public static Bitmap getNetBitmap(String url) throws AppException {
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
		        InputStream inStream = httpGet.getResponseBodyAsStream();
		        //BitmapFactory.Options opts = new BitmapFactory.Options();
		    	//opts.inSampleSize = 4;
		    	//bitmap = BitmapFactory.decodeFile(url, opts);
		    	//bitmap = BitmapFactory.decodeStream(inStream, null, opts);
		        bitmap = BitmapFactory.decodeStream(inStream);
		        inStream.close();
		        break;
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		
		return bitmap;
	}
	
	/**
	 * 检查版本更新
	 * @param appContext
	 * @return Update
	 */
	public static Update checkVersion(AppContext appContext) throws AppException {
			Update update = new Update();
			try{
				InputStream ism =  ApiClient._post(appContext, URLs.URL_UPDATASOFT, null, null);
				// 获取响应结果
				String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
				// json解析过程
				JSONObject person = new JSONObject(json);
				
				System.out.println(json);
				// 接下来的就是JSON对象的操作了
				int StatusCode = person.getInt("StatusCode");
				String msg = person.getString("Error");
				int version = person.getInt("Version");
				String url = person.getString("Url");
				Boolean force = person.getBoolean("Force");
				String log = person.getString("Log");
				update.setStatusCode(StatusCode+"");
				if (StatusCode == 200) {
					update.setVersionCode(version);
					update.setDownloadUrl(url);
					update.setForce(force);
					update.setUpdateLog(log);
				}else{
					update.setError(msg);
				}
			} catch(Exception e) {
				e.printStackTrace();
				e.setStackTrace(null);
			}
			return update;
	}
	/**
	 * 修改完成的日程项
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static int saveScheduleComplete(AppContext appContext,
			Map<String, Object> paramMap) {
		
		int k=0;
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_SAVECOMPLETESCHEDULE, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else{
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
		}
		
		return k;
	}
	/**
	 * 获取作息中的图片列表
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static String[] getdiarypiclist(AppContext appContext,
			Map<String, Object> paramMap) {
		int k=0;
		ArrayList arraypic=new ArrayList<String>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETDIARYPICLIST, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				JSONArray piclist=person.getJSONArray("PicList");
				for (int i = 0; i < piclist.length(); i++) {
					JSONObject item = piclist.getJSONObject(i); // 得到每个对象
					arraypic.add(item.getString("Path")+item.getString("UploadPic"));
				}
				
				k=0;
			}else{
				String s="";
			}
			
			int size=arraypic.size(); 
			String[] array=new String[size]; 
			for(int i=0;i<arraypic.size();i++){ 
			array[i]=(String)arraypic.get(i); 
			} 
			return array;
			
			
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
			return null;
		}
		
	}
	public static ArrayList<InfoItem> getInfoItemList(AppContext appContext,
			Map<String, Object> paramMap) {
		
		ArrayList<InfoItem> list=new ArrayList<InfoItem>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETINFOITEMLIST, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("InfoItemList");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				int byuser=item.getInt("ByUser");
				int touser=item.getInt("ToUser");
				String content=item.getString("Content");
				String tiem=item.getString("ShowDate");
				int infotype=item.getInt("InfoType");
				String imgpath=item.getString("ImgPath");
				//String DiaryTypeName = item.getString("DiaryTypeName");
//				String name = item.getString("Nickname");
//				String imgpath=item.getString("HeadImgpath");
//				String imgfile=item.getString("HeadImg");
//				imgpath=imgpath.replace("~", "http://www.babysaga.cn");
				InfoItem dto= new InfoItem();
				//dto.setScheduletitle(DiaryTypeName);
				dto.setByuser(byuser);
				dto.setTouser(touser);
				dto.setContent(content);
				dto.setTime(tiem);
				dto.setImgpath(imgpath);
				dto.setInfotype(infotype);
				dto.setMyself(item.getBoolean("IsSelf"));
				
//				dto.setImageurlfile(imgpath+imgfile);

				
						
				list.add(dto);
			}
			}else{
				
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	
	
	/**
	 * 教师账户获得消息列表
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static ArrayList<InfoItem> getTeacherInfoItemList(AppContext appContext,
			Map<String, Object> paramMap) {
		
		ArrayList<InfoItem> list=new ArrayList<InfoItem>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_GETTEACHERINFOITEMLIST, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("InfoItemList");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				int byuser=item.getInt("ByUser");
				int touser=item.getInt("ToUser");
				String content=item.getString("Content");
				String tiem=item.getString("ShowDate");
				int infotype=item.getInt("InfoType");
				String imgpath=item.getString("ImgPath");
				//String DiaryTypeName = item.getString("DiaryTypeName");
//				String name = item.getString("Nickname");
//				String imgpath=item.getString("HeadImgpath");
//				String imgfile=item.getString("HeadImg");
//				imgpath=imgpath.replace("~", "http://www.babysaga.cn");
				InfoItem dto= new InfoItem();
				//dto.setScheduletitle(DiaryTypeName);
				dto.setByuser(byuser);
				dto.setTouser(touser);
				dto.setContent(content);
				dto.setTime(tiem);
				dto.setImgpath(imgpath);
				dto.setInfotype(infotype);
				dto.setMyself(item.getBoolean("IsSelf"));
				
//				dto.setImageurlfile(imgpath+imgfile);

				
						
				list.add(dto);
			}
			}else{
				
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	/**
	 * 保存班级修改信息
	 * @param appContext
	 * @param paramMap
	 * @param file
	 * @return
	 */
	public static BanjiDto saveBanjiInfo(AppContext appContext,
			Map<String, Object> paramMap, Map<String, File> file) {
		BanjiDto banji = new BanjiDto();
		try{
			InputStream ism =  ApiClient._post2(appContext, URLs.URL_EDITBANJIINFO, paramMap, file);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			JSONObject BanjiInfo = person.getJSONObject("Banji");
			if (StatusCode == 200) {

				banji.setName(BanjiInfo.getString("Name"));
				banji.setDescribe(BanjiInfo.getString("Description"));
				banji.setSchoool(BanjiInfo.getString("School"));
				banji.setPassword(BanjiInfo.getString("Password"));

			}else{
				banji=null;
						
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return banji;
	}
	public static int delDiary(AppContext appContext,
			Map<String, Object> paramMap) {
		
		int k=0;

		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_DELDIARY, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
			if (StatusCode == 200) {
				k=0;
			}else{
				k=-1;
			}
			
			return k;
			
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
			k=-1;
			return k;
		}
	}
	public static ArrayList<ScheduleDto> getBabyClassSchedule(
			AppContext appContext, Map<String, Object> paramMap) {
		ArrayList<ScheduleDto> list=new ArrayList<ScheduleDto>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_LISTSCHEDULEBYDAYANDCLASS, paramMap,null);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
			
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				//baby.setValidate(StatusCode+"");
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonDiary = jsonObject.getJSONArray("ListSchedule");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				int id = item.getInt("Id"); // 获取对象对应的值
				//String DiaryTypeName = item.getString("DiaryTypeName");
				String t1=item.getString("StartTime");
				String t2=item.getString("EndTime");
				String EntryTime="";
				if(t1.equals(t2)){
					EntryTime=t1;
				}else
				{
					EntryTime=t1+"-"+t2;
				}
				
				int diarytypeid=item.getInt("DiaryType");

				ScheduleDto dto= new ScheduleDto();
				//dto.setScheduletitle(DiaryTypeName);
				dto.setId(id);
				dto.setEntrytime(EntryTime);
				dto.setSchedulecontext(item.getString("Content"));
				dto.setScheduletypeid(String.valueOf(diarytypeid));
						
				list.add(dto);
			}
			}else{
				
				String s="";
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return list;
	}
	public static int uploadErrorFile(AppContext appContext,
			Map<String, Object> paramMap, Map<String, File> file) {
		int k=-1;
		try{
			InputStream ism =  ApiClient._post3(appContext, URLs.URL_UPLOADERRORFILE, paramMap, file);
			// 获取响应结果
			String json = new String(ApiClient.dealResponseResult(ism).getBytes(), "utf-8");
			// json解析过程
			JSONObject person = new JSONObject(json);
			
			System.out.println("json" + json);
			
			// 接下来的就是JSON对象的操作了
			int StatusCode = person.getInt("StatusCode");
			String msg = person.getString("Error");
//			JSONObject UserInfo = person.getJSONObject("UserInfo");
			if (StatusCode == 200) {
				k=0;
			}else{
				k=-1;
			}
		} catch(Exception e) {
			e.printStackTrace();
			e.setStackTrace(null);
		}
		return k;
	}

}
