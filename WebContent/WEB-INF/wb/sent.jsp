<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>江苏网微博</title>
<link href="<%=sPrefix %>/css/style_wb.css" rel="stylesheet" type="text/css" />
<script src="<%=sPrefix %>/js/jquery.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.json-2.2.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/hotEdit.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){

});
</script>


</head>
<body>
<div id="header">
	<h1 id="logo"><a href="#">中国江苏网</a></h1>
	<div id="topNav">
		<a href="#">广播大厅</a>
		<a href="#">排行榜</a>
		<a href="#">找朋友</a>
		<a href="#">邀请</a>
		<a href="#">sike</a>
		<a href="#">模板</a> 
		<a href="#">设置</a> 
		<a href="#">私信</a> 
		<a href="#">退出</a>
		
	</div>

   	<div id="topMenu">
		<a href="#">我的首页</a> | <a href="#">我的微博</a> | <a href="#">关系</a>
    </div>
	<div id="searchr">
	<select id="search_select"><option value="talk">广播</option><option value="user">用户</option></select>
	<input type="text" id="searchr-input" name="q" value="请输入关键字" onfocus="this.value=''" onkeydown="if(event.keyCode==13){dosearch()}">
	<input type="button" id="searchr-submit" value="搜 索" onclick="dosearch();">
	</div>
</div>

<div id="wrapper">

	<div id="mainContent">
		<div id="sendBox">
			<div id="sendBox_title">来，说点什么吧- sent</div>
			<div id="sendAreaDiv">
				<textarea name="sendArea"></textarea>
			</div>
			<div id="sendsub">
			<span class="fleft">
					<a href="#">图片</a> | <a href="#">话题</a> </span>
			
				<input type="button" name="" value=" 发布 " id="sendbt"/>
				<span id="sendtip">
					还能输入 <span id="countTxt">140</span> 字
				</span>
<div class="clear"></div>
			</div>
		</div>
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
						<div class="msgCnt">感谢Edward支持：）游戏音乐作为游戏文化传播的一个载体，相信会越来越受到网游厂商的重视。<em rel="@gametalk"><a href="/gametalk" title="" >百家游坛</a>(@gametalk)</em>小飞马(@matrix) 游博小贝(@hadeszhang) 老姚(@老姚) 火狼(@火狼) </div>
						<div class="replyBox">
							<div class="msgBox">
						
								<div class="msgCnt">
						<strong><a href="/gugong" title="故宫(@故宫)" card="1" ctype="2" rel="故宫(@故宫)">故宫</a><a href="/certification" title="腾讯认证" target="_blank" class="vip" card="1" ctype="2" rel="腾讯认证"></a>:</strong><a href="/k/%E6%95%85%E5%AE%AB%E7%A7%98%E5%A2%83%E7%B3%BB%E5%88%97">#故宫秘境系列#</a>符望阁位于宁寿宫花园第四进院落，建于乾隆三十七年（1772）。符望阁在形制上模仿建福宫花园的延春阁。平面呈方形，外观两层，内实三层，四角攒尖顶。室内装修颇具特色，以各种不同类型的装修巧妙地分隔空间，穿门越槛之际，往往迷失方向，故俗有“迷楼”之称。<a href="#" class="url" target="_blank" title="">3</a></div>
						
								<div class="mediaWrap">
									<div class="picBox">
										<div class="tools"><a href="#" class="btnBack">向左转</a> | <a href="#" class="btnPrev">向右转</a><a href="#" class="btnOriginal"  target="_blank">查看原图</a></div><a href="#" target="_blank" class="pic"><img alt="[图片]" src="<%=sPrefix %>/images/460.jpg" style="display: inline; "></a></div>			
	        						</div>

								<div class="pubInfo"><span class="fleft">            <a class="time" target="_blank" href="/p/t/39552051902918" rel="1303395437" from="3" title="2011年4月21日 22:17">14分钟前</a> 来自网页 <a class="zfNum" href="/p/t/39552051902918" target="_blank">查看转播和评论(<b class="relayNum">37</b>)</a>          </span>  </div>
							
							</div>
	<div class="clear"></div>
						
					</div><!-- replyBox end -->
						
						
						<div class="pubInfo"><span class="fleft"><a class="time" target="_blank" href="/p/t/9579026057805" from="4" rel="1303395885" title="2011年4月21日 22:24">7分钟前</a> <a href="#" class="f" target="_blank">来自手机(t.3g.qq.com)</a>    </span>
							<div class="funBox"><a href="#" class="relay" num="37">转播</a> <span>|</span> <a href="/p/t/39552051902918" class="comt" num="0">评论</a> <span>|</span> 
								<span class="mFun"><a href="#">更多<em class="btn_ldrop"></em></a>
									<div class="mFunDrop"><b></b><b class="mask"></b><p><a href="#" class="reply">对话</a></p><p><a href="#" class="fav" type="1">收藏</a></p><div class="shareBtn"><p><a href="#">发邮件</a></p></div><p><a href="/t/9579026057805" class="detil" target="_blank">详情</a></p><p><a href="#" class="alarm">举报</a></p>
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
						<div class="msgCnt">感谢Edward支持：）游戏音乐作为游戏文化传播的一个载体，相信会越来越受到网游厂商的重视。<em rel="@gametalk"><a href="/gametalk" title="" >百家游坛</a>(@gametalk)</em>小飞马(@matrix) 游博小贝(@hadeszhang) 老姚(@老姚) 火狼(@火狼) </div>
						
						
						<div class="pubInfo"><span class="fleft"><a class="time" target="_blank" href="/p/t/9579026057805" from="4" rel="1303395885" title="2011年4月21日 22:24">7分钟前</a> <a href="#" class="f" target="_blank">来自手机(t.3g.qq.com)</a>    </span>
							
						</div>
						
					</div>
				</li>

			</ul>
