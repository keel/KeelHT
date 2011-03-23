/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;

import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;

/**
 * KObjConfig中的daoConfig
 * @author keel
 *
 */
public class KObjDaoConfig {

	private KObjDaoConfig() {
	}
	
	/**
	 * 创建一个新的KObjDaoConfig
	 * @param map HashMap<String,Object>
	 * @return 如果创建失败返回null
	 */
	@SuppressWarnings("unchecked")
	public static final KObjDaoConfig newInstance(HashMap<String,Object> map){
		KObjDaoConfig kdc = new KObjDaoConfig();
		if(!JSONTool.checkMapTypes(map,new String[]{"daoName","newDaoName"},new Class[]{String.class,String.class})){
			return null;
		}
		kdc.daoName = (String) map.get("daoName");
		kdc.newDaoName = (String) map.get("newDaoName");
		//如果newDaoName不为空,则必须有tableName字段
		if (!map.get("newDaoName").toString().trim().equals("")) {
			if ((!map.containsKey("tableName")) || map.get("tableName").toString().length()<=0) {
				return null;
			}
			kdc.tableName = (String) map.get("tableName");
		}
		Object o = map.get("props");
		if (o != null && o instanceof HashMap) {
			HashMap<String,Object> m = (HashMap<String, Object>) o;
			Object ido = m.get("id");
			Object typeo = m.get("type");
			if (StringUtil.isDigits(ido.toString())) {
				kdc.setPropId(Integer.parseInt(ido.toString()));
			}
			if (typeo != null && typeo instanceof String) {
				kdc.setPropType(typeo.toString());
			}
		}
		
		return kdc;
	}
	
	private String daoName;
	
	private String newDaoName;
	
	private String tableName;
	
	private Props props = new Props();
	
	class Props{
		public Props(){};
		int id;
		String type;
		
		HashMap<String,Object> toMap(){
			HashMap<String,Object> m = new HashMap<String, Object>();
			m.put("id", this.id);
			m.put("type", this.type);
			return m;
		}
	}
	
	public final HashMap<String,Object> toMap(){
		HashMap<String,Object> m = new HashMap<String, Object>();
		m.put("daoName", this.daoName);
		m.put("newDaoName", this.newDaoName);
		m.put("tableName", this.tableName);
		m.put("props", this.props.toMap());
		return m;
	}
	
	public final void setPropId(int id){
		this.props.id = id;
	}
	
	public final void setPropType(String type){
		this.props.type = type;
	}

	/**
	 * @return the daoName
	 */
	public final String getDaoName() {
		return daoName;
	}

	/**
	 * @param daoName the daoName to set
	 */
	public final void setDaoName(String daoName) {
		this.daoName = daoName;
	}

	/**
	 * @return the newDaoName
	 */
	public final String getNewDaoName() {
		return newDaoName;
	}

	/**
	 * @param newDaoName the newDaoName to set
	 */
	public final void setNewDaoName(String newDaoName) {
		this.newDaoName = newDaoName;
	}

	/**
	 * @return the tableName
	 */
	public final String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public final void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
