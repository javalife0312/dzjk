package com.template.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

public class SessionCheckFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		HttpServletResponse servletResponse = (HttpServletResponse)response;
		HttpSession session = servletRequest.getSession();
		
		String uri = servletRequest.getRequestURI();
		String[] arrStr = uri.split("\\.");
		if(uri.equals("/ls3x/") || uri.equals("/ls3x/loginAction!login") || arrStr[arrStr.length-1].equals("jsp") || arrStr[arrStr.length-1].equals("css")
				|| arrStr[arrStr.length-1].equals("gif") || arrStr[arrStr.length-1].equals("js") || arrStr[arrStr.length-1].equals("png") 
				|| arrStr[arrStr.length-1].equals("jpg")){
		}else{
			System.out.println(uri);
			Object object = session.getAttribute("uid1");
			if(object == null){
				ServletActionContext.getResponse().setStatus(501);
				servletResponse.sendRedirect("http://www.baidu.com");
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

}