<div class="clear"></div>
		</div>

	</div>
	
	
	<div id="sideNav">
		<div id="userInfo">
			<div id="userHead">
				<div class="fleft">
				<a href="#"><img border="0" src="<%=sPrefix %>/images/user001.jpg" /></a></div>
				<div id="userNameLocal" class="fright">
					<a href="#">SIke</a><br />
					江苏 南京
				</div>
<div class="clear"></div>
			</div>
			<ul id="userNums" class="ul_inline">
				<li>
					<span class="uNumNum">3<span><br /><a href="#">关注</a>
				</li>
				<li>
					<span class="uNumNum">3<span><br /><a href="#">粉丝</a>
				</li>
				<li>
					<span class="uNumNum">4<span><br /><a href="#">微博</a>
				</li>

			</ul>
			<div id="badgeBox">
				badgeBox
			</div>
		</div>
		<div id="sideMenu">
			<ul class="ul_inline">
				<li class="sm_c"><a href="#"><sapn class="ico_mypub">icon</span> 我的主页</a></li>
				<li class="sm_m"><a href="#"><sapn class="ico_mypub">icon</span> 我的广播</a></li>
				<li class="sm_m"><a href="#"><sapn class="ico_mypub">icon</span> 提到我的</a></li>
				<li class="sm_m"><a href="#"><sapn class="ico_mypub">icon</span> 我的收藏</a></li>
				<li class="sm_m"><a href="#"><sapn class="ico_mypub">icon</span> 私信</a></li>
			</ul>
<div class="clear"></div>
		</div>
		
		<div id="proposal" class="SC">
			<div id="propo">
				<span  class="sideTitle">推荐好友：</span> &gt;&gt; <a href="#" >换一换</a>
			</div>
			<div class="imgList">
			<ul id="user_proposal" class="ul_imgList">
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a><br /><a href="#" class="f_link">+收听</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a><br /><a href="#" class="f_link">+收听</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a><br /><a href="#" class="f_link">+收听</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a><br /><a href="#" class="f_link">+收听</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a><br /><a href="#" class="f_link">+收听</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a><br /><a href="#" class="f_link">+收听</a></li>
			</ul>
			</div>
<div class="clear"></div>
		</div>
		
		<div id="hotTopics" class="SC">
			<div class="sideTitle">
				热门话题：
			</div>
			<ul>
				<li><a href="#">热门话题目题目</a><span class="num">(32423422)</span></li>
				<li><a href="#">热门话题目</a><span class="num">(32423422)</span></li>
				<li><a href="#">热门话题目</a><span class="num">(32423422)</span></li>
			</ul>
<div class="clear"></div>
		</div>

	</div>

<div class="clear"></div>

</div>

<div id="footer">
	<div id="copyright">
		Copyright:&copy;2010-2012 KEEL.SIKE All rights reserved. 
	</div>
</div>

</body>
</html>