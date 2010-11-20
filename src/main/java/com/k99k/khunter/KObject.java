/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * KObject:基础的可扩展对象,包含一个可扩展的属性列表,(带列表关系的参考KSObject).<br />
 * 直接json方式的toString方法
 * @author keel
 *
 */
public class KObject{
	

	public KObject() {
	}

	/**
	 * @param id
	 */
	public KObject(long id) {
		this.id = id;
	}

	/**
	 * @param id
	 * @param url
	 */
	public KObject(long id,  String url) {
		this.id = id;
		this.url = url;
	}

	

	/**
	 * @param id
	 * @param state
	 * @param level
	 * @param info
	 * @param createTime
	 * @param version
	 * @param name
	 * @param creatorName
	 * @param creatorId
	 * @param url
	 */
	public KObject(long id, int state, int level, String info,
			String createTime, int version, String name, String creatorName,
			long creatorId, String url) {
		this.id = id;
		this.state = state;
		this.level = level;
		this.info = info;
		this.createTime = createTime;
		this.version = version;
		this.name = name;
		this.creatorName = creatorName;
		this.creatorId = creatorId;
		this.url = url;
	}



	private long id;
	
	private int state = 0;
	
	private int level = 0;
	
	private String info = "";
	
	private String createTime = "";
	
	private int version = 1;
	
	private String name = "";
	
	private String creatorName = "";
	
	private long creatorId = 0;
	
	/**
	 * 用于标记本对象资源的url，每个KObject都可通过url进行访问和管理,也可作为key
	 */
	private String url = "";
	
	
	/**
	 * 属性hashMap,key为字段名
	 */
	final HashMap<String, Object> propMap = new HashMap<String, Object>(50);
	
	/**
	 *  内部属性hashMap
	 */
	final HashMap<String,Object> innerPropMap = new HashMap<String,Object>(50){{
		put("id", id);
		put("state", state);
		put("level", level);
		put("info", info);
		put("createTime", createTime);
		put("version", version);
		put("name", name);
		put("creatorName", creatorName);
		put("creatorId", creatorId);
		put("url", url);
		
	}};
	
	
	
	/**
	 * 获取外部属性名称的set
	 * @return Set<String>
	 */
	public final Set<String> getPropKeySet(){
		return propMap.keySet();
	}
	
	/**
	 * 添加/设置一个属性
	 * @param propName 属性名
	 * @param propValue 属性值，此时需要根据type进行转换
	 * @return Object 之前或新的属性值
	 */
	public final Object setProp(String propName,Object propValue) {
		return this.propMap.put(propName, propValue);
	}
	
	public final boolean containsProp(String key){
		return this.propMap.containsKey(key);
	}
	
	/**
	 * 删除属性
	 * @param key String
	 */
	public final Object removeProp(String key){
		return this.propMap.remove(key);
	}
	
	public final Map<String, Object> getPropMap(){
		return this.propMap;
	}
	
	/**
	 * 获取属性值,如果外部属性值中没有则到内部属性值中去找
	 * @param key String
	 * @return Object
	 */
	public final Object getProp(String key){
		Object o = this.propMap.get(key);
		if (o == null) {
			o = this.innerPropMap.get(key);
		}
		return o;
	}
	
	/**
	 * 由内部属性生成json,所有继承于本类的对象只需要覆盖此方法即可自动完成toString方法<br />
	 * 例如：
	 * <pre>
	   StringBuilder innerPropToJsonString() {
			StringBuilder sb = super.innerPropToJsonString();
			sb.append("\"nick\":\"").append(this.nick).append("\",");
			sb.append("\"x\":").append(this.x).append(",");
			return sb;
		}
		</pre>
	 * @return StringBuilder
	 */
	StringBuilder innerPropToJsonString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		//内置属性
		sb.append("\"id\":").append(this.id).append(",");
		sb.append("\"name\":\"").append(this.name).append("\",");
		sb.append("\"state\":").append(this.state).append(",");
		sb.append("\"level\":").append(this.level).append(",");
		sb.append("\"version\":").append(this.version).append(",");
		sb.append("\"creatorId\":").append(this.creatorId).append(",");
		sb.append("\"info\":\"").append(this.info).append("\",");
		sb.append("\"createTime\":\"").append(this.createTime).append("\",");
		sb.append("\"creatorName\":\"").append(this.creatorName).append("\",");
		sb.append("\"url\":\"").append(this.url).append("\",");
		return sb;
	}
	
	
	/**
	 * 以json方式输出toString
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb  = this.innerPropToJsonString();
		//处理动态属性
		for (Iterator<String> iterator = this.propMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			Object o = this.propMap.get(key);
			sb.append("\"").append(key).append("\":");
			if (o instanceof String || o instanceof Character) {
				sb.append("\"").append(o).append("\"");
			}else{
				sb.append(o);
			}
//			if (o instanceof Integer || o instanceof Long || o instanceof Float || o instanceof Double) {
//				sb.append(o);
//			} else {
//				sb.append("\"").append(o).append("\"");
//			}
			sb.append(",");
		}
		sb.append("\"_class\":\"").append(this.getClass().getName()).append("\"");
		//sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		return sb.toString();
	}
	

	/**
	 * @return the id
	 */
	public final long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the state
	 */
	public final int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public final void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the level
	 */
	public final int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public final void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the info
	 */
	public final String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public final void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the createTime
	 */
	public final String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the version
	 */
	public final int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public final void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the creatorName
	 */
	public final String getCreatorName() {
		return creatorName;
	}

	/**
	 * @param creatorName the creatorName to set
	 */
	public final void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	/**
	 * @return the creatorId
	 */
	public final long getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creatorId the creatorId to set
	 */
	public final void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}


	/**
	 * @return the url
	 */
	public final String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public final void setUrl(String url) {
		this.url = url;
	}

	
}
