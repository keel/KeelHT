/**
 * 
 */
package com.k99k.khunter;

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
		ob.append("id", 12);
		coll.save(ob);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MongoConfig mc = new MongoConfig();
		mc.test();
	}

}
