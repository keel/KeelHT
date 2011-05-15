/**
 * 
 */
package com.k99k.wb.acts;

import java.util.Map;

import org.apache.log4j.Logger;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.KIoc;
import com.k99k.tools.JSONTool;
import com.k99k.tools.SendMail;

/**
 * 邮件Action
 * @author keel
 *
 */
public class WBEmail extends Action {
	
	static final Logger log = Logger.getLogger(WBEmail.class);

	private final static SendMail mail = new SendMail();
	
	private Thread t;
	
	/**
	 * @param name
	 */
	public WBEmail(String name) {
		super(name);
	}
	
	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		//TODO 可查询mail队列的状态等
		msg.addData("[print]", mail.isRun()+"");
		return super.act(msg);
	}

	/**
	 * 添加发送任务
	 * @param to 接收方邮件地址
	 * @param subject 邮件主题
	 * @param txt 邮件正文
	 */
	public static final void addTask(String to,String subject,String txt){
		mail.addTask(to, subject, txt);
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {
		if (mail != null ) {
			mail.setRunFlag(false);
			if (t != null) {
				this.t.interrupt();
				try {
					Thread.sleep(mail.getSleep());
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return "wb.json";
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			String ini = KIoc.readTxtInUTF8(HTManager.getIniPath()+getIniPath());
			Map<String,?> root = (Map<String,?>) JSONTool.readJsonString(ini);
			//先定位到json的cookies属性
			Map<String, ?> m = (Map<String, ?>) root.get("sendMail");
			String server = m.get("server").toString();
			String sender = m.get("sender").toString();
			int port = Integer.parseInt(m.get("port").toString());
			String user = m.get("user").toString();
			String pwd = m.get("pwd").toString();
			mail.init(server, sender, port, user, pwd);
			if (m.containsKey("sleep")) {
				int sleep = Integer.parseInt(m.get("sleep").toString());
				mail.setSleep(sleep);
			}
			this.t = new Thread(mail);
			t.start();
		} catch (Exception e) {
			log.error("WBEmail init Error!", e);
		}
		
	}

}
