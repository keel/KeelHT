/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;

import com.k99k.tools.JSONTool;

/**
 * KObjConfig
 * @author keel
 *
 */
public class KObjConfig {

	public KObjConfig() {
	}
	
	private String kobjName;
	
	private String intro;
	
	private HashMap<String,Object> daoConfig;
	
	private KObjSchema kobjSchema;
	
	
	/**
	 * KObjConfig的Json化HashMap，注意此Map不含kobjName
	 * @return
	 */
	public HashMap<String,Object> toMap() {
		 HashMap<String,Object> map = this.kobjSchema.toMap();
		 map.put("intro", this.intro);
		 map.put("dao", daoConfig);
		 return map;
	}
	
	/**
	 * 验证Dao的配置
	 * @param map
	 * @return
	 */
	public boolean checkDaoMap(HashMap<String, Object> map){
		if(!JSONTool.checkMapTypes(map,new String[]{"daoName","newDaoName"},new Class[]{String.class,String.class})){
			return false;
		}
		//如果create为new,则必须有tableName字段
		if (map.get("newDaoName").toString().trim().equals("")) {
			if ((!map.containsKey("tableName")) || map.get("tableName").toString().length()<=0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the kobjName
	 */
	public final String getKobjName() {
		return kobjName;
	}

	/**
	 * @param kobjName the kobjName to set
	 */
	public final void setKobjName(String kobjName) {
		this.kobjName = kobjName;
	}

	/**
	 * @return the daoConfig
	 */
	public final HashMap<String, Object> getDaoConfig() {
		return daoConfig;
	}

	/**
	 * @param daoConfig the daoConfig to set
	 */
	public final boolean setDaoConfig(HashMap<String, Object> daoConfig) {
		if (!checkDaoMap(daoConfig)) {
			return false;
		}
		this.daoConfig = daoConfig;
		return true;
	}
	
	

	/**
	 * @return the kobjSchema
	 */
	public final KObjSchema getKobjSchema() {
		return kobjSchema;
	}

	/**
	 * @param kobjSchema the kobjSchema to set
	 */
	public final void setKobjSchema(KObjSchema kobjSchema) {
		this.kobjSchema = kobjSchema;
	}

	/**
	 * @return the intro
	 */
	public final String getIntro() {
		return intro;
	}

	/**
	 * @param intro the intro to set
	 */
	public final void setIntro(String intro) {
		this.intro = intro;
	}

}
