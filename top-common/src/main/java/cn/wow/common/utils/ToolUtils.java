package cn.wow.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ToolUtils {

	/**
	 * 把字符串转换成列表
	 * @param str        字符串
	 * @param separator  分隔符
	 */
	public static List<String> getListByString(String str, String separator){
		String[] arry = str.split(separator);
		List<String> list = Arrays.asList(arry);
		
		return list;
	}
	
	
	/**
	 * 把字符转换成整型
	 */
	public static int getDateIntVal(String data){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(data);
			Long val = date.getTime() / 1000;
			
			return val.intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 获取指定日期的最大最小时间数
	 * @param str  日期字符串
	 * @parma type 1：最大，2：最小
	 */
	public static int getMaxMinDate(String str, int type){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date date = null;
			if(type == 1){
				date = sdf.parse(str + " 23:59:59");
			}else{
				date = sdf.parse(str + " 00:00:00");
			}
			Long val = date.getTime() / 1000;
			
			return val.intValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	
}
