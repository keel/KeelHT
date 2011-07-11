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

	talkForm("#talkForm");

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

	comms = $("#commsDiv");
	commsLoading = $("#commsLoading");
	
	$("#replycheckbox").change(function(){
		var $me = $(this);
		if(this.checked){
			$("#comm_talk_state").val("0");
		}else{
			$("#comm_talk_state").val("1");
		}
	});
	setInterval(notify, 10000);
});
var checkNotify = true;
function talkForm(form,readNew){
    var $form = $(form),$a = $form.find("textarea"),$countTxt = $form.find(".countTxt"),rests = $form.find(".restTxt"),sendbt = $form.find(".sendbt");
	$a.keyup(function(){
		var cc = 140-$(this).val().length;
		$(rests[1]).text("字").removeClass("red");
		if(cc >= 0){
			$countTxt.text(cc).removeClass("red");
			$(rests[0]).text("还能输入");
		}else{
			$countTxt.text(cc).addClass("red");
			$(rests[0]).text("超出");
		}
		
	});
	
	$form.submit(function(event) {
	    event.preventDefault(); 
       msg = $.trim($a.val()),
       url = $form.attr( 'action' );
       if(msg.length <= 0){
       		$(rests[0]).text("");$countTxt.text("");
       		$(rests[1]).text("请输入内容").addClass("red").hide().fadeIn('slow');
       		return;
       }
       if(msg.length>140){
       		$countTxt.parent().hide().fadeIn('slow');
       		return;
       }
	   $.post( url, $form.serialize(),function( data ) {
	         checkNotify = false;
	       
			$.fancybox(
			'<p class="fancyMsgBox1" >消息发送成功!</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
			$a.val("");
			setTimeout("$.fancybox.close();",1000);
			var readnew = "readNew();";
			if(readNew){readnew=readNew;};
			setTimeout(readnew,2000);
	     }
	   ).error(function(){
			$.fancybox(
				'<p class="fancyMsgBox2" >消息发送失败.您可能需要重新登录。</p>',
				{	'autoDimensions'	: false,
					'width'         	: 300,
					'height'        	: 'auto',
					'transitionIn'		: 'none',
					'transitionOut'		: 'none',
					'hideOnContentClick': true
			});
		});
	});
}
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
			s += talkLI(d,"<%=prefix %>","<%=sPrefix %>",<%=user.getId() %>);
		}
		$("#msgList").prepend(s);
		//console.log(data);
	});
	checkNotify = true;
}

var comms,commsLoading;
function readComms(mid,cc,prefix,sPrefix,isON){
	if(isON){comms.ON = false;};
	if(comms.ON && (comms.mid==mid)){
		comms.appendTo($("#hideContent"));
		comms.ON = false;
		return;
	}else{
		comms.ON = true;
	}
	comms.mid = mid;
	var $m = $("#m_"+mid);
	$("#comm_rt_id").val(mid);
	$("#comm_rt_userId").val($m.find(".r_userId").val());
	$("#comm_rt_name").val($m.find(".r_name").val());
	talkForm("#commForm","readComms("+mid+",1,'"+prefix+"','"+sPrefix+"',true);");
	if(cc>0){
		commsLoading.appendTo($m.find(".msgBox")[0]).show();	
		$.getJSON(prefix+"/msg/comms?mid="+mid+"&uid=0&r="+new Date(),function(data){
			var s = "";
			for(var i = 0,j=data.length;i<j;i++){
				var d = data[i];
				s+="<li class='commsLi'><div style='float:left;padding:0 10px;'><img src='";
				s+=sPrefix;
				s+="/images/upload/";
				s+=d.user_name;
				s+="_3.jpg' alt='' width='40' height='40' /></div> <div style='padding:5px;'>";
				s+="<a href=\"/";
				s += prefix;
				s += "/";
				s += d.user_name;
				s += "\" title=\"";
				s += d.user_screen;
				s += "(@";
				s += d.user_name;
				s += ")\"  target='_blank'>";
				s += d.user_screen;
				s += "</a> <span class='commSubInfo'>";
				s+=sentTime(d.createTime);
				s+=" 通过 ";
				s += d.source;
				s+="</span> <br />";
				s+=d.text;
				s+="</div></li>";
			}
			comms.find("#commsUL").html(s);
			$m.find(".commNUM").text(data.length);
			commsLoading.hide();
			if(data.length>=10){$("#forAllComms").show();}else{$("#forAllComms").hide()};
			comms.appendTo($m.find(".msgBox")[0]);
			return;
		}).error(function(){
			
		});
	}
	commsLoading.hide();
	comms.find("#commsUL").html("");
	comms.appendTo($m.find(".msgBox")[0]);
	return false;
}
-->
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div id="sendBox">
			<form name="talkForm" id="talkForm" action="/KHunter/talk" method="post">
			<div id="sendBox_title">来，说点什么吧</div>
			<div id="sendAreaDiv">
				<textarea name="talk" id="talk" rows="5" cols="10"></textarea>
				<input type="hidden" value="" name="pic_url">
			</div>
			<div class="sendsub" id="sendsub">
			<span class="fleft">
					<a href="#">图片</a> | <a href="#">话题</a> </span>
			
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
			
			<div id="newsBox" style="display:none;"><a href="#"></a></div>
			<ul id="msgList" class="ul_inline"><li></li></ul>
			<div id="pageNav"></div>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>