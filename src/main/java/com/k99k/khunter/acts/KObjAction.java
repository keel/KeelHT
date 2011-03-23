/**
 * 
 */
package com.k99k.khunter.acts;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import com.k99k.khunter.Action;
import com.k99k.khunter.ActionManager;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.ErrorCode;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KObjConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * KObj和管理，以及Dao的管理
 * @author keel
 *
 */
public class KObjAction extends Action{

	public KObjAction(String name) {
		super(name);
	}
	
	static final Logger log = Logger.getLogger(ActionManager.class);
	
	public static final int ERR_CODE1 = 15;
	
//	/**
//	 * 存储Action的Map,初始化大小为50
//	 */
//	private static final HashMap<String, Object> kobjMap = new HashMap<String, Object>(50);

	
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
	
//	/**
//	 * 保存配置,同时将原文件按时间扩展名备份
//	 * @return
//	 */
//	private boolean save(){
//		//保存
//		String configFile = HTManager.getIniPath()+getIniPath();
//		HashMap<String,Object> map = new HashMap<String,Object>();
//		map.put("kobjs", kobjMap);
//		if (this.checkKObjJson(kobjMap) == null) {
//			return false;
//		}
//		int re = KIoc.saveJsonToFile(configFile, map);
//		if (re != 0) {
//			ErrorCode.logError(log, 9,re, " - in KObjAction.save()");
//			return false;
//		}
//		return true;
//	}
	
