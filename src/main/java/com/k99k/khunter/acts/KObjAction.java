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
import com.k99k.khunter.KObjDaoConfig;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
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
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			KObjConfig kc = KObjManager.findKObjConfig(key);
			msg.addData("right", "schema");
			msg.addData("schema_find", kc);
		}
		//schema_update
		else if(subact.equals("schema_update")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String part  = httpmsg.getHttpReq().getParameter("schema_part");
			if (key == null || part == null) {
				msg.addData("print", "err:schema_key or part error.");
				return super.act(msg);
			}
			KObjConfig kc = KObjManager.findKObjConfig(key);
			if (kc == null) {
				msg.addData("print", "err:kc not found.");
				return super.act(msg);
			}
			//失败原因
			String rePrint = "error.";
			//----------------更新intro---------------
			if (part.equals("intro")) {
				String intro = httpmsg.getHttpReq().getParameter("schema_intro");
				if (StringUtil.isStringWithLen(intro, 0)) {
					kc.setIntro(intro);
					//print在jsp前面，所以不用remove jsp
					msg.addData("print", "ok:"+intro);
					return super.act(msg);
				}
				rePrint = "intro error";
			}
			//----------------更新dao---------------
			else if(part.equals("dao")){
				String daoJson = httpmsg.getHttpReq().getParameter("schema_daojson");
				if (StringUtil.isStringWithLen(daoJson, 2)) {
					KObjDaoConfig kdc = KObjDaoConfig.newInstance(JSONTool.readJsonString(daoJson));
					if (kdc != null) {
						//更新KObjConfig
						kc.setDaoConfig(kdc);
						msg.addData("print", "ok:"+kdc.toMap());
						return super.act(msg);
					}
					rePrint = "KObjDaoConfig.newInstance error";
				}else{
					rePrint = "daoJson error";
				}
			}
			//----------------更新column---------------
			else if(part.equals("col_edit")){
				String colJson = httpmsg.getHttpReq().getParameter("schema_coljson");
				if (StringUtil.isStringWithLen(colJson, 2)) {
					KObjSchema ks = kc.getKobjSchema();
					if(ks.setColumn(JSONTool.readJsonString(colJson)) == 0){
						msg.addData("print", "ok:"+colJson);
						return super.act(msg);
					}
					rePrint = "ks.setColumn error";
				}else{
					rePrint = "schema_coljson error";
				}
			}
			//----------------更新index---------------
			else if(part.equals("index_edit")){
				String indexJson = httpmsg.getHttpReq().getParameter("schema_indexjson");
				if (StringUtil.isStringWithLen(indexJson, 2)) {
					KObjSchema ks = kc.getKobjSchema();
					if (ks.setIndex(JSONTool.readJsonString(indexJson),true) == 0) {
						msg.addData("print", "ok:"+indexJson);
						return super.act(msg);
					}
					rePrint = "setIndex error";
				}else{
					rePrint = "schema_indexjson error";
				}
			}
			//失败的情况
			msg.addData("re", "error");
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
		//新增KobjConfig
		else if(subact.equals("kc_add")){
			String key  = httpmsg.getHttpReq().getParameter("schema_key");
			String kcjson  = httpmsg.getHttpReq().getParameter("schema_kcjson");
			String rePrint = "para error";
			if (StringUtil.isStringWithLen(key, 2) && StringUtil.isStringWithLen(kcjson, 2)) {
				int re = KObjManager.createKObjConfigToDB(key, JSONTool.readJsonString(kcjson));
				if (re == 0) {
					msg.addData("print", "ok:"+kcjson);
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
				msg.addData("newIni", JSONTool.writeFormatedJsonString(KObjManager.getCurrentIni(), 5));
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
