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
	}

	/**
	 * 物品类型,可以是多个类型(质数)合成的复值,也可以是基本类型
	 */
	private int type;
	
	/**
	 * 功能
	 */
	private String function;
	
	/**
	 * 攻击力
	 */
	private int ack;
	
	/**
	 * 防御力
	 */
	private int def;
	
	/**
	 * 特殊能力,暂以String方式体现,可以是集合
	 */
	private String special;
	
	/**
	 * 对HP的消耗量
	 */
	private int costHP;
	
	/**
	 * 基本价格
	 */
	private int price;
	
	/**
	 * 占用空间
	 */
	private int space;
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.KObject#innerPropToJsonString()
	 */
	@Override
	StringBuilder innerPropToJsonString() {
		StringBuilder sb = super.innerPropToJsonString();
		sb.append("\"type\":").append(this.type).append(",");
		sb.append("\"function\":\"").append(this.function).append("\",");
		sb.append("\"ack\":").append(this.ack).append(",");
		sb.append("\"def\":").append(this.def).append(",");
		sb.append("\"special\":\"").append(this.special).append("\",");
		sb.append("\"costHP\":").append(this.costHP).append(",");
		sb.append("\"space\":").append(this.space).append(",");
		sb.append("\"price\":").append(this.price).append(",");
		return sb;
	}

	/**
	 * @return the type
	 */
	public final int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the function
	 */
	public final String getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public final void setFunction(String function) {
		this.function = function;
	}

	/**
	 * @return the ack
	 */
	public final int getAck() {
		return ack;
	}

	/**
	 * @param ack the ack to set
	 */
	public final void setAck(int ack) {
		this.ack = ack;
	}

	/**
	 * @return the def
	 */
	public final int getDef() {
		return def;
	}

	/**
	 * @param def the def to set
	 */
	public final void setDef(int def) {
		this.def = def;
	}

	/**
	 * @return the special
	 */
	public final String getSpecial() {
		return special;
	}

	/**
	 * @param special the special to set
	 */
	public final void setSpecial(String special) {
		this.special = special;
	}

	/**
	 * @return the costHP
	 */
	public final int getCostHP() {
		return costHP;
	}

	/**
	 * @param costHP the costHP to set
	 */
	public final void setCostHP(int costHP) {
		this.costHP = costHP;
	}

	/**
	 * @return the price
	 */
	public final int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public final void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the space
	 */
	public final int getSpace() {
		return space;
	}

	/**
	 * @param space the space to set
	 */
	public final void setSpace(int space) {
		this.space = space;
	}
	
	
	public static void main(String[] args) {
		HTItem i = new HTItem();
		i.setId(33323);
		i.setProp("haha", 3);
		i.setSpace(5);
		System.out.println(i);
		
	}
	
}
