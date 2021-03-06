<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*" session="false" %>
<%!
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
%>
<%
Object o = request.getAttribute("[jspAttr]");
HttpActionMsg data = null;
if(o != null ){
	data = (HttpActionMsg)o;
}else{
	out.print("ERROR:100404");
	return;
}
KObject user = (KObject)data.getData("wbUser");
Object co = data.getData("coUser");
//cookie用户
KObject coUser = (co==null)?null:(KObject)co;
String uName = user.getName();
long userId = user.getId();
int p = (StringUtil.isDigits(request.getParameter("p")))?Integer.parseInt(request.getParameter("p")):1;
int cc = Integer.parseInt(user.getProp("statuses_count")+"");
int pn = (cc%10>0)?cc/10+1:cc/10;
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/pagenav.min.js"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/talk.js"></script>
<script type="text/javascript">
<!--
$(function(){
	$("#wbUserUrl").empty().append("<%=uName%>");
	$("#r_follow_num").empty().append("<%=user.getProp("friends_count")%>");
	$("#r_fans_num").empty().append("<%=user.getProp("followers_count")%>");
	$("#r_uname").empty().append("<%=uName%>");
	$("#r_mgs_num").empty().append("<%=user.getProp("statuses_count")%>");
	$("#r_icon_1").empty().append("<img src='<%=sPrefix+"/images/upload/"+uName+"_2.jpg"%>' height='60' width='60' alt='icon' />");
	$("#r_location").empty().append("<%=user.getProp("location")%>");
	
	$("#logoutBT").text("登录");
	$("#logoutBT").click(function(){
		window.location="<%=prefix %>/login";
		return false;
	});
	$.sPrefix = "<%=prefix %>";
	$.prefix = "<%=sPrefix %>";
	pageNav.pre="上一页";
 	pageNav.next="下一页";
	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/msg/sent?p="+p+"&pz=10&uid=<%=userId%>&r="+new Date(),function(data){
			var s = "";
			for(var i = 0,j=data.length;i<j;i++){
				var d = data[i];
				s += talkLI(d,<%=(coUser==null)?0:coUser.getId() %>);
			}
			$("#msgList").html(s);
			
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
	comms = $("#commsDiv");
	commsLoading = $("#commsLoading");
	commsTalk = $("#commForm");
	talkForm(commsTalk);
	
	$("#replycheckbox").change(function(){
		var $me = $(this);
		if(this.checked){
			$("#comm_talk_state").val("0");
		}else{
			$("#comm_talk_state").val("1");
		}
	});
});

-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div id="profile">
			<div id="myIcon" class="icon"><img alt="<%= user.getName() %>" src="<%=user.getProp("icon_url") %>_1.jpg"/></div>
			<div id="myInfo">
				<div class="bigTxt"><%= user.getProp("screen_name") %>(@<%= user.getName() %>)</div>
				<div><a href="<%=prefix %>/<%= user.getName() %>">http://localhost:8080<%=prefix %>/<%= user.getName() %></a></div>
				<%
				StringBuilder sb = new StringBuilder();
				sb.append("<div>微博:<a class='bigTxt' href='").append(prefix).append("/").append(user.getName()).append("'>").append(user.getProp("statuses_count")).append("</a> 条 | ");
				sb.append("关注:<a class='bigTxt' href='").append(prefix).append("/follow'>").append(user.getProp("friends_count")).append("</a> 人 | ");
				sb.append("粉丝:<a class='bigTxt' href='").append(prefix).append("/fans'>").append(user.getProp("followers_count")).append("</a> 人 </div><div>");
				
				int sex = Integer.parseInt(user.getProp("sex").toString());
				if(sex==1){sb.append("男");} else if(sex==2){sb.append("女");} else{sb.append("");}
				sb.append(" , ").append(user.getProp("location")).append("</div><div>");
				String url = StringUtil.isStringWithLen(user.getProp("user_url").toString(),4)?"<a href='"+user.getProp("user_url")+"' target='_blank'>"+user.getProp("user_url")+"</a>":"无";
				sb.append("用户主页:").append(url).append("</div><div>简介:<br />");
				String intro = StringUtil.isStringWithLen(user.getProp("description").toString(),1)?user.getProp("description").toString():"无";
				sb.append(intro+"</div>");
				out.println(sb);
				%> 
				<div id="followAct">
					<a href="#" class="followBT">关注</a>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<div id="wbList">
			<div id="ad1"></div>
			<div id="listTools">
				所有微博：
			</div>
			<ul id="msgList" class="ul_inline ul_fix"><li>em</li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>

<% out.println(WBJSPCacheOut.out("@foot_sent")); %>