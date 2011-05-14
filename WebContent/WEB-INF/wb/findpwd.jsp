<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
out.println(WBJSPCacheOut.out("header1"));
%>
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){

});
</script>
<% out.println(WBJSPCacheOut.out("header2_noSide")); %>
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
<% out.println(WBJSPCacheOut.out("footer_noSide")); %>