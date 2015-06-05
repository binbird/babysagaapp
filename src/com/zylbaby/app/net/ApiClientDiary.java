package com.zylbaby.app.net;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.TimeTag;
import com.zylbaby.app.global.AppContext;

public class ApiClientDiary {
	/**
	 * 获取班级学生列表
	 * @param appContext
	 * @param paramMap
	 * @return
	 */
	public static ArrayList<TimeTag> getTimeTagList(AppContext appContext,Map<String, Object> paramMap){
		ArrayList<TimeTag> list=new ArrayList<TimeTag>();
		try{
			InputStream ism =  ApiClient._post(appContext, URLs.URL_SETTIMETAGLIST, paramMap,null);
			
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
				JSONArray jsonDiary = jsonObject.getJSONArray("ListTimeTag");
				for (int i = 0; i < jsonDiary.length(); i++) {
				JSONObject item = jsonDiary.getJSONObject(i); // 得到每个对象
				String time = item.getString("Date");
				int tag=item.getInt("Tag");
				TimeTag dto= new TimeTag();

				dto.setTime(time);
				dto.setTag(tag);
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

}
