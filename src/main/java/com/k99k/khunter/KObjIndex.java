/**
 * 
 */
package com.k99k.khunter;

/**
 * KObj索引
 * @author keel
 *
 */
public class KObjIndex {

	

	/**
	 * @param col
	 */
	public KObjIndex(String col) {
		super();
		this.col = col;
	}


	/**
	 * @param col
	 * @param asc
	 * @param intro
	 */
	public KObjIndex(String col, boolean asc, String intro) {
		super();
		this.col = col;
		this.asc = asc;
		this.intro = intro;
	}


	/**
	 * @param col
	 * @param asc
	 * @param type
	 * @param intro
	 * @param unique
	 */
	public KObjIndex(String col, boolean asc, String type, String intro,
			boolean unique) {
		super();
		this.col = col;
		this.asc = asc;
		this.type = type;
		this.intro = intro;
		this.unique = unique;
	}



	/**
	 * 索引字段名,复合索引时使用,号分开
	 */
	private String col;
	
	/**
	 * 顺序,true为1,false为-1
	 */
	private boolean asc = true;
	
	/**
	 * 索引类型,如唯一索引等
	 */
	private  String type = "normal";
	
	private String intro = "";
	
	/**
	 * 索引是否为unique
	 */
	private boolean unique = false;

	/**
	 * @return the col
	 */
	public final String getCol() {
		return col;
	}

	/**
	 * @param col the col to set
	 */
	public final void setCol(String col) {
		this.col = col;
	}

	/**
	 * @return the asc
	 */
	public final boolean isAsc() {
		return asc;
	}

	/**
	 * @param asc the asc to set
	 */
	public final void setAsc(boolean asc) {
		this.asc = asc;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the intro
	 */
	public final String getIntro() {
		return intro;
	}

	/**
	 * @param intro the intro to set
	 */
	public final void setIntro(String intro) {
		this.intro = intro;
	}

	/**
	 * @return the unique
	 */
	public final boolean isUnique() {
		return unique;
	}

	/**
	 * @param unique the unique to set
	 */
	public final void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	
	
}
