package com.k99k.khunter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * 处理由KFilter过来的请求,由Action进行处理,实际是一个Action执行者
 */
public final class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static final Logger log = Logger.getLogger(ActionServlet.class);
	

//	static boolean isInited = false;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActionServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		String iniPath = config.getInitParameter("ini");
		boolean initOK = HTManager.init(iniPath);
		if (!initOK) {
			log.error("---------KHunter init failed!!!------------");
		}
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// 清理ActionServlet
		HTManager.exit();
	}
	
	/**
	 * 设置输入输出的编码
	 * @param charset
	 * @param req
	 * @param resp
	 * @throws UnsupportedEncodingException
	 */
	public static final void setCharset(String charset,HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException{
		req.setCharacterEncoding(charset);
		resp.setCharacterEncoding(charset);
		resp.setHeader("Content-Encoding",charset);
		resp.setHeader("content-type","text/html; charset="+charset);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//全程使用utf-8
		//setCharset("utf-8",req,resp);
		
		//TODO get方式测试用
		this.doPost(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//全程使用utf-8
		setCharset("utf-8",req,resp);
		try {
			//由act参数定位Action,此参数在Filter中验证
			String actName = req.getParameter("act");
			//TODO act参数移向Filter验证
			if (actName == null) {
				resp.getWriter().print("404 - 1");
				return;
			}
			ActionMsg msg = new HttpActionMsg(actName, req, resp);
			Action action = ActionManager.findAction(actName);
			if (action == null) {
				resp.getWriter().print("404 - 2");
				return;
			}
			//执行action
			msg = action.act(msg);
			//是否打印
			if (msg.getData("print") != null) {
				resp.getWriter().print(msg.getData("print"));
				return;
			}
			//是否发向JSP
			else if (msg.getData("jsp") != null) {
				String to = (String) msg.getData("jsp");
				RequestDispatcher rd = req.getRequestDispatcher(to);
				rd.forward(req, resp);
				return;
			}
			//是否跳转
			else if (msg.getData("redirect") != null) {
				String redirect = (String) msg.getData("redirect");
				resp.sendRedirect(redirect);
				return;
			}else{
				resp.getWriter().print(msg.getData("404 - 3"));
			}
		} catch (Exception e) {
			log.error("Action servlet error!", e);
		}

	}

}
