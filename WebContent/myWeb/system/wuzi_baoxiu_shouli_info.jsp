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
		<title>权限信息</title>

		<link rel="stylesheet" type="text/css" href="ext3x/resources/css/ext-all.css" />
		<script type="text/javascript" src="ext3x/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="ext3x/ext-all.js"></script>
		<script type="text/javascript" src="ext3x/adapter/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript">Ext.BLANK_IMAGE_URL ='img/extjs/bank.gif';</script>
		
		<!-- 自定义js -->
		<script type="text/javascript" src="myJs/system/wuzi_baoxiu_shouli_info.js"></script>

	</head>
<body>
	<div id="center">&nbsp;</div>
</body>
</html>
