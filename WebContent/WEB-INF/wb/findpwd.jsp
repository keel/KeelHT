<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript">
$(function(){
	//处理请求
	$.validator.dealAjax = {
		bt:$("#submitBT"),
		ok:function(data){
			$.fancybox(
			'<p class="fancyMsgBox1" >密码已发往您注册时使用的邮箱，请查收.</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
		},
		err:function(){
			$.fancybox(
			'<p class="fancyMsgBox2" >对不起,您输入的账号和邮箱不匹配.</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
		}
	};
	
	//开始验证
	$('#findpwdForm').validate({
	    // 设置验证规则
	    rules: {
			uName: {
	            required:true,
	            rangelength:[4,15]
	        },
	        uEmail:{required:true,email:true}
	    }
	});
	
});
</script>
<% out.println(WBJSPCacheOut.out("header2_noSide")); %>
		<div class="indexh">
			<div class="taboff"><a href="<%=prefix %>/reg">快速注册</a></div>
			<div class="taboff"><a href="<%=prefix %>/login">登陆微博</a></div>
			<div class="tabon">忘记密码</div>
		</div>
		<div class="mainBox">
			<form class="centerForm" name="findpwdForm" action="<%=prefix %>/findpwd/find" id="findpwdForm" method="post">
				<p><label for="uName">用户帐号：</label><span class="txt2">请填写注册的用户贴(英文/数字)</span><br />
				<input type="text" name="uName" value="" id="uName"/></p>
				<p><label for="uEmail">电子邮箱：</label><span class="txt2">请填写注册时使用的电子邮箱</span><br />
				<input type="text" name="uEmail" value="" id="uEmail"/></p>
			<div class="centerBT"><input type="submit" id="submitBT" value=" 找回密码 " /></div>
			</form>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("footer_noSide")); %>