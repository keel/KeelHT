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

	
	$("#logoutBT").click(function(){
		$.post("<%=prefix %>/login/logout", "uName=<%=uName %>" ,function(data) {
			window.location="<%=prefix %>/<%=uName %>";
		});
		return false;
	});
	$.sPrefix = "<%=prefix %>";
	$.prefix = "<%=sPrefix %>";
	var talkF = $("#talkForm");
	talkF.readNew = "readNew();";
	talkForm(talkF);
	pageNav.pre="上一页";
 	pageNav.next="下一页";
	pageNav.fn = function(p,pn){
		//按页载入消息
		$.getJSON("<%=prefix %>/msg/inbox?p="+p+"&pz=10&uid=<%=userId%>&r="+new Date(),function(data){
			var s = "";
			for(var i = 0,j=data.length;i<j;i++){
				var d = data[i];
				s += talkLI(d,<%=user.getId() %>);
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
	initUpload();
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
	setInterval(notify, 10000);
});
function notify(){
	if(checkNotify){
		$.getJSON("<%=prefix %>/notify?r="+new Date(),function(data){
			if(data && data.length == 4){
				var s = "";
				if(data[0] > 0){
					s += "有"+data[0]+"条新消息.";
				}
				if(data[1] > 0){
					s += "有"+data[1]+"个新粉丝.";
				}
				if(data[2] > 0){
					s += "有"+data[2]+"条新私信.";
				}
				if(data[3] > 0){
					s += "有"+data[3]+"条新消息提到您.";
				}
				if(s != ""){
					$("#newsBox a").html(s);$("#newsBox").show().click(function(){
						readNew();
					});
				}else{
					$("#newsBox a").html("");$("#newsBox").hide();
				}
			}
		});
	}
}

function readNew(){
	checkNotify = false;
	$("#newsBox a").html("");$("#newsBox").hide();
	$.getJSON("<%=prefix %>/msg/unread?max=15&uid=<%=userId%>",function(data){
		var s = "";
		for(var i = 0,j=data.length;i<j;i++){
			var d = data[i];
			s += talkLI(d,<%=user.getId() %>);
		}
		$("#msgList").prepend(s);
	});
	checkNotify = true;
}
var swfu ;
function initUpload(){
 swfu= new SWFUpload({
	upload_url : "<%=prefix %>/picupload",
	flash_url : "<%=sPrefix %>/js/swfupload.swf",
	post_params: {"f":"<%=uName %>.jpg"},
	use_query_string:true, 
	
	button_placeholder_id : "spanSWFUploadButton",
	button_width: "100",
	button_height: "30",
	button_text: "<span class='aboxTxt1'>选择上传图片</span>",
	button_text_style: ".aboxTxt1 { font-size: 12px; }",
	button_text_left_padding: 12,
	button_text_top_padding: 6,
	
	prevent_swf_caching:false,
	file_queue_error_handler : fileQueueError,
	file_dialog_complete_handler : fileDialogComplete,
	upload_progress_handler : uploadProgress,
	upload_error_handler : uploadError,
	upload_success_handler : uploadSuccess,

prevent_swf_caching: 'false',

	file_types : "*.jpg;*.png;*.gif",  
      file_types_description : "图片文件", 
	file_size_limit : "3000"
	//,debug:true
	
	});
}
function swferr(info){
	$("#uploadInfo").html(info).css("color","red");
}
function swfinfo(info){
	$("#uploadInfo").html(info).css("color","black");
}
function swfok(info){
	$("#uploadInfo").html(info).css("color","green");
}
function swfreset(){
//将picUrl设置为空
$("#pic_url").val("");
	$.fancybox.close();
	swfinfo("图片最大不超过3M,图片格式为jpg,png,gif");
	$("#uploadPreview").html("<div style='background-color: #eee; padding:58px 0 70px 0;color:#999;text-align: center;'>图片预览</div>");
}
function fileQueueError(file, errorCode, message) {
	try {
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			swferr("图片文件大小为空.");
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			swferr("图片文件超过大小限制.");
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			swferr("无效的文件类型.");
			break;
		default:
			swferr("选择文件错误.");
			break;
		}
	} catch (ex) {
		swferr("选择文件错误.");
	}
}
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesQueued > 0) {
			this.startUpload();return;
		}
	} catch (ex) {}
}
function uploadProgress(file, bytesLoaded) {

	try {
		if(swfu.startProg){
			var percent = Math.ceil((bytesLoaded / file.size) * 100);
			$("#swfuploadProgress").text(percent);			
		}else{
			swfu.startProg = true;	
			swfinfo("文件上传中......已上传 [<span id='swfuploadProgress'>0</span> ] % , <a href='javascript:swfu.cancelUpload(\""+file.id+"\");'>取消</a>");
		}
	} catch (ex) {}
}
function uploadSuccess(file, serverData) {
	try {
		var re = eval((serverData));
		if(re.length==3){
		$("#pic_url").val(re[2]);
		swfok("上传成功!............[ <a href='javascript:$.fancybox.close();'>点击完成</a> ]  [<a href='javascript:swfreset();'>取消图片</a>]");
		$("#uploadPreview").html("<div style='text-align:center;'><img src='../images/upload/"+re[2]+"' alt='"+re[0]+"' /></div>");			
		}else{swferr("上传后出现错误.");}
	} catch (ex) {}
}
function uploadError(file, errorCode, message) {
	try {
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			swfinfo("上传已取消.");
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			swfinfo("上传已停止.");
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			swferr("上传文件超过服务器限制.");
			break;
		default:
			swferr("上传失败:"+message);
			break;
		}
	} catch (ex3) {
	}
}

-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div id="sendBox">
			<form name="talkForm" id="talkForm" action="/KHunter/talk" method="post">
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
				所有广播：
			</div>
			
			<div id="newsBox" style="display:none;"></div>
			<ul id="msgList" class="ul_inline ul_fix"><li>empty</li></ul>
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