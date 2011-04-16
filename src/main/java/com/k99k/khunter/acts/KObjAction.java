/**
 * 
 */
package com.k99k.khunter.acts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.k99k.khunter.Action;
import com.k99k.khunter.ActionManager;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.ErrorCode;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KObjConfig;
import com.k99k.khunter.KObjDaoConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.khunter.KObject;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * KObj的管理和查询
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
	 * 保存配置,实际上是KObjManager的配置
	 * @return
	 */
	public final boolean save(){
		return KObjManager.saveIni();
	}
	
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
			msg.addData("list", KObjManager.getKObjMap());
		}
		//schema_find
		else if(subact.equals("schema_find")){
			String js = "<script type=\"text/javascript\" src=\"js/jquery.json-2.2.min.js\"></script><script type=\"text/javascript\" src=\"js/hotEdit.js\"></script>";
			msg.addData("headerAdd", js);
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			KObjConfig kc = KObjManager.findKObjConfig(key);
			msg.addData("right", "schema");
			msg.addData("schema_find", kc);
		}
		//schema_add
		else if(subact.equals("schema_add")){
			String js = "<script type=\"text/javascript\" src=\"js/jquery.json-2.2.min.js\"></script><script type=\"text/javascript\" src=\"js/hotEdit.js\"></script>";
			msg.addData("headerAdd", js);
			msg.addData("right", "schema_add");
		}
		//schema_update
		else if(subact.equals("schema_update")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String part  = httpmsg.getHttpReq().getParameter("schema_part");
			if (key == null || part == null) {
				msg.addData("print", "{\"re\":\"err\",\"d\":{\"schema_key\":\"schema_key or part error.\"}}");
				return super.act(msg);
			}
			KObjConfig kc = KObjManager.findKObjConfig(key);
			if (kc == null) {
				msg.addData("print", "{\"re\":\"err\",\"d\":{\"schema_key\":\"not found.\"}}");
				return super.act(msg);
			}
			//失败原因
			String rePrint = "{\"re\":\"err\",\"d\":{}";
			//----------------更新intro---------------
			if (part.equals("intro")) {
				String intro = httpmsg.getHttpReq().getParameter("schema_intro");
				if (StringUtil.isStringWithLen(intro, 0)) {
					kc.setIntro(intro);
					//print在jsp前面，所以不用remove jsp
					msg.addData("print", "{\"re\":\"ok\",\"d\":{\"schema_intro\":\""+intro+"\"}}");
					return super.act(msg);
				}
				rePrint = "{\"re\":\"err\",\"d\":{\"schema_intro\":\"intro input error\"}}";
			}
			//----------------更新dao---------------
			else if(part.equals("dao")){
				String daoJson = httpmsg.getHttpReq().getParameter("schema_daojson");
				if (StringUtil.isStringWithLen(daoJson, 2)) {
					KObjDaoConfig kdc = KObjDaoConfig.newInstance(JSONTool.readJsonString(daoJson));
					if (kdc != null) {
						//更新KObjConfig
						kc.setDaoConfig(kdc);
						msg.addData("print", "{\"re\":\"ok\",\"d\":{\""+JSONTool.writeJsonString(kdc.toMap())+"\"}}");
						return super.act(msg);
					}
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_daojson\":\"KObjDaoConfig.newInstance error\"}}";
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_daojson\":\"daoJson error\"}}";
				}
			}
			//----------------更新column---------------
			else if(part.equals("col_edit")){
				String colJson = httpmsg.getHttpReq().getParameter("schema_coljson");
				if (StringUtil.isStringWithLen(colJson, 2)) {
					KObjSchema ks = kc.getKobjSchema();
					if(ks.setColumn(JSONTool.readJsonString(colJson)) == 0){
						msg.addData("print", "{\"re\":\"ok\",\"d\":"+colJson+"}");
						return super.act(msg);
					}
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_coljson\":\"ks.setColumn error\"}}";
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_coljson\":\"schema_coljson error\"}}";
				}
			}
			//----------------删除column---------------
			else if(part.equals("col_del")){
				String colJson = httpmsg.getHttpReq().getParameter("schema_coljson");
				if (StringUtil.isStringWithLen(colJson, 2)) {
					KObjSchema ks = kc.getKobjSchema();
					String k = JSONTool.readJsonString(colJson).get("col").toString();
					if(ks.containsColumn(k)){
						ks.removeColumn(k);
						msg.addData("print", "{\"re\":\"ok\",\"d\":"+colJson+"}");
						return super.act(msg);
					}
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_coljson\":\"Column key not exsit.\"}}";
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_coljson\":\"schema_coljson error\"}}";
				}
			}
			//----------------更新index---------------
			else if(part.equals("index_edit")){
				String indexJson = httpmsg.getHttpReq().getParameter("schema_indexjson");
				if (StringUtil.isStringWithLen(indexJson, 2)) {
					KObjSchema ks = kc.getKobjSchema();
					if (ks.setIndex(JSONTool.readJsonString(indexJson),true) == 0) {
						msg.addData("print", "{\"re\":\"ok\",\"d\":"+indexJson+"}");
						return super.act(msg);
					}
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_indexjson\":\"setIndex error\"}}";
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_indexjson\":\"schema_indexjson error\"}}";
				}
			}
			//----------------删除index---------------
			else if(part.equals("index_del")){
				String indexJson = httpmsg.getHttpReq().getParameter("schema_indexjson");
				if (StringUtil.isStringWithLen(indexJson, 2)) {
					KObjSchema ks = kc.getKobjSchema();
					String k = JSONTool.readJsonString(indexJson).get("col").toString();
					if (ks.removeIndex(k)) {
						msg.addData("print", "{\"re\":\"ok\",\"d\":"+indexJson+"}");
						return super.act(msg);
					}
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_indexjson\":\"removeIndex error\"}}";
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"schema_indexjson\":\"schema_indexjson error\"}}";
				}
			}
			//失败的情况
			msg.addData("print", rePrint);
			return super.act(msg);	
		}
		//查找KObj
		else if(subact.equals("query")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String qjson  = httpmsg.getHttpReq().getParameter("query_json");
			String rePrint = "para error";
			//跳转到query.jsp
			msg.addData("right", "query");
			if (StringUtil.isStringWithLen(key, 2) && StringUtil.isStringWithLen(qjson, 2)) {
				if (JSONTool.validateJsonString(qjson)) {
					HashMap qm = JSONTool.readJsonString(qjson);
					if (JSONTool.checkMapTypes(qm, new String[]{"query"}, new Class[]{HashMap.class})) {
						HashMap query = (HashMap) qm.get("query");
						HashMap fields = (JSONTool.checkMapTypes(qm, new String[]{"fields"}, new Class[]{HashMap.class}))?(HashMap)qm.get("fields"):null;
						HashMap sortby = (JSONTool.checkMapTypes(qm, new String[]{"sortby"}, new Class[]{HashMap.class}))?(HashMap)qm.get("sortby"):null;
						int skip = (qm.containsKey("skip") && StringUtil.isDigits(qm.get("skip").toString()))?Integer.parseInt(qm.get("skip").toString()):0;
						int len = (qm.containsKey("len") && StringUtil.isDigits(qm.get("len").toString()))?Integer.parseInt(qm.get("len").toString()):0;
						HashMap hint = (JSONTool.checkMapTypes(qm, new String[]{"hint"}, new Class[]{HashMap.class}))?(HashMap)qm.get("hint"):null;
						DaoInterface dao = KObjManager.findKObjConfig(key).getDaoConfig().findDao();
						if (dao != null) {
							List re = dao.query(query, fields, sortby, skip, len, hint);
							if (re != null) {
								msg.addData("query_list", "ok:"+re);
								return super.act(msg);	
							}
							rePrint = "dao.query failed.";
						}
						rePrint = "dao not found.";
					}else{
						rePrint = "query node error.";
					}
				}else{
					rePrint = "json error";
				}
			}
			//失败的情况
			msg.addData("re", rePrint);
			msg.addData("query_list", "");
			return super.act(msg);	
		}
		//具体KObject的crud操作
		else if(subact.equals("kobj_act")){
			String kobj_act = httpmsg.getHttpReq().getParameter("kobj_act");
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String rePrint = "{\"re\":\"err\",\"d\":{}";
			if (!StringUtil.isStringWithLen(key, 2) || !StringUtil.isStringWithLen(key, 2)) {
				msg.addData("print", "{\"re\":\"err\",\"d\":{\"schema_key\":\"schema_key or kobj_act error.\"}}");
				return super.act(msg);
			}
			KObjConfig kc = KObjManager.findKObjConfig(key);
			if (kc == null) {
				msg.addData("print", "{\"re\":\"err\",\"d\":{\"schema_key\":\"not found.\"}}");
				return super.act(msg);
			}
			//查询kobj
			if(kobj_act.equals("search")){
				String kobj_q = httpmsg.getHttpReq().getParameter("kobj_queryjson");
				String kobj_f = httpmsg.getHttpReq().getParameter("kobj_fieldsjson");
				String kobj_sort = httpmsg.getHttpReq().getParameter("kobj_sortjson");
				String kobj_skip = httpmsg.getHttpReq().getParameter("kobj_skip");
				String kobj_len = httpmsg.getHttpReq().getParameter("kobj_len");
				String kobj_hint = httpmsg.getHttpReq().getParameter("kobj_hint");
				HashMap<String,Object> query = (StringUtil.isStringWithLen(kobj_q, 2) && JSONTool.validateJsonString(kobj_q)) ? JSONTool.readJsonString(kobj_q) : null;
				if (query != null) {
					HashMap<String,Object> fields = (StringUtil.isStringWithLen(kobj_f, 2) && JSONTool.validateJsonString(kobj_f)) ? JSONTool.readJsonString(kobj_f) : null;
					HashMap<String,Object> sortBy = (StringUtil.isStringWithLen(kobj_sort, 2) && JSONTool.validateJsonString(kobj_sort)) ? JSONTool.readJsonString(kobj_sort) : null;
					int skip = (StringUtil.isDigits(kobj_skip))?Integer.parseInt(kobj_skip):0;
					//默认长度为20
					int len = (StringUtil.isDigits(kobj_len))?Integer.parseInt(kobj_len):20;
					HashMap<String,Object> hint = (StringUtil.isStringWithLen(kobj_hint, 2) && JSONTool.validateJsonString(kobj_hint)) ? JSONTool.readJsonString(kobj_hint) : null;
					List list = kc.getDaoConfig().findDao().query(query, fields, sortBy, skip, len, hint);
					String d = JSONTool.writeJsonString(list);
					msg.addData("print", "{\"re\":\"ok\",\"d\":{\"list\":"+d+"}}");
					return super.act(msg);
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"kobj_queryjson\":\"kobj_queryjson error\"}}";
				}
				
			}
			//删除kobj
			else if(kobj_act.equals("del")){
				String kobj_id = httpmsg.getHttpReq().getParameter("kobj_id");
				if (StringUtil.isDigits(kobj_id)) {
					long kid = Long.parseLong(kobj_id);
					if (httpmsg.getHttpReq().getParameter("delforever") == null) {
						if(kc.getDaoConfig().findDao().deleteOne(kid)){
							msg.addData("print", "{\"re\":\"ok\",\"d\":{\"id\":"+kid+"}}");
							return super.act(msg);
						}
					}else{
						if (kc.getDaoConfig().findDao().deleteForever(Long.parseLong(kobj_id))) {
							msg.addData("print", "{\"re\":\"ok\",\"d\":{\"id\":"+kid+",\"forever\":true}}");
							return super.act(msg);
						}
					}
				}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"kobj_id\":\"kobj_id error\"}}";
				}
			}
			//add or update KObj
			else if (kobj_act.equals("update")) {
				String kobj_json = httpmsg.getHttpReq().getParameter("kobj_json");
				//如果有kobj_id参数则为更新,无则为添加
				String kobj_id = httpmsg.getHttpReq().getParameter("kobj_id");
				if (StringUtil.isStringWithLen(key, 2) && StringUtil.isStringWithLen(kobj_json, 2)) {
					if (JSONTool.validateJsonString(kobj_json)) {
						HashMap nk = JSONTool.readJsonString(kobj_json);
						KObjSchema ks = kc.getKobjSchema();
						DaoInterface dao = kc.getDaoConfig().findDao();
						KObject newKObj = (StringUtil.isDigits(kobj_id))?dao.findOne(kobj_id):ks.createEmptyKObj();
						if (ks.setPropFromMap(nk, newKObj) && dao.save(newKObj)) {
							msg.addData("print", "{\"re\":\"ok\",\"d\":{\"id\":"+newKObj.getId()+"}}");
							return super.act(msg);
						}else{
							rePrint = "{\"re\":\"err\",\"d\":{\"kobj_json\":\"setPropFromMap or dao.save error\"}}";
						}
					}else{
					rePrint = "{\"re\":\"err\",\"d\":{\"kobj_json\":\"readJsonString error\"}}";
					}
				}else{
				rePrint = "{\"re\":\"err\",\"d\":{\"kobj_json\":\"kobj_json error\"}}";
				}
			}
			//失败的情况
			msg.addData("re", rePrint);
			return super.act(msg);
		}
		//新增KobjConfig
		else if(subact.equals("kc_add")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String kcjson  = httpmsg.getHttpReq().getParameter("schema_kcjson");
			String rePrint = "para error";
			if (StringUtil.isStringWithLen(key, 2) && StringUtil.isStringWithLen(kcjson, 2)) {
				int re = KObjManager.createKObjConfigToDB(key, JSONTool.readJsonString(kcjson));
				if (re == 0) {
					msg.addData("print", "ok");
					return super.act(msg);	
				}
				rePrint = ErrorCode.getErrorInfo(8, re);
			}
			//失败的情况
			msg.addData("print", "err:"+rePrint);
			return super.act(msg);	
		}
		//保存KObjManager的配置文件
		else if(subact.equals("ini_save")){
			String update  = httpmsg.getHttpReq().getParameter("update");
			if (update == null) {
				msg.addData("right", "kobjmgr_saveini");
				String js = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery.snippet.min.css\" /><script type=\"text/javascript\" src=\"js/jquery.snippet.min.js\"></script>";
				msg.addData("headerAdd", js);
				msg.addData("newIni", JSONTool.writeFormatedJsonString(KObjManager.getCurrentIni(), 6));
				return super.act(msg);
			}else{
				String re = "err:savaIni error.";
				if(KObjManager.saveIni()){
					re = "ok:ok";
				}
				msg.addData("print", re);
				return super.act(msg);
			}
		}
		//查询dao的Names,返回以,号分开的所有daoNames
		else if(subact.equals("dao_names")){
			ArrayList<String> list = DaoManager.getDaoNames();
			StringBuilder sb = new StringBuilder();
			for (Iterator<String> it = list.iterator(); it.hasNext();) {
				String str = it.next();
				sb.append(str).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			msg.addData("print", sb.toString());
			return super.act(msg);
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

	@Override
	public void exit() {
		
	}
	
	
}
