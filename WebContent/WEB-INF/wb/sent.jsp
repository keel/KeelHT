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
int cc = Integer.parseInt(user.getProp("statuses_count")+"");
int pn = (cc%10>0)?cc/10+1:cc/10;
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/pagenav.min.js"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/talk.js"></script>
<script type="text/javascript" src="<%=sPrefix %>/js/swfupload.min.js"></script>
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
				s += talkLI(d,<%=user.getId() %>);
			}
			$("#msgList").html(s);
			
		});
	};
	pageNav.go(<%= p %>,<%= pn %>);
	var talkF = $("#talkForm");
	talkForm(talkF,true);
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
	initUpload("<%=uName %>");
	$("#single_image").fancybox(
		{'autoDimensions'	: false,
		'width'         		: 'auto',
		'height'        		: 'auto',
		'transitionIn'		: 'none',
		'transitionOut'		: 'none',
		'hideOnContentClick': false
		}
	);
	
	$("#addtopic").click(function(){
		var ta = document.getElementById("talk"); //文本域
	    var con = "输入话题"; 
	    //转载文字 
	    ta.value += " #"+con+"#"; 
	    var l = ta.value.length; 
	    //创建选择区域	
	    if(ta.createTextRange){//IE浏览器 
	        var range = ta.createTextRange(); 
	        range.moveEnd("character",-l)         
	        range.moveEnd("character",l-1); 
	        range.moveStart("character", l-1-con.length); 
	        range.select(); 
	    }else{ 
	        ta.setSelectionRange(l-1-con.length,l-1); 
	        ta.focus(); 
	    } 
	    return false;
	}); 
});

-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div id="sendBox">
			<form name="talkForm" id="talkForm" action="<%=prefix %>/talk" method="post">
			<div id="sendBox_title">来，说点什么吧</div>
			<div id="sendAreaDiv">
				<textarea name="talk" id="talk" rows="5" cols="10"></textarea>
				<input type="hidden" value="" name="pic_url" id="pic_url" />
			</div>
			<div class="sendsub" id="sendsub">
			<div class="fleft smallTxt">
					<a href="#data" id="single_image" style="background:url('<%=sPrefix %>/images/addpic.png') no-repeat;background-position:center left;padding: 0 5px 0 20px;"> 图片</a> |  <a href="#" id="addtopic"  style="background:url('<%=sPrefix %>/images/addpic.png') no-repeat;background-position:center left;padding: 0 5px 0 20px;"> 话题</a> 
			</div>
				<input type="submit" id="sendbt" name="sendbt" value="发表微博" class="sendbt" />
				<span class="sendtip">
					<span class="restTxt">还能输入</span> <span class="countTxt">140</span> <span class="restTxt">字</span>
				</span>
<div class="clear"></div>
			</div>
			</form>
		</div>
		<div id="wbList">
			<div id="ad1"></div>
			<div id="listTools">
				我发送的微博：
			</div>
			
			<ul id="msgList" class="ul_inline ul_fix"><li>em</li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
<div style="display:none">
			<div id="data" class="abox">
				<div class="aboxTitle">
					图片上传
				</div>
				<div class="aboxContent">
				<form name="picupload" id="picupload" action="../picupload" method="post" enctype="multipart/form-data">
				<div id="swfBT">
					<span id="spanSWFUploadButton"></span> 
				</div>
				<div style="padding-left:10px;float: right;width:280px;">
				<div id="uploadInfo" style='font-size: 12px;background-color: #eee; padding:8px;'>图片最大不超过3M,图片格式为jpg,png,gif</div>
				</div><div class="clear"></div>
			</form>
				<div id="uploadPreview" style='padding-top: 5px;'>
					<div style='background-color: #eee; padding:30px 0 70px 0;color:#999;text-align: center;'>图片预览</div>
				</div>
				</div>
			</div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot_sent")); %>