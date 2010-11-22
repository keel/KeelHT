/**
 * 
 */
package com.k99k.khunter;

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
	 * @param propArr 包含两个String的数组, 如["userDao","daos"],将从DaoManager中找出name为userDao的对象
	 * @return 如果未找到则返回null
	 */
	public static final Object findFromManager(String[] propArr){
		if (propArr.length != 2) {
			return null;
		}
		//FIXME 定位到指定的manager
		if (propArr[1].equals("actions")) {
			return ActionManager.findAction(propArr[0]);
		}else if(propArr[1].equals("daos")){
			return new MongoUserDao("testMongoUserDao",new MongoConn());
		}else if(propArr[1].equals("io")){
			
		}else if(propArr[1].equals("dataSources")){
			
		}

		
		return null;
	}
	
	/**
	 * FIXME 查找DataSource,需要DataSourceManager
	 * @param name DataSource的name
	 * @return DataSource
	 */
	public static final DataSourceInterface findDataSource(String name){
		DataSourceInterface ds = new MongoConn();
		
		return ds;
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
