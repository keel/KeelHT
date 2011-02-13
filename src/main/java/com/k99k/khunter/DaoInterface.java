package com.k99k.khunter;

import java.util.HashMap;

/**
 * DAO接口
 * @author keel
 *
 */
public interface DaoInterface extends Cloneable{
	
	public DataSourceInterface getDataSource();
	
	public void setDataSource(DataSourceInterface dataSource);
	
	public String getName();
	
	public boolean init();
	
	public int getId();
	
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
