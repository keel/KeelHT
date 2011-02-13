/**
 * 
 */
package com.k99k.khunter.acts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionManager;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.ErrorCode;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
import com.k99k.tools.IO;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class KObjAction extends Action{

	public KObjAction(String name) {
		super(name);
	}
	
	static final Logger log = Logger.getLogger(ActionManager.class);
	
	public static final int ERR_CODE1 = 15;
	/**
	 * 存储Action的Map,初始化大小为50
	 */
	private static final HashMap<String, Object> kobjMap = new HashMap<String, Object>(50);

	
//	/**
//	 * 获取所有KObj对象的列表
//	 * @return ArrayList<ArrayList<?>>
//	 */
//	public ArrayList<ArrayList<?>> getKObjList(){
//		ArrayList<ArrayList<?>> list = new ArrayList<ArrayList<?>>();
//		for (Iterator<String> it = kobjMap.keySet().iterator(); it.hasNext();) {
//			String kobj =  it.next();
//			list.add((ArrayList<?>) kobjMap.get(kobj));
//		}
//		return list;
//	}
	
	public Map<String, Object> getKObjMap(){
		return kobjMap;
	}
	
	/**
	 * 查找所有包含key字符串的的KObj的名称List,不区分大小写
	 * @param key 查找key
	 * @return ArrayList<String>
	 */
	public HashMap<String, Object> searchKObj(String key){
		key = StringUtil.objToStrNotNull(key).trim();
		if (key.equals("")) {
			return kobjMap;
		}
		HashMap<String, Object> reMap = new HashMap<String, Object>(50);
		for (Iterator<String> it = kobjMap.keySet().iterator(); it.hasNext();) {
			String kobj =  it.next();
			if (kobj.toLowerCase().indexOf(key.toLowerCase()) > -1) {
				reMap.put(kobj,kobjMap.get(kobj));
			}
		}
		return reMap;
	}
	
	/**
	 * 查看某一具体的KObj结构
	 * @param key KObj的key
	 * @return Map<String,?>
	 */
	@SuppressWarnings("unchecked")
	public Map<String,?>  findKObj(String key){
		Object o = kobjMap.get(key);
		if (o == null) {
			return null;
		}
		Map<String,?> m = (Map<String,?>)o;
		return m;
	}
	
	
	/**
	 * 保存配置,同时将原文件按时间扩展名备份
	 * @return
	 */
	private boolean save(){
		//保存
		String configFile = HTManager.getIniPath()+getIniPath();
		String bak = configFile+"."+StringUtil.getFormatDateString("yyyyMMdd-HH_mm_ss");
		try {
			IO.copy(new File(configFile), new File(bak));
		} catch (IOException e) {
			ErrorCode.logError(log, KObjAction.ERR_CODE1, 2, e,bak);
		}
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("kobjs", kobjMap);
		boolean re = KIoc.writeTxtInUTF8(configFile, JSONTool.writeJsonString(map));
		return re;
	}
	
	/**
	 * FIXME 添加一个新的KObj对象,更新配置文件，同时创建出新的DAO加入，并更新kconfig.json主文件
	 * @param json String 
	 * @return 是否操作成功
	 */
	@SuppressWarnings("unchecked")
	public int createKObjTable(String json){
		//先检验json
		HashMap<String,Object> kobj = this.checkKObjJson(json);
		if (kobj == null) {
			return 6;
		}
		//必须有key
		String key = kobj.keySet().iterator().next();
		HashMap<String,Object> root = (HashMap<String,Object>)kobj.get(key);
		//根据DAO配置获取dao,并验证是否存在
		HashMap<String,Object> daoMap = (HashMap<String,Object>)root.get("dao");
		String daoKey = daoMap.get("daoName").toString();
		DaoInterface dao = DaoManager.findDao(daoKey);
		if (dao == null) {
			//ErrorCode.logError(log, KObjAction.ERR_CODE1, 5, JSONTool.writeJsonString(kobj));
			return 5;
		}
		
		//更新kobj.json
		if(!KIoc.updateIniFileNode(HTManager.getIniPath()+getIniPath(), new String[]{"kobjs"},0, key, root)){
			//ErrorCode.logError(log, KObjAction.ERR_CODE1, 7, JSONTool.writeJsonString(kobj));
			return 7;
		}
		
		
		
		//创建新Dao---------------------
		if (dao.getTableName().equals("*")) {
			KIoc.setProp(dao, "tableName", daoMap.get("tableName").toString());
			if (kobj.containsKey("props")) {
				KIoc.setProps(dao,(Map<String,Object>)daoMap.get("props"));
			}
			
			
			//先更新kobj.json
			
		}
		//直接引用dao-------------------
		else{
			
			//先更新kobj.json
			
			
		}
		
		return 0;
	}
	
	/**
	 * FIXME 更新KObj表结构,同时更新DAO和kconfig.json,根据情况确定是否更新已有数据
	 * 
	 * @param key
	 * @param newKObj
	 * @return
	 */
	public boolean updateKObjTable(String key, Map<String, ?> newKObj) {
		// Object o = kobjMap.get(key);

		// FIXME 根据情况决定是否处理数据表更新

		// 如果是新表则直接添加，老表则更新
		kobjMap.put(key, newKObj);
		return this.save();
		/*
		 * Map<String,?> kobj = (Map<String,?>)o;
		 * 
		 * for (Iterator<Map<String, ?>> it = newKObj.iterator(); it.hasNext();)
		 * { Map<String, ?> map = it.next(); if
		 * (kobj.contains(map.get("column"))) { //判断是否已有 } }
		 */

	}


	
	/**
	 * 检查Kobj的json String
	 * @param json String 样例如下:
	 * <pre>
"kuser":{
	"intro":"用户",
	"dao":{
		"daoName":"mongoDao",
		"create":"new",
		"tableName":"HTItem",
		"props":{
			"id":"11",
			"type":"single"
		}
	},
	"columns":[	
		{"col":"imei","def":"","type":"string","intro":"the imei of handset","len":"30"},
		{"col":"pwd","def":"qwertnm","type":"string","intro":"password","len":"20"}
	],
	"indexs":[
		{"col":"imei","order":"1","intro":"IMEI"}
	]
}
		</pre>
	 * 
	 * @return 不合格则返回null
	 */
	@SuppressWarnings("unchecked")
	private final HashMap<String,Object> checkKObjJson(String json){
		HashMap<String,Object> map = null;
		try {
			map = JSONTool.readJsonString(json);
			if (map == null) {
				return null;
			}
			Iterator<String> iter = map.keySet().iterator();
			if (!iter.hasNext()) {
				return null;
			}
			String strKey = iter.next();
			HashMap<String,Object> m = (HashMap<String, Object>) map.get(strKey);
			//检查key
			if(!JSONTool.checkMapKeys(m,new String[]{"intro","dao","column","indexes"})){
				return null;
			}
			
			//intro就不检查了
			//HashMap<String,Object> m1 = (HashMap<String, Object>) map.get("intro");
			
			HashMap<String,Object> m2 = (HashMap<String, Object>) map.get("dao");
			if(!JSONTool.checkMapKeys(m2,new String[]{"daoName","create"})){
				return null;
			}
			//如果create为new,则必须有tableName字段
			if (m2.get("create").equals("new") && (!m2.containsKey("tableName"))) {
				return null;
			}
			ArrayList<HashMap<String,Object>> m3 = (ArrayList<HashMap<String,Object>>) map.get("column");
			for (Iterator<HashMap<String,Object>> it = m3.iterator(); it.hasNext();) {
				HashMap<String, Object> _map = it.next();
				//{"col":"pwd","def":"qwertnm","type":"string","intro":"password here","len":"20"}
				if(!JSONTool.checkMapKeys(_map,new String[]{"col","def","type","intro","len"})){
					return null;
				}
			}
			
			ArrayList<HashMap<String,Object>> m4 = (ArrayList<HashMap<String,Object>>) map.get("indexes");
			for (Iterator<HashMap<String,Object>> it = m4.iterator(); it.hasNext();) {
				HashMap<String, Object> _map = it.next();
				//{"col":"imei","order":"1","intro":"IMEI"}
				if(!JSONTool.checkMapKeys(_map,new String[]{"col","order","intro"})){
					return null;
				}
			}
		} catch (Exception e) {
			ErrorCode.logError(log, KObjAction.ERR_CODE1, 1, e,"");
			return null;
		}
		
		return map;
	}
	
	
//创建KObj,由json配置文件载入
	
	/*
	 * 管理KObj表，由配置文件配置表受到管理，可读取表结构(取出某一条记录)，索引，
	 * 对应的Dao
	 * 
	 */
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String subact = httpmsg.getHttpReq().getParameter("subact");
		if (subact == null || subact.trim().length() <3) {
			subact = "list";
		}
		msg.addData("subact", subact);
		if (subact.equals("list")) {
			msg.addData("list", kobjMap);
		}
		//find
		else if(subact.equals("find")){
			String key  = httpmsg.getHttpReq().getParameter("find_key");
			Map<String,?> m = this.findKObj(key);
			msg.addData("find", m);
		}
		//更新或新增
		else if(subact.equals("update")){
			String key  = httpmsg.getHttpReq().getParameter("update_key");
			String val = httpmsg.getHttpReq().getParameter("update_json");
			HashMap<String,Object> newOBJ = JSONTool.readJsonString(val);
			Map<String,?> newKObj = (Map<String, ?>) newOBJ.get("val");
			boolean re = this.updateKObjTable(key, newKObj);
			msg.addData("update", re);
		}
		//查询
		else if(subact.equals("search")){
			String key  = httpmsg.getHttpReq().getParameter("search_key");
			if (key != null) {
				HashMap<String, Object> re = this.searchKObj(key);
				msg.addData("list", re);
			}else{
				msg.addData("list", kobjMap);
			}
		}
		//创建新的KObj
		else if(subact.equals("createnew")){
			//String key  = httpmsg.getHttpReq().getParameter("kobj_key");
			String json  = httpmsg.getHttpReq().getParameter("kobj_json");
			int err = this.createKObjTable(json);
			msg.addData("createnew", ErrorCode.getErrorInfo(KObjAction.ERR_CODE1, err));
		}
		//更新KObj
		else if(subact.equals("updatekobj")){
			
		}
		//其他未知subact
		else{
			msg.addData("subact", "list");
			msg.addData("list", kobjMap);
		}
		return super.act(msg);
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
			Object mapobj =  root.get("kobjs");
			if (mapobj != null) {
				Map<String, Object> m = (Map<String, Object>)mapobj;
				kobjMap.putAll(m);
				log.info("KObjAction init OK! size:"+kobjMap.size());
			}else{
				ErrorCode.logError(log, KObjAction.ERR_CODE1, 3, "");
				return;
			}
		} catch (Exception e) {
			ErrorCode.logError(log, KObjAction.ERR_CODE1, 4,e, "");
		}
	}
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return "kobj.json";
	}
	
	
}
