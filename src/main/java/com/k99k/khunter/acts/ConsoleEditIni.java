/**
 * 
 */
package com.k99k.khunter.acts;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HTManager;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.KIoc;
import com.k99k.tools.JSONTool;

/**
 * 编辑系统配置文件
 * @author keel
 *
 */
public class ConsoleEditIni extends Action {

	/**
	 * @param name
	 */
	public ConsoleEditIni(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		//子命令
		String subact = httpmsg.getHttpReq().getParameter("subact");
		if (subact == null || subact.trim().length() <=3) {
			subact = "load";
		}
		msg.addData("subact", subact);
		//载入
		String iniFile = HTManager.getIniFilePath();
		if (subact.equals("load")) {
			String ini = KIoc.readTxtInUTF8(iniFile);
			if (ini != null) {
				msg.addData("json", ini);
			}else{
				msg.addData("json", "");
			}
			
		}
		//保存
		else if(subact.equals("save")){
			String json = httpmsg.getHttpReq().getParameter("json");
			if (json == null || json.length() < 10) {
				msg.addData("save", "no para");
			}else {
				//验证json格式
				if (JSONTool.validateJsonString(json)) {
					//保存
					if(KIoc.writeTxtInUTF8(iniFile, json)){
						msg.addData("save", "ok");
					}else{
						msg.addData("save", "save fail");
					}
				}else{
					msg.addData("save", "validate fail");
				}
			}
		}
		return super.act(msg);
	}

	
	
}
