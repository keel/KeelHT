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
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
import com.k99k.khunter.KObject;
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
	
	/**
	 * 存储Action的Map,初始化大小为100
	 */
	private static Map<String, Object> kobjMap;// = new HashMap<String, Object>(50);

	public boolean createKObj(String name,KObject kobj){
		
		return true;
	}
	
	/**
	 * 获取所有KObj对象的列表
	 * @return ArrayList<ArrayList<?>>
	 */
	public ArrayList<ArrayList<?>> getKObjList(){
		ArrayList<ArrayList<?>> list = new ArrayList<ArrayList<?>>();
		for (Iterator<String> it = kobjMap.keySet().iterator(); it.hasNext();) {
			String kobj =  it.next();
			list.add((ArrayList<?>) kobjMap.get(kobj));
		}
		return list;
	}
	
	public Map<String, Object> getKObjMap(){
		return kobjMap;
	}
	
	/**
	 * 查找所有包含key字符串的的KObj的名称List
	 * @param key 查找key
	 * @return ArrayList<String>
	 */
	public ArrayList<String> searchKObj(String key){
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<String> it = kobjMap.keySet().iterator(); it.hasNext();) {
			String kobj =  it.next();
			if (kobj.indexOf(key.trim()) > -1) {
				list.add(kobj);
			}
		}
		return list;
	}
	
	/**
	 * 查看某一具体的KObj结构
	 * @param key KObj的key
	 * @return ArrayList<Map<String,?>>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Map<String,?>>  findKObj(String key){
		Object o = kobjMap.get(key);
		if (o == null) {
			return null;
		}
		ArrayList<Map<String,?>> list = (ArrayList<Map<String,?>>)o;
		return list;
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
			e.printStackTrace();
			log.error("save failed - save bak file failed:"+bak, e);
		}
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("kobjs", kobjMap);
		boolean re = KIoc.writeTxtInUTF8(configFile, JSONTool.writeJsonString(map));
		return re;
	}
	
//	/**
//	 * 添加一个KObj对象
//	 * @param key
//	 * @param kobj
//	 * @return 是否操作成功
//	 */
//	public boolean addKObj(String key,HashMap<String,Object> kobj){
//		
//		
//		return true;
//	}
//	
//	/**
//	 * 更新一个KObj对象
//	 * @param key
//	 * @param kobj
//	 * @return 是否操作成功
//	 */
//	public boolean updateKObj(String key,HashMap<String,Object> kobj){
//		
//		
//		return true;
//	}
//	
//	
//	/**
//	 * 删除一个KObj对象(从数据库中永久删除)
//	 * @param key
//	 * @param kobj
//	 * @return 是否操作成功
//	 */
//	public boolean delKObj(String key,HashMap<String,Object> kobj){
//		
//		
//		return true;
//	}
	
	
	
	
	/**
	 * 更新KObj表结构
	 * @param key
	 * @param newKObj
	 * @return
	 */
	public boolean updateKObjTable(String key,ArrayList<Map<String,?>> newKObj){
		Object o = kobjMap.get(key);
		if (o != null) {
			
			//FIXME 根据情况决定是否处理数据表更新
			kobjMap.put(key, newKObj);
			return this.save();
		}
		/*
		ArrayList<Map<String,?>> kobj = (ArrayList<Map<String,?>>)o;
		
		for (Iterator<Map<String, ?>> it = newKObj.iterator(); it.hasNext();) {
			Map<String, ?> map = it.next();
			if (kobj.contains(map.get("column"))) {
				//判断是否已有
			}
		}
		*/
		
		return false;
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
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String subact = httpmsg.getHttpReq().getParameter("subact");
		if (subact == null || subact.trim().length() <=3) {
			subact = "list";
		}
		msg.addData("subact", subact);
		if (subact.equals("list")) {
			msg.addData("list", kobjMap);
		}
		//find
		else if(subact.equals("find")){
			String key  = httpmsg.getHttpReq().getParameter("key");
			ArrayList<Map<String,?>> l = this.findKObj(key);
			msg.addData("find", l);
		}
		//更新
		else if(subact.equals("update")){
			String key  = httpmsg.getHttpReq().getParameter("key");
			String val = httpmsg.getHttpReq().getParameter("json");
			HashMap<String,Object> newOBJ = JSONTool.readJsonString(val);
			ArrayList<Map<String,?>> newKObj = (ArrayList<Map<String, ?>>) newOBJ.get("val");
			boolean re = this.updateKObjTable(key, newKObj);
			msg.addData("update", re);
		}
		//查询
		else if(subact.equals("search")){
			String key  = httpmsg.getHttpReq().getParameter("key");
			if (key != null) {
				ArrayList<String> re = this.searchKObj(key);
			}
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
				kobjMap = (Map<String, Object>)mapobj;
				log.info("KObjAction init OK!");
			}else{
				log.error("kobjs node not exist! KObjAction init failed.");
				return;
			}
		} catch (Exception e) {
			log.error("ConsoleAction init Error!", e);
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
