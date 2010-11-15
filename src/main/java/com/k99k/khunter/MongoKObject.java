/**
 * 
 */
package com.k99k.khunter;

import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;

/**
 * KObject的MongoDB对象，内部属性集成到属性propMap中
 * @author keel
 *
 */
public class MongoKObject extends KObject implements DBObject {
		
	/**
	 * 
	 */
	public MongoKObject() {
		initPropMap();
	}

	/**
	 * @param id
	 */
	public MongoKObject(long id) {
		super(id);
		initPropMap();
	}

	/**
	 * @param id
	 * @param url
	 */
	public MongoKObject(long id, String url) {
		super(id, url);
		initPropMap();
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
	public MongoKObject(long id, int state, int level, String info,
			String createTime, int version, String name, String creatorName,
			long creatorId, String url) {
		super(id, state, level, info, createTime, version, name, creatorName,
				creatorId, url);
		initPropMap();
	}
	
	private boolean isPartial = false;
	
	/**
	 * 内部属性集成到属性中
	 */
	private final void initPropMap(){
		//内部属性集成到属性中
		super.propMap.putAll(innerPropMap);
	}

	/* (non-Javadoc)
	 * @see com.mongodb.DBObject#isPartialObject()
	 */
	@Override
	public boolean isPartialObject() {
		return isPartial;
	}

	/* (non-Javadoc)
	 * @see com.mongodb.DBObject#markAsPartialObject()
	 */
	@Override
	public void markAsPartialObject() {
		isPartial = true;
	}


	/* (non-Javadoc)
	 * @see org.bson.BSONObject#containsField(java.lang.String)
	 */
	@Override
	public boolean containsField(String s) {
		return super.getPropKeySet().contains(s);
	}

	/**
	 * @deprecated
	 * @see org.bson.BSONObject#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String s) {
		return super.getPropKeySet().contains(s);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		return super.getProp(key);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return super.getPropKeySet();
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object put(String key, Object v) {
		
		return super.setProp(key, v);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#putAll(org.bson.BSONObject)
	 */
	@Override
	public void putAll(BSONObject o) {
		super.propMap.putAll(o.toMap());
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map m) {
		super.propMap.putAll(m);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#removeField(java.lang.String)
	 */
	@Override
	public Object removeField(String key) {
		//内部属性无法移除
		if (super.innerPropMap.containsKey(key)) {
			return null;
		}
		return super.removeProp(key);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#toMap()
	 */
	@Override
	public Map<String , Object> toMap() {
		return super.getPropMap();
	}

}
