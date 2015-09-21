<%@page import="com.template.util.SysUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String appsPath = request.getSession().getServletContext().getRealPath("/");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	 	<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body onload="printWB();saveKdd();">
		type1_beizhu_x : <%=SysUtil.getPageProperty("type1_beizhu_x") %>
	</body>
</html>