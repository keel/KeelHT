/**
 * 
 */
package com.k99k.khunter;

/**
 * 宠物
 * @author keel
 *
 */
public class HTPet extends KObject{

	public HTPet() {
		super();
		propMap.put("type", 0);
		propMap.put("age", 0);
		propMap.put("ack", 0);
		propMap.put("def", 0);
		propMap.put("special", "");
		propMap.put("hp", 0);
		propMap.put("space", 0);
		propMap.put("price", 0);
	
	}

//	/**
//	 * 物品类型,可以是多个类型(质数)合成的复值,也可以是基本类型
//	 */
//	private int type;
//	
//	/**
//	 * 功能
//	 */
//	private int age;
//	
//	/**
//	 * 攻击力
//	 */
//	private int ack;
//	
//	/**
//	 * 防御力
//	 */
//	private int def;
//	
//	/**
//	 * 特殊能力,暂以String方式体现,可以是集合
//	 */
//	private String special;
//	
//	/**
//	 * 宠物的HP值
//	 */
//	private int hp;
//	
//	/**
//	 * 基本价格
//	 */
//	private int price;
//	
//	/**
//	 * 占用空间
//	 */
//	private int space;
	

	public final int getHp() {
		return getIntByName("hp");
	}

	public final void setHp(int hp) {
		this.propMap.put("hp", hp);
	}

	public final int getPrice() {
		return getIntByName("price");
	}

	public final void setPrice(int price) {
		this.propMap.put("price", price);
	}

	public final int getDef() {
		return getIntByName("def");
	}

	public final void setDef(int def) {
		this.propMap.put("def", def);
	}

	public final int getAck() {
		return getIntByName("ack");
	}

	public final void setAck(int ack) {
		this.propMap.put("ack", ack);
	}

	public final int getAge() {
		return getIntByName("age");
	}

	public final void setAge(int age) {
		this.propMap.put("age", age);
	}

	public final String getSpecial() {
		return getStringByName("special");
	}

	public final void setSpecial(String special) {
		this.propMap.put("special", special);
	}

	public final int getSpace() {
		return getIntByName("space");
	}

	public final void setSpace(int space) {
		this.propMap.put("space", space);
	}

	public final int getType() {
		return getIntByName("type");
	}

	public final void setType(int type) {
		this.propMap.put("type", type);
	}
	
	
}
