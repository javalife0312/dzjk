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
		<link rel="stylesheet" type="text/css" href="ext3x/resources/css/ext-all.css" />
		<script type="text/javascript" src="ext3x/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="ext3x/ext-all.js"></script>
		<script type="text/javascript" src="ext3x/adapter/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript">Ext.BLANK_IMAGE_URL ='img/extjs/bank.gif';</script>
	</head>
<body style="width: 100%;height: 100%;">
	<img alt="" src="img/logo/<%=SysUtil.getProperty("homepic") %>" width="100%" height="99%"/>
</body>
</html>
