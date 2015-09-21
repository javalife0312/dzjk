package com.template.action;



import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.template.model.Org_User;
import com.template.service.LoginService;
import com.template.service.Service;
import com.template.util.SysUtil;

public class LoginAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request = ServletActionContext.getRequest();
	public HttpServletResponse response = ServletActionContext.getResponse();
	
	/*********************************************************************************
	* Spring注入
	*********************************************************************************/
	private LoginService loginService;
	private Service service;
	
	public LoginService getLoginService() {
		return loginService;
	}
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	/*********************************************************************************
	 * Spring注入
	 * @throws IOException 
	 *********************************************************************************/

	public void login() throws IOException {
		Org_User org_user = null;
		Map<String, String> result = new HashMap<String, String>();
		String goToWhere = "login";
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			boolean checkFlag = SysUtil.loginGuoQiCheck();
			checkFlag = true;
			if(checkFlag){
				org_user = loginService.isExitAuthorizationUser(username, password);
				if(org_user != null){
					request.getSession().setAttribute("uid", org_user.getId());
					request.getSession().setAttribute("ptype", org_user.getType());
					result.put("success", "true");
					result.put("msg", "myWeb/system/home.jsp");
					goToWhere = "home";
				}else {
					result.put("success", "false");
					goToWhere = "login";
				}
			}else{
				result.put("success", "false");
				goToWhere = "guoqi";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.getWriter().print(JSONObject.fromObject(result));
			if(goToWhere.equals("home")){
				response.sendRedirect("myWeb/system/home.jsp");
			}else if(goToWhere.equals("guoqi")){
				response.sendRedirect("myWeb/login/login_guoqi.jsp");
			}else{
				response.sendRedirect("myWeb/login/login.jsp");
			}
		}
	}
	
	/**
	 * @throws IOException
	 */
	public void loginIp() throws IOException {
		Org_User org_user = null;
		Map<String, String> result = new HashMap<String, String>();
		String goToWhere = "login";
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			String ip = request.getParameter("ip");
			String hql = "From Org_User where ip='"+ip+"' order by id";
			List<Object> list = service.listByHql(hql,0,5);
			
			if(list != null){
				org_user = (Org_User) list.get(0);
				request.getSession().setAttribute("uid", org_user.getId());
				request.getSession().setAttribute("ptype", org_user.getType());
				result.put("success", "true");
				result.put("msg", "myWeb/system/home.jsp");
				goToWhere = "home";
			}else {
				result.put("success", "false");
				goToWhere = "login";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.getWriter().print(JSONObject.fromObject(result));
			if(goToWhere.equals("home")){
				response.sendRedirect("myWeb/system/home.jsp");
			}else if(goToWhere.equals("guoqi")){
				response.sendRedirect("myWeb/login/login_guoqi.jsp");
			}else{
				response.sendRedirect("myWeb/login/login.jsp");
			}
		}
	}
}
