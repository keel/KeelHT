/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KIoc;
import com.k99k.tools.JSONTool;

/**
 * 读取页面固定模块(模板)的配置文件到内存,以帮助输出JSP;<br />
 * 初始化后通过out方法实现;
 * @author keel
 *
 */
public class WBJSPCacheOut extends Action {
	static final Logger log = Logger.getLogger(WBJSPCacheOut.class);
	/**
	 * @param name
	 */
	public WBJSPCacheOut(String name) {
		super(name);
	}
	
	/**
	 * 保存jspCache的map,其值为最终的cache String
	 */
	private final static HashMap<String,String> jspCacheMap = new HashMap<String,String>(30);
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		//重新载入
		//TODO 后期要加上管理员验证
		this.reLoad();
		msg.addData("[print]", "ok");
		return super.act(msg);
	}


	/**
	 * 输出缓存
	 * @param cacheName
	 * @return String
	 */
	public static final String out(String cacheName){
		return jspCacheMap.get(cacheName);
	}


	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {
		jspCacheMap.clear();
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return "jspCache.json";
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			String ini = KIoc.readTxtInUTF8(HTManager.getIniPath()+getIniPath());
			HashMap<String,?> root = (HashMap<String,?>) JSONTool.readJsonString(ini);
			ArrayList<HashMap<String,Object>> jc = (ArrayList<HashMap<String,Object>>) root.get("jspCache");
			for (Iterator<HashMap<String, Object>> it = jc.iterator(); it.hasNext();) {
				HashMap<String, Object> jspc = it.next();
				if (jspc.get("type").equals("str")) {
					String s = KIoc.readTxtInUTF8(HTManager.getIniPath()+jspc.get("txt").toString());
					s = s.replaceAll("#@sPrefix@#", KFilter.getStaticPrefix());
					s = s.replaceAll("#@prefix@#", KFilter.getPrefix());
					jspCacheMap.put(jspc.get("name").toString(), s);
				}else if(jspc.get("type").equals("array")){
					StringBuilder sb = new StringBuilder();
					ArrayList<String> ls = (ArrayList<String>) jspc.get("txt");
					for (Iterator<String> ita = ls.iterator(); ita.hasNext();) {
						String name = ita.next();
						sb.append((jspCacheMap.containsKey(name))?jspCacheMap.get(name):"");
					}
					String s = sb.toString();
					s = s.replaceAll("#@sPrefix@#", KFilter.getStaticPrefix());
					s = s.replaceAll("#@prefix@#", KFilter.getPrefix());
					jspCacheMap.put(jspc.get("name").toString(), s);
				}
			}
			
		} catch (Exception e) {
			log.error("WBJSPCacheOut init Error!", e);
		}
	}

}
