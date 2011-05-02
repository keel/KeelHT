/**
 * 
 */
package com.k99k.wb.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.DaoManager;
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
		//TODO 更新用户信息
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


	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {
		//初始化Dao
		wbUserDao = DaoManager.findDao("wbUserDao");
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
