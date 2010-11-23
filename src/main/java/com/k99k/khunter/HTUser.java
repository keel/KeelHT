/**
 * 
 */
package com.k99k.khunter;

/**
 * HTUser 玩家
 * @author keel
 *
 */
public class HTUser extends KObject{

	public HTUser() {
		super();
		this.propMap.put("pwd", "");
		this.propMap.put("imei", "");
		this.propMap.put("ip", "");
		this.propMap.put("nick", "");
		this.propMap.put("sex", 0);
		this.propMap.put("synId", "");
		this.propMap.put("email", "");
		this.propMap.put("icon", "");
		this.propMap.put("hp", 0);
		this.propMap.put("camp", "");
		this.propMap.put("lastLogin", "");
		this.propMap.put("gold", 0);
		this.propMap.put("medal", 0);
		this.propMap.put("honor", 0);
		this.propMap.put("x", 0);
		this.propMap.put("y", 0);
		this.propMap.put("z", 0);
		this.propMap.put("rank", 0);
		this.propMap.put("maxWareHouse", 0);
		this.propMap.put("curWareHouse", 0);
		this.propMap.put("equips", null);
		this.propMap.put("pets", null);
		this.propMap.put("dress", null);
		this.propMap.put("friends", null);
		
	}
/*	

	 * 密码

	private String upwd;
	

	 * imei设备号
	
	private String imei;
	
	private String ip;
	
	private String nick;
	
	private int sex;
	
	
	 * 同步id
	
	private String synId;
	
	private String email;
	
	
	 * 头像ID
	
	private String icon;
	
	
	 * 体力hp值
	
	private int hp;
	
	
	 * 阵营
	
	private String camp;
	
	private String lastLogin;
	
	
	 * 钱
	
	private int gold;
	
	
	 * 勋章
	
	private int medal;
	
	
	 * 荣誉值,cash 购买
	
	private int honor;
	
	
	 * 位置x坐标
	
	private int x;
	
	
	 * 位置y坐标
	
	private int y;
	
	
	 * 位置z坐标
	
	private int z;
	
	
	 * 军阶
	
	private int rank;
	
	private int maxWareHouse;
	
	private int curWareHouse;
	
	private Object equips;
	
	private Object pets;
	
	private Object dress;
	
	private Object friends;
*/
	public final String getIcon() {
		return getStringByName("icon");
	}

	public final void setIcon(String icon) {
		this.propMap.put("icon", icon);
	}

	public final int getSex() {
		return getIntByName("sex");
	}

	public final void setSex(int sex) {
		this.propMap.put("sex", sex);
	}

	public final int getHp() {
		return getIntByName("hp");
	}

	public final void setHp(int hp) {
		this.propMap.put("hp", hp);
	}

	public final int getHonor() {
		return getIntByName("honor");
	}

	public final void setHonor(int honor) {
		this.propMap.put("honor", honor);
	}

	public final int getCurWareHouse() {
		return getIntByName("curWareHouse");
	}

	public final void setCurWareHouse(int curWareHouse) {
		this.propMap.put("curWareHouse", curWareHouse);
	}

	public final String getImei() {
		return getStringByName("imei");
	}

	public final void setImei(String imei) {
		this.propMap.put("imei", imei);
	}

	public final int getMaxWareHouse() {
		return getIntByName("maxWareHouse");
	}

	public final void setMaxWareHouse(int maxWareHouse) {
		this.propMap.put("maxWareHouse", maxWareHouse);
	}

	public final Object getEquips() {
		return getObjectByName("equips");
	}

	public final void setEquips(Object equips) {
		this.propMap.put("equips", equips);
	}

	public final String getSynId() {
		return getStringByName("synId");
	}

	public final void setSynId(String synId) {
		this.propMap.put("synId", synId);
	}

	public final String getIp() {
		return getStringByName("ip");
	}

	public final void setIp(String ip) {
		this.propMap.put("ip", ip);
	}

	public final int getRank() {
		return getIntByName("rank");
	}

	public final void setRank(int rank) {
		this.propMap.put("rank", rank);
	}

	public final String getPwd() {
		return getStringByName("pwd");
	}

	public final void setPwd(String pwd) {
		this.propMap.put("pwd", pwd);
	}

	public final String getCamp() {
		return getStringByName("camp");
	}

	public final void setCamp(String camp) {
		this.propMap.put("camp", camp);
	}

	public final String getLastLogin() {
		return getStringByName("lastLogin");
	}

	public final void setLastLogin(String lastLogin) {
		this.propMap.put("lastLogin", lastLogin);
	}

	public final Object getFriends() {
		return getObjectByName("friends");
	}

	public final void setFriends(Object friends) {
		this.propMap.put("friends", friends);
	}

	public final String getNick() {
		return getStringByName("nick");
	}

	public final void setNick(String nick) {
		this.propMap.put("nick", nick);
	}

	public final String getEmail() {
		return getStringByName("email");
	}

	public final void setEmail(String email) {
		this.propMap.put("email", email);
	}

	public final Object getDress() {
		return getObjectByName("dress");
	}

	public final void setDress(Object dress) {
		this.propMap.put("dress", dress);
	}

	public final Object getPets() {
		return getObjectByName("pets");
	}

	public final void setPets(Object pets) {
		this.propMap.put("pets", pets);
	}

	public final int getGold() {
		return getIntByName("gold");
	}

	public final void setGold(int gold) {
		this.propMap.put("gold", gold);
	}

	public final long getZ() {
		return getLongByName("z");
	}

	public final void setZ(long z) {
		this.propMap.put("z", z);
	}

	public final int getMedal() {
		return getIntByName("medal");
	}

	public final void setMedal(int medal) {
		this.propMap.put("medal", medal);
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




	public static void main(String[] args) {
		HTUser u = new HTUser();
		u.setId(2323);
		u.setPwd("123456");
		u.setProp("newprop", "just test prop value");
		System.out.println(u);
	}
	
	
}
