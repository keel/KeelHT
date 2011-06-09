/**
 * 
 */
package com.k99k.khunter.dao;

import java.util.ArrayList;

import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.DataSourceInterface;
import com.k99k.khunter.KObjConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.khunter.KObject;
import com.k99k.khunter.MongoDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

/**
 * WB用户Dao,附件相关的操作实现
 * @author keel
 *
 */
public class WBUserDao extends MongoDao {

	/**
	 * @param daoName
	 * @param dataSource
	 */
	public WBUserDao(String daoName, DataSourceInterface dataSource) {
		super(daoName, dataSource);
	}
	
	private DaoInterface wbTagsDao;
	private DaoInterface wbMentionDao;
	private DaoInterface wbMsgDao;
	
	private KObjConfig wbTagConfig;
	private KObjConfig wbMentionConfig;
	private KObjConfig wbMsgConfig;
	
	/**
	 * 创建一个topic,并将新的talk的id加入,如果topic已存在则不创建只加入
	 * @param topic
	 * @param talkId long,msgId
	 */
	public void addTopic(String topic,long talkId){
		//save一个topic
		if (wbTagConfig==null) {
			wbTagConfig = KObjManager.findKObjConfig("wbtags");
			wbTagsDao = wbTagConfig.getDaoConfig().findDao();
		}
		DBCollection coll = wbTagsDao.getColl();
		BasicDBObject q = new BasicDBObject();
		q.append("name", topic);
		
		//FIXME 在数量达到峰值时，需要保存到另外的存储表
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("tag_ids",talkId));
		update.put("$inc", new BasicDBObject("sum",1));
		coll.findAndModify(q, null, null, false, update, false, true);
	}
	
	/**
	 * 添加消息中一个提到的用户
	 * @param mentionUserName 提到的用户名
	 * @param talkId
	 */
	public void addMention(String mentionUserName,long talkId){
		if (wbMentionConfig==null) {
			wbMentionConfig = KObjManager.findKObjConfig("wb_mention");
			wbMentionDao = wbMentionConfig.getDaoConfig().findDao();
		}
		
		KObject newMetion = wbMentionConfig.getKobjSchema().createEmptyKObj(wbMentionDao);
		newMetion.setProp("user_name", mentionUserName);
		newMetion.setProp("mention_id", talkId);
		wbMentionDao.save(newMetion);
		
//		//更新wbMention表
//		DBCollection coll = wbMentionDao.getColl();
//		BasicDBObject q = new BasicDBObject();
//		q.put("user_name", mentionUserName);
//		BasicDBObject o = new BasicDBObject();
//		o.append("$push", new BasicDBObject("mention_ids",talkId));
//		coll.update(q, o, true, false);
//		//更新user表的metion
//		BasicDBObject qu = new BasicDBObject("name", mentionUserName);
//		BasicDBObject ou = new BasicDBObject("$push",new BasicDBObject("mentions",talkId));
//		this.update(qu,ou,false,false);
//		
//		qu.put("notify", 4);
//		ou = new BasicDBObject("$inc", new BasicDBObject("notify.$", 1));
//		this.update(qu,ou,false,false);
	}
	
	public long addTalk(String talkMsg,String userName,String source,String place,String pic_url,String urls){
		if (wbMsgConfig==null) {
			wbMsgConfig = KObjManager.findKObjConfig("wbmsg");
			wbMsgDao = wbMsgConfig.getDaoConfig().findDao();
		}
		KObject newO = wbMsgConfig.getKobjSchema().createEmptyKObj(wbMsgDao);
		newO.setProp("text", talkMsg);
		
		
		return newO.getId();
	}
	
	public static void main(String[] args) {
		
	}

}
