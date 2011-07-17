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
	$.sPrefix = "<%=prefix %>";
	$.prefix = "<%=sPrefix %>";
	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/msg/comms?p="+p+"&pz=10&uid=0&mid=<%=mid%>&r="+new Date(),function(data){
			var s = "";
			for(var i = 1,j=data.length;i<j;i++){
				var d = data[i];
				s += talkLI(d,<%=userId%>,true);
			}
			$("#msgList").html(s);
			
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
	var ont_time_long = Number($("#one_time").text());
	$("#one_time").text(sentTime(ont_time_long));
	var oneReSendForm = $("#oneReSendForm");
	oneReSendForm.readNew = "readNew();";
	talkForm(oneReSendForm);
});
function readNew(d){
	var s = talkLI(d,<%=user.getId() %>,true);
	$("#msgList").prepend(s);
}
-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>

		<div id="m_<%=one.getId()%>" style="padding:15px 10px;">
		<div class="userPic"><a href="/KHunter/<%= uName %>" class="icon"> <img src="/KHunter/images/upload/<%= uName %>_3.jpg" alt="<%= uName %>"></a></div>
		<div style="width:470px;float:right;padding:10px;border: 1px solid #DCDCDC;background: #F8F8F8;"><a href="/KHunter/<%= uName %>" title="<%= user.getProp("screen_name") %>(@<%= uName %>)"><%= user.getProp("screen_name") %></a>&nbsp;:&nbsp; <span class="msgCnt"><%=one.getProp("text") %></span>
			<%if(StringUtil.isStringWithLen(one.getProp("pic_url"),2)){%>
			<div class="picbox" id="pic_m_<%=one.getId()%>"><a href="javascript:pic_in(<%=one.getId()%>);"><img src="/KHunter/images/upload/<%=one.getProp("pic_url").toString() %>" alt="pic"></a></div>
			<%} %>
			<div class="pubInfo">
			<span class="fleft"><span id="one_time"><%= one.getProp("createTime") %></span> 来自<%= one.getProp("source") %> </span>
			<div class="funBox"><a href="javascript:favCheck(<%=one.getId()%>);" class="comt">收藏</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href="javascript:void(0);" class="alarm">举报</a> </div>
			<div class="clear"></div></div>
			<form name="commForm" id="oneReSendForm" class="commForm" style="border: 1px solid #CCC;background: #FFF;" action="/KHunter/talk" method="post"> <div class="commTalk"> <textarea name="talk" rows="4" cols="40"></textarea> <input type="hidden" name="isRT" value="true" id="re_isRT"><input type="hidden" name="rt_id" value="<%=one.getId()%>" id="re_rt_id"><input type="hidden" name="rt_userId" value="<%=one.getProp("creatorId")%>" id="re_rt_userId"><input type="hidden" name="rt_name" value="<%=one.getProp("creatorName")%>" id="re_rt_name"><input type="hidden" name="talk_state" value="0" id="re_talk_state"></div><div class="sendsub" style="color: #999;text-align: right;padding:6px 0;"> <span class="restTxt">还能输入</span> <span class="countTxt" style="font-size:20px;">125</span> <span class="restTxt">字</span> <input type="submit" name="sendbt" value="转播" class="sendbt bt_re"> <div class="clear"></div> </div> </form>
			
		</div>
		<div class="clear"></div>
		</div>
		<div id="wbList">
			<div id="ad1"></div>
			<ul id="msgList" class="ul_inline ul_fix" style="border-top:1px dashed #D8D8D8;"><li></li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot")); %>