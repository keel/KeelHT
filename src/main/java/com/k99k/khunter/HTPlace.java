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
	}

	/**
	 * x坐标
	 */
	private int x;
	
	/**
	 * y坐标
	 */
	private int y;
	
	/**
	 * z坐标
	 */
	private int z;
	
	/**
	 * 类型,可以是多个类型(质数)合成的复值,也可以是基本类型
	 */
	private int type;
	
	/**
	 * 建筑物ID
	 */
	private String building;
	
	/**
	 * 特殊作用,暂以String方式体现,可以是集合
	 */
	private String special;
	
	/**
	 * 阵营
	 */
	private String camp;
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.KObject#innerPropToJsonString()
	 */
	@Override
	StringBuilder innerPropToJsonString() {
		StringBuilder sb = super.innerPropToJsonString();
		sb.append("\"type\":").append(this.type).append(",");
		sb.append("\"x\":").append(this.x).append(",");
		sb.append("\"y\":").append(this.y).append(",");
		sb.append("\"z\":").append(this.z).append(",");
		sb.append("\"special\":\"").append(this.special).append("\",");
		sb.append("\"building\":\"").append(this.building).append("\",");
		sb.append("\"camp\":\"").append(this.camp).append("\",");
		return sb;
	}

	
	
	
	/**
	 * @return the x
	 */
	public final int getX() {
		return x;
	}




	/**
	 * @param x the x to set
	 */
	public final void setX(int x) {
		this.x = x;
	}




	/**
	 * @return the y
	 */
	public final int getY() {
		return y;
	}




	/**
	 * @param y the y to set
	 */
	public final void setY(int y) {
		this.y = y;
	}




	/**
	 * @return the z
	 */
	public final int getZ() {
		return z;
	}




	/**
	 * @param z the z to set
	 */
	public final void setZ(int z) {
		this.z = z;
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
	 * @return the building
	 */
	public final String getBuilding() {
		return building;
	}




	/**
	 * @param building the building to set
	 */
	public final void setBuilding(String building) {
		this.building = building;
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
	 * @return the camp
	 */
	public final String getCamp() {
		return camp;
	}




	/**
	 * @param camp the camp to set
	 */
	public final void setCamp(String camp) {
		this.camp = camp;
	}

	
}
