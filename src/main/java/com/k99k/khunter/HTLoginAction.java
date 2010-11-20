/**
 * 
 */
package com.k99k.khunter;

/**
 * 登录Action
 * @author keel
 *
 */
public class HTLoginAction extends Action {

	/**
	 * @param name
	 */
	public HTLoginAction(String name) {
		super(name);
	}
	
	private HTUserDaoInterface userDao;

	/**
	 * 执行登录操作,若为新用户则自动注册
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		//从数据源获取用户信息
		
		//如果数据源无此用户数据,则进行注册操作
		
		//有此用户数据则进行验证
		
		//结果为成功和失败两种,成功则直接返回,失败也可直接返回或转到处理失败的Action
		
		return super.act(msg);
	}

	/**
	 * @return the userDao
	 */
	public final HTUserDaoInterface getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao the userDao to set
	 */
	public final void setUserDao(HTUserDaoInterface userDao) {
		this.userDao = userDao;
	}
	
	

}
