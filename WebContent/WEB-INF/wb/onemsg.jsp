<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*,com.k99k.khunter.dao.*" session="false" %>
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
KObject one = (KObject)data.getData("one");
String uName = user.getName();
long userId = user.getId();
long mid = (Long)data.getData("mid");
int p = (Integer)data.getData("p");
int cc = (Integer)one.getProp("rt_comm_count");
int pn = (cc%10>0)?cc/10+1:cc/10;
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=sPrefix %>/js/pagenav.js"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/talk.js"></script>
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

	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/msg/one?p="+p+"&pz=10&uid=0&mid=<%=mid%>&r="+new Date(),function(data){
			var s = "";
			for(var i = 0,j=data.length;i<j;i++){
				var d = data[i];
				s += talkLI(d,"<%=prefix %>","<%=sPrefix %>",0);
			}
			$("#msgList").html(s);
			
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
	
});

-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>

		<div class="mainBox">
			onemsg
		</div>
		<div id="wbList">
			<div id="ad1"></div>
			<ul id="msgList" class="ul_inline"><li></li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>