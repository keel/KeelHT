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
	out.print("attr is null.");
	return;
}
KObject user = (KObject)data.getData("wbUser");
String uName = user.getName();
out.println(WBJSPCacheOut.out("header1"));
%>
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#wbUserUrl").empty().append("<%=uName%>");
	$("#r_follow_num").empty().append("<%=user.getProp("friends_count")%>");
	$("#r_fans_num").empty().append("<%=user.getProp("followers_count")%>");
	$("#r_uname").empty().append("<%=uName%>");
	$("#r_mgs_num").empty().append("<%=user.getProp("statuses_count")%>");
	$("#r_icon_1").empty().append("<img src='<%=sPrefix+"/images/upload/"+uName+"_2.jpg"%>' height='60' width='60' />");
	$("#r_location").empty().append("<%=user.getProp("location")%>");
	
	$("#logoutBT").text("登录");
	$("#logoutBT").click(function(){
		window.location="<%=prefix %>/login";
		return false;
	});
});
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		
		<div id="wbList">
			<div id="ad1"></div>
			<div id="listTools">
				所有广播：
			</div>
			
			<div id="newsBox"><a href="#">有2条新消息,点击查看</a></div>
			<ul id="msgList" class="ul_inline">
				<li>
					<div class="userPic"><a href="/vivizhang2010"> <img src="<%=sPrefix %>/images/user001.jpg" title="" /></a></div>
						
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
							<div class="funBox"><a href="#" class="relay">转播</a> <span>|</span> <a href="/p/t/39552051902918" class="comt">评论</a> <span>|</span> 
								<span class="mFun"><a href="#">更多<em class="btn_ldrop"></em></a>
									<div class="mFunDrop">
<p><a href="#" class="reply">对话</a></p><p><a href="#" class="fav" type="1">收藏</a></p><div class="shareBtn"><p><a href="#">发邮件</a></p></div><p><a href="/t/9579026057805" class="detil" target="_blank">详情</a></p><p><a href="#" class="alarm">举报</a></p>
									</div>
								</span>
							</div>
						</div>
						
					</div>
				</li>
				<li>
					<div class="userPic"><a href="/vivizhang2010"> <img src="<%=sPrefix %>/images/user001.jpg" title="" /></a></div>
						
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