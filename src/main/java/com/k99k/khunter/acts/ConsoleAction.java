/**
 * 
 */
package com.k99k.khunter.acts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionManager;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
import com.k99k.tools.JSONTool;

/**
 * 控制台总Action,同时管理其他Console 用到的action
 * @author keel
 *
 */
public class ConsoleAction extends Action {

	/**
	 * @param name
	 */
	public ConsoleAction(String name) {
		super(name);
	}
	static final Logger log = Logger.getLogger(ActionManager.class);
	
	
	/**
	 * 存储Action的Map,初始化大小为100
	 */
	private static final Map<String, Action> consoleActMap = new HashMap<String, Action>(50);
	
	/**
	 * 管理员map
	 */
	private static final Map<String, Map<String,Object>> adminMap = new HashMap<String, Map<String,Object>>(50);
	
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		msg.addData("jsp", "/WEB-INF/to/console.jsp");
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		String right = "state";
		HttpSession se = httpmsg.getHttpReq().getSession();
		if (httpmsg.getHttpReq().getParameter("right") != null) {
			right = httpmsg.getHttpReq().getParameter("right");
		}
		
		if (right.equals("login")) {
			//处理登录
			String name = httpmsg.getHttpReq().getParameter("form_name");
			String pwd = httpmsg.getHttpReq().getParameter("form_pwd");
			if (name != null && pwd != null && adminMap.containsKey(name) && pwd.equals(adminMap.get(name).get("pwd"))) {
				//ok
				se.setAttribute("admin", name);
				//msg.addData("right", "state");
				//msg.addData("jspAttr", msg);
				msg.removeDate("jsp");
				msg.addData("redirect", "act?act=console&right=state");
			}else{
				//error
				msg.addData("jsp", "/WEB-INF/to/login.jsp");
			}
		}
		//退出
		else if (right.equals("exit")) {
			se.removeAttribute("admin");
			se.invalidate();
			msg.addData("jsp", "/WEB-INF/to/login.jsp");
		}
		else{
			//验证登录
			
			if (se == null || se.getAttribute("admin")==null ) {
				msg.addData("jsp", "/WEB-INF/to/login.jsp");
			}else{
				//处理其他管理动作
				msg.addData("right", right);
				//调用相关的ConsoleAction
				Action a = consoleActMap.get(right);
				if (a !=null) {
					a.act(httpmsg);
				}else{
					log.error("Can't find in consoleActMap:"+right);
				}
				msg.addData("jspAttr", msg);
				msg.setNextAction(null);
			}
		}

		return super.act(msg);
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			String ini = KIoc.readTxtInUTF8(HTManager.getClassPath()+"../console.json");
			Map<String,?> root = (Map<String,?>) JSONTool.readJsonString(ini);
			//先定位到json的actions属性
			Map<String, ?> actionsMap = (Map<String, ?>) root.get(ActionManager.getName());
			//循环加入Action
			int i = 0;
			for (Iterator<String> iter = actionsMap.keySet().iterator(); iter.hasNext();) {
				String actionName = iter.next();
				Map<String, ?> m = (Map<String, ?>) actionsMap.get(actionName);
				//读取必要的属性，如果少则报错并继续下一个
				if (m.containsKey("_class")) {
					String _class = (String) m.get("_class");
					
					/*
					//type默认为normal //--直接在属性中加入
					//String _type = (m.containsKey("_type"))?"normal":(String) m.get("_type");
					*/
					
					Object o = KIoc.loadClassInstance("file:/"+HTManager.getClassPath(), _class, new Object[]{actionName});
					if (o == null) {
						log.error("loadClassInstance error! _class:"+_class+" _name:"+actionName);
						continue;
					}
					Action action = (Action)o;
					
					HTManager.fetchProps(action, m);
					//加入Action,无论是否已存在
					consoleActMap.put(action.getName(), action);
					log.info("- ConsoleAction added: "+action.getName());
					//这里没必要进行action的init()操作
				}else{
					log.error("ConsoleAction init Error! miss one or more key props. Position:"+i);
					continue;
				}
				i++;
			}
		} catch (Exception e) {
			log.error("ConsoleAction init Error!", e);
		}
	}
	
	/**
	 * 设置所有管理员
	 * @param admins 多个管理员
	 */
	@SuppressWarnings("unchecked")
	public final void setAdmin(Object admins){
		ArrayList<Map<String,Object>> as = (ArrayList<Map<String,Object>> )admins;
		for (Iterator<Map<String,Object>> it = as.iterator(); it.hasNext();) {
			Map<String, Object> admin =  it.next();
			adminMap.put(admin.get("name").toString(), admin);
		}
	}

}
