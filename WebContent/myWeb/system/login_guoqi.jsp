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
		<script type="text/javascript" src="myJs/system/login.js"></script>
		
		<script type="text/javascript">
			
			function divPos(){
				var height = document.body.clientHeight;
				var width = document.body.clientWidth;
				var loginPanel = document.getElementById("loginPanel");
				
				loginPanel.style.top = height*0.29;
				loginPanel.style.left = width*0.3;
			}
		
		</script>
		
	</head>
<!-- 
<body onload="divPos()">
<body style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='img/system/loginBg1.jpg', sizingMethod='scale')" onload="divPos()">
 -->
<body style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='img/system/loginBg4.jpg', sizingMethod='scale')" onload="divPos()">
	<div id="loginPanel" style="position:absolute;top:100; left:100;"></div>
</body>
</html>
