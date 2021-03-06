/**
 * 
 */
package com.k99k.wb.acts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KFilter;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.khunter.KObject;
import com.k99k.khunter.dao.WBUserDao;
import com.k99k.tools.JSONTool;
import com.k99k.tools.StringUtil;
import com.k99k.tools.encrypter.Base64Coder;

/**
 * wb用户类,获取用户信息,修改用户信息
 * (新增用户请参考WBReg)
 * @author keel
 *
 */
public class WBUser extends Action {

	/**
	 * @param name
	 */
	public WBUser(String name) {
		super(name);
	}

	/**
	 * wbUserDao,初始化时获取
	 */
	private static WBUserDao wbUserDao = null;
	
	private static KObjSchema wbUserSchema = null;
	
	private int pageSize = 20;
	
	/**
	 * 
	 * @param req
	 * @return KObject User
	 */
	public static final KObject authCookie(HttpServletRequest req){
		String reqUserStr = WBUrl.getCookieValue(req.getCookies(),"wbu","");
		boolean hasName = (!reqUserStr.equals(""));
		if (!hasName) {
			return null;
		}
		String[] u_p = Base64Coder.decodeString(reqUserStr).split(":");
		if (u_p.length >= 2) {
			KObject wbUser = WBLogin.auth(u_p[0], u_p[1]);
			if (wbUser != null) {
				return wbUser;
			}
		}
		return null;
	}
	
	/**
	 * 此act不一定使用,直接使用静态方法即可
	 * 
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		String subact = KFilter.actPath(msg, 2, "fans");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String re = null;
		//验证用户请求是否合法
		KObject user = WBLogin.cookieAuth(httpmsg);
		if (user == null) {
			JOut.err(401, httpmsg);
			return super.act(msg);
		}
		String p_str = httpmsg.getHttpReq().getParameter("p");
		String pz_str = httpmsg.getHttpReq().getParameter("pz");
		int page = StringUtil.isDigits(p_str)?Integer.parseInt(p_str):1;
		int pz = StringUtil.isDigits(pz_str)?Integer.parseInt(pz_str):this.pageSize;
		if (subact.equals("fans")) {
			re = JSONTool.writeJsonString(WBUserDao.getFans(user.getId(), page, pz));
		}else if(subact.equals("follows")){
			re = JSONTool.writeJsonString(WBUserDao.getFollows(user.getId(), page, pz));
		}
		if (re == null || re.equals("null")) {
			re = "[]";
		}
		msg.addData("[print]", re);
		return super.act(msg);
	}

	/**
	 * 按名称查找WBUser,静态方法,可直接调用
	 * @param name String
	 * @return KObject
	 */
	public static final KObject findWBUser(String name){
		return wbUserDao.findOne(name);
	}

	/**
	 * 按id查找WBUser,静态方法,可直接调用
	 * @param id long
	 * @return KObject
	 */
	public static final KObject findWBUser(long id){
		return wbUserDao.findOne(id);
	}
	
	/**
	 * 新通知
	 * @param userId
	 * @return
	 */
	public static final String hasNew(String userName){
		return WBUserDao.hasNew(userName);
	}

	/**
	 * 验证此用户名是否已存在,存在则返回true
	 * @param uName
	 * @return 存在则返回true
	 */
	public static final boolean checkWBUserName(String uName){
		return wbUserDao.checkName(uName);
	}
	
	public static final boolean isFollow(long userId,long targetId){
		return WBUserDao.isFollow(userId, targetId);
	}
	
	public static final boolean isBothFollow(long userId,long targetId){
		return WBUserDao.isBothFollow(userId, targetId);
	}
	
	public static final boolean isFan(long userId,long targetId){
		return WBUserDao.isFan(userId, targetId);
	}
	
	/**
	 * 保存用户的属性
	 * TODO 后期仅更新用户的属性字段,不直接保存
	 * @param user
	 * @return
	 */
	public static final boolean saveUserProp(long userId,Map<String,Object> props){
		return wbUserDao.saveProp(userId,props);
	}
	
	/**
	 * 新用户注册
	 * @param uName 4个或以上字符串
	 * @param uPwd 6个或以上字符串
	 * @param uPwd2 同上
	 * @param email 6个或以上字符串
	 * @return KObject 注册成功返回用户对象,失败返回null
	 */
	public static final KObject regUser(HashMap<String,Object> paraMap){
//		if (!StringUtil.isStringWithLen(uName, 4) || !StringUtil.isStringWithLen(uPwd, 6) || !StringUtil.isStringWithLen(email, 6) || !uPwd.equals(uPwd2)) {
//			return null;
//		}
		KObject _kobj = wbUserSchema.createEmptyKObj();
		if (!wbUserSchema.setPropFromMapForCreate(paraMap, _kobj)) {
			//字段验证失败
			return null;
		}
		
		//如果用户名已存在,返回null
		if (wbUserDao.checkName(_kobj.getName())) {
			return null;
		}
		if(wbUserDao.save(_kobj)){
			return _kobj;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {
		//初始化Dao和wbUserSchema
		wbUserDao = (WBUserDao)DaoManager.findDao("wbUserDao");
		wbUserSchema = KObjManager.findSchema("wbuser");
	}

	/**
	 * @return the pageSize
	 */
	public final int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public final void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
