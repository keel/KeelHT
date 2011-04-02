package com.k99k.tools;

/**
 * 字符串工具类
 * @author keel
 *
 */
public final class StringUtil {
	
	private StringUtil(){
		
	}
	
	
	/**
	 * Object转为String,如果是null则转为""
	 * @param obj
	 * @return String (not null)
	 */
	public static final String objToStrNotNull(Object obj){
		String s = (obj == null)?"":obj.toString();
		return s;
	}
	
	/**
	 * Object转为非负整数int,若为null或其他,返回-1
	 * @param obj
	 * @return int
	 */
	public static final int objToNonNegativeInt(Object obj){
		if(obj != null){
			String s = obj.toString();
			if(s.matches("[0-9]*")){
				return Integer.parseInt(s);
			}
		}
		return -1;
	}
	
//	/**
//	 * 复制HashMap
//	 * @param map
//	 * @return hashMap with the same keys and values.
//	 */
//	@SuppressWarnings("unchecked")
//	public static final HashMap hashMapClone(HashMap map){
//		HashMap target = new HashMap();
//		target.putAll(map);
//        return target;
//	}
	
	/**
	 * 判断一个字符串是不是数字组成
	 * @param s 字符。
	 * @return
	 */
	public static final boolean isDigits(String s){
		if(s==null||s.length()==0)return false;
		for(int i=0;i<s.length();i++){
			if(!Character.isDigit(s.charAt(i)))return false;
		}
		return true;
	}

	/**
	 * 判断String是否达到指定长度
	 * @param str
	 * @param len
	 * @return
	 */
	public static final boolean isStringWithLen(String str,int len){
		if (str == null) {
			return false;
		}
		if (str.trim().length() < len) {
			return false;
		}
		return true;
	}
	
	public static final int[] stringToIntArray(String arrString,String separator){
		try {
			String s = arrString.replaceAll("\\s", "");
			String[] sa = s.split(separator);
			int[] ia = new int[sa.length];
			for (int i = 0; i < sa.length; i++) {
				if (isDigits(sa[i])) {
					ia[i] = Integer.parseInt(sa[i]);
				}else{
					//出现非数字,直接返回
					return null;
				}
			}
			return ia;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	/**
	 * 得到一个格式化日期
	 * @param format yyyyMMdd 或yyyyMM等
	 * @return
	 */
	public static final String getFormatDateString(String format){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		java.util.Date d = new java.util.Date();
		return sdf.format(d);
	}
	
	/**
	 * 把字符串中的HTML代码转换成页面显示的代码
	 * @param 字符串 str
	 * @return String 替换后的字符传
	 */
	public static final String repstr(String str)	{
		if(str==null)return "";
		str=str.replaceAll(" ", "&nbsp;");
		str=str.replaceAll("<", "&lt;");
		str=str.replaceAll(">", "&gt;");
		str=str.replaceAll("\"", "&quot;");
		str=str.replaceAll("\n", "<br />");
		return str;
	}
	
	/**
	 * 把页面的HTML代码转换成页面显示的代码
	 * @param String 替换前的字符串
	 * @return String 替换后的字符串
	 */
	public static final String repstr1(String str){
		if(str==null)return "";
		str=str.replaceAll("<", "&lt;");
		str=str.replaceAll(">", "&gt;");
		str=str.replaceAll("\"", "&quot;");
		return str;
	}
	
	/**
	 * 把页面显示的代码替换成HTML代码
	 * @param String 替换前的字符串
	 * @return String 替换后的字符串
	 */
	public static final String repstr2(String str){
		if(str==null)return "";
		str=str.replaceAll("&lt;", "<");
		str=str.replaceAll("&gt;", ">");
		str=str.replaceAll("&quot;", "\"");
		return str;
	}
	
	/**
	 * 把字符串里的换行、尖括号、双引号去掉，用于在HTML头的keywords里显示
	 * @param str
	 * @return
	 */
	public static final String clearHtmlTagAndBlank(String str){
		if(str==null)return "";
		str=str.replaceAll("<", "");
		str=str.replaceAll(">", "");
		str=str.replaceAll("\"", "");
		str=str.replaceAll("\\s", "");
		return str;
	}
	
	/**
	 * 去掉<>和"，屏蔽HTML
	 * @param str
	 * @return
	 */
	public static final String clearHTML(String str){
		if(str==null)return "";
		str=str.replaceAll("<", "&lt;");
		str=str.replaceAll(">", "&gt;");
		str=str.replaceAll("\"", "&quot;");
		return str;
	}
	
	public static void main(String[] args) {
		String s = "23,234234,2342,21341234,22";
		int[] ia = stringToIntArray("23,32,342,3,,",",");
		if (ia==null) {
			System.out.println("null");
		}else{
	
			for (int i = 0; i < ia.length; i++) {
				System.out.println(ia[i]);
			}
		}
		
		System.out.println(s.matches("(\\d+,)*(\\d+)"));
	}
}
