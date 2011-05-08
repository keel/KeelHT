<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>江苏网微博</title>
<link rel="stylesheet" type="text/css" href="<%=sPrefix %>/css/style_wb.css" />
<script src="<%=sPrefix %>/js/jquery.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){

});
</script>


</head>
<body>
<div id="header" class="wrapper">
	<h1 id="logo"><a href="#">中国江苏网</a></h1>
</div>

<div id="wrapper" class="wrapper">

	<div id="loginB">
		<div class="indexh">
			<div class="taboff"><a href="#">快速注册</a></div>
			<div class="taboff"><a href="#">登陆微博</a></div>
			<div class="tabon">忘记密码</div>
		</div>
		<div class="mainBox">
			<form class="centerForm" name="loginForm" action="#" method="post">
				<p><label for="uName">电子邮箱：</label><span class="txt2">请填写注册时使用的电子邮箱</span><br /><input type="text" name="uName" value="" id="uName"/></p>
			<div class="centerBT"><input type="submit" value=" 找回密码 " /></div>
			</form>
<div class="clear"></div>
		</div>
	</div>

<div class="clear"></div>

</div>

<div id="footer">
	<div id="copyright">
		Copyright:&copy;2010-2012 KEEL.SIKE All rights reserved. 
	</div>
</div>

</body>
</html>