	/*
		 * 检查Kobj的json String
		 * @param json String 样例如下:
		 * <pre>
	"kuser":{
		"intro":"用户",
		"dao":{
			"daoName":"mongoDao",
			"newDaoName":"mongoItemDao",
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
		 * @return 不合格则返回null,合格则返回所需要的HashMap
		 */
//		@SuppressWarnings("unchecked")
//		private final HashMap<String,Object> checkKObjJson(HashMap<String,Object> map){
//			//HashMap<String,Object> map = null;
//			try {
//	//			HashMap<String,Object> map = JSONTool.readJsonString(json);
//	//			if (map == null) {
//	//				return null;
//	//			}
//				
//				Iterator<String> iter = map.keySet().iterator();
//				if (!iter.hasNext()) {
//					return null;
//				}
//				String strKey = iter.next();
//				HashMap<String,Object> m = (HashMap<String, Object>) map.get(strKey);
//				//检查key
//				if(!JSONTool.checkMapTypes(m,new String[]{"intro","dao","columns","indexes"},new Class[]{String.class,HashMap.class,ArrayList.class,ArrayList.class})){
//					return null;
//				}
//				
//				//intro就不检查了
//				//HashMap<String,Object> m1 = (HashMap<String, Object>) map.get("intro");
//				
//				HashMap<String,Object> m2 = (HashMap<String, Object>) map.get("dao");
//				if(!JSONTool.checkMapTypes(m2,new String[]{"daoName","newDaoName"},new Class[]{String.class,String.class})){
//					return null;
//				}
//				//如果create为new,则必须有tableName字段
//				if (m2.get("newDaoName").equals("") && (!m2.containsKey("tableName"))) {
//					return null;
//				}
//				ArrayList<HashMap<String,Object>> m3 = (ArrayList<HashMap<String,Object>>) map.get("column");
//				for (Iterator<HashMap<String,Object>> it = m3.iterator(); it.hasNext();) {
//					HashMap<String, Object> _map = it.next();
//					//{"col":"pwd","def":"qwertnm","type":"string","intro":"password here","len":"20"}
//					if(!JSONTool.checkMapTypes(map,new String[]{"col","def","type","intro","len"},new Class[]{String.class,Object.class,Integer.class,String.class,Integer.class})){
//						return null;
//					}
//				}
//				ArrayList<HashMap<String,Object>> m4 = (ArrayList<HashMap<String,Object>>) map.get("indexes");
//				for (Iterator<HashMap<String,Object>> it = m4.iterator(); it.hasNext();) {
//					HashMap<String, Object> _map = it.next();
//					//{"col":"imei","order":"1","intro":"IMEI"}
//					if(!JSONTool.checkMapTypes(_map,new String[]{"col","asc","intro","type","unique"},new Class[]{String.class,Boolean.class,String.class,String.class,Boolean.class})){
//						return null;
//					}
//				}
//			} catch (Exception e) {
//				ErrorCode.logError(log, KObjAction.ERR_CODE1, 1, e,"");
//				return null;
//			}
//			
//			return map;
//		}

//	/**
//	 * 添加一个新的KObj对象,更新配置文件，如果需求则创建新的DAO，并更新dao配置
//	 * @param json String 
//	 * @return 是否操作成功
//	 */
//	@SuppressWarnings("unchecked")
//	public int addKObjSchema(String json){
//		//先检验json
//		HashMap<String,Object> map = JSONTool.readJsonString(json);
//		if (map == null) {
//			return 6;
//		}
//		HashMap<String,Object> kobj = this.checkKObjJson(map);
//		if (kobj == null) {
//			return 6;
//		}
//		//kobj的key
//		String key = kobj.keySet().iterator().next();
//		HashMap<String,Object> root = (HashMap<String,Object>)kobj.get(key);
//		//根据DAO配置获取dao,并验证是否存在
//		HashMap<String,Object> daoMap = (HashMap<String,Object>)root.get("dao");
//		String daoKey = daoMap.get("daoName").toString();
//		DaoInterface dao = DaoManager.findDao(daoKey);
//		if (dao == null) {
//			//ErrorCode.logError(log, KObjAction.ERR_CODE1, 5, JSONTool.writeJsonString(kobj));
//			return 5;
//		}
//		
//		//更新kobj.json
//		if(!KIoc.updateIniFileNode(HTManager.getIniPath()+getIniPath(), new String[]{"kobjs"},0,-1, key, root)){
//			//ErrorCode.logError(log, KObjAction.ERR_CODE1, 7, JSONTool.writeJsonString(kobj));
//			return 7;
//		}
//		
//		
//		
//		//创建新Dao---------------------
//		if (dao.getTableName().equals("*")) {
//			//设置dao属性
//			KIoc.setProp(dao, "tableName", daoMap.get("tableName").toString());
//			if (kobj.containsKey("props")) {
//				if(!KIoc.setProps(dao,(Map<String,Object>)daoMap.get("props"))){
//					return 8;
//				}
//			}
//			if(!DaoManager.addDao(dao)){
//				return 9;
//			}
//			if(!DaoManager.storeDao(dao)){
//				return 10;
//			}
//			
//		}
//		//直接引用dao-------------------
////		else{
////		}
//		
//		return 0;
//	}
	
//	/**
//	 * 更新KObj表结构,同时更新DAO和dao的配置,根据情况确定是否更新已有数据
//	 * 
//	 * @param jsonPath 需要更新的路径
//	 * @param key
//	 * @param newKObj
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public boolean updateKObjTable(String[] jsonPath,int opType,int arrPosiion, String key,Map<String, ?> newValue) {
//
//		//生成新的配置文件并检查
//		HashMap<String, Object> kobjMap2 = (HashMap<String, Object>)kobjMap.clone();
//		kobjMap2 = KIoc.updateJsonNode(kobjMap2, jsonPath, opType,arrPosiion, key, newValue);
//		
//		if (this.checkKObjJson(kobjMap2) == null) {
//			return false;
//		}
//		//更新kobjMap对象
//		HashMap<String,Object> target = kobjMap;
//		for (int i = 0; i < jsonPath.length; i++) {
//			target = (HashMap<String,Object>)(target.get(jsonPath[i]));
//		}
//		target.put(key,newValue);
//		
//		//更新kobj.json配置文件
//		return KIoc.updateIniFileNode(HTManager.getIniPath()+this.getIniPath(), jsonPath, 0,arrPosiion, key, newValue);
//
//	}
	
//	/**
//	 * 更新Dao
//	 * @param daoNode
//	 * @param value
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public int updateDao(String kobjName,String daoNode,Object value){
//		if (!kobjMap.containsKey(kobjName)) {
//			return 14;
//		}
//		HashMap<String, Object> kobjMap2 = (HashMap<String, Object>)kobjMap.clone();
//		HashMap<String,Object> kobjNode = (HashMap<String, Object>) kobjMap2.get(kobjName);
//		HashMap<String,Object> daoMap = (HashMap<String, Object>) kobjNode.get("dao");
//		//更新props
//		if (daoNode.equals("props")) {
//			HashMap<String,Object> newProp = (HashMap<String,Object>)value;
//			daoMap.putAll(newProp);
//			//注意测试错误参数时, kobjMap2是否工作 ,save是否执行
//			if((this.checkKObjJson(kobjMap2)!=null) ){
//				DaoInterface dao = DaoManager.findDao(((HashMap)daoMap.get("dao")).get("daoName").toString());
//				if (dao == null) {
//					return 16;
//				}
//				//设置dao属性
//				KIoc.setProp(dao, "tableName", daoMap.get("tableName").toString());
//				if(KIoc.setProps(dao,(Map<String,Object>)daoMap.get("props"))){
//					return 11;
//				}
//				//创建新Dao---------------------
//				if (dao.getTableName().equals("*")) {
//					if(!DaoManager.addDao(dao)){
//						return 12;
//					}
//				}
//				//直接更新引用dao-------------------
//				else{
//					if(!DaoManager.changeDao(daoNode, dao)){
//						return 15;
//					}
//				}
//				if(!DaoManager.storeDao(dao) || !this.save()){
//					return 13;
//				}
//			}
//		}
//		//更新dao属性
//		else {
//		}
//		
//		return 0;
//	}
	
	
	

	
//	/**
//	 * 更换Kobj的Dao,只能更换DaoManager中已有的可引用Dao,若要新建需要先在
//	 * @param kobjName
//	 * @param newDaoName 必须是可引用的Dao
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public int changeKObjDao(String kobjName,String newDaoName){ 
//		HashMap<String,Object> kobjNode = (HashMap<String, Object>) kobjMap.get(kobjName);
//		if (kobjNode == null) {
//			return 24;
//		}
//		DaoInterface dao = DaoManager.findDao(newDaoName);
//		if (dao == null) {
//			return 27;
//		}
//		if (dao.getTableName().equals("*")) {
//			//如果是新建Dao，则返回错误码，进入新建DAO流程
//			return 26;
//		}
//		//如果新daoName是引用的,直接更换
//		HashMap<String,Object> daoMap = (HashMap<String,Object>)kobjNode.get("dao");
//		daoMap.put("daoName", dao.getName());
//		daoMap.put("newDaoName", "");
//		daoMap.remove("tableName");
//		daoMap.remove("props");
//		if(!KObjManager.saveIni()){
//			return 25;
//		}
//		return 0;
//	}
//	
	
	/**
	 * 保存配置,实际上是KObjManager的配置
	 * @return
	 */
	public final boolean save(){
		return KObjManager.saveIni();
	}
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String subact = httpmsg.getHttpReq().getParameter("subact");
		if (subact == null || subact.trim().length() <3) {
			subact = "list";
		}
		msg.addData("subact", subact);
		if (subact.equals("list")) {
			msg.addData("list", KObjManager.getKObjMap());
		}
		//schema_find
		else if(subact.equals("schema_find")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			KObjConfig kc = KObjManager.findKObjConfig(key);
			msg.addData("right", "schema");
			msg.addData("schema_find", kc);
		}
		//schema_update
		else if(subact.equals("schema_update")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String json  = httpmsg.getHttpReq().getParameter("schema_json");
			KObjConfig kc = KObjManager.findKObjConfig(key);
			KObjSchema ks = kc.getKobjSchema();
			DaoInterface dao = KObjManager.findDao(key);
			
			//FIXME ks.setProp(kobj, kobjPath, JSONTool.readJsonString(json));
			msg.addData("schema_update", ks);
		}
		//查找KObj
		else if(subact.equals("query")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String json  = httpmsg.getHttpReq().getParameter("query_json");
			DaoInterface dao = KObjManager.findDao(key);
			if (JSONTool.validateJsonString(json)) {
				HashMap m = JSONTool.readJsonString(json);
				List re = dao.query(m, null, null, 0, 0, null);
				msg.addData("query_list", re);
			}else{
				msg.addData("query_list", "");
			}
			
		}
		//更新或新增
		else if(subact.equals("update")){
			boolean re = false;
			String jsonPathString = httpmsg.getHttpReq().getParameter("update_path");
			String val = httpmsg.getHttpReq().getParameter("update_json");
			int position = StringUtil.objToNonNegativeInt(httpmsg.getHttpReq().getParameter("update_position"));
			int opType = StringUtil.objToNonNegativeInt(httpmsg.getHttpReq().getParameter("update_optype"));
			String[] jsonPath = jsonPathString.split(",");
			String key  = httpmsg.getHttpReq().getParameter("update_key");
			Object newOBJ = JSONTool.readJsonString(val);
			//re = this.updateKObjTable(jsonPath,opType, position,key, (Map<String, Object>) newOBJ);
			msg.addData("update", re);
		}
		
		//创建新的KObj
		else if(subact.equals("addkobjschema")){
			String key  = httpmsg.getHttpReq().getParameter("kobj_key");
			String json  = httpmsg.getHttpReq().getParameter("kobj_json");
			int err =KObjManager.createKObjConfigToDB(key, JSONTool.readJsonString(json));
			msg.addData("addkobjschema", ErrorCode.getErrorInfo(KObjAction.ERR_CODE1, err));
		}
		//更新KObjConfig的intro
		else if(subact.equals("kc_intro_update")){
			
		}
		//更新或增加KObj的单个column
		else if(subact.equals("kc_column_update")){
			
		}
		//更新或新增KObj的索引
		else if(subact.equals("kc_index_update")){
			
		}
		//更新KObj的dao(引用及参数)
		else if(subact.equals("kc_dao_update")){
			
		}
		//保存KObjManager的配置文件
		else if(subact.equals("km_ini_save")){
			
		}
		//查询
		else if(subact.equals("search")){
			String key  = httpmsg.getHttpReq().getParameter("search_key");
			if (key != null) {
				HashMap<String, KObjConfig> re = KObjManager.searchKObjList(key);
				msg.addData("list", re);
			}else{
				msg.addData("list", KObjManager.searchKObjList(""));
			}
		}
		//其他未知subact
		else{
			msg.addData("subact", "list");
			msg.addData("list", KObjManager.searchKObjList(""));
		}
		return super.act(msg);
	}


	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {
	}
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return "kobj.json";
	}
	
	
}
