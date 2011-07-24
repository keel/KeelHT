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
int p = (StringUtil.isDigits(request.getParameter("p")))?Integer.parseInt(request.getParameter("p")):1;
int cc = Integer.parseInt(user.getProp("followers_count")+"");
int pn = (cc%10>0)?cc/10+1:cc/10;
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/pagenav.min.js"></script>
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
			window.location="<%=prefix %>/";
		});
		return false;
	});

	pageNav.pre="上一页";
 	pageNav.next="下一页";
	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/user/fans?p="+p+"&pz=10&uid=<%=userId%>&r="+new Date(),function(data){
			console.log(data);
			if(data && data.length>0){
				$("#emptyLI").remove();
				
			}
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
});

-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div class="indexh">
			<div class="taboff"><a href="<%=prefix %>/follow">我的关注</a></div>
			<div class="tabon">我的粉丝</div>
		</div>
		<div id="wbList">
			<ul id="msgList" class="ul_inline ul_fix"><li id="emptyLI" style="text-align: center;">暂无</li>
			
			<li id="m_250"><div class="userPic"><a href="/KHunter/aaaa" class="icon"> <img src="/KHunter/images/upload/aaaa_3.jpg" alt="aaaa" /></a></div>
			<div class="msgBox"><div class="userName"><a target="_blank" href="/KHunter/aaaa" title="我是aaaa(@aaaa)">我是aaaa(@aaaa)</a></div>
			<div class="msgCnt">收听 17 人    听众 47 人    广播 6 条</div>
			<div class="funBox"><a href="javascript:reSend(250,6,'aaaa')" class="relay">已关注</a> | <a href="#">取消</a></div></div>
			</li>
			</ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
		
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>