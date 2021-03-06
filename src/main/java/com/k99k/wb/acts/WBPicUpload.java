/**
 * 
 */
package com.k99k.wb.acts;

import javax.servlet.http.HttpServletRequest;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;
import com.k99k.khunter.HttpActionMsg;
import com.k99k.khunter.acts.Uploader;
import com.k99k.tools.StringUtil;

/**
 * @author keel
 *
 */
public class WBPicUpload extends Action {

	/**
	 * @param name
	 */
	public WBPicUpload(String name) {
		super(name);
	}
	
	/**
	 * 保存文件的路径
	 */
	private String savePath = "";

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		HttpActionMsg httpmsg = (HttpActionMsg)msg;
		HttpServletRequest req = httpmsg.getHttpReq();
		String file = req.getParameter("f");
		if (!StringUtil.isStringWithLen(file, 1)) {
			JOut.err(400, httpmsg);
			return super.act(msg);
		}
		String re = Uploader.upload(req,this.savePath,file,true);
		String org = this.savePath+re;
		String f1 = Uploader.addFileTail("_1", re);
		String f2 = Uploader.addFileTail("_2", re);
		
		String small1 = this.savePath+f1;
		String small2 = this.savePath+f2;
		Uploader.makeSmallPic(org, small1, 450, 380);
		Uploader.makeSmallPic(org, small2, 250, 150);
		StringBuilder sb = new StringBuilder("[\"");
		sb.append(re).append("\",\"");
		sb.append(f1).append("\",\"");
		sb.append(f2).append("\"]");
		msg.addData("[print]", sb.toString());
		return super.act(msg);
	}
	
	
	/**
	 * @return the savePath
	 */
	public final String getSavePath() {
		return savePath;
	}


	/**
	 * @param savePath the savePath to set
	 */
	public final void setSavePath(String savePath) {
		this.savePath = savePath;
	}


}
