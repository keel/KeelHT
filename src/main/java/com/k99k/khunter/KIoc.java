/**
 * 
 */
package com.k99k.khunter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

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
			log.error("loadClassInstance ERROR!", e);
			return null;
		}
	}
	
	/**
	 * 装载一个class文件并生成Object,要求此类有一个不带参数的构造方法
	 * @param classURL class文件所在的路径(不含包路径),本地文件以file:/开头
	 * @param className 类的全名，含包名在内
	 * @param args Object[]形式的参数,
	 * <br /><strong>[注意:]</strong>仅根据构造方法参数的数量来创建新对象,不按实际参数类型查找具体的构造方法,
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
			log.error("loadClassInstance ERROR!", e);
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
			log.error("setProp ERROR! Method can't be found:"+propName);
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
			log.error("setProp ERROR:"+propName, e);
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
					log.error("setProps ERROR! one of the props can't be found,para position:"+currentProp);
				}
			}
			
			return setAllOk;
		} catch (Exception e) {
			log.error("setProps ERROR!para position:"+currentProp, e);
			return false;
		}
		
	}
	
	/**
	 * Maps wrapper <code>Class</code>es to their corresponding primitive types.
	 */
	private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap = createWrapperPrimitiveMap();

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
			log.error("readTxt encoding ERROR:"+filePath, e);
		} catch (IOException e) {
			log.error("readTxt io ERROR:"+filePath, e);
		}

		return sb.toString();
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
