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

	/**
	 * 
	 */
	public HTUser() {
	}
	
	/**
	 * 密码
	 */
	private String upwd;
	
	/**
	 * imei设备号
	 */
	private String imei;
	
	private String ip;
	
	private String nick;
	
	private int sex;
	
	/**
	 * 同步id
	 */
	private String synId;
	
	private String email;
	
	/**
	 * 头像ID
	 */
	private String icon;
	
	/**
	 * 体力hp值
	 */
	private int hp;
	
	/**
	 * 阵营
	 */
	private String camp;
	
	private String lastLogin;
	
	/**
	 * 钱
	 */
	private int gold;
	
	/**
	 * 勋章
	 */
	private int medal;
	
	/**
	 * 荣誉值,cash 购买
	 */
	private int honor;
	
	/**
	 * 位置x坐标
	 */
	private int x;
	
	/**
	 * 位置y坐标
	 */
	private int y;
	
	/**
	 * 位置z坐标
	 */
	private int z;
	
	/**
	 * 军阶
	 */
	private int rank;
	
	private int maxWareHouse;
	
	private int curWareHouse;
	
	private Object equips;
	
	private Object pets;
	
	private Object dress;
	
	private Object friends;

	/* (non-Javadoc)
	 * @see com.k99k.khunter.KObject#innerPropToJsonString()
	 */
	@Override
	StringBuilder innerPropToJsonString() {
		StringBuilder sb = super.innerPropToJsonString();
		sb.append("\"upwd\":\"").append(this.upwd).append("\",");
		sb.append("\"imei\":\"").append(this.imei).append("\",");
		sb.append("\"ip\":\"").append(this.ip).append("\",");
		sb.append("\"nick\":\"").append(this.nick).append("\",");
		sb.append("\"sex\":").append(this.sex).append(",");
		sb.append("\"synId\":\"").append(this.synId).append("\",");
		sb.append("\"email\":\"").append(this.email).append("\",");
		sb.append("\"icon\":\"").append(this.icon).append("\",");
		sb.append("\"hp\":").append(this.hp).append(",");
		sb.append("\"camp\":\"").append(this.camp).append("\",");
		sb.append("\"lastLogin\":\"").append(this.lastLogin).append("\",");
		sb.append("\"gold\":").append(this.gold).append(",");
		sb.append("\"medal\":").append(this.medal).append(",");
		sb.append("\"x\":").append(this.x).append(",");
		sb.append("\"y\":").append(this.y).append(",");
		sb.append("\"z\":").append(this.z).append(",");
		sb.append("\"rank\":").append(this.rank).append(",");
		sb.append("\"maxWareHouse\":").append(this.maxWareHouse).append(",");
		sb.append("\"curWareHouse\":").append(this.curWareHouse).append(",");
		return sb;
	}
	
	
	/**
	 * @return the upwd
	 */
	public final String getUpwd() {
		return upwd;
	}


	/**
	 * @param upwd the upwd to set
	 */
	public final void setUpwd(String upwd) {
		this.upwd = upwd;
	}


	/**
	 * @return the imei
	 */
	public final String getImei() {
		return imei;
	}


	/**
	 * @param imei the imei to set
	 */
	public final void setImei(String imei) {
		this.imei = imei;
	}


	/**
	 * @return the ip
	 */
	public final String getIp() {
		return ip;
	}


	/**
	 * @param ip the ip to set
	 */
	public final void setIp(String ip) {
		this.ip = ip;
	}


	/**
	 * @return the nick
	 */
	public final String getNick() {
		return nick;
	}


	/**
	 * @param nick the nick to set
	 */
	public final void setNick(String nick) {
		this.nick = nick;
	}


	/**
	 * @return the sex
	 */
	public final int getSex() {
		return sex;
	}


	/**
	 * @param sex the sex to set
	 */
	public final void setSex(int sex) {
		this.sex = sex;
	}


	/**
	 * @return the synId
	 */
	public final String getSynId() {
		return synId;
	}


	/**
	 * @param synId the synId to set
	 */
	public final void setSynId(String synId) {
		this.synId = synId;
	}


	/**
	 * @return the email
	 */
	public final String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public final void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the icon
	 */
	public final String getIcon() {
		return icon;
	}


	/**
	 * @param icon the icon to set
	 */
	public final void setIcon(String icon) {
		this.icon = icon;
	}


	/**
	 * @return the hp
	 */
	public final int getHp() {
		return hp;
	}


	/**
	 * @param hp the hp to set
	 */
	public final void setHp(int hp) {
		this.hp = hp;
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


	/**
	 * @return the lastLogin
	 */
	public final String getLastLogin() {
		return lastLogin;
	}


	/**
	 * @param lastLogin the lastLogin to set
	 */
	public final void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}


	/**
	 * @return the gold
	 */
	public final int getGold() {
		return gold;
	}


	/**
	 * @param gold the gold to set
	 */
	public final void setGold(int gold) {
		this.gold = gold;
	}


	/**
	 * @return the medal
	 */
	public final int getMedal() {
		return medal;
	}


	/**
	 * @param medal the medal to set
	 */
	public final void setMedal(int medal) {
		this.medal = medal;
	}


	/**
	 * @return the honor
	 */
	public final int getHonor() {
		return honor;
	}


	/**
	 * @param honor the honor to set
	 */
	public final void setHonor(int honor) {
		this.honor = honor;
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
	 * @return the rank
	 */
	public final int getRank() {
		return rank;
	}


	/**
	 * @param rank the rank to set
	 */
	public final void setRank(int rank) {
		this.rank = rank;
	}


	/**
	 * @return the maxWareHouse
	 */
	public final int getMaxWareHouse() {
		return maxWareHouse;
	}


	/**
	 * @param maxWareHouse the maxWareHouse to set
	 */
	public final void setMaxWareHouse(int maxWareHouse) {
		this.maxWareHouse = maxWareHouse;
	}


	/**
	 * @return the curWareHouse
	 */
	public final int getCurWareHouse() {
		return curWareHouse;
	}


	/**
	 * @param curWareHouse the curWareHouse to set
	 */
	public final void setCurWareHouse(int curWareHouse) {
		this.curWareHouse = curWareHouse;
	}


	/**
	 * @return the equips
	 */
	public final Object getEquips() {
		return equips;
	}


	/**
	 * @param equips the equips to set
	 */
	public final void setEquips(Object equips) {
		this.equips = equips;
	}


	/**
	 * @return the pets
	 */
	public final Object getPets() {
		return pets;
	}


	/**
	 * @param pets the pets to set
	 */
	public final void setPets(Object pets) {
		this.pets = pets;
	}


	/**
	 * @return the dress
	 */
	public final Object getDress() {
		return dress;
	}


	/**
	 * @param dress the dress to set
	 */
	public final void setDress(Object dress) {
		this.dress = dress;
	}


	/**
	 * @return the friends
	 */
	public final Object getFriends() {
		return friends;
	}


	/**
	 * @param friends the friends to set
	 */
	public final void setFriends(Object friends) {
		this.friends = friends;
	}


	public static void main(String[] args) {
		HTUser u = new HTUser();
		u.setId(2323);
		u.setUpwd("123456");
		u.setProp("newprop", "just test prop value");
		System.out.println(u);
	}
	
	
}
