<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,com.k99k.wb.acts.*" session="false" %>
<%
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
out.println(WBJSPCacheOut.out("header1"));
%>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//处理请求
	$.validator.dealAjax = {
		bt:$("#submitBT"),
		ok:function(data){
			//200表示成功,data为返回的uName
			window.location = ("<%=sPrefix %>/"+data);
		},
		err:function(){
			alert('登录失败!用户名和密码未通过验证.');
		}
	};

	//开始验证
	$('#loginForm').validate({
	    /* 设置验证规则 */
	    rules: {
	        uName: {
	            required:true,
	            stringCheck:true,
	            rangelength:[4,15]
	        },
	        uPwd:{
	            required:true,
	            pwdCheck:true,
	            rangelength:[6,30]
	        }
	    }
	});
});
</script>
</head>
<body>
<div id="home_header" class="wrapper">
	<h1 id="logo"><a href="#">中国江苏网</a></h1>
</div>

<div id="home_wrapper" class="wrapper">

	<div id="home_main">
		<div id="newMsgs">
			<div class="sideTitle">
				他们正在说……
			</div>
				<div class="SC">
			<ul id="msgList" class="ul_inline">
				<li>
					<div class="userPic"><a href="/vivizhang2010"> <img src="images/user001.jpg" title="" /></a></div>
						
					<div class="msgBox">
						<div class="userName" ><a href="/vivizhang2010" title="饭小团(@vivizhang2010)" >饭小团</a>转播:&nbsp;</div>
						<div class="msgCnt">感谢Edward支持：）游戏音乐作为游戏文化传播的一个载体，相信会越来越受到网游厂商的重视。<em rel="@gametalk"><a href="/gametalk" title="" >百家游坛</a>(@gametalk)</em>小飞马(@matrix) 游博小贝(@hadeszhang) 老姚(@老姚) 火狼(@火狼) </div>
						
						
						<div class="pubInfo"><span class="fleft"><a class="time" target="_blank" href="/p/t/9579026057805" from="4" rel="1303395885" title="2011年4月21日 22:24">7分钟前</a> <a href="#" class="f" target="_blank">来自手机(t.3g.qq.com)</a>    </span>
							
						</div>
						
					</div>
				</li>
				<li>
					<div class="userPic"><a href="/vivizhang2010"> <img src="images/user001.jpg" title="" /></a></div>
						
					<div class="msgBox">
						<div class="userName" ><a href="/vivizhang2010" title="饭小团(@vivizhang2010)" >饭小团</a>转播:&nbsp;</div>
						<div class="msgCnt">感谢Edward支持：）游戏音乐作为游戏文化传播的一个载体，相信会越来越受到网游厂商的重视。<em rel="@gametalk"><a href="/gametalk" title="" >百家游坛</a>(@gametalk)</em>小飞马(@matrix) 游博小贝(@hadeszhang) 老姚(@老姚) 火狼(@火狼) </div>
						
						
						<div class="pubInfo"><span class="fleft"><a class="time" target="_blank" href="/p/t/9579026057805" from="4" rel="1303395885" title="2011年4月21日 22:24">7分钟前</a> <a href="#" class="f" target="_blank">来自手机(t.3g.qq.com)</a>    </span>
							
						</div>
						
					</div>
				</li>
			</ul>
				</div>
		</div>

		<div id="somebody">
			<div class="sideTitle">
				有趣的人
			</div>
			<div class="SC">
				<ul id="user_proposal" class="ul_imgList">
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
				<li><a href="#" ><img src="<%=sPrefix %>/images/50.jpg" ></a><br /><a title="马伊琍(@马伊琍)" href="#" target="_blank">马伊琍</a></li>
			</ul>
				</div>
		</div>
		<div id="sometopics">
			<div class="sideTitle">
				有趣的话题
			</div>
			<div class="SC">
			<div id="hotTopics">
			<ul>
				<li><a href="#">热门话题目题目</a><span class="num">(32423422)</span></li>
				<li><a href="#">热门话题目</a><span class="num">(32423422)</span></li>
				<li><a href="#">热门话题目</a><span class="num">(32423422)</span></li>
			</ul>
				
			</div>

			</div>
		</div>
<div class="clear"></div>
	</div>

	
	<div id="home_sideNav">
		<div id="reg">
			注册微博
		</div>
		<form name="loginForm" id="loginForm" method="post" action="<%=sPrefix %>/login/login" class="outBorder">
			<div class="inBorder">
			<p>
			<label for="uName">用户名：</label><br /><input type="text" name="uName" value="" id="uName"/></p>
			<p>
			<label for="uPwd">密码：</label><br /><input type="password" name="uPwd" value="" id="uPwd"/></p>
			<p>
        <input type="checkbox" name="uCookie" id="uCookie"  value="true" /><label class="txt2" for="uCookie">下次自动登录</label>
    </p>
			<p class="tCenter">
			<input type="submit" id="submitBT" name="submitBT" value="立即登录"/>
			</p>
			</div>
		</form>
		<div id="help">
			<div class="sideTitle">
				使用帮助
			</div>
			<div class="SC">
			<ul>
				<li><a href="#">热门话题目题目</a><span class="num">(32423422)</span></li>
				<li><a href="#">热门话题目</a><span class="num">(32423422)</span></li>
				<li><a href="#">热门话题目</a><span class="num">(32423422)</span></li>
			</ul>
			</div>
		</div>
	</div>
<div class="clear"></div>
</div>
<div id="home_footer" class="wrapper">
	<div id="copyright">
		Copyright:&copy;2010-2012 KEEL.SIKE All rights reserved. 
	</div>
</div>
</body>
</html>