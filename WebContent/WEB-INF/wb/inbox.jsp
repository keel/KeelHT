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
int cc = Integer.parseInt(user.getProp("inbox_count")+"");
int pn = (cc%10>0)?cc/10+1:cc/10;
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/pagenav.js"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/talk.js"></script>
<script type="text/javascript">
<!-- 
$(function(){
	
	$("#wbUserUrl").empty().append("<%=uName%>");
	$("#r_follow_num").empty().append("<%=user.getProp("friends_count")%>");
	$("#r_fans_num").empty().append("<%=user.getProp("followers_count")%>");
	$("#r_uname").empty().append("<%=uName%>");
	$("#r_mgs_num").empty().append("<%=user.getProp("statuses_count")%>");
	$("#r_icon_1").empty().append("<img src='<%=sPrefix+"/images/upload/"+uName+"_2.jpg"%>' height='60' width='60' alt='me' />");
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
				s += talkLI(d,"<%=prefix %>","<%=sPrefix %>",<%=user.getId() %>);
			}
			$("#msgList").html(s);
			
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
});

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
			<ul id="msgList" class="ul_inline"><li></li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>