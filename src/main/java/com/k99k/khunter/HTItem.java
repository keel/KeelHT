/**
 * 
 */
package com.k99k.khunter;

/**
 * 物品
 * @author keel
 *
 */
public class HTItem extends KObject{

	public HTItem() {
		super();
		this.propMap.put("type", 0);
		this.propMap.put("function", "");
		this.propMap.put("ack", 0);
		this.propMap.put("def", 0);
		this.propMap.put("special", "");
		this.propMap.put("costHP", 0);
		this.propMap.put("space", 0);
		this.propMap.put("price", 0);
	}

//	/**
//	 * 物品类型,可以是多个类型(质数)合成的复值,也可以是基本类型
//	 */
//	private int type;
//	
//	/**
//	 * 功能
//	 */
//	private String function;
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
//	 * 对HP的消耗量
//	 */
//	private int costHP;
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

	public final int getCostHP() {
		return getIntByName("costHP");
	}

	public final void setCostHP(int costHP) {
		this.propMap.put("costHP", costHP);
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

	public final String getFunction() {
		return getStringByName("function");
	}

	public final void setFunction(String function) {
		this.propMap.put("function", function);
	}
	
	
	public static void main(String[] args) {
		HTItem i = new HTItem();
		i.setId(33323);
		i.setProp("haha", 3);
		i.setSpace(5);
		System.out.println(i);
		
	}
	
}
