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
out.println(WBJSPCacheOut.out("header1"));
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
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
			setTimeout("$.fancybox.close();",800);
			
			setTimeout("readNew();",2800);
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

	
	
	//按页载入消息
	$.getJSON("<%=prefix %>/msg/inbox?p=1&pz=5&uid=<%=userId%>",function(data){
		for(var i = 0,j=data.length;i<j;i++){
			var d = data[i];
			$("#msgList").append($(talkLI(d,"<%=prefix %>","<%=sPrefix %>")));
		}
		//console.log(data);
	});
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
	s += "</div><div class=\"pubInfo\"><span class=\"fleft\"><a class=\"time\" target=\"_blank\" href=\"";
	//oneTopicUrl
	//s += "/p/t/9579026057805";
	s += "\" title=\"";
	s += new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");
	s += "\">";
	s += sentTime(d.createTime);
	s += "</a> 来自";
	s += d.source;
	s += "</span><div class=\"funBox\"><a href=\"#\" class=\"relay\">转播</a>&nbsp;&nbsp; |&nbsp;&nbsp; <a href=\"/p/t/39552051902918\" class=\"comt\">评论</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"/p/t/39552051902918\" class=\"comt\">收藏</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"#\" class=\"alarm\">举报</a> </div></div></div></li>";
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
		$("#msgList").prepend($(s));
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
				<li>
					<div class="userPic"><a href="/vivizhang2010"> <img src="<%=sPrefix %>/images/user001.jpg" alt="" /></a></div>
						
					<div class="msgBox">
						<div class="userName" ><a href="/vivizhang2010" title="饭小团(@vivizhang2010)" >饭小团</a>转播:&nbsp;</div>
						<div class="msgCnt">感谢Edward支持：）游戏音乐作为游戏文化传播的一个载体，相信会越来越受到网游厂商的重视。<em ><a href="/gametalk" title="" >百家游坛</a>(@gametalk)</em>小飞马(@matrix) 游博小贝(@hadeszhang) 老姚(@老姚) 火狼(@火狼) </div>
						<div class="replyBox">
							<div class="msgBox">
						
								<div class="msgCnt">
						<strong><a href="/gugong" title="故宫(@故宫)" rel="故宫(@故宫)">故宫</a><a href="/certification" title="腾讯认证" target="_blank" class="vip"  rel="腾讯认证"></a>:</strong><a href="/k/%E6%95%85%E5%AE%AB%E7%A7%98%E5%A2%83%E7%B3%BB%E5%88%97">#故宫秘境系列#</a>符望阁位于宁寿宫花园第四进院落，建于乾隆三十七年（1772）。符望阁在形制上模仿建福宫花园的延春阁。平面呈方形，外观两层，内实三层，四角攒尖顶。室内装修颇具特色，以各种不同类型的装修巧妙地分隔空间，穿门越槛之际，往往迷失方向，故俗有“迷楼”之称。<a href="#" class="url" target="_blank" title="">3</a></div>
						
								<div class="mediaWrap">
									<div class="picBox">
										<div class="tools"><a href="#" class="btnBack">向左转</a> | <a href="#" class="btnPrev">向右转</a><a href="#" class="btnOriginal"  target="_blank">查看原图</a></div><a href="#" target="_blank" class="pic"><img alt="[图片]" src="<%=sPrefix %>/images/460.jpg" style="display: inline; " /></a></div>			
	        						</div>

								<div class="pubInfo"><span class="fleft">            <a class="time" target="_blank" href="/p/t/39552051902918" rel="1303395437" title="2011年4月21日 22:17">14分钟前</a> 来自网页 <a class="zfNum" href="/p/t/39552051902918" target="_blank">查看转播和评论(<b class="relayNum">37</b>)</a>          </span>  </div>
							
							</div>
	<div class="clear"></div>
						
					</div><!-- replyBox end -->
						
						
						<div class="pubInfo"><span class="fleft"><a class="time" target="_blank" href="/p/t/9579026057805" title="2011年4月21日 22:24">7分钟前</a> <a href="#" class="f" target="_blank">来自手机(t.3g.qq.com)</a>    </span>
							<div class="funBox"><a href="#" class="relay">转播</a>  <a href="/p/t/39552051902918" class="comt">评论</a>  
								<div class="mFun"><a href="#">更多 </a>
									<div class="mFunDrop">
<p><a href="#" class="reply">对话</a></p><p><a href="#" class="fav" type="1">收藏</a></p><div class="shareBtn"><p><a href="#">发邮件</a></p></div><p><a href="/t/9579026057805" class="detil" target="_blank">详情</a></p><p><a href="#" class="alarm">举报</a></p>
									</div>
								</div>
							</div>
						</div>
						
					</div>
				</li>
				<li>
					<div class="userPic"><a href="/vivizhang2010"> <img src="<%=sPrefix %>/images/user001.jpg" alt="" /></a></div>
						
					<div class="msgBox">
						<div class="userName" ><a href="/vivizhang2010" title="饭小团(@vivizhang2010)" >饭小团</a>转播:&nbsp;</div>
						<div class="msgCnt">感谢Edward支持：）游戏音乐作为游戏文化传播的一个载体，相信会越来越受到网游厂商的重视。<em><a href="/gametalk" title="" >百家游坛</a>(@gametalk)</em>小飞马(@matrix) 游博小贝(@hadeszhang) 老姚(@老姚) 火狼(@火狼) </div>
						
						
						<div class="pubInfo"><span class="fleft"><a class="time" target="_blank" href="/p/t/9579026057805"  rel="1303395885" title="2011年4月21日 22:24">7分钟前</a> <a href="#" class="f" target="_blank">来自手机(t.3g.qq.com)</a>    </span>
							
						</div>
						
					</div>
				</li>
			</ul>
<div class="clear"></div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot_inbox")); %>