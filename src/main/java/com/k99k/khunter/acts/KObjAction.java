/**
 * 
 */
package com.k99k.khunter.acts;

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
import com.k99k.khunter.KObjConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.khunter.KObject;
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
	
	///----------------以下为DAO操作-----------------
	
	/**
	 * 添加DAO,并更新DAO配置
	 * @param daoName
	 * @param _class
	 * @param _dataSource
	 * @param dbType
	 * @param type
	 * @param tableName
	 * @param id
	 * @return
	 */
	public int addDao(String daoName,String _class,String _dataSource,String dbType,String type,String tableName,int id){
		if(!DaoManager.addDao(daoName,_class,_dataSource,dbType,type,tableName,id)){
			return 27;
		}
		if(!DaoManager.storeDao(daoName)){
			return 28;
		}
		return 0;
	}
	
	/**
	 * 删除dao,同时更新配置
	 * @param daoName
	 * @return
	 */
	public int removeDao(String daoName){
		if (!DaoManager.removeDao(daoName)) {
			return 29;
		}
		return 0;
	}
	
	/**
	 * 更新Dao属性,不影响Kobj,注意_class和_dataSource无法在这里更新
	 * @param daoName
	 * @param propName
	 * @param propValue
	 * @return
	 */
	public int updateDaoProps(String daoName,String propName,String propValue){
		DaoInterface dao = DaoManager.findDao(daoName);
		if (dao == null) {
			return 30;
		}
		if (propName.equals("dbType")) {
			dao.setDbType(propValue);
		}else if(propName.equals("type")){
			dao.setType(propValue);
		}else if(propName.equals("tableName")){
			dao.setTableName(propValue);
		}else if(propName.equals("id") && propValue.matches("\\d+")){
			dao.setId(Integer.parseInt(propValue));
		}
		//更新配置
		boolean re = DaoManager.storeDao(daoName);
		if (!re) {
			return 31;
		}
		return 0;
	}
	
	/**
	 * 重新载入DAO,使用配置文件的当前配置
	 * @param daoName
	 * @return
	 */
	public boolean reloadDao(String daoName){
		return DaoManager.reLoadDao(daoName);
	}
	
	
	/**
	 * 执行DAO请求
	 * @param kobjName String
	 * @param daoFunc 序号为DaoInterface中方法的顺序,如1为findOne(long id)
	 * @param jsonReq String 结构如下:
<pre>
{
	"req":"23" //id为23
	"req":{ //其他请求使用json参数方式
		...
	}
}
</pre>
	 * @return Object 执行后的结果,如果为int类型则为错误码(count的结果以String返回)
	 */
	@SuppressWarnings("unchecked")
	public final Object execDaoFunction(String kobjName,int daoFunc,String jsonReq){
		DaoInterface dao = KObjManager.findDao(kobjName);
		if (dao == null || jsonReq == null) {
			return 20;
		}
		HashMap<String,Object> reqMap = JSONTool.readJsonString(jsonReq);
		if (reqMap == null || (!reqMap.containsKey("req"))) {
			return 21;
		}
		Object o = reqMap.get("req");
		if (o == null) {
			return 21;
		}
		//1-15个function的处理
		
		switch (daoFunc) {
		
		case 1:
			//---------------findOne(long id)
			if (o instanceof Long) {
				long id = (Long)o;
				return dao.findOne(id);
			}else{
				return 22;
			}
			
		case 2:
			//---------------findOne(String name)
			if (o instanceof String) {
				return dao.findOne(o.toString());
			}else{
				return 22;
			}
		case 3:
			//---------------findOneMap(BasicDBObject query,BasicDBObject fields)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				Object o2 = req.get("fields");
				if (o1==null || o2==null) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> && o2 instanceof HashMap<?,?>) {
					return dao.findOneMap((HashMap<String,Object>)o1,(HashMap<String,Object>)o2);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 4:
			//---------------findOneMap(long id)
			if (o instanceof Long) {
				long id = (Long)o;
				return dao.findOneMap(id);
			}else{
				return 22;
			}
		case 5:
			//---------------query(HashMap<String,Object> query,HashMap<String,Object> fields,
			//HashMap<String,Object> sortBy,int skip,int len,HashMap<String,Object> hint)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				Object o2 = req.get("fields");
				Object o3 = req.get("sortBy");
				Object o4 = req.get("skip");
				Object o5 = req.get("len");
				Object o6 = req.get("hint");
				if (o1==null || o2==null || o3==null  || o4==null  || o5==null  || o6==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> && o2 instanceof HashMap<?,?> && o3 instanceof HashMap<?,?> && o4 instanceof Long && o5 instanceof Long && o6 instanceof HashMap<?,?>) {
					return dao.query((HashMap<String,Object>)o1,(HashMap<String,Object>)o2,(HashMap<String,Object>)o3,Integer.parseInt(o4.toString()),Integer.parseInt(o5.toString()),(HashMap<String,Object>)o6);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 6:
			//---------------count(HashMap<String,Object> query,HashMap<String,Object> hint)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				Object o2 = req.get("hint");
				if (o1==null || o2==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> && o2 instanceof HashMap<?,?> ) {
					//注意返回String的形式，与错误码区分
					return dao.count((HashMap<String,Object>)o1,(HashMap<String,Object>)o2)+"";
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 7:
			//---------------count(HashMap<String,Object> query)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				if (o1==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> ) {
					//注意返回String的形式，与错误码区分
					return dao.count((HashMap<String,Object>)o1)+"";
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 8:
			//---------------add(KObject kObj)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				KObject kobj = new KObject(req);
				return dao.add(kobj);
			}else{
				return 22;
			}
		case 9:
			//---------------save(KObject kObj)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				KObject kobj = new KObject(req);
				return dao.save(kobj);
			}else{
				return 22;
			}
		case 10:
			//---------------updateOne(long id,KObject newObj)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("id");
				Object o2 = req.get("newObj");
				if (o1==null || o2==null ) {
					return 22;
				}
				if (o1 instanceof Long && o2 instanceof HashMap<?,?> ) {
					//注意返回String的形式，与错误码区分
					KObject kobj = new KObject((HashMap<String,Object>)o2);
					return dao.updateOne((Long)o1, kobj);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 11:
			//---------------updateOne(HashMap<String,Object> query,HashMap<String,Object> set)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				Object o2 = req.get("set");
				if (o1==null || o2==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> && o2 instanceof HashMap<?,?> ) {
					//注意返回String的形式，与错误码区分
					return dao.updateOne((HashMap<String,Object>)o1,(HashMap<String,Object>)o2);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 12:
			//---------------update(HashMap<String,Object> query,HashMap<String,Object> set,boolean upset,boolean multi)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				Object o2 = req.get("set");
				Object o3 = req.get("upset");
				Object o4 = req.get("multi");
				if (o1==null || o2==null || o3==null || o4==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> && o2 instanceof HashMap<?,?> && o3 instanceof Boolean && o4 instanceof Boolean ) {
					//注意返回String的形式，与错误码区分
					return dao.update((HashMap<String,Object>)o1,(HashMap<String,Object>)o2,(Boolean)o3,(Boolean)o4);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 13:
			//---------------deleteOne(long id)
			if (o instanceof Long) {
				long id = (Long)o;
				return dao.deleteOne(id);
			}else{
				return 22;
			}
		case 14:
			//---------------delete(HashMap<String,Object> query,boolean multi)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				Object o2 = req.get("multi");
				if (o1==null || o2==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> && o2 instanceof Boolean ) {
					//注意返回String的形式，与错误码区分
					return dao.delete((HashMap<String,Object>)o1,(Boolean)o2);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		case 15:
			//---------------deleteForever(HashMap<String,Object> query)
			if (o instanceof HashMap<?,?>) {
				HashMap<String,Object> req  = (HashMap<String,Object>)o;
				Object o1 = req.get("query");
				if (o1==null ) {
					return 22;
				}
				if (o1 instanceof HashMap<?,?> ) {
					//注意返回String的形式，与错误码区分
					return dao.deleteForever((HashMap<String,Object>)o1);
				}else{
					return 22;
				}
			}else{
				return 22;
			}
		default:
			return 23;
		}
		
	}
	
	
	///----------------以上为DAO操作-----------------
	
	
	
	
	
	
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
			KObjSchema ks = KObjManager.findSchema(key);
			msg.addData("schema_find", ks);
		}
		//schema_update
		else if(subact.equals("schema_update")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String json  = httpmsg.getHttpReq().getParameter("schema_json");
			KObjConfig kc = KObjManager.findKObjConfig(key);
			KObjSchema ks = kc.getKobjSchema();
			
			//FIXME ks.setProp(kobj, kobjPath, JSONTool.readJsonString(json));
			msg.addData("schema_update", ks);
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
		//更新KObj
		else if(subact.equals("updatekobjintro")){
			
		}
		//更新KObj
		else if(subact.equals("updatekobjcolumns")){
			
		}
		//更新KObj
		else if(subact.equals("updatekobjindexes")){
			
		}
		//更新KObj
		else if(subact.equals("updatedaoprops")){
			
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
