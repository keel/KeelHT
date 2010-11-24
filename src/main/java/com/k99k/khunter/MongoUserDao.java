/**
 * 
 */
package com.k99k.khunter;

import java.util.Map;

import com.mongodb.DBCollection;

/**
 * FIXME Mongodb下的UserDao
 * @author keel
 *
 */
public class MongoUserDao extends MongoDao implements HTUserDaoInterface{

	/**
	 * @param daoName 数据表名
	 * @param dataSource DataSourceInterface
	 */
	public MongoUserDao(String daoName, DataSourceInterface dataSource) {
		super(daoName,dataSource);
	}
	
	//注册
	
	/**
	 * 根据id查找用户
	 * @param id
	 * @return
	 */
	public HTUser findUser(long id,DBCollection coll){
		Map<String, Object> m = this.findMap(id,coll);
		if (m != null) {
			return new HTUser(m);
		}
		return null;
	}
	
	//按条件查找批量用户
	
	//更新用户属性
	
	//批量更新用户属性
	
	//删除用户
	
	//统计用户数

}
