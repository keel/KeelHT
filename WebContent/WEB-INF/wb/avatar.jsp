<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,java.net.URLEncoder,java.io.*" session="false" %>
<%!//编辑页面中包含 camera.swf 的 HTML 代码
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
	public String renderHtml(String id,String basePath) {
		String u = basePath + "/upload/avatarUpload.jsp";
		String uc_api = null;
		try{
		uc_api = URLEncoder.encode(u,"utf-8");
		}catch(Exception e){
		}
		String urlCameraFlash = prefix+"/js/camera.swf?nt=1&inajax=1&appid=1&input=1&uploadSize=1000&ucapi="
				+ uc_api;
		urlCameraFlash = "<script src=\""+prefix+"/js/common.js?B6k\" type=\"text/javascript\"></script><script type=\"text/javascript\">document.write(AC_FL_RunContent(\"width\",\"450\",\"height\",\"253\",\"scale\",\"exactfit\",\"src\",\""
				+ urlCameraFlash
				+ "\",\"id\",\"mycamera\",\"name\",\"mycamera\",\"quality\",\"high\",\"bgcolor\",\"#000000\",\"wmode\",\"transparent\",\"menu\",\"false\",\"swLiveConnect\",\"true\",\"allowScriptAccess\",\"always\"));</script>";

		return urlCameraFlash;
	}
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>江苏网微博</title>
<link rel="stylesheet" type="text/css" href="<%=sPrefix %>/css/style_wb.css" />
<script src="<%=sPrefix %>/js/jquery.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.city_date.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){

//放下地区选择
$.cnCity.cnCity("#cnCity");
//---------------
//选择生日
$.cnBirth(1891,2011,"#cnBirth");
//--------

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
$('#settingForm').validate({
    /* 设置验证规则 */
    rules: {
        uNick: {
            required:true,
            rangelength:[4,15]
        },
        uUrl:{url:true},
        uSex:{required:true},
        cnLocal2:{required:function(element) {
        	return $(element).val() == "";
        }}
    }
});

});
function updateavatar(){
	alert("OK!");
}
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
	<input type="text"  name="q" value="请输入关键字"  />
	<input type="button"  value="搜 索"  />
	</div>
</div>

<div id="wrapper">

	<div id="mainContent">
		<div class="indexh">
			<div class="taboff"><a href="#">基本设置</a></div>
			<div class="tabon"><a href="#">修改头像</a></div>
			<div class="taboff"><a href="#">修改密码</a></div>
			<div class="taboff"><a href="#">邮箱验证</a></div>
			<div class="taboff"><a href="#">个性模板</a></div>
		</div>
		<div class="mainBox">
			<div id="pic_big" class="tCenter">
				<img src="<%=sPrefix %>/images/avatar_pic1.jpg" />
			</div>
			<div class="tCenter">
<%
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ request.getContextPath();
//System.out.println(basePath);
out.print(renderHtml("5",basePath));
%>
			</div>
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
					<span class="uNumNum">3</span><br /><a href="#">关注</a>
				</li>
				<li>
					<span class="uNumNum">3</span><br /><a href="#">粉丝</a>
				</li>
				<li>
					<span class="uNumNum">4</span><br /><a href="#">微博</a>
				</li>

			</ul>
			<div id="badgeBox">
				badgeBox
			</div>
		</div>
		<div id="sideMenu">
			<ul class="ul_inline">
				<li class="sm_c"><a href="#"><span class="ico_mypub">icon</span> 我的主页</a></li>
				<li class="sm_m"><a href="#"><span class="ico_mypub">icon</span> 我的广播</a></li>
				<li class="sm_m"><a href="#"><span class="ico_mypub">icon</span> 提到我的</a></li>
				<li class="sm_m"><a href="#"><span class="ico_mypub">icon</span> 我的收藏</a></li>
				<li class="sm_m"><a href="#"><span class="ico_mypub">icon</span> 私信</a></li>
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
