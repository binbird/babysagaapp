package com.zylbaby.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件访问和操作类
 * @Description: 文件访问和操作类

 * @File: FileAccess.java

 * @Author zhuyuliang

 * @Date 2012-6-18 下午04:24:30

 * @Version V1.0
 */
public class FileAccess {
	/**
	 * String --> InputStream
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}
	/**
	 * InputStream --> String
	 * @param in
	 * @return
	 */
	public static String inputStream2String(InputStream in) throws IOException   { 
        StringBuffer out = new StringBuffer(); 
        byte[] b = new byte[4096]; 
        for(int n; (n = in.read(b))!= -1;){ 
             out.append(new String(b,0,n)); 
        } 
        return out.toString(); 
	} 
	
	public   static   String   inputStream2String1(InputStream   is)   throws   IOException{ 
        ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
        int   i=-1; 
        while((i=is.read())!=-1){ 
        baos.write(i); 
        } 
       return   baos.toString(); 
} 
}
