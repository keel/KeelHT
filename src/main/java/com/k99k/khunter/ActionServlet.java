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
	
	//TODO 定义好log
	static final Logger log = Logger.getLogger(ActionServlet.class);
	
       
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
		// TODO 初始化ActionServlet
		
		//初始化ActionManager
		if(!ActionManager.init()){
			//处理ActionManager初始化失败的情况
		}
		
		
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO 清理ActionServlet
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
		setCharset("utf-8",req,resp);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//全程使用utf-8
		setCharset("utf-8",req,resp);
		
		
		//由act参数定位Action,此参数在Filter中验证
		String actName = req.getParameter("act");
		ActionMsg msg = new HttpActionMsg(actName, req, resp);
		Action acttion = ActionManager.findAction(actName);
		//执行action
		msg = acttion.act(msg);
		//是否打印
		if (msg.getData("print") != null) {
			resp.getWriter().print(msg.getData("print"));
			return;
		}
		//是否发向JSP
		if (msg.getData("jsp") != null) {
			String to = (String) msg.getData("jsp");
			RequestDispatcher rd = req.getRequestDispatcher(to);
			rd.forward(req, resp);
			return;
		}
		//是否跳转
		if (msg.getData("redirect") != null) {
			String redirect = (String) msg.getData("redirect");
			resp.sendRedirect(redirect);
			return;
		}

	}

}
