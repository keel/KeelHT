/**
 * 
 */
package com.k99k.khunter;

import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;

/**
 * 把KObject包装成Mongodb可用的DBObject
 * @author keel
 *
 */
public class MongoWrapper1 implements DBObject {

	/**
	 * 包装KObject,将其innerPropMap全部放到propMap中
	 * @param kobj KObject
	 */
	public MongoWrapper1(KObject1 kobject) {
		this.kobj = kobject;
		this._id = kobj.getId();
		this.kobj.propMap.put("_id", this._id);
		this.kobj.propMap.putAll(this.kobj.innerPropMap);
	}
	
	/**
	 * 用于mongodb的_id字段
	 */
	private long _id;
	
	/**
	 * 实际被包装的KObject
	 */
	private KObject1 kobj;
	
	private boolean isPartial = false;
	
	
	
	/**
	 * @return the _id
	 */
	public final long get_id() {
		return _id;
	}

	/**
	 * @param id the _id to set
	 */
	public final void set_id(long id) {
		_id = id;
		this.kobj.setId(id);
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
		this.isPartial = true;
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#containsField(java.lang.String)
	 */
	@Override
	public boolean containsField(String arg0) {
		return kobj.containsProp(arg0);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String arg0) {
		return kobj.containsProp(arg0);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#get(java.lang.String)
	 */
	@Override
	public Object get(String arg0) {
		return kobj.getProp(arg0);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return kobj.getPropKeySet();
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object put(String key, Object v) {
		return kobj.setProp(key, v);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#putAll(org.bson.BSONObject)
	 */
	@Override
	public void putAll(BSONObject arg0) {
		kobj.propMap.putAll(arg0.toMap());
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map arg0) {
		kobj.propMap.putAll(arg0);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#removeField(java.lang.String)
	 */
	@Override
	public Object removeField(String arg0) {
		return kobj.removeProp(arg0);
	}

	/* (non-Javadoc)
	 * @see org.bson.BSONObject#toMap()
	 */
	@Override
	public Map<String, ?> toMap() {
		return kobj.getPropMap();
	}

}
