package com.k99k.khunter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤器，用于过滤非法请求，并处理参数
 */
public final class KFilter implements Filter {

    /**
     * Default constructor. 
     */
    public KFilter() {
    }
    
    private static boolean isStop = false;

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}
	
	public static final void stop(){
		isStop = true;
	}

	public static final void start(){
		isStop = false;
	}
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (isStop) {
			HttpServletRequest req = (HttpServletRequest)request;
			//FIXME 穿透控制,注意这里一定要加ip控制
			if (req.getParameter("keelcontrolsall") != null) {
				chain.doFilter(request, response);
				return;
			}else{
				HttpServletResponse resp = (HttpServletResponse)response;
				resp.setCharacterEncoding("utf-8");
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().print("System under maintenance, please come back later.");
				return;
			}
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
