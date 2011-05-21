/**
 * 
 */
package com.k99k.wb.acts;

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
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * 发表微博
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
		StringBuilder sb = new StringBuilder();
		sb.append("在劫难逃  ss .江泽民在村要胡锦涛要\r\n大地fss d 昨时");
		t.keywords = new String[]{"江泽民","胡锦涛","fss"};
		sb = t.dealKeyWords(sb);
		//System.out.println(sb);
		sb = new StringBuilder("asdfs sf #一个# ttps://sss.www.com/sfsf/sdf.js sf#两个#://www.dd.com djjf");
		String s = t.dealTopic(sb).toString();
		//System.out.println(s);
		sb = new StringBuilder("asdfs sf @一个 # ttps://sss.www.com/sfsf/sdf.js sf@两个 #://www.dd.com djjf");
		s = t.dealMetion(sb).toString();
		System.out.println(s);*/
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
		//发表WB
		
		//关键词过滤
		
		//话题词
		
		//@用户,提到的用户
		
		
		return super.act(msg);
	}

	/**
	 * 处理关键词,从关键词库中将相关词替换掉
	 * @return
	 */
	public static final StringBuilder dealKeyWords(StringBuilder sb){
		for (int i = 0; i < keywords.length; i++) {
			String s = keywords[i];
			for (int j = sb.indexOf(s); j > 0; j = sb.indexOf(s)) {
				sb.replace(j, j+s.length(), "***");
			}
		}
		return sb;
	}
	
	/**
	 * 处理话题,识别#符号,使用正则实现
	 * @return
	 */
	public static final StringBuffer dealTopic(StringBuilder sb){
		//Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:]+");
		Pattern pattern = Pattern.compile("#(([^#]+))#");
		Matcher matcher = pattern.matcher(sb);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){   
//			for (int i = 0; i < matcher.groupCount(); i++) {
//				System.out.println("["+i+"]"+matcher.group(i));
//			}
			//TODO 处理#号的链接
			matcher.appendReplacement(buffer, "<topic>"+matcher.group(1)+"</topic>");           
		}
		matcher.appendTail(buffer);
		return buffer;
	}
	
	/**
	 * 处理提到的用户
	 * @return
	 */
	public static final StringBuffer dealMetion(StringBuilder sb){
		Pattern pattern = Pattern.compile("@((\\S+))");
		Matcher matcher = pattern.matcher(sb);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){   
//			for (int i = 0; i < matcher.groupCount(); i++) {
//				System.out.println("["+i+"]"+matcher.group(i));
//			}
			//TODO 处理@号的链接
			matcher.appendReplacement(buffer, "<metion>"+matcher.group(1)+"</metion>");           
		}
		matcher.appendTail(buffer);
		return buffer;
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
			Map<String, ?> m = (Map<String, ?>) root.get("wbSettings");
			String keywordsString = m.get("keywords").toString();
			if(!initKeywords(keywordsString)){
				log.error("WBTalk initKeywords Error!");
			}
			
			
		} catch (Exception e) {
			log.error("WBTalk init Error!", e);
		}
	}

}
