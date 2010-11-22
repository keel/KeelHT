/**
 * 
 */
package com.k99k.khunter;

import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.stringtree.json.ExceptionErrorListener;
import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;

/**
 * KHunter系统管理者,协调管理中心
 * @author keel
 *
 */
public final class HTManager {

	private HTManager() {
	}
	
	static final Logger log = Logger.getLogger(HTManager.class);
	
	//private static final HTManager me = new HTManager();
	
	/**
	 * jsonReader
	 */
	static final JSONReader jsonReader = new JSONValidatingReader(new ExceptionErrorListener());
	
	
	/**
	 * 配置文件路径,注意这里需要配置成绝对路径
	 */
	private static String ini;
	
	/**
	 * 类加载路径,注意这里需要配置成绝对路径
	 */
	private static String classPath;
	
	/**
	 * 处理各Manager的初始化
	 * @param iniFile 配置文件路径
	 * @return
	 */
	public static boolean init(String iniFile){
		boolean initOK = false;
		log.info("================ [HTManager starting... ] ================");
		try {
			String iniJson = KIoc.readTxtInUTF8(iniFile);
			Map<String,?> root = (Map<String,?>) jsonReader.read(iniJson);
			if (root.containsKey("classPath") && root.containsKey("iniPath")) {
				classPath = (String) root.get("classPath");
				ini = (String) root.get("iniPath");
				
				//FIXME 初始化各个Manager
				//初始化DataSourceManager
				initOK = DataSourceManager.init(ini,classPath);
				log.info("DataSourceManager inited OK? " + initOK);
				
				//初始化DaoManager
				initOK = DaoManager.init(ini,classPath);
				log.info("DaoManager inited OK? " + initOK);
				
				//初始化ActionManager
				initOK = ActionManager.init(ini,classPath);
				log.info("ActionManager inited OK? " + initOK);
				
				
			}
		} catch (Exception e) {
			log.error("HTManager init error!", e);
			return false;
		}
		if (initOK) {
			log.info("================ [HTManager init OK!] ================");
		}
		return initOK;
	}
	
	/**
	 * 从指定的Manager中找到指定对象
	 * @param managerName Manager的name
	 * @param name 被查找对象的name
	 * @return 如果未找到则返回null
	 */
	public static final Object findFromManager(String managerName,String name){
		//FIXME 定位到指定的manager
		if (managerName.equals("actions")) {
			return ActionManager.findAction(name);
		}else if(managerName.equals("daos")){
			return DaoManager.findDao(name);
		}else if(managerName.equals("io")){
			
		}else if(managerName.equals("dataSources")){
			return DataSourceManager.findDataSource(name);
		}

		
		return null;
	}
	
	
	/**
	 * 供各个Manager从配置文件的Map中设置对象属性用
	 * @param obj 待设置对象
	 * @param m 由json配置文件对应节点读取出的对象属性Map
	 * @return 设置属性后的对象
	 */
	static final Object fetchProps(Object obj,Map<String,?> m){
		//加入属性值
		for (Iterator<String> it = m.keySet().iterator(); it.hasNext();) {
			String prop = it.next();
			//不以下划线开头的属性用setter方法注入
			if (!prop.startsWith("_")) {
				if (prop.indexOf("#") == -1) {
					Object value = m.get(prop);
					//处理Long形式的整数属性值,因为stringtree对数字读取为Long, BigInteger, Double or BigDecimal
					//TODO 对浮点数未处理 
					if (value instanceof Long) {
						int iv = ((Long)value).intValue();
						KIoc.setProp(obj, prop, iv);
					}else{
						KIoc.setProp(obj, prop, value);
					}
					
				}
				//由#号分为propName#manager两部分,后部分为指定的manager名
				else{
					String[] propArr = prop.split("#");
					String targetName = (String) m.get(prop);
					Object value = HTManager.findFromManager(propArr[1],targetName);
					if (value != null) {
						KIoc.setProp(obj, propArr[0], value);
					}else{
						log.error("The prop can't find from HTManager, didn't set this prop:"+prop);
					}
				}
				
			}
		}
		return obj;
	}
	
	
	/**
	 * 退出操作
	 */
	public static void exit(){
		log.info("================ [HTManager exit] ================");
	}
	
//	static final Map<String,Object> managerMap = createManagerMap();
//	
//	private static final Map<String,Object> createManagerMap(){
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("actions", ActionManager.getInstance());
//		return map;
//	}
}
