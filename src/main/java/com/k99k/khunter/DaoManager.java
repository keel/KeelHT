/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.stringtree.json.ExceptionErrorListener;
import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;

/**
 * FIXME DAO管理器
 * @author keel
 *
 */
public final class DaoManager {

	private DaoManager() {
	}
	
	static final Logger log = Logger.getLogger(ActionManager.class);
	
	//private static final DaoManager me = new DaoManager();
	
	/**
	 * 用于在json中定位
	 * @return 返回"daos"
	 */
	public static final String getName(){
		return "daos";
	}
	
	private static final JSONReader jsonReader = new JSONValidatingReader(new ExceptionErrorListener());
	
	/**
	 * 存储Action的Map,初始化大小为50
	 */
	private static final Map<String, DaoInterface> daoMap = new HashMap<String, DaoInterface>(50);
	
	private static boolean isInitOK = false;
	
	
	public static final boolean isInitOK(){
		return isInitOK;
	}
	
	/**
	 * 初始化DaoManager
	 * @param iniFile 配置文件路径
	 * @param classPath class文件所在的路径
	 * @return 是否初始化成功
	 */
	public static boolean init(String iniFile,String classPath){
		if (!isInitOK) {
			//读取配置文件刷新注入的Dao数据
			try {
				String ini = KIoc.readTxtInUTF8(iniFile);
				Map<String,?> root = (Map<String,?>) jsonReader.read(ini);
				//先定位到json的actions属性
				List<Map<String, ?>> actionList = (List<Map<String, ?>>) root.get(DaoManager.getName());
				//循环加入Dao
				int i = 0;
				for (Iterator<Map<String, ?>> map = actionList.iterator(); map.hasNext();) {
					Map<String, ?> m = map.next();
					//读取必要的属性，如果少则报错并继续下一个
					if (m.containsKey("_name") && m.containsKey("_class") &&  m.containsKey("_dataSource")) {
						String _name = (String) m.get("_name");
						String _class = (String) m.get("_class");
						
						//定位_dataSource
						String _dataSource = (String) m.get("_dataSource");
						DataSourceInterface ds = HTManager.findDataSource(_dataSource);
						//Dao初始化需要dataSource
						Object o = KIoc.loadClassInstance("file:/"+classPath, _class, new Object[]{_name,ds});
						if (o == null) {
							log.error("loadClassInstance error! _class:"+_class+" _name:"+_name+" _dataSource:"+_dataSource);
							continue;
						}
						DaoInterface dao = (DaoInterface)o;
						
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
										KIoc.setProp(dao, prop, iv);
									}else{
										KIoc.setProp(dao, prop, value);
									}
									
								}
								//由#号分为name#manager两部分,后部分为指定的manager名
								else{
									String[] propArr = prop.split("#");
									Object value = HTManager.findFromManager(propArr);
									if (value != null) {
										KIoc.setProp(dao, propArr[0], HTManager.findFromManager(propArr));
									}else{
										log.error("The prop can't find from HTManager, didn't set this prop:"+prop);
									}
								}
								
							}
							
						}
						//加入Dao
						if(!addDao(dao)){
							log.error("Dao name alread exist! failed load this Dao:"+dao.getName()+" id:"+dao.getId());
						}
						
					}else{
						log.error("Dao init Error! miss one or more key props. Position:"+i);
						continue;
					}
					i++;
				}
				
				
				
			} catch (Exception e) {
				log.error("DaoManager init Error!", e);
				isInitOK = false;
				return false;
			}
			isInitOK = true;
		}
		return true;
	}
	
	/**
	 * 重新初始化
	 * @param iniFile 配置文件路径
	 * @param classPath class文件所在的路径
	 * @return 是否初始化成功
	 */
	public static boolean reInit(String iniFile,String classPath){
		isInitOK = false;
		return init(iniFile,classPath);
	}

	/**
	 * 获取一个Dao
	 * @param name Dao的name
	 * @return Dao,未找到返回null
	 */
	public static final DaoInterface findDao(String name){
		return daoMap.get(name);
	}
	
	
	/**
	 * 添加一个Dao,同时确定它 获取方式(单例或是每次均新建)
	 * @param dao Dao对象
	 * @return
	 */
	public static final boolean addDao(DaoInterface dao){
		if (dao == null) {
			return false;
		}
		if (daoMap.containsKey(dao.getName())) {
			return false;
		}
		daoMap.put(dao.getName(), dao);
		return true;
	}
	
	/**
	 * 更改某一个key对应的Dao实例
	 * @param name Dao的name
	 * @param dao 新的Dao
	 */
	public static final void changeDao(String name,DaoInterface dao){
		if (dao == null) {
			return;
		}
		daoMap.put(name, dao);
	}
	
	/**
	 * FIXME 刷新(重载)一个Dao
	 * @param name
	 */
	public static final void reLoadDao(String name){
		
		
	}
	
	public static void main(String[] args) {
		String webRoot = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/";
		String jsonFilePath = webRoot+"kconfig.json";
		String classPath = webRoot+"classes/";
		DaoManager.init(jsonFilePath, classPath);
		DaoInterface a = DaoManager.findDao("mongoUserDao");
		System.out.println(a.getName()+ " id:"+a.getId());
	}
	
	
}
