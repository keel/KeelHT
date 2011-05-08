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
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){

//处理请求
$.validator.dealAjax = {
	bt:$("#submitBT"),
	ok:function(data){
		//200表示成功,data为返回的uName
		window.location = ("<%=sPrefix %>/"+data);
	},
	err:function(){
		alert('登录失败!用户名和密码未通过验证.');
	}
}

//开始验证
$('#loginForm').validate({
    /* 设置验证规则 */
    rules: {
        uName: {
            required:true,
            stringCheck:true,
            rangelength:[4,15]
        },
        uPwd:{
            required:true,
            pwdCheck:true,
            rangelength:[6,30]
        }
    }
});

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
			<div class="tabon">登陆微博</div>
			<div class="taboff"><a href="#">忘记密码</a></div>
		</div>
		<div class="mainBox">
			<form class="centerForm" id="loginForm" name="loginForm" action="<%=sPrefix %>/login/login" method="post">
				<p><label for="uName">用户名：</label><span class="txt2">4-15个英文字符、数字或下划线</span><br /><input type="text" name="uName" value="" id="uName"/></p>
				<p><label for="uPwd">密码：</label><span class="txt2">请正确填写密码</span><br /><input type="password" name="uPwd" value="" id="uPwd"/></p>
				<p>
        <input type="checkbox" name="uCookie" id="uCookie"  value="true" /><label class="txt2" for="uCookie">下次自动登录</label>
    </p>
			<div class="centerBT"><input type="submit" value=" 立即登录 " id="submitBT"/></div>
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