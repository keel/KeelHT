<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
out.println(WBJSPCacheOut.out("header1"));
%>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
//处理请求
$.validator.dealAjax = {
	bt:$("#submitBT"),
	ok:function(data){
		//200表示成功,data为返回的uName
		var j = $.parseJSON(data);
		window.location = ("<%=sPrefix %>/"+j.uName);
	},
	err:function(){
		alert('注册用户失败,该用户名已存在.');
	}
}

//开始验证
$('#regForm').validate({
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
        },
        uPwd2:{
        	equalTo: "#uPwd"
        },
        email:{email:true}
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

	<div id="main">
		<div id="mainTop" class="topBox">
			欢迎加入中国江苏网微博！请填写以下信息进行注册。
		</div>
		<div id="mainBox" class="mainBox">
			<form id="regForm" name="regForm" action="<%=sPrefix %>/reg/reg" method="post">
				<p><label for="uName">用户名：</label><span class="txt2">帐户名长度最多 6 个汉字或 12 个字符</span><br /><input type="text" name="uName" value="" id="uName"/><span class="red bold"> *</span></p>
				<p><label for="uPwd">注册密码：</label><span class="txt2">密码长度4-20位，字母区分大小写</span><br /><input type="text" name="uPwd" value="" id="uPwd"/><span class="red bold"> *</span></p>
				<p><label for="uPwd2">确认密码：</label><span class="txt2">请再次输入一次注册密码，以确保正确</span><br /><input type="text" name="uPwd2" value="" id="uPwd2"/><span class="red bold"> *</span></p>
				<p><label for="email">电子邮箱：</label><span class="txt2">电子邮箱是找回密码的途径，请正确填写</span><br /><input type="text" name="email" value="" id="email"/><span class="red bold"> *</span></p>
			<p><input type="submit" id="submitBT" value=" 立即注册 " /></p>
			</form>
			<div id="rightBox">
				<div>
					已有账号？请直接登陆：
				</div>
				<div class="centerBT">
					<input type="button" value=" 登录微博 " />
				</div>
				<div class="SC">
					<ul>
						<li>其他信息</li>
						<li>其他信息</li>
						<li>其他信息</li>
					</ul>
				</div>
			</div>
<div class="clear"></div>

		</div>
	</div>
<% out.println(WBJSPCacheOut.out("footer_noSide")); %>