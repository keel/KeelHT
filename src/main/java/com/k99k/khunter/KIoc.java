/**
 * 
 */
package com.k99k.khunter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import com.k99k.tools.JSONTool;

/**
 * 注入解析器,支持热加载类文件
 * @author keel
 *
 */
public final class KIoc {

	/**
	 * 
	 */
	public KIoc() {
		
	}
	
	public static final int ERR_CODE1 = 9;
	
	static final Logger log = Logger.getLogger(KIoc.class);
	
	/**
	 * 装载一个class文件并生成Object,要求此类有一个不带参数的构造方法
	 * @param classURL class文件所在的路径(不含包路径),本地文件以file:/开头
	 * @param className 类的全名，含包名在内
	 * @return class生成的Instance,失败则返回null
	 */
	public final static Object loadClassInstance(String classURL,String className){
		try {
			URL url = new URL(classURL);
			URLClassLoader urlCL = URLClassLoader.newInstance(new URL[] { url }, KIoc.class.getClassLoader());
			Class<?> c = urlCL.loadClass(className);
			Object object = c.newInstance();
			return object;
		} catch (Exception e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 15, e, className);
			return null;
		}
	}
	
	/**
	 * 装载一个class文件并生成Object
	 * @param classURL class文件所在的路径(不含包路径),本地文件以file:/开头
	 * @param className 类的全名，含包名在内
	 * @param args 构造方法中的参数,Object[]形式
	 * <strong>[注意:]</strong>仅根据构造方法参数的数量来创建新对象,不按实际参数类型查找具体的构造方法,
	 * 避免参数是extends和implement的对象时出现NoSuchMethodException
	 * @return class生成的Instance,失败则返回null
	 */
	public final static Object loadClassInstance(String classURL,String className,Object[] args){
		try {
			URL url = new URL(classURL);
			URLClassLoader urlCL = URLClassLoader.newInstance(new URL[] { url }, KIoc.class.getClassLoader());
			Class<?> c = urlCL.loadClass(className);
			
			/*
			ClassLoader loader = KIoc.class.getClassLoader();
			Class<?> c = loader.loadClass(className);
			*/
			

			
			//仅根据构造方法参数的数量来创建新对象，不按实际参数查找具体的构造方法
			Constructor<?>[] consArr = c.getConstructors();
			for (int i = 0; i < consArr.length; i++) {
				Constructor<?> co = consArr[i];
				//Class<?>[] paraTypes = co.getParameterTypes();
				if (co.getParameterTypes().length == args.length) {
					return co.newInstance(args);
				}
			}
			
			/* 往下实际不会被执行到,这是另一种构建方法 */
			//实际去查找，此时需要参数的类型精确匹配，参数如果是extends和implement的对象均不可以
			//得到参数的Class数组
			Class<?>[] argsClass = new Class[args.length];
			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
			}
			Constructor<?> cons = c.getConstructor(argsClass);
			return cons.newInstance(args);
		} catch (Exception e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 15, e, className);
			return null;
		}
	}
	
	
	/**
	 * 使用setter注入对象属性,注意属性名必须以英文字母开头,setter方法格式为"set+首字母大写的propName",不支持isAbc的注入
	 * @param obj 被注入对象
	 * @param propName 属性名
	 * @param value 属性值
	 * @return 注入是否成功
	 */
	public final static boolean setProp(Object obj,String propName,Object value){
		//先转化属性为setter方法名,无需在Method循环时每次都生成
		propName = getSetterMethodName(propName);
		try {
			
			//因无法处理基本类型而采用的轮循方式,仅比较方法名，不考虑参数
			Method[] ms = obj.getClass().getMethods();
			for (int i = 0,j = ms.length; i < j; i++) {
				if (ms[i].getName().equals(propName)) {
					ms[i].invoke(obj, value);
					return true;
				}
			}
			ErrorCode.logError(log, KIoc.ERR_CODE1, 16,propName);
			//log.error("setProp ERROR! Method can't be found:"+propName);
			return false;
			
			/*
			 * 此种方式在参数中运用接口时会出现NoSuchMethodException!
			//--处理基本类型参数，如setId(int id)
			Class<?> vc = value.getClass();
			Class<?> pvc = wrapperPrimitiveMap.get(vc);
			if (pvc != null) {
				vc = pvc;
			}
			Method m = obj.getClass().getMethod(propName, vc); 
			m.invoke(obj, value);
			
			return true;
			
			*/
		} catch (Exception e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 17,e,propName);
			return false;
		}
	}
	
	/**
	 * 由属性名得到setter方法名,如属性name，返回setName.<br />
	 * 注意属性名必须以英文字母开头,setter方法格式为"set+首字母大写的propName",不支持isAbc的注入
	 * @param propName
	 * @return 
	 */
	static final String getSetterMethodName(String propName){
		StringBuilder sb = new StringBuilder("set");
		sb.append(Character.toUpperCase(propName.charAt(0)));
		sb.append(propName.substring(1));
		return sb.toString();
	}
	
	/**
	 * 由属性名得到getter方法名,如属性name，返回getName.<br />
	 * 注意属性名必须以英文字母开头,getter方法格式为"get+首字母大写的propName",不支持isAbc的注入
	 * @param propName
	 * @return 
	 */
	static final String getGetterMethodName(String propName){
		StringBuilder sb = new StringBuilder("get");
		sb.append(Character.toUpperCase(propName.charAt(0)));
		sb.append(propName.substring(1));
		return sb.toString();
	}
	
	/**
	 * 使用setter注入对象属性,注意属性名必须以英文字母开头,setter方法格式为"set+首字母大写的propName",不支持isAbc的注入
	 * @param obj 被注入对象
	 * @param propNames 属性名数组
	 * @param values 属性值数组
	 * @return 注入是否全部成功
	 */
	public final static boolean setProps(Object obj,String[] propNames,Object[] values){
		int currentProp = 0;
		int propCounts = propNames.length;
		
		/*
		 * 此种方式在参数中运用接口时会出现NoSuchMethodException!
		try {
			for (; currentProp < propCounts; currentProp++) {
				//--处理基本类型参数，如setId(int id)
				Class<?> vc = values[currentProp].getClass();
				Class<?> pvc = wrapperPrimitiveMap.get(vc);
				if (pvc != null) {
					vc = pvc;
				}
				Method m = obj.getClass().getMethod(getSetterMethodName(propNames[currentProp]), vc); 
				m.invoke(obj, values[currentProp]);
			}
			return true;
			
		} catch (Exception e) {
			log.error("setProps ERROR:"+propNames[currentProp], e);
			return false;
		}
		*/
		
		/* 轮循方式,可解决NoSuchMethodException问题,效率略低 */		
		//先转化属性为setter方法名,无需在Method循环时每次都生成
		for (int i = 0; i < propCounts; i++) {
			propNames[i] = getSetterMethodName(propNames[i]);
		}
		boolean setAllOk = true;
		try {
			Method[] ms = obj.getClass().getMethods();
			
			for (; currentProp < propCounts; currentProp++) {
				boolean setOk = false;
				for (int i = 0,j = ms.length; i < j; i++) {
					if (ms[i].getName().equals(propNames[currentProp])) {
						ms[i].invoke(obj, values[currentProp]);
						setOk = true;
						break;
					}
				}
				if (!setOk) {
					setAllOk = false;
					ErrorCode.logError(log, KIoc.ERR_CODE1, 18 , currentProp+"");
					//log.error("setProps ERROR! one of the props can't be found,para position:"+currentProp);
				}
			}
			
			return setAllOk;
		} catch (Exception e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 19 , e,currentProp+"");
			return false;
		}
		
	}
	
	/**
	 * 使用setter注入对象属性,注意属性名必须以英文字母开头,setter方法格式为"set+首字母大写的propName",不支持isAbc的注入
	 * @param obj 被注入对象
	 * @param props Map<String(属性名),Object(属性值)>
	 * @return 注入是否全部成功
	 */
	public final static boolean setProps(Object obj,Map<String,Object> props){
		int len = props.size();
		String[] keys = new String[len];
		Object[] values = new Object[len];
		int i = 0;
		for (Iterator<String> it = props.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			keys[i] = key;
			values[i] = props.get(key);
			i++;
		}
		return setProps(obj,keys,values);
	}
	
	/**
	 * Maps wrapper <code>Class</code>es to their corresponding primitive types.
	 */
	static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = createWrapperPrimitiveMap();

	private static final Map<Class<?>, Class<?>> createWrapperPrimitiveMap() {
		Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
		map.put(Boolean.class, boolean.class);
		map.put(Byte.class, byte.class);
		map.put(Character.class, char.class);
		map.put(Short.class, short.class);
		map.put(Integer.class, int.class);
		map.put(Long.class, long.class);
		map.put(Double.class, double.class);
		map.put(Float.class, float.class);
		return map;
	}
	
	
	/**
	 * 读取一个UTF-8编码的文件
	 * @param filePath 文件路径
	 * @return
	 */
	public static final String readTxtInUTF8(String filePath){
		return readTxt(filePath,"UTF8");
	}

	
	/**
	 * 读取一个指定编码的文件
	 * @param filePath 文件路径
	 * @param encoding 编码
	 * @return
	 */
	public static final String readTxt(String filePath,String encoding){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(filePath), encoding));

			String s;
			while ((s = in.readLine()) != null) {
				sb.append(s).append("\n");
			}
		} catch (UnsupportedEncodingException e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 20 , e,filePath);
