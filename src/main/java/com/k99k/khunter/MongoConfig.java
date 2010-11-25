/**
 * 
 */
package com.k99k.khunter;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * TODO Mongodb配置及设置
 * @author keel
 *
 */
public class MongoConfig {

	/**
	 * 
	 */
	public MongoConfig() {
	}
	
	public void test(){
		MongoConn conn = new MongoConn();
		conn.init();
		DB db = conn.getDB();
		DBCollection coll = db.getCollection("testKHT");
		BasicDBObject ob = new BasicDBObject();
		ob.append("_id", 1);
		ob.append("id", 12);
		coll.save(ob);
		
	}
	
	public void testMongoDao(){
		String ini = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/kconfig.json";
		HTManager.init(ini);
		MongoDao mdao = (MongoDao) DaoManager.findDao("mongoUserDao");
		HTUser user = new HTUser();
		user.setImei("test IMEI");
		user.setName("keel");
		user.setEmail("keel@keel.com");
		user.setCreateTime(new Date()+"");
		user.setCreatorName("admin");
		user.setHp(100);
		user.setGold(20);
		mdao.add(user);
	}

	public void testFind(){
		String ini = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/kconfig.json";
		HTManager.init(ini);
		MongoUserDao mdao = (MongoUserDao) DaoManager.findDao("mongoUserDao");
		HTUser user =  mdao.findUser(3);
		System.out.println(user);
		System.out.println(user.getEmail());
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MongoConfig mc = new MongoConfig();
		mc.testFind();
	}

}
