/**
 * 
 */
package com.k99k.khunter.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.KObject;

/**
 * @author keel
 *
 */
public class KObjAction extends Action{

	public KObjAction(String name) {
		super(name);
	}
	
	
	public boolean createKObj(String name,KObject kobj){
		
		return true;
	}
	
	//创建KObj,由json配置文件载入
	
	/*
	 * 管理KObj表，所有HT_开头的表均受到管理，可读取表结构(取出某一条记录)，索引，
	 * 对应的Dao
	
	 */
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		// TODO Auto-generated method stub
		return super.act(msg);
	}


	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}

}