//			log.error("readTxt encoding ERROR:"+filePath, e);
			return null;
		} catch (IOException e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 21 , e,filePath);
//			log.error("readTxt io ERROR:"+filePath, e);
			return null;
		}

		return sb.toString();
	}
	/**
	 * 写入文件,utf-8方式
	 * @param file 本地文件名
	 * @param input 需要写入的字符串
	 */
	public static final boolean writeTxtInUTF8(String file, String input) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF8"));
			out.write(input);
			out.close();
		} catch (IOException e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 22 , e,file);
			//log.error("writeTxtInUTF8 io ERROR:"+file, e);
			return false;
		}
		return true;
	}
	
	/**
	 * 保存json形式的配置文件
	 * @param iniFileName 配置文件名
	 * @param json 数据内容
	 * @return 0表示成功,其他为错误码
	 */
	public static final int saveJsonIni(String iniFileName,String json){
		if (iniFileName == null || iniFileName.trim().length()<2) {
			return 11;
			//msg.addData("save", "ini not found.");
		}else{
			if (json == null || json.length() < 10) {
				//msg.addData("save", "no para");
				return 12;
			}else {
				//验证json格式
				if (JSONTool.validateJsonString(json)) {
					//保存
					if(KIoc.writeTxtInUTF8(HTManager.getIniPath()+iniFileName+".json", json)){
						return 0;
						//msg.addData("save", "ok");
					}else{
						//msg.addData("save", "save fail");
						return 13;
					}
				}else{
					return 14;
					//msg.addData("save", "validate fail");
				}
			}
		}
	}
	

	/**
	 * 更新json配置文件的某一个节点,这里的目标数据是一个键值对
	 * @param iniFilePath 配置文件全路径
	 * @param jsonPath json路径 ,String[]
	 * @param opType 操作方式:2为删除,其他值为更新或新增
	 * @param iniKey 需要更新的键
	 * @param iniValue 更新的值
	 * @return 是否更新成功
	 */
	@SuppressWarnings("unchecked")
	public static final boolean updateIniFileNode(String iniFilePath,String[] jsonPath,int opType,String iniKey,Object iniValue){
		try {
			String ini = KIoc.readTxtInUTF8(iniFilePath);
			HashMap<String,Object> root = (HashMap<String,Object>) JSONTool.readJsonString(ini);
			HashMap<String,Object> target = root;
			//定位到需要更新的节点
			for (int i = 0; i < jsonPath.length; i++) {
				target = (HashMap<String,Object>)(target.get(jsonPath[i]));
			}
			if (opType == 2) {
				target.remove(iniKey);
			}else{
				target.put(iniKey, iniValue);
			}
			
			KIoc.writeTxtInUTF8(iniFilePath, JSONTool.writeFormatedJsonString(root));
		} catch (Exception e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1, 23,e, iniFilePath+" | "+jsonPath+" | "+iniValue);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 更新json配置文件的某一个数组节点,加入目标值(iniValue)
	 * @param iniFilePath 配置文件全路径
	 * @param jsonPath json路径 ,String[]，最后一个为ArrayList的键
	 * @param opType 操作方式:0或其他值为新增,1为更新,2为删除
	 * @param position 操作位置,即ArrayList中的index
	 * @param iniValue 新增节点的值
	 * @return 是否更新成功
	 */
	@SuppressWarnings("unchecked")
	public static final boolean updateIniFileNode(String iniFilePath,String[] jsonPath,int opType,int position,Object iniValue){
		try {
			String ini = KIoc.readTxtInUTF8(iniFilePath);
			HashMap<String,Object> root = (HashMap<String,Object>) JSONTool.readJsonString(ini);
			HashMap<String,Object> target = root;
			//定位到需要更新的节点
			for (int i = 0; i < jsonPath.length-1; i++) {
				target = (HashMap<String,Object>)(target.get(jsonPath[i]));
			}
			ArrayList<Object> listTarget = (ArrayList<Object>)target.get(jsonPath[jsonPath.length-1]);
			if (opType == 0) {
				listTarget.add(iniValue);
			}else if(opType == 1){
				listTarget.set(position, iniValue);
			}else if(opType == 2){
				listTarget.remove(position);
			}else{
				listTarget.add(iniValue);
			}
			KIoc.writeTxtInUTF8(iniFilePath, JSONTool.writeFormatedJsonString(root));
		} catch (Exception e) {
			ErrorCode.logError(log, KIoc.ERR_CODE1,24,e, iniFilePath+" | "+jsonPath+" | "+iniValue);
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		/*
		Action a = (Action)KIoc.loadClassInstance("file:/F:/works/workspace_keel/KHunter/WebContent/WEB-INF/classes/", "com.k99k.khunter.Action", new Object[]{"test"});
		System.out.println("Action name:"+a.getName());
		int idi = 3344;
		KIoc.setProp(a, "id", idi);
		System.out.println(a.getId());
		
		HTItem item = (HTItem)KIoc.loadClassInstance("file:/F:/works/workspace_keel/KHunter/WebContent/WEB-INF/classes/", "com.k99k.khunter.HTItem");
		String[] props = new String[]{"ack","function","special"};
		Object[] values = new Object[]{34,"some function","very special"};
		KIoc.setProps(item, props, values);
		System.out.println(item.getAck()+"|"+item.getFunction()+"|"+item.getSpecial());
		*/
		
		
		/*
		String s = KIoc.readTxtInUTF8("f:/works/workspace_keel/KHunter/WebContent/WEB-INF/actions.json");
		System.out.println(s);
		*/
		
		
	}

}
