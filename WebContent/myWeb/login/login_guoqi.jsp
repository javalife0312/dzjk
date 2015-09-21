<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<base href="<%=basePath%>">
<TITLE>用户登录</TITLE>
<LINK href="myWeb/login/images/Default.css" type=text/css rel=stylesheet>
<LINK href="myWeb/login/images/User_Login.css" type=text/css rel=stylesheet>
<META http-equiv=Content-Type content="text/html; charset=gb2312">
<META content="MSHTML 6.00.6000.16674" name=GENERATOR>
</head>
<body id=userlogin_body>
	<form action="loginAction!login" method="post">
	<div id=user_login>
		<dl>
			<dd id=user_top>
				<ul>
					<li class=user_top_l></li>
					<li class=user_top_c></li>
					<li class=user_top_r></li>
				</ul>
			<dd id=user_main>
				<ul>
					<li class=user_main_l></li>
					<li class=user_main_c>
						<div class=user_main_box>
							<ul>
								<li class=user_main_text>用户名：</li>
								<li class=user_main_input>
									<input class="txtusernamecssclass" id="username" type="text" name="username" style="width: 140px;">
								</li>
							</ul>
							<ul>
								<li class=user_main_text>密&nbsp;码：</li>
								<li class=user_main_input>
									<input class="txtusernamecssclass" id="password" type="password" name="password" style="width: 140px;">
								</li>
							</ul>
							<ul>
								<li><font color="red">软件已经过期,请续费</font></li>
							</ul>
						</div>
					</li>
					<li class=user_main_r>
						<input class="ibtnentercssclass" type="image" src="myWeb/login/images/user_botton.gif">
					</li>
				</ul>
			<dd id=user_bottom>
				<ul>
					<li class=user_bottom_l></li>
					<li class=user_bottom_c></li>
					<li class=user_bottom_r></li>
				</ul>
			</dd>
		</dl>
	</div>
	</form>
</body>
</html>
