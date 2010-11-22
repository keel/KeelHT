package com.k99k.khunter;

/**
 * DAO接口
 * @author keel
 *
 */
public interface DaoInterface {
	
	public DataSourceInterface getDataSource();
	
	public void setDataSource(DataSourceInterface dataSource);
	
	public String getName();
	
	public int getId();
	
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

}
