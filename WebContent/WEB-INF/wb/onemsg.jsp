<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
Object o = request.getAttribute("[jspAttr]");
HttpActionMsg data = null;
if(o != null ){
	data = (HttpActionMsg)o;
}else{
	out.print("ERROR:100404");
	return;
}
KObject user = (KObject)data.getData("wbUser");
String uName = user.getName();
long userId = user.getId();
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.js" type="text/javascript"></script>
<script type="text/javascript">
<!-- 
$(function(){
	
	$("#wbUserUrl").empty().append("<%=uName%>");
	$("#r_follow_num").empty().append("<%=user.getProp("friends_count")%>");
	$("#r_fans_num").empty().append("<%=user.getProp("followers_count")%>");
	$("#r_uname").empty().append("<%=uName%>");
	$("#r_mgs_num").empty().append("<%=user.getProp("statuses_count")%>");
	$("#r_icon_1").empty().append("<img src='<%=sPrefix+"/images/upload/"+uName+"_2.jpg"%>' height='60' width='60' alt='' />");
	$("#r_location").empty().append("<%=user.getProp("location")%>");

	
	$("#logoutBT").click(function(){
		$.post("<%=prefix %>/login/logout", "uName=<%=uName %>" ,function(data) {
			window.location="<%=prefix %>";
		});
		return false;
	});


	
});

-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>

		<div class="mainBox">
			onemsg
		</div>
		
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>