<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,java.net.URLEncoder,java.io.*,com.k99k.wb.acts.*" session="false" %>
<%!//编辑页面中包含 camera.swf 的 HTML 代码
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
	%>
<% out.println(WBJSPCacheOut.out("header1")); %>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){


});
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div class="indexh">
			<div class="taboff"><a href="<%=prefix %>/settings/basic">基本设置</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/avatar">修改头像</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/changepwd">修改密码</a></div>
			<div class="tabon">邮箱验证</div>
			<div class="taboff">个性模板</div>
		</div>
		<div class="mainBox">
			<div id="pic_big" class="tCenter">
				<img src="<%=sPrefix %>/images/avatar_pic1.jpg" />
			</div>
			<div class="tCenter">
<%

%>
			</div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot")); %>