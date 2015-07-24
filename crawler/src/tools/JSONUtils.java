package tools;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtils {
	public static JSONObject toJSONObject(Object o){
		if(o == null){
			return null;
		}
		if(o instanceof JSONObject){
			return (JSONObject)o;
		}else {
			return JSONObject.fromObject(o);
		}
	}
	
	public static JSONArray toJSONArray(Object o){
		if(o == null){
			return null;
		}
		if(o instanceof JSONArray){
			return (JSONArray)o;
		}else {
			return JSONArray.fromObject(o);
		}
	}
	
	public static int getIntValue(String json, String name){
		String str = getValue(json, name ,0 ,":", ",");
		int rtn = 0;
		if(!str.equals(StringUtils.EMPTY)){
			StringBuffer sb = new StringBuffer();
			char[] c = str.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if(Character.isDigit(c[i]) || c[i] == '.'){
					sb.append(c[i]);
				}
			}
			String temp = sb.toString();
			if(!StringUtils.isBlank(temp)){
				rtn = Integer.parseInt(temp);
			}
		}
		return rtn;
	}
	
	public static double getDoubleValue(String json, String name){
		String str = getValue(json, name ,0 ,":", ",");
		double rtn = 0;
		if(!str.equals(StringUtils.EMPTY)){
			StringBuffer sb = new StringBuffer();
			char[] c = str.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if(Character.isDigit(c[i]) || c[i] == '.'){
					sb.append(c[i]);
				}
			}
			String temp = sb.toString();
			if(!StringUtils.isBlank(temp)){
				rtn = Double.parseDouble(temp);
			}
		}
		return rtn;
	}
	
	public static String getValue(String json, String name,int beginIndex, String startSign,String endSign){
		int index = json.indexOf(name,beginIndex) + name.length();
		return getValue(json, index, startSign, endSign);
	}
	
	public static String getValue(String json,int beginIndex, String startSign,String endSign){
		int start = json.indexOf(startSign,beginIndex);
		int end = json.indexOf(endSign,start + startSign.length()) + endSign.length();
		if(start != -1 && end != -1 && start < end){
			return json.substring(start,end);
		}else{
			return StringUtils.EMPTY;
		}
	}
}
