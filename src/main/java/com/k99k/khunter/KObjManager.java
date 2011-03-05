/**
 * 
 */
package com.k99k.khunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * KObject管理器
 * @author keel
 *
 */
public final class KObjManager {

	private KObjManager() {
	}
	
	static final Logger log = Logger.getLogger(KObjManager.class);
	
	
	/**
	 * 用于在json中定位
	 * @return 返回"daos"
	 */
	public static final String getName(){
		return "kobjs";
	}
	

	/**
	 * 存储Action的Map,初始化大小为50
	 */
	private static final HashMap<String, KObjConfig> kobjMap = new HashMap<String, KObjConfig>(50);

	
	private static boolean isInitOK = false;
	
	private static String iniFilePath;
	
	public static final boolean isInitOK(){
		return isInitOK;
	}
	
	/**
	 * 查找所有包含key字符串的的KObj的名称List,不区分大小写
	 * @param key 查找key
	 * @return HashMap<String, KObjConfig> 
	 */
	@SuppressWarnings("unchecked")
	public static final HashMap<String, KObjConfig> searchKObjList(String key){
		key = StringUtil.objToStrNotNull(key).trim();
		if (key.equals("")) {
			return (HashMap<String, KObjConfig>) kobjMap.clone();
		}
		key = key.toLowerCase();
		HashMap<String, KObjConfig> reMap = new HashMap<String, KObjConfig>(50);
		for (Iterator<String> it = kobjMap.keySet().iterator(); it.hasNext();) {
			String kobj =  it.next();
			KObjConfig kc = (KObjConfig) kobjMap.get(kobj);
			if (kobj.toLowerCase().indexOf(key) > -1 || (kc.getIntro().toLowerCase().indexOf(key) > -1)) {
				reMap.put(kobj,kc);
			}
		}
		return reMap;
	}
	
	/**
	 * 保存配置,同时将原文件按时间扩展名备份
	 * @return
	 */
	public static final boolean saveIni(){
		//保存
		HashMap<String,Object> root = new HashMap<String,Object>();
		HashMap<String,Object> map = new HashMap<String,Object>();
		for (Iterator<String> iterator = kobjMap.keySet().iterator(); iterator.hasNext();) {
			String kobjName = iterator.next();
			map.put(kobjName, ((KObjConfig)kobjMap.get(kobjName)).toMap());
		}
		root.put("kobjs", map);
		int re = KIoc.saveJsonToFile(iniFilePath, root);
		if (re != 0) {
			ErrorCode.logError(log, 9,re, " - in KObjManager.save()");
			return false;
		}
		return true;
	}
	
	/**
	 * 返回kobjMap,这里返回的是clone后的对象
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public static final HashMap<String, KObjConfig> getKObjMap(){
		return (HashMap<String, KObjConfig>) kobjMap.clone();
	}
	
	public static final KObjSchema findSchema(String kobjName){
		KObjConfig kc = (KObjConfig)kobjMap.get(kobjName);
		return kc.getKobjSchema();
	}
	
	public static final KObject createEmptyKObj(String kobjName){
		KObjConfig kc = (KObjConfig)kobjMap.get(kobjName);
		return kc.getKobjSchema().createEmptyKObj();
	}
	
	public static final boolean setProp(String kobjName,KObject kobj,String kobjPath,Object prop){
		KObjConfig kc = (KObjConfig)kobjMap.get(kobjName);
		return kc.getKobjSchema().setProp(kobj, kobjPath, prop);
	}
	
	public static final boolean validateKObjMap(String kobjName,HashMap<String,Object> kMap){
		KObjConfig kc = (KObjConfig)kobjMap.get(kobjName);
		return kc.getKobjSchema().validate(kMap);
	}
	
	public static final boolean validateKObjPath(String kobjName,String kobjPath,Object value){
		KObjConfig kc = (KObjConfig)kobjMap.get(kobjName);
		return kc.getKobjSchema().validateColumns(kobjPath, value);
	}
	
	public static final KObjConfig findKObjConfig(String kobjName){
		return kobjMap.get(kobjName);
	}
	
	/**
	 * 初始化KObjManager
	 * @param iniFile 配置文件路径
	 * @param classPath class文件所在的路径
	 * @return 是否初始化成功
	 */
	@SuppressWarnings("unchecked")
	public final static boolean init(String iniFile){
		if (!isInitOK) {
			//读取配置文件
			try {
				
				String ini = KIoc.readTxtInUTF8(iniFile);
				Map<String,?> root = (Map<String,?>) JSONTool.readJsonString(ini);
				//先定位到json的对应属性
				Map<String, ?> mgr = (Map<String, ?>) root.get(getName());

				//循环加入
				int i = 0;
				for (Iterator<String> iter = mgr.keySet().iterator(); iter.hasNext();) {
					String keyName = iter.next();
					Map<String, ?> m = (Map<String, ?>) mgr.get(keyName);
					if(!JSONTool.checkMapTypes(m, new String[]{"intro","dao","columns","indexes"}, new Class[]{String.class,HashMap.class,ArrayList.class,ArrayList.class})){
						ErrorCode.logError(log, 8, 6," key:"+keyName);
						continue;
					}
					KObjConfig kc = new KObjConfig();
					if(!kc.setDaoConfig((HashMap<String, Object>) m.get("dao"))){
						ErrorCode.logError(log, 8, 7, " dao error. i:"+i);
						continue;
					}
					KObjSchema ks = new KObjSchema();
					boolean initSchema = ks.initSchema((ArrayList<HashMap<String, Object>>) m.get("columns"),(ArrayList<HashMap<String, Object>>) m.get("indexes"));
					if (!initSchema) {
						ErrorCode.logError(log, 8, 7, " i:"+i);
						continue;
					}
					kc.setKobjSchema(ks);
					kc.setKobjName(keyName);
					kobjMap.put(keyName, kc);
				}
				
			} catch (Exception e) {
				ErrorCode.logError(log, 8, 8, e, "");
				isInitOK = false;
				return false;
			}
			isInitOK = true;
			//更新配置文件位置
			iniFilePath = iniFile;
			//classFilePath = classPath;
			log.info("KObjManager init OK!");
		}
		return true;
	}
	
	/**
	 * 重新初始化
	 * @param iniFile 配置文件路径
	 * @return 是否初始化成功
	 */
	public final static boolean reInit(String iniFile){
		isInitOK = false;
		return init(iniFile);
	}

	/**
	 * 退出KObjManager时的操作
	 */
	public static final void exit(){
		log.info("KObjManager exited.");
	}
	
	
}
