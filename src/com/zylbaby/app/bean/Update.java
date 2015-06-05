package com.zylbaby.app.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.StringUtils;

/**
 * 应用程序更新实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Update implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "OSSystem";
	
	private int versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	private Boolean force;
	private String StatusCode;
	private String error;
	
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	public Boolean getForce() {
		return force;
	}
	public void setForce(Boolean force) {
		this.force = force;
	}
	public String getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
}
