/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
import com.k99k.khunter.KObject;
import com.k99k.khunter.TaskManager;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * 发表微博的同步操作,异步任务由WBTalkAct实现.<br />
 * 同步任务主要包括：<br />
 * 1.处理关键字屏蔽;<br />
 * 2.处理url;<br />
 * 3.发表/转发msg;<br />
 * 4.生成任务;<br />
 * @author keel
 *
 */
public class WBTalk extends Action {

	/**
	 * @param name
	 */
	public WBTalk(String name) {
		super(name);
	}
	
	static final Logger log = Logger.getLogger(WBTalk.class);
	
	/**
	 * 关键词数组
	 */
	private static String[] keywords;
	
	private static String[] topicReStr;
	
	private static String[] urlReStr;
	
	private static String[] mentionReStr;
	
	//private WBUserDao userDao;
	
	
	/**
	 * 初始化关键词,处理按,号分隔的关键词集合
	 * @param keywordsStr
	 * @return
	 */
	private static boolean initKeywords(String keywordsStr){
		if (StringUtil.isStringWithLen(keywordsStr, 1)) {
			keywords = keywordsStr.split(",");
		}
		return true;
	}
	
	public static String[]	getKeywords(){
		return keywords;
	}
	
	public static void main(String[] args) {
		
	/*
		WBTalk t = new WBTalk("sss");
		t.topicReStr = "<a href='###'>###</a>".split("###");
		t.mentionReStr = "<a href='###'>###</a>".split("###");
		t.urlReStr = "<a href='###' target='_blank'>###</a>".split("###");
		
		StringBuffer sb = new StringBuffer();
		sb.append("在劫难逃  ss .江泽民在村要胡锦涛要\r\n大地fss d 昨时");
		t.keywords = new String[]{"江泽民","胡锦涛","fss"};
		sb = t.dealKeyWords(sb);
		System.out.println("[TEST dealKeyWords] "+sb);
		sb = new StringBuffer("asdfs sf #一个# ttps://sss.www.com/sfsf/sdf.js sf#两个#://www.dd.com djjf");
		ArrayList<String> ts = t.dealTopic(sb);
		for (Iterator it = ts.iterator(); it.hasNext();) {
			String str = (String) it.next();
			System.out.println("topics:"+str);
		}
		System.out.println("[TEST dealTopic] "+sb);
		sb = new StringBuffer("asdfs sf @一个 # ttps://sss.www.com/sfsf/sdf.js sf@两个 #://www.dd.com djjf");
		ArrayList<String> ms = t.dealMention(sb);
		for (Iterator it = ms.iterator(); it.hasNext();) {
			String s = (String) it.next();
			System.out.println("mention:"+s);
		}
		System.out.println("[TEST dealMention] "+sb);
		*/
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		//验证用户请求是否合法
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user == null) {
			msg.addData("[redirect]", "/");
			return super.act(msg);
		}
		
		String talk = httpmsg.getHttpReq().getParameter("talk");
		String pic_url = httpmsg.getHttpReq().getParameter("pic_url");
		
		StringBuffer sb = new StringBuffer(talk);
		KObject newMsg = WBUserDao.newMsg();
		long msgId = newMsg.getId();
		//关键词过滤
		sb = dealKeyWords(sb);
		//url处理
		ArrayList<String> us = dealUrl(sb);
		//topic
		ArrayList<String> ts = dealTopic(sb,msgId);
		//mention
		ArrayList<String> ms = dealMention(sb,msgId);
		//TODO pic upload url
		//String picUrl = ;
		String txt = sb.toString();
		// 发表/转发
		WBUserDao.addTalk(newMsg,txt, user.getId(),user.getName(), (String)user.getProp("screen"),"web", "unknown place",pic_url, us,ts,ms);
		//生成异步任务
		ActionMsg task = new ActionMsg("talkact");
		task.addData(TaskManager.TASK_TYPE, TaskManager.TASK_TYPE_EXE_POOL);
		task.addData("userId", user.getId());
		task.addData("txt", txt);
		task.addData("msgId", msgId);
		TaskManager.makeNewTask("WBTalkAct:"+msgId, task);
		
