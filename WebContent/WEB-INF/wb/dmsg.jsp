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
String uName = user.getName();
long userId = user.getId();
int p = (StringUtil.isDigits(request.getParameter("p")))?Integer.parseInt(request.getParameter("p")):1;
int cc = Integer.parseInt(user.getProp("dmsg_count")+"");
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
		$.getJSON("<%=prefix %>/dmsg/list?p="+p+"&pz=10&uid=<%=userId%>&r="+new Date(),function(data){
			var i = 0;
			for(var j=data.length;i<j;i++){
				var li = $(dmLI(data[i],"<%=uName %>"));
				li.hover(function(){$(this).find(".dmDel").show();},function(){$(this).find(".dmDel").hide();});
				$("#msgList").append(li);
			}
			if(i>0){$("#emptyLI").remove();};
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
	var talkF = $("#talkForm");
	talkF.errTip = "<div class='reErr'>私信发送失败！对方可能并非您的粉丝。<br />请正确填写对方的名称(@后方的名称,非用户呢称),并确保处于登录状态。<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	talkF.sucFn = function(json){
		abox("发送私信","<div class='reOk'>私信发送成功！<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>");
		talkF.find("textarea").val("");
		setTimeout("$.fancybox.close();",1000);
		var s = $(dmLI(json,json.creatorName));
		s.hide();
		$("#msgList").prepend(s);
		s.fadeIn(3000);	
	};
	talkForm(talkF);
	
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
<div id="sendBox">
<form name="talkForm" id="talkForm" action="<%=prefix %>/dmsg/add" method="post">
<div id="sendBox_title">发送私信<div style="float:right;font-size:12px;color:#555;">私信只能发送给你的粉丝，若想与朋友通过私信交流，请先相互收听</div></div>
<div id="sendAreaDiv">
	<textarea name="talk" id="talk" rows="5" cols="10"></textarea>
	<input type="hidden" value="" name="pic_url" id="pic_url" />
</div>
<div class="sendsub" id="sendsub">
<div class="fleft smallTxt">
收信人：@<input type="text" id="dmTO" name="dmTO" />
</div>
	<input type="submit" id="sendbt" name="sendbt" value="发送私信" class="sendbt" />
	<span class="sendtip">
		<span class="restTxt">还能输入</span> <span class="countTxt">140</span> <span class="restTxt">字</span>
	</span>
<div class="clear"></div>
</div>
</form>
</div>
		<div id="wbList">
			<div id="listTools">
				所有私信：
			</div>
			<ul id="msgList" class="ul_inline ul_fix"><li id="emptyLI" style="text-align: center;">暂无</li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>

<% out.println(WBJSPCacheOut.out("@foot_dm")); %>