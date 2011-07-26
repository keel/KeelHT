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
//cookie用户
Object userObj = data.getData("wbUser");
KObject user = null;
String uName = "null";
long userId = 0;
if(userObj!=null){
	user = (KObject)userObj;
	uName = user.getName();
	userId = user.getId();
}
int p = (StringUtil.isDigits(request.getParameter("p")))?Integer.parseInt(request.getParameter("p")):1;
int cc = Integer.parseInt(data.getData("sum")+"");
if(cc>50){cc=50;};
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
	
	$.sPrefix = "<%=prefix %>";
	$.prefix = "<%=sPrefix %>";
	pageNav.pre="上一页";
 	pageNav.next="下一页";
	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/topic/list?t=<%=data.getData("topic")%>&p="+p+"&pz=10&r="+new Date(),function(data){
			var s = "";
			for(var i = 0,j=data.length;i<j;i++){
				var d = data[i];
				s += talkLI(d,0);
			}
			$("#msgList").html(s);
			
		});
	};
	
	pageNav.go(<%= p %>,<%= p %>);
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
			<div id="sendBox_title">说点和此话题相关的</div>
			<div id="sendAreaDiv">
				<textarea name="talk" id="talk" rows="5" cols="10">#<%=data.getData("topic") %># </textarea>
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
				此话题相关的微博：
			</div>
			<ul id="msgList" class="ul_inline ul_fix"><li id="emptyLI" style="text-align: center;">暂无</li></ul>
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
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>