package com.k99k.khunter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO接口
 * @author keel
 *
 */
public interface DaoInterface extends Cloneable{
	
	/**
	 * 获取ID管理器
	 * @return
	 */
	public IDManager getIdm();
	
	/**
	 * 增加或更新一个索引
	 * @param ki
	 * @return
	 */
	public boolean updateIndex(KObjIndex ki);
	
	/**
	 * 删除一个索引
	 * @param ki
	 * @return
	 */
	public boolean removeIndex(KObjIndex ki);
	
	/**
	 * 按ID查找单个对象,顺序号为1
	 * @param id long
	 * @return 未找到返回null
	 */
	public KObject findOne(long id);
	
	/**
	 * 按名称查找KObject对象,顺序号为2
	 * @param name KObject的name
	 * @return
	 */
	public KObject findOne(String name);
	
	/**
	 * 查找单个对象的Map形式,顺序号为3
	 * @param query
	 * @param fields
	 * @return Map<String,Object>
	 */
	public Map<String,Object> findOneMap(HashMap<String,Object> query,HashMap<String,Object> fields);
	
	/**
	 * 查找Map形式对象,顺序号为4
	 * @param id long
	 * @return Map形式,未找到返回null
	 */
	public Map<String, Object> findOneMap(long id);
	
	/**
	 * 通用的查找过程,顺序号为5
	 * @param query 必须有
	 * @param fields 全部则为null
	 * @param sortBy 无则为null
	 * @param skip 无则为0
	 * @param len 无则为0
	 * @param hint 无则为null
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> query(HashMap<String,Object> query,HashMap<String,Object> fields,HashMap<String,Object> sortBy,int skip,int len,HashMap<String,Object> hint);
	
	/**
	 * 按条件查询数量,顺序号为6
	 * @param query 必须有
	 * @param hint 无则为null
	 * @return 数量
	 */
	public int count(HashMap<String,Object> query,HashMap<String,Object> hint);
	
	/**
	 * 按条件查询数量,顺序号为7
	 * @param query 必须有
	 * @return 数量
	 */
	public int count(HashMap<String,Object> query);
	
	/**
	 * 创建新对象,自动生成新ID,顺序号为8
	 * @param kObj KObject
	 * @return 
	 */
	public boolean add(KObject kObj);
	
	
	/**
	 * 创建或更新对象,注意此方法不自动生成ID,顺序号为9
	 * @param kObj KObject
	 * @return
	 */
	public boolean save(KObject kObj);
	
	
	/**
	 * 更新对象,顺序号为10
	 * @param id long
	 * @param newObj KObject
	 * @return
	 */
	public boolean updateOne(long id,KObject newObj);
	
	/**
	 * 更新单个对象,顺序号为11
	 * @param query HashMap<String,Object>
	 * @param set HashMap<String,Object>
	 * @return
	 */
	public boolean updateOne(HashMap<String,Object> query,HashMap<String,Object> set);
	
	/**
	 * 更新对象,顺序号为12
	 * @param query HashMap<String,Object>
	 * @param set HashMap<String,Object>
	 * @param upset 如果不存在是否新建
	 * @param multi 是否更新多个
	 * @return 是否完成更新
	 */
	public boolean update(HashMap<String,Object> query,HashMap<String,Object> set,boolean upset,boolean multi);
	
	/**
	 * 标记删除,即将state置为-1,顺序号为13
	 * @param id
	 * @return
	 */
	public boolean deleteOne(long id);
	
	/**
	 * 标记删除,即将state置为-1,顺序号为14
	 * @param query
	 * @param multi 是否批量
	 * @return
	 */
	public boolean delete(HashMap<String,Object> query,boolean multi);
	
	
	/**
	 * 从数据库中按条件批量彻底删除,顺序号为15
	 * @param id
	 * @return
	 */
	public boolean deleteForever(HashMap<String,Object> query);
	
	
	
	public DataSourceInterface getDataSource();
	
	public void setDataSource(DataSourceInterface dataSource);
	
	public String getName();
	
	public void setName(String name);
	
	public boolean init();
	
	public int getId();
	
	public void setId(int id);
	
	public String getTableName();
	
	public void setTableName(String tableName);
	
	/**
	 * 生成此dao的配置,用于更新配置文件,如：<pre>
"_class":"com.k99k.khunter.dao.MongoUserDao",
"_dataSource":"mongodb_local",
"dbType":"mongodb",
"type":"single",
"tableName":"testKHT",
"id":2
	 * </pre>
	 * @return HashMap<String,Object>
	 */
	public HashMap<String,Object> toJsonConfig();
	
	/**
	 * 数据库类型,如mongodb
	 * @return
	 */
	public String getDbType();

	/**
	 * 设置数据库类型,如mongodb
	 * @param dbType
	 */
	public void setDbType(String dbType);
	
	/**
	 * DAO的获取方式,如单例为single
	 */
	public String getType();

	/**
	 * 设置DAO的获取方式,如单例为single
	 */
	public void setType(String type);
	
	/**
	 * 支持clone
	 * @see java.lang.Object#clone()
	 * @return 
	 */
	public Object clone();

}
