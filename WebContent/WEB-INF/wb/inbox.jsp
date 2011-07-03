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
long cc = (Long)user.getProp("inbox_count");
long pn = (cc%10>0)?cc/10+1:cc/10;
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/pagenav.js"></script>
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

	$("#talk").keyup(function(){
		var cc = 140-$(this).val().length;
		if(cc >= 0){
			$("#countTxt").text(cc).removeClass("red");
			$("#restTxt").text("还能输入");
		}else{
			$("#countTxt").text(cc).addClass("red");
			$("#restTxt").text("超出");
		}
		
	});

	//处理请求
	$.validator.dealAjax = {
		bt:$("#sendbt"),
		ok:function(data){
			//console.log(data);
			
			$.fancybox(
			'<p class="fancyMsgBox1" >消息发送成功!</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
			$("#talk").val("");
			//setTimeout("$.fancybox.close();",800);
			
			setTimeout("readNew();",2500);
		},
		err:function(){
			$.fancybox(
			'<p class="fancyMsgBox2" >消息发送失败.您可能需要重新登录。</p>',
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
	$('#talkForm').validate({
	    // 设置验证规则
	    rules: {
	        talk: {
	            required:true,
	            rangelength:[1,140]
	        }
	    }
	});

	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/msg/inbox?p="+p+"&pz=10&uid=<%=userId%>&r="+new Date(),function(data){
			var s = "";
			for(var i = 0,j=data.length;i<j;i++){
				var d = data[i];
				s += talkLI(d,"<%=prefix %>","<%=sPrefix %>");
			}
			$("#msgList").html(s);
			
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
});

function talkLI(d,prefix,sPrefix){
	var s = "<li><div class=\"userPic\"><a href=\"/";
	s += d.creatorName;
	s += "\"> <img src=\"";
	s += sPrefix;
	s += "/images/upload/";
	s += d.creatorName;
	s += "_3.jpg\" alt=\"";
	s += d.creatorName;
    s += "\" /></a></div>";
	s += "<div class=\"msgBox\"><div class=\"userName\" ><a href=\"/";
	s += d.creatorName;
	s += "\" title=\"";
	s += d.author_screen;
	s += "(@";
	s += d.creatorName;
	s += ")\" >";
	s += d.author_screen;
	s += "</a>";
	if(d.isRT){s+="&nbsp;&nbsp;转播:&nbsp;&nbsp;"};
	s += "</div><div class=\"msgCnt\">";
	s += d.text;
	s += "</div>";
	if (d.rtmsg) {
		var r = d.rtmsg;
		s += "<div class='replyBox'> <div class='msgBox'><a href=\"/";
		s += d.creatorName;
		s += "\" title=\"";
		s += d.author_screen;
		s += "(@";
		s += d.creatorName;
		s += ")\" >";
		s += d.author_screen;
		s += "</a>&nbsp;:&nbsp; <span class='msgCnt'>";
		s += r.text;
		s += "</span><div class=\"pubInfo\"><span class=\"fleft\"><a class=\"time\" target=\"_blank\" href=\"";
		//oneTopicUrl
		//s += "/p/t/9579026057805";
		s += "\" title=\"";
		s += new Date(r.createTime).format("yyyy-MM-dd hh:mm:ss");
		s += "\">";
		s += sentTime(r.createTime);
		s += "</a> 来自";
		s += r.source;
		s += "</span> &nbsp; <a href='<%=prefix %>/m?mid=";
		s += r._id;
		s += "' target='_blank'>原文转播与评论(";
		s += r.rt_comm_count;
		s += ")</a></div></div><div class='clear'></div> </div>";
	}
	s += "<div class=\"pubInfo\"><span class=\"fleft\"><a class=\"time\" target=\"_blank\" href=\"";
	//oneTopicUrl
	//s += "/p/t/9579026057805";
	s += "\" title=\"";
	s += new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");
	s += "\">";
	s += sentTime(d.createTime);
	s += "</a> 来自";
	s += d.source;
	if (d.rt_comm_count > 0) {
		s += " &nbsp; <a href='#' class=\"readComm\">查看转播与评论(";
		s += d.rt_comm_count;
		s += ")</a>";
	}
	s += "</span><div class=\"funBox\">";
	if (d.creatorId == <%=user.getId()%>) {
		s += "<a href=\"#\" class=\"delMsg\">删除</a>&nbsp;&nbsp; |&nbsp;&nbsp;";
	}
	s += "<a href=\"#\" class=\"relay\">转播</a>&nbsp;&nbsp; |&nbsp;&nbsp; <a href=\"#\" class=\"comt\">评论</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"/p/t/39552051902918\" class=\"comt\">收藏</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"#\" class=\"alarm\">举报</a> </div></div></div></li>";
	return s;
}

function sentTime(ms){
　　	var t = new Date(ms);
　　var now = new Date();
　　var showDate = 172800000;
　　var showYestoday = 172800000;
　　var lastHour = 172800000;
　　var pas = now-t;
　　if (pas>=showDate) {
　　return (t.format("yyyy-MM-dd hh:mm:ss"));
　　}else if(pas>=showYestoday && pas<showDate){
　　	return ("昨天:"+t.format("hh:mm:ss"));
　　}else if(pas>=lastHour && pas<showYestoday){
　　return ("今天:"+t.format("hh:mm:ss"));
　　}else{
　　return (t.format("mm")+"分钟前");
　　}
}

function readNew(){
	$.getJSON("<%=prefix %>/msg/unread?max=15&uid=<%=userId%>",function(data){
		var s = "";
		for(var i = 0,j=data.length;i<j;i++){
			var d = data[i];
			s += talkLI(d,"<%=prefix %>","<%=sPrefix %>");
		}
		$("#msgList").prepend(s);
		//console.log(data);
	});
}
-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div id="sendBox">
			<form name="talkForm" id="talkForm" action="<%=prefix %>/talk" method="post">
			<div id="sendBox_title">来，说点什么吧</div>
			<div id="sendAreaDiv">
				<textarea name="talk" id="talk" rows="5" cols="10"></textarea>
				<input type="hidden" value="" name="pic_url" />
			</div>
			<div id="sendsub">
			<span class="fleft">
					<a href="#">图片</a> | <a href="#">话题</a> </span>
			
				<input type="submit" name="sendbt" value=" 发布 " id="sendbt"/>
				<span id="sendtip">
					<span id="restTxt">还能输入</span> <span id="countTxt">140</span> 字
				</span>
<div class="clear"></div>
			</div>
			</form>
		</div>
		<div id="wbList">
			<div id="ad1"></div>
			<div id="listTools">
				所有广播：
			</div>
			
			<div id="newsBox"><a href="#">有2条新消息,点击查看</a></div>
			<ul id="msgList" class="ul_inline">
				
			</ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>