		return super.act(msg);
	}

	/**
	 * 处理关键词,从关键词库中将相关词替换掉
	 * @return
	 */
	public static final StringBuffer dealKeyWords(StringBuffer sb){
		for (int i = 0; i < keywords.length; i++) {
			String s = keywords[i];
			for (int j = sb.indexOf(s); j > 0; j = sb.indexOf(s)) {
				sb.replace(j, j+s.length(), "***");
			}
		}
		return sb;
	}
	
	/**
	 * topic,处理话题,识别#符号,使用正则实现
	 * @return
	 */
	public static final ArrayList<String> dealTopic(StringBuffer sb,long msgId){
		//Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+");
		Pattern pattern = Pattern.compile("#(([^#]+))#");
		Matcher matcher = pattern.matcher(sb);
		StringBuffer buffer = new StringBuffer();
		ArrayList<String> topics = null;
		while(matcher.find()){   
			//TODO 处理#号的链接
			String topic = matcher.group(1);
			if (topics == null) {
				topics = new ArrayList<String>();
			}
			topics.add(topic);
			matcher.appendReplacement(buffer, JOut.templetOut(topicReStr,new String[]{"?topic="+topic,topic}));           
		}
		matcher.appendTail(buffer);
		if (topics != null) {
			ActionMsg task = new ActionMsg("topic");
			//注意topic任务必须是单个队列的
			task.addData(TaskManager.TASK_TYPE, TaskManager.TASK_TYPE_EXE_SINGLE);
			task.addData("topics", topics);
			task.addData("msgId", msgId);
			TaskManager.makeNewTask("topic", task);
		}
		sb.delete(0, sb.length()).append(buffer);
		return topics;
	}
	
	public static final ArrayList<String> dealUrl(StringBuffer sb){
		Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+");
		Matcher matcher = pattern.matcher(sb);
		StringBuffer buffer = new StringBuffer();
		ArrayList<String> urls = null;
		while(matcher.find()){   
			//TODO 处理url的链接
			String url = matcher.group(1); 
			if (urls == null) {
				urls = new ArrayList<String>();
			}
			urls.add(url);
			matcher.appendReplacement(buffer, JOut.templetOut(urlReStr,new String[]{url,url}));           
		}
		matcher.appendTail(buffer);
		sb.delete(0, sb.length()).append(buffer);
		return urls;
	}
	
	/**
	 * 处理提到的用户
	 * @return
	 */
	public static final ArrayList<String> dealMention(StringBuffer sb,long msgId){
		Pattern pattern = Pattern.compile("@((\\S+))");
		Matcher matcher = pattern.matcher(sb);
		StringBuffer buffer = new StringBuffer();
		ArrayList<String> mentions = null;
		while(matcher.find()){   
			//TODO 处理@号的链接
			String re = matcher.group(1);
			if (mentions == null) {
				mentions = new ArrayList<String>();
			}
			mentions.add(re);
			matcher.appendReplacement(buffer, JOut.templetOut(mentionReStr,new String[]{"?mention="+re,re}));           
		}
		matcher.appendTail(buffer);
		if (mentions != null) {
			ActionMsg task = new ActionMsg("mention");
			task.addData(TaskManager.TASK_TYPE, TaskManager.TASK_TYPE_EXE_POOL);
			task.addData("mentions", mentions);
			task.addData("msgId", msgId);
			TaskManager.makeNewTask("mention", task);
		}
		
		sb.delete(0, sb.length()).append(buffer);
		return mentions;
	}
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {

	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return "wb.json";
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			String ini = KIoc.readTxtInUTF8(HTManager.getIniPath()+getIniPath());
			Map<String,?> root = (Map<String,?>) JSONTool.readJsonString(ini);
			//先定位到json的cookies属性
			Map<String, ?> m = (Map<String, ?>) root.get("wbTalk");
			String keywordsString = m.get("keywords").toString();
			if(!initKeywords(keywordsString)){
				log.error("WBTalk initKeywords Error!");
			}
			topicReStr = m.get("topicReplace").toString().split("###");
			mentionReStr =  m.get("mentionReplace").toString().split("###");
			urlReStr = m.get("urlReplace").toString().split("###");
			//userDao = (WBUserDao)DaoManager.findDao("wbUserDao");
		} catch (Exception e) {
			log.error("WBTalk init Error!", e);
		}
	}

}
