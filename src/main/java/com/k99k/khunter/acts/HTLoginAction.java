/**
 * 
 */
package com.k99k.khunter.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.DaoInterface;
import com.k99k.khunter.HTUser;
import com.k99k.khunter.MongoUserDao;

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
	
	private MongoUserDao userDao;

	/**
	 * FIXME 执行登录操作,若为新用户则自动注册
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		//从数据源获取用户信息
		
		//如果数据源无此用户数据,则进行注册操作
		
		//有此用户数据则进行验证
		
		//结果为成功和失败两种,成功则直接返回,失败也可直接返回或转到处理失败的Action
		msg.addData("something", "nothing");
		msg.addData("dao", this.userDao.getName());
		msg.addData("dataSource", this.userDao.getDataSource().getName());
		//FIXME coll传递问题
		HTUser user =  this.userDao.findUser(3,null);
		msg.addData("print",user.toString());
		msg.setNextAction(null);
		
		return super.act(msg);
	}

	/**
	 * @return the userDao
	 */
	public final MongoUserDao getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao the userDao to set
	 */
	public final void setUserDao(MongoUserDao userDao) {
		this.userDao = userDao;
	}
	
	

}
