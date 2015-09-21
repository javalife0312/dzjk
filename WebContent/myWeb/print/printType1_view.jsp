<%@page import="java.text.SimpleDateFormat"%>
<%@taglib uri="/struts-dojo-tags" prefix="sx"%>
<%@ page import="java.util.*" pageEncoding="utf-8"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%!
	public String checkObject(Object obj){
		String result = "";
		if(obj != null){
			result = obj.toString();
		}
		return result;
	}
%>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	String nowDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date());
	
	String apply_person = new String(request.getParameter("apply_person").getBytes("ISO-8859-1"),"utf8");
	String apply_org_name = new String(request.getParameter("apply_org_name").getBytes("ISO-8859-1"),"utf8");
	String apply_wuziName = new String(request.getParameter("apply_wuziName").getBytes("ISO-8859-1"),"utf8");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- 自定义js -->
<script type="text/javascript">
	function goToPrint(){
		window.print();
		window.close();
	}
</script>
<style type="text/css">
 	/*主面板样式*/
     #container { 
         width:100%; 
         /*主面板DIV居中*/
         margin:0px auto;
         text-align:left;  
		 vertical-align:middle;  
         
     }
    /*顶部面板样式*/
     #header {
         width:100%;
         height:50px;
         text-align:center;
         font-weight:bold;
         font-size:large;
         border:0px #000 solid;
     }
     /*中间部分面板样式*/
     #apply_pt_dt {
         width:100%;
         height:50px;
         border:1px #000 solid;
     } 
     #apply_person {
         width:100%;
         height:50px;
         border:1px #000 solid;
     } 
     #apply_device {
         width:100%;
         height:50px;
         border:1px #000 solid;
     }
     #apply_qianzi {
         width:100%;
         height:100px;
         border:1px #000 solid;
     }
     #apply_split_line {
         width:100%;
         height:50px;
         border:0px #000 solid;
     }
	 .apply_pt {
         float:left;
         width:50%;
         height:100%;
         border:1px #000 solid;
     }
     .apply_dt {
         float:left;
         width:49.9%;
         height:100%;
         border:0px #f00 solid;
     }
	 .apply_person_left {
         float:left;
         width:30%;
         height:100%;
         border:1px #000 solid;
     }
     .apply_person_right {
         float:left;
         width:70%;
         height:100%;
         border:1px 0F00 solid;
     }
	 .apply_device_left {
         float:left;
         width:30%;
         height:100%;
         border:1px #000 solid;
     }
     .apply_device_right {
         float:left;
         width:70%;
         height:100%;
         border:1px 0F00 solid;
     }
	 .apply_qianzi_left {
         float:left;
         width:70%;
         height:100%;
         border:0px #000 solid;
     }
     .apply_qianzi_right {
         float:left;
         width:30%;
         height:100%;
         border:1px 0F00 solid;
     }
     
     
     
 	/*主面板样式*/
     #container1 { 
         width:100%; 
         /*主面板DIV居中*/
         margin:0px auto;
         text-align:left;  
		 vertical-align:middle;  
         
     }
    /*顶部面板样式*/
     #header1 {
         width:100%;
         height:50px;
         text-align:center;
         font-weight:bold;
         font-size:large;
         border:0px #000 solid;
     }
     /*中间部分面板样式*/
     #apply_pt_dt1 {
         width:100%;
         height:50px;
         border:1px #000 solid;
     } 
     #apply_person1 {
         width:100%;
         height:50px;
         border:1px #000 solid;
     } 
     #apply_device1 {
         width:100%;
         height:50px;
         border:1px #000 solid;
     }
     #apply_qianzi1 {
         width:100%;
         height:100px;
         border:1px #000 solid;
     }
     #apply_split_line1 {
         width:100%;
         height:50px;
         border:0px #000 solid;
     }
	 .apply_pt1 {
         float:left;
         width:50%;
         height:100%;
         border:1px #000 solid;
     }
     .apply_dt1 {
         float:left;
         width:49.9%;
         height:100%;
         border:0px #f00 solid;
     }
	 .apply_person_left1 {
         float:left;
         width:30%;
         height:100%;
         border:1px #000 solid;
     }
     .apply_person_right1 {
         float:left;
         width:70%;
         height:100%;
         border:1px 0F00 solid;
     }
	 .apply_device_left1 {
         float:left;
         width:30%;
         height:100%;
         border:1px #000 solid;
     }
     .apply_device_right1 {
         float:left;
         width:70%;
         height:100%;
         border:1px 0F00 solid;
     }
	 .apply_qianzi_left1 {
         float:left;
         width:70%;
         height:100%;
         border:0px #000 solid;
     }
     .apply_qianzi_right1 {
         float:left;
         width:30%;
         height:100%;
         border:1px 0F00 solid;
     }
</style>
<style type="text/css" media=print> 
	.noprint {display : none } 
</style> 
</head>
<body>
	<div id="container">
		<div id="header">办公设备申请表</div>
		<div id="apply_pt_dt">
			<div class="apply_pt">部门 : <%= apply_org_name %></div>  
        	<div class="apply_dt">时间 ：<%= nowDate %></div>  
		</div>
		<div id="apply_person">
			<div class="apply_person_left">申请人 : </div>  
        	<div class="apply_person_right"><%= apply_person %></div>  
		</div>
		<div id="apply_device">
			<div class="apply_device_left">资产名称 : </div>  
        	<div class="apply_device_right"> <%= apply_wuziName %></div>  
		</div>
		<div id="apply_qianzi">
			<div class="apply_qianzi_left"></div>  
        	<div class="apply_qianzi_right">领取人签字 ：</div>  
		</div>
		<div id="apply_split_line"></div>
		<hr/>
	</div>
	
	<div id="container1">
		<div id="header1">办公设备申请表</div>
		<div id="apply_pt_dt1">
			<div class="apply_pt1">部门 : <%= apply_org_name%></div>  
        	<div class="apply_dt1">时间 ：<%= nowDate %></div>  
		</div>
		<div id="apply_person1">
			<div class="apply_person_left1">申请人 : </div>  
        	<div class="apply_person_right1"><%= apply_person %></div>  
		</div>
		<div id="apply_device1">
			<div class="apply_device_left1">资产名称 : </div>  
        	<div class="apply_device_right1"> <%= apply_wuziName %></div>  
		</div>
		<div id="apply_qianzi1">
			<div class="apply_qianzi_left1"></div>  
        	<div class="apply_qianzi_right1">领取人签字 ：</div>  
		</div>
	</div>
	<div class="noprint"><button onclick="goToPrint();">打印</button></div>
</body>
</html>