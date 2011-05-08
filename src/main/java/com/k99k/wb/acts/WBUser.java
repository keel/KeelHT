/**
 * 
 */
package com.k99k.wb.acts;

import java.util.HashMap;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
import com.k99k.khunter.KObjManager;
import com.k99k.khunter.KObjSchema;
import com.k99k.khunter.KObject;

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
	private static DaoInterface wbUserDao = null;
	
	private static KObjSchema wbUserSchema = null;
	
	/**
	 * 此act不一定使用,直接使用静态方法即可
	 * 
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		
		/*
		String subact = KFilter.actPath(msg, 3, "");
		//用户查找
		if (subact.equals("find")) {
			Object ido = msg.getData("wbUId");
			//id查找
			if (ido != null) {
				long id = Long.parseLong(ido.toString());
				msg.addData("wbUser", findWBUser(id));
				return super.act(msg);
			}else {
				Object nameo = msg.getData("wbUName");
				if (nameo != null) {
					msg.addData("wbUser", findWBUser(nameo.toString()));
					return super.act(msg);
				}
			}
		}
		//更新用户信息
		else if(subact.equals("update")){
			
		}
		//请求参数错误,直接返回
		else{
			return super.act(msg);
		}
		*/
		
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
	 * 验证此用户名是否已存在,存在则返回true
	 * @param uName
	 * @return 存在则返回true
	 */
	public static final boolean checkWBUserName(String uName){
		return wbUserDao.checkName(uName);
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
		wbUserDao = DaoManager.findDao("wbUserDao");
		wbUserSchema = KObjManager.findSchema("wbuser");
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {

	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return null;
	}

}
