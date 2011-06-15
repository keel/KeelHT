/**
 * 
 */
package com.k99k.khunter.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.DataSourceInterface;
import com.k99k.khunter.KObjColumn;
import com.k99k.khunter.KObjConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.khunter.KObject;
import com.k99k.khunter.MongoDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * WB用户Dao,附加其他相关的操作实现
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
	private static DaoInterface wbCommDao;
	
	private static KObjConfig wbTagConfig;
	private static KObjConfig wbMentionConfig;
	private static KObjConfig wbMsgConfig;
	private static KObjConfig wbCommConfig;
	//private static KObjConfig wbFansConfig;
	private static KObjConfig wbInboxConfig;
	private static KObjConfig wbSentConfig;
	
	private static BasicDBObject noSubProp;
	
	static final Logger log = Logger.getLogger(WBUserDao.class);
	
	public static final KObject newMsg(){
		return wbMsgConfig.getKobjSchema().createEmptyKObj();
	}
	
	public static final KObject newComm(){
		return wbCommConfig.getKobjSchema().createEmptyKObj();
	}
	
	/**
	 * 注意:这里的初始化操作在WBTalkAct的init方法中实现
	 */
	public static final void initKCs(){
		wbTagConfig = KObjManager.findKObjConfig("wbtags");
		wbMentionConfig = KObjManager.findKObjConfig("wb_mention");
		wbMsgConfig = KObjManager.findKObjConfig("wbmsg");
		wbCommConfig = KObjManager.findKObjConfig("wbcomm");
		//wbFansConfig = KObjManager.findKObjConfig("wb_fans");
		wbInboxConfig = KObjManager.findKObjConfig("wb_inbox");
		wbSentConfig = KObjManager.findKObjConfig("wb_sent");
		
		wbTagsDao = DaoManager.findDao("wbTagsDao");
		wbMentionDao = DaoManager.findDao("wbMentionDao");
		wbMsgDao = DaoManager.findDao("wbMsgDao");
		wbCommDao = DaoManager.findDao("wbCommDao");
		wbFansDao = DaoManager.findDao("wbFansDao");
		wbUserDao = DaoManager.findDao("wbUserDao");
		wbSentDao = DaoManager.findDao("wbSentDao");
		wbInboxDao = DaoManager.findDao("wbInboxDao");
		Object[] kcs = new Object[]{
				wbTagConfig,wbMentionConfig,wbMsgConfig,wbInboxConfig,wbSentConfig,
				wbTagsDao,wbMentionDao,wbMsgDao,wbFansDao,wbUserDao,wbSentDao,wbInboxDao
		};
		for (int i = 0; i < kcs.length; i++) {
			if (kcs[i] == null) {
				log.error("[WBUserDao initKCs failed]:"+i);
			}
		}
		//初始化不含子对象的属性，以便实现findOne重写
		noSubProp = new BasicDBObject();
		KObjSchema sch = KObjManager.findSchema("wbuser");
		for (Iterator<KObjColumn> iterator = sch.getColListWithNoSub().iterator(); iterator.hasNext();) {
			KObjColumn kc = iterator.next();
			noSubProp.append(kc.getCol(), 1);
		}
		if (noSubProp.size() <= 0) {
			log.error("[WBUserDao initKCs noSubProp error]");
		}
		log.info("WBUserDao initKCs done. noSubProp size:"+noSubProp.size());
	}
	
	
	
	/**
	 * 取出一个用户,注意这里不包含内嵌文档数据
	 * @see com.k99k.khunter.MongoDao#findOne(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public KObject findOne(long id) {
		
		try {
			DBCollection coll = this.getColl();
			DBObject o = coll.findOne(id,noSubProp);
			if (o != null) {
				return new KObject((Map<String, Object>)o);
			}
			return null;
		} catch (Exception e) {
			log.error("findOne by Id error!", e);
			return null;
		}
	}

	/**
	 * 取出一个用户,注意这里不包含内嵌文档数据
	 * @see com.k99k.khunter.MongoDao#findOne(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public KObject findOne(String name) {
		try {
			DBCollection coll = this.getColl();
			DBObject o = coll.findOne(new BasicDBObject("name", name),noSubProp);
			if (o != null) {
				return new KObject((Map<String, Object>)o);
			}
			return null;
		} catch (Exception e) {
			log.error("findOne by name error!", e);
			return null;
		}
	}
	
	/**
	 * 保存User的属性,不包含有子属性的属性
	 * @param userId
	 * @param props
	 * @return
	 */
	public boolean saveProp(long userId,Map<String,Object> props){
		try {
			DBCollection coll = this.getColl();
			coll.update(new BasicDBObject("_id",userId),new BasicDBObject("$set",props),false,false);
		} catch (Exception e) {
			log.error("save error!", e); 
			return false;
		}
		return true;
	}
	
	//以下属性是用于查询的静态条件,节省开销
	private static final BasicDBObject prop_msg_id = new BasicDBObject("msg_id",1);
	private static final BasicDBObject prop_fans_id = new BasicDBObject("fans_id",1);
	private static final BasicDBObject prop_sum = new BasicDBObject("sum",1);
	private static final BasicDBObject prop_notify_msg = new BasicDBObject("notify_msg",1).append("inbox_count", 1);
	private static final BasicDBObject prop_id_desc = new BasicDBObject("_id",-1);
	private static final BasicDBObject prop_follows_id = new BasicDBObject("follows.id",1);//.append("follows.both", 1);
	private static final BasicDBObject prop_notify_msg_reset = new BasicDBObject("$set",new BasicDBObject("notify_msg",0));
	private static final BasicDBObject prop_followers_count_inc = new BasicDBObject("followers_count",1);
	private static final BasicDBObject prop_fans_count_inc = new BasicDBObject("followers_count",1).append("notify_fan", 1);
	private static final BasicDBObject prop_followers_count_dec = new BasicDBObject("followers_count",-1);
	private static final BasicDBObject prop_fans_count_dec = new BasicDBObject("friends_count",-1);
	private static final BasicDBObject prop_statuses_count = new BasicDBObject("statuses_count",1);
	
	
	/**
	 * 添加评论的同步操作
	 * @param newComm 带ID的新评论
	 * @param msgId 原消息ID
	 * @param re_userId 原消息用户ID
	 * @param userId 发表者ID
	 * @param msg 评论内容
	 * @param source 来源
	 * @param place 发布地点
	 * @param urls
	 * @param topics
	 * @param mentions
	 * @return
	 */
	public static final boolean addComm(KObject newComm,long msgId,long re_userId,long userId,String msg,String source,String place,ArrayList<String> urls,ArrayList<String> topics,ArrayList<String> mentions){
		//添加一个comm
		newComm.setProp("text", msg);
		newComm.setProp("source", source);
		newComm.setProp("place", place);
		newComm.setProp("user_id", userId);
		newComm.setProp("re_msg_id", msgId);
		newComm.setProp("re_user_id", re_userId);
		
		if (topics != null) {
			newComm.setProp("tags", topics);
		}
		if (mentions != null) {
			newComm.setProp("mentions", mentions);
		}
		if (urls != null) {
			newComm.setProp("urls", urls);
		}
		return wbCommDao.save(newComm);
	}
	
	/**
	 * 转发
	 * @param rtMsg
	 * @param msgId
	 * @param userId
	 * @param msg
	 * @param source
	 * @param place
	 * @return
	 */
	public static final boolean rt(KObject rtMsg,long msgId,long userId,String msg,String source,String place,ArrayList<String> urls,ArrayList<String> topics,ArrayList<String> mentions){
		//新增一个转发msg，包括原msg的id
		
		//原消息增加一个评论数量
		
		//其他同comm
		
		return true;
	}
	
	public static final ArrayList<KObject> readComms(long msgId,int page,int pageSize){
		
		
		return null;
	}
	
	public static final boolean addFavor(long msgId,long userId){
		
		
		return true;
	}
	
	public static final ArrayList<KObject> getFans(long userId,int page,int pageSize){
		
		
		return null;
	}
	
	public static final ArrayList<KObject> getFollows(long userId,int page,int pageSize){
		
		
		return null;
	}
	
	public static final ArrayList<KObject> getOneTopicList(long userId,int page,int pageSize){
		
		
		return null;
	}
	
	public static final ArrayList<KObject> getFavList(long userId,int page,int pageSize){
		
		
		return null;
	}
	
	/**
	 * TODO 话题排行
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static final ArrayList<KObject> getTopicTopList(long userId,int page,int pageSize){
		
		
		return null;
	}
	
	
	/**
	 * follow操作,原用户$addToSet一个follows,friends_count++,目标用户$addToSet一个fans,followers_count++;<br />
	 * 注：follows和fans存储String：对方有关注自己则follows为"targetId,1",否则为"targetId,0"
	 * @param userId 用户
	 * @param targetId 需要follow的对象
	 * @return 是否成功
	 */
	public static final boolean follow(long userId,long targetId){
		KObject user = wbUserDao.findOne(userId);
		KObject target = wbUserDao.findOne(targetId);
		if (user == null || target== null) {
			return false;
		}
		try {
			List<Map<String,Object>> myFollow = wbUserDao.query(new BasicDBObject("_id", userId).append("follows.id",targetId),prop_follows_id, null, 0, 0, null);
			if (myFollow != null && myFollow.size()>0) {
				return true;
			}
			
			int isTargetFans = 0;
			List<Map<String,Object>> targetFollow = wbUserDao.query(new BasicDBObject("_id",targetId).append("follows.id", userId),prop_follows_id, null, 0, 0, null);
			if (targetFollow !=null && targetFollow.size() > 0) {
				isTargetFans = 1;
			}
			
			//更新原用户
			BasicDBObject update =  new BasicDBObject("$addToSet",new BasicDBObject("follows",new BasicDBObject("id",targetId).append("both",isTargetFans).append("name",target.getName()).append("screen", target.getProp("screen_name"))));
			update.put("$inc", prop_followers_count_inc);
			wbUserDao.updateOne(new BasicDBObject("_id",userId),update);
			//更新目标用户
			BasicDBObject update2 =  new BasicDBObject("$addToSet",new BasicDBObject("fans",new BasicDBObject("id",userId).append("both",isTargetFans).append("name",user.getName()).append("screen", user.getProp("screen_name"))));
			update2.put("$inc", prop_fans_count_inc);
			if (isTargetFans == 1) {
				update2.put("$set", new BasicDBObject("follows.$.both",1));
				wbUserDao.updateOne(new BasicDBObject("_id",targetId).append("follows.id", userId),update2);
			}else{
				wbUserDao.updateOne(new BasicDBObject("_id",targetId),update2);
			}
		} catch (Exception e) {
			log.error("follow error!", e);
			return false;
		}
		return true;
	}
	
	/**
	 * unFollow操作,原用户$pull一个follows,friends_count--,目标用户$pull一个fans,followers_count--;<br />
	 * 注：follows和fans存储String：对方有关注自己则follows为"targetId,1",否则为"targetId,0"
	 * @param userId 用户
	 * @param targetId 需要follow的对象
	 * @return 是否成功
	 */
	public static final boolean unFollow(long userId,long targetId){
		KObject user = wbUserDao.findOne(userId);
		KObject target = wbUserDao.findOne(targetId);
		if (user == null || target== null) {
			return false;
		}
		try {
			List<Map<String,Object>> myFollow = wbUserDao.query(new BasicDBObject("_id", userId).append("follows.id",targetId),prop_follows_id, null, 0, 0, null);
			if (myFollow == null || myFollow.size()<=0) {
				return true;
			}
			
			int isTargetFans = 0;
			List<Map<String,Object>> targetFollow = wbUserDao.query(new BasicDBObject("_id",targetId).append("follows.id", userId),prop_follows_id, null, 0, 0, null);
			if (targetFollow !=null && targetFollow.size() > 0) {
				isTargetFans = 1;
			}
			
			//更新原用户
			BasicDBObject update =  new BasicDBObject("$pull",new BasicDBObject("follows.id",targetId));
			update.put("$inc", prop_followers_count_dec);
			wbUserDao.updateOne(new BasicDBObject("_id",userId),update);
			//更新目标用户
			BasicDBObject update2 =  new BasicDBObject("$pull",new BasicDBObject("fans.id",userId));
			update2.put("$inc", prop_fans_count_dec);
			if (isTargetFans == 1) {
				update2.put("$set", new BasicDBObject("follows.$.both",0));
				wbUserDao.updateOne(new BasicDBObject("_id",targetId).append("follows.id", userId),update2);
			}else{
				wbUserDao.updateOne(new BasicDBObject("_id",targetId),update2);
			}
			
		} catch (Exception e) {
			log.error("follow error!", e);
			return false;
		}
		return true;
	}
	
	public static final ArrayList<KObject> readSentMsgs(long userId,int page,int pageSize){
		KObject user = wbUserDao.findOne(userId);
		int msgCount = Integer.parseInt(user.getProp("sent_count").toString());
		if (msgCount<=0 || pageSize<=0) {
			return null;
		}
		int pageCount = msgCount/pageSize;
		int mod = msgCount%pageSize;
		if (mod != 0) {
			pageCount++;
		}
		if (page<=0) {
			page = 1;
		}
		if (page > pageCount) {
			page = pageCount;
		}
		int skip = pageSize*(page-1);
		return getSentMsgList(userId,skip,pageSize);
		
	}
	
	/**
	 * 读取最新的几条msg
	 * @param userId
	 * @param max 一次载入的最大数量
	 * @return 若无则返回null,有则返回指定数量的ArrayList<KObject> 
	 */
	public static final ArrayList<KObject> readNewMsgs(long userId,int max){
		KObject user = wbUserDao.findOne(userId);
		int newMsg = Integer.parseInt(user.getProp("notify_msg").toString());
		if (newMsg<=0) {
			return null;
		}
		int num = (newMsg>max) ? max: newMsg;
		return getInboxMsgList(userId,0,num);
	}
	
	/**
	 * 按页读取某用户的inbox msg
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return 若无则返回null,有则返回指定数量的ArrayList<KObject>
	 */
	public static final ArrayList<KObject> readOnePageMsgs(long userId,int page,int pageSize){
		KObject user = wbUserDao.findOne(userId);
		int msgCount = Integer.parseInt(user.getProp("inbox_count").toString());
		if (msgCount<=0 || pageSize<=0) {
			return null;
		}
		int pageCount = msgCount/pageSize;
		int mod = msgCount%pageSize;
		if (mod != 0) {
			pageCount++;
		}
		if (page<=0) {
			page = 1;
		}
		if (page > pageCount) {
			page = pageCount;
		}
		int skip = pageSize*(page-1);
		return getInboxMsgList(userId,skip,pageSize);
	}
	
	/**
	 * 获取用户的msg列表
	 * @param userId
	 * @param skip
	 * @param max
	 * @return ArrayList<KObject> 
	 */
	private static final ArrayList<KObject> getInboxMsgList(long userId,int skip,int max){
		ArrayList<Long> msgIdList = new ArrayList<Long>(30);
		List<Map<String,Object>> msgIds = wbInboxDao.query(new BasicDBObject("user_id",userId), prop_msg_id,prop_id_desc, skip, max, null);
		for (Iterator<Map<String, Object>> it = msgIds.iterator(); it.hasNext();) {
			Map<String, Object> m = it.next();
			msgIdList.add((Long)m.get("msg_id"));
		}
		
		ArrayList<KObject> list = getMsgList(msgIdList,30);	
		wbUserDao.updateOne(new BasicDBObject("_id",userId), prop_notify_msg_reset);
		return list;
	}
	
	/**
	 * 从msgId列表中取出MSG对象列表
	 * @param msgIdList ArrayList<Long>
	 * @param initSize 初始化list的大小,一般为每页的size大小
	 * @return ArrayList<KObject>
	 */
	@SuppressWarnings("unchecked")
	private static final ArrayList<KObject> getMsgList(ArrayList<Long> msgIdList,int initSize){
		ArrayList<KObject> list = new ArrayList<KObject>(initSize);		
		DBCollection coll = wbMsgDao.getColl();
		DBCursor cur = coll.find(new BasicDBObject("_id",new BasicDBObject("$in",msgIdList)));
		while (cur.hasNext()) {
			Map<String,Object> m = (Map<String,Object>) cur.next();
			list.add(new KObject(m));
		}
		return list;
	}
	
	/**
	 * 获取用户的sent msg列表
	 * @param userId
	 * @param skip
	 * @param max
	 * @return ArrayList<KObject> 
	 */
	@SuppressWarnings("unchecked")
	private static final ArrayList<KObject> getSentMsgList(long userId,int skip,int max){
		ArrayList<Long> msgIdList = new ArrayList<Long>(30);
		List<Map<String,Object>> msgIds = wbSentDao.query(new BasicDBObject("user_id",userId), prop_msg_id,prop_id_desc, skip, max, null);
		for (Iterator<Map<String, Object>> it = msgIds.iterator(); it.hasNext();) {
			Map<String, Object> m = it.next();
			msgIdList.add((Long)m.get("msg_id"));
		}
		
		ArrayList<KObject> list = new ArrayList<KObject>(30);		
		DBCollection coll = wbMsgDao.getColl();
		DBCursor cur = coll.find(new BasicDBObject("_id",new BasicDBObject("$in",msgIdList)));
		while (cur.hasNext()) {
			Map<String,Object> m = (Map<String,Object>) cur.next();
			list.add(new KObject(m));
		}
		return list;
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
			update.put("$inc", prop_sum);
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
	 * TODO 最好还是放到用户自己的数据中去，当消息获取超过用户data长度时，从公共消息列表中获取
	 * @param newMsg 已带有ID的空消息
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
		DBCursor cur = coll.find(prop_fans_id, new BasicDBObject("user_id",userId)).sort(new BasicDBObject("hot",-1));
		while (cur.hasNext()) {
			list.add((Long)(cur.next().get("fans_id")));
		}
		return list;
	}
	
	public static final void updateLastMsg(long userId,String msg,long msgId){
		wbUserDao.updateOne(new BasicDBObject("_id",userId), new BasicDBObject("$set",new BasicDBObject("lastMsg",msg)).append("$inc", prop_statuses_count));
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
		wbUserDao.updateOne(new BasicDBObject("_id",userId), new BasicDBObject("$inc",prop_notify_msg));
		
		//向fans推送
		for (Iterator<Long> it = fansIds.iterator(); it.hasNext();) {
			long fanId = it.next();
			KObject inbox = wbInboxConfig.getKobjSchema().createEmptyKObj();
			in.setProp("user_id", userId);
			in.setProp("msg_id", msgId);
			wbInboxDao.save(inbox);
			wbUserDao.updateOne(new BasicDBObject("_id",fanId), new BasicDBObject("$inc",prop_notify_msg));
		}
		
	}
	
	public static void main(String[] args) {
		
	}

}
