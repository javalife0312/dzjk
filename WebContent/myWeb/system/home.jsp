<%@page import="com.template.util.SysUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	 	<base href="<%=basePath%>">
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>主页</title>

		<link rel="stylesheet" type="text/css" href="ext3x/resources/css/ext-all.css" />
		<script type="text/javascript" src="ext3x/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="ext3x/ext-all.js"></script>
		<script type="text/javascript" src="ext3x/adapter/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript">Ext.BLANK_IMAGE_URL ='img/extjs/bank.gif';</script>
		
		<!-- 自定义js -->
		<script type="text/javascript" src="myJs/system/home.js"></script>

	</head>
<body>
	<input type="hidden" id="ptype" value="<%=request.getSession().getAttribute("ptype")%>">
	<input type="hidden" id="logopic" value="<%= SysUtil.getProperty("logopic")%>">
	<input type="hidden" id="logocolor" value="<%= SysUtil.getProperty("logocolor")%>">
	<div id="center">&nbsp;</div>
	<div id="tree">&nbsp;</div>
</body>
</html>
