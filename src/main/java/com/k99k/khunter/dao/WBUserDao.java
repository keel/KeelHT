/**
 * 
 */
package com.k99k.khunter.dao;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.DataSourceInterface;
import com.k99k.khunter.KObjConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObject;
import com.k99k.khunter.MongoDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

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
	
	private static DaoInterface wbTagsDao;
	private static DaoInterface wbMentionDao;
	private static DaoInterface wbMsgDao;
	private static DaoInterface wbFansDao;
	private static DaoInterface wbInboxDao;
	private static DaoInterface wbSentDao;
	private static DaoInterface wbUserDao;
	
	private static KObjConfig wbTagConfig;
	private static KObjConfig wbMentionConfig;
	private static KObjConfig wbMsgConfig;
	//private static KObjConfig wbFansConfig;
	private static KObjConfig wbInboxConfig;
	private static KObjConfig wbSentConfig;
	
	static final Logger log = Logger.getLogger(WBUserDao.class);
	
	public static final KObject newMsg(){
		return wbMsgConfig.getKobjSchema().createEmptyKObj();
	}
	
	/**
	 * 注意:这里的初始化操作在WBTalkAct的init方法中实现
	 */
	public static final void initKCs(){
		wbTagConfig = KObjManager.findKObjConfig("wbtags");
		wbTagsDao = wbTagConfig.getDaoConfig().findDao();
		wbMentionConfig = KObjManager.findKObjConfig("wb_mention");
		wbMentionDao = wbMentionConfig.getDaoConfig().findDao();
		wbMsgConfig = KObjManager.findKObjConfig("wbmsg");
		wbMsgDao = wbMsgConfig.getDaoConfig().findDao();
		//wbFansConfig = KObjManager.findKObjConfig("wb_fans");
		wbInboxConfig = KObjManager.findKObjConfig("wb_inbox");
		wbSentConfig = KObjManager.findKObjConfig("wb_sent");
		
		
		wbFansDao = DaoManager.findDao("wbFansDao");
		wbUserDao = DaoManager.findDao("wbUserDao");
		wbSentDao = DaoManager.findDao("wbSentDao");
		wbInboxDao = DaoManager.findDao("wbInboxDao");
		log.info("WBUserDao initKCs done.");
	}
	
	/**
	 * 创建一个topic,并将新的talk的id加入,如果topic已存在则不创建只加入<br />
	 * <strong>注意:此操作不支持并行,适用于单线程队列</strong>
	 * @param topic
	 * @param talkId long,msgId
	 */
	public static final void addTopic(String topic,long talkId){
		//save一个topic
		KObject tp = wbTagsDao.findOne(topic);
		if (tp == null) {
			tp = wbTagConfig.getKobjSchema().createEmptyKObj(wbTagsDao);
			tp.setProp("name", topic);
			tp.setProp("sum", 1);
			tp.setProp("tag_ids", new ArrayList<Long>().add(talkId));
			wbTagsDao.save(tp);
		}else{
			//FIXME 在数量达到峰值时，需要保存到另外的存储表
			BasicDBObject update = new BasicDBObject();
			update.put("$push", new BasicDBObject("tag_ids",talkId));
			update.put("$inc", new BasicDBObject("sum",1));
			wbTagsDao.updateOne(new BasicDBObject("_id",tp.getId()), update);
		}
	}
	
	/**
	 * 添加消息中一个提到的用户
	 * @param mentionUserName 提到的用户名
	 * @param talkId
	 */
	public static final void addMention(String mentionUserName,long talkId){
		
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
	
	/**
	 * 添加一个talk msg
	 * @param talkMsg
	 * @param userId
	 * @param userName
	 * @param screenName
	 * @param source
	 * @param place
	 * @param pic_url
	 * @param urls
	 * @param topics
	 * @param mentions
	 * @return
	 */
	public static final boolean addTalk(KObject newMsg,String talkMsg,long userId,String userName,String screenName,String source,String place,String pic_url,ArrayList<String> urls,ArrayList<String> topics,ArrayList<String> mentions){
		//KObject newO = newMsg;
		newMsg.setProp("text", talkMsg);
		newMsg.setProp("author_id", userId);
		newMsg.setProp("author_name", userName);
		newMsg.setProp("author_screen", screenName);
		newMsg.setProp("source", source);
		newMsg.setProp("place", place);
		if (topics != null) {
			newMsg.setProp("tags", topics);
		}
		if (mentions != null) {
			newMsg.setProp("mentions", mentions);
		}
		if (pic_url != null) {
			newMsg.setProp("pic_url", pic_url);
		}
		if (urls != null) {
			newMsg.setProp("urls", urls);
		}
		return wbMsgDao.save(newMsg);
		//return newMsg.getId();
	}
	
	public static final ArrayList<Long> getAllFans(long userId){
		DBCollection coll = wbFansDao.getColl();
		ArrayList<Long> list = new ArrayList<Long>();
		//List<Map<String, Object>> re = wbFansDao.query(new BasicDBObject("user_id",userId), new BasicDBObject("fans_id",1), new BasicDBObject("hot",-1), 0, 0, null);
		DBCursor cur = coll.find(new BasicDBObject("fans_id",1), new BasicDBObject("user_id",userId)).sort(new BasicDBObject("hot",-1));
		while (cur.hasNext()) {
			list.add((Long)(cur.next().get("fans_id")));
		}
		return list;
	}
	
	public static final void updateLastMsg(long userId,String msg,long msgId){
		wbUserDao.updateOne(new BasicDBObject("_id",userId), new BasicDBObject("$set",new BasicDBObject("lastMsg",msg)));
		KObject newSent = wbSentConfig.getKobjSchema().createEmptyKObj();
		newSent.setProp("user_id", userId);
		newSent.setProp("msg_id", msgId);
		wbSentDao.save(newSent);
	}
	
	/**
	 * 向fans推送新消息,同时也推送给自己
	 * TODO 推送消息最好的实现:只更新fans的wbuser表的inbox[]字段,在相关fans进行消息查看操作时,由异步任务将新消息处理到wb_inbox表中
	 * @param userId
	 * @param msgId
	 */
	public static final void pushMsgToFans(long userId,long msgId){
		
		ArrayList<Long> fansIds = getAllFans(userId);
		//先向自己推送
		KObject in = wbInboxConfig.getKobjSchema().createEmptyKObj();
		in.setProp("user_id", userId);
		in.setProp("msg_id", msgId);
		wbInboxDao.save(in);
		wbUserDao.updateOne(new BasicDBObject("_id",userId), new BasicDBObject("$inc",new BasicDBObject("notify_msg",1)));
		
		//向fans推送
		for (Iterator<Long> it = fansIds.iterator(); it.hasNext();) {
			long fanId = it.next();
			KObject inbox = wbInboxConfig.getKobjSchema().createEmptyKObj();
			in.setProp("user_id", userId);
			in.setProp("msg_id", msgId);
			wbInboxDao.save(inbox);
			wbUserDao.updateOne(new BasicDBObject("_id",fanId), new BasicDBObject("$inc",new BasicDBObject("notify_msg",1)));
		}
		
	}
	
	public static void main(String[] args) {
		
	}

}
