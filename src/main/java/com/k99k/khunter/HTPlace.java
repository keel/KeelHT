/**
 * 
 */
package com.k99k.khunter;

/**
 * 地点
 * @author keel
 *
 */
public class HTPlace extends KObject{

	public HTPlace() {
		super();
		propMap.put("type", 0);
		propMap.put("x", 0);
		propMap.put("y", 0);
		propMap.put("z", 0);
		propMap.put("special", "");
		propMap.put("building", "");
		propMap.put("camp", "");
	}

//	/**
//	 * x坐标
//	 */
//	private int x;
//	
//	/**
//	 * y坐标
//	 */
//	private int y;
//	
//	/**
//	 * z坐标
//	 */
//	private int z;
//	
//	/**
//	 * 类型,可以是多个类型(质数)合成的复值,也可以是基本类型
//	 */
//	private int type;
//	
//	/**
//	 * 建筑物ID
//	 */
//	private String building;
//	
//	/**
//	 * 特殊作用,暂以String方式体现,可以是集合
//	 */
//	private String special;
//	
//	/**
//	 * 阵营
//	 */
//	private String camp;
	
	public final String getCamp() {
		return getStringByName("camp");
	}

	public final void setCamp(String camp) {
		this.propMap.put("camp", camp);
	}

	public final String getBuilding() {
		return getStringByName("building");
	}

	public final void setBuilding(String building) {
		this.propMap.put("building", building);
	}

	public final String getSpecial() {
		return getStringByName("special");
	}

	public final void setSpecial(String special) {
		this.propMap.put("special", special);
	}

	public final int getType() {
		return getIntByName("type");
	}

	public final void setType(int type) {
		this.propMap.put("type", type);
	}

	public final int getZ() {
		return getIntByName("z");
	}

	public final void setZ(int z) {
		this.propMap.put("z", z);
	}

	public final int getY() {
		return getIntByName("y");
	}

	public final void setY(int y) {
		this.propMap.put("y", y);
	}

	public final int getX() {
		return getIntByName("x");
	}

	public final void setX(int x) {
		this.propMap.put("x", x);
	}
	
}
