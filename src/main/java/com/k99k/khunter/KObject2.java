/**
 * 
 */
package com.k99k.khunter;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 成员变量最多不能超过100个
 * @author keel
 *
 */
public class KObject2 {

	public KObject2(int maxProp) {
		this.propLength = maxProp;
		this.props = new Object[maxProp];
		this.propMap = new HashMap<String,Integer>(maxProp);
		propMap.put("id", 1);
		
	}
	
	/**
	 * 数组长度
	 */
	int propLength;
	
	/**
	 * 属性数组,从1开始,0表示不存在
	 */
	private Object[] props;
	
	private Map<String,Integer> propMap;
	
	/**
	 * 当前对象最大的属性数组位置
	 * <p>
	 * <strong>注意<strong>:其默认值需要根据对象的默认属性数调整
	 * <p>
	 */
	private int max = 1;
	
	public void setId(long id){
		props[1] = id;
	}
	
	public long getId(){
		return Long.parseLong(props[1].toString());
	}
	
	public Object getPropByPosition(int p){
		return props[p];
	}
	
	public void setPropByPosition(int p,Object value){
		props[p] = value;
	}
	
	public Object getProp(String key){
		if (propMap.get(key) == null) {
			return null;
		}
		return props[propMap.get(key)];
	}

	public void addProp(String key,Object value){
		propMap.put(key, ++max);
		props[max] = value;
	}
	
	
	public void setProp(String key,Object value){
		if (propMap.get(key) == null) {
			this.addProp(key, value);
		}
		props[propMap.get(key)] = value;
	}

	public int getPropCount(){
		return this.max;
	}
	
	public static void main(String[] args) {
		KObject2 kk = new KObject2(100);
		kk.setId(23);
		System.out.println(kk.getId());
		System.out.println(kk.getPropByPosition(1));
		System.out.println(kk.getProp("id"));
		System.out.println("----");
		kk.setId(233);
		System.out.println(kk.getId());
		System.out.println(kk.getPropByPosition(1));
		System.out.println(kk.getProp("id"));
		System.out.println("----");
		kk.setProp("id", 690);
		System.out.println(kk.getId());
		System.out.println(kk.getPropByPosition(1));
		System.out.println(kk.getProp("id"));
		System.out.println("----");
		kk.setPropByPosition(1, 690);
		System.out.println(kk.getId());
		System.out.println(kk.getPropByPosition(1));
		System.out.println(kk.getProp("id"));
		System.out.println("----");
		kk.addProp("name", "keel");
		System.out.println(kk.getProp("name"));
		System.out.println("----");
		kk.setProp("name3", "keel");
		System.out.println(kk.getProp("name3"));
		System.out.println(kk.getPropCount());
	}
	
	
	
}
