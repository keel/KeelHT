/**
 * 
 */
package com.k99k.khunter;

/**
 * Mongodb下的UserDao
 * @author keel
 *
 */
public class MongoUserDao extends MongoDao implements HTUserDaoInterface{

	/**
	 * @param daoName 数据表名
	 */
	public MongoUserDao(String daoName, DataSourceInterface dataSource) {
		super(daoName,dataSource);
	}
	
	//注册
	
	//查找用户
	
	//按条件查找批量用户
	
	//更新用户属性
	
	//批量更新用户属性
	
	//删除用户
	
	//统计用户数

}
