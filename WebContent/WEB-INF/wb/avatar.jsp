<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,java.net.URLEncoder,java.io.*,com.k99k.wb.acts.*" session="false" %>
<%!//编辑页面中包含 camera.swf 的 HTML 代码
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();
	public String renderHtml(String id,String basePath,String userName) {
		String u = basePath + "/upload/avatarUpload.jsp";
		String uc_api = null;
		try{
		uc_api = URLEncoder.encode(u,"utf-8");
		}catch(Exception e){
		}
		String urlCameraFlash = prefix+"/js/camera.swf?nt=1&inajax=1&appid=1&input="+userName+"&uploadSize=500&ucapi="
				+ uc_api;
		urlCameraFlash = "<script src=\""+prefix+"/js/common.js?B6k\" type=\"text/javascript\"></script><script type=\"text/javascript\">document.write(AC_FL_RunContent(\"width\",\"450\",\"height\",\"253\",\"scale\",\"exactfit\",\"src\",\""
				+ urlCameraFlash
				+ "\",\"id\",\"mycamera\",\"name\",\"mycamera\",\"quality\",\"high\",\"bgcolor\",\"#000000\",\"wmode\",\"transparent\",\"menu\",\"false\",\"swLiveConnect\",\"true\",\"allowScriptAccess\",\"always\"));</script>";

		return urlCameraFlash;
	}
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
out.println(WBJSPCacheOut.out("header1")); %>
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.city_date.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	$("#wbUserUrl").empty().append("<%=uName%>");
	$("#r_follow_num").empty().append("<%=user.getProp("friends_count")%>");
	$("#r_fans_num").empty().append("<%=user.getProp("followers_count")%>");
	$("#r_uname").empty().append("<%=uName%>");
	$("#r_mgs_num").empty().append("<%=user.getProp("statuses_count")%>");
	$("#r_icon_1").empty().append("<img src='<%=sPrefix+"/images/upload/"+uName+"_2.jpg"%>' height='60' width='60' />");
	$("#r_location").empty().append("<%=user.getProp("location")%>");
	
	
	$("#logoutBT").click(function(){
		$.post("<%=prefix %>/login/logout", "uName=<%=uName %>" ,function(data) {
			window.location="<%=prefix %>/";
		});
		return false;
	});

});
function updateavatar(){
	var r = new Date();
	var src = "<%=sPrefix+"/images/upload/"+user.getName()+"_2.jpg?t="%>"+r;
	$("#avatar_u").attr("src",src);
	$("#r_icon_1 img").attr("src",src);
}
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div class="indexh">
			<div class="taboff"><a href="<%=prefix %>/settings/basic">基本设置</a></div>
			<div class="tabon">修改头像</div>
			<div class="taboff"><a href="<%=prefix %>/settings/changepwd">修改密码</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/checkmail">邮箱验证</a></div>
			<div class="taboff">个性模板</div>
		</div>
		<div class="mainBox">
			<div id="pic_big" class="tCenter">
				<img src="<%=sPrefix %>/images/avatar_pic1.jpg" />
			</div>
			<div class="tCenter">
			<div>
			<div style="float:left;width:150px;padding:20px 0 20px 40px;"><img src="<%=sPrefix+"/images/upload/"+user.getName()+"_2.jpg" %>" alt="<%=user.getName() %>" id="avatar_u" style="border:1px solid #999999;padding:1px;width:120px;height:120px"></div>
			<div style="float:right;width:330px;padding:30px 10px;text-align:left;"><div class="bold">上传并设置新头像</div><div>请选择一个新照片进行上传编辑。
头像保存后，如果头像不能立即显示，请刷新本页面。</div></div>
			<div class="clear"></div>
			</div>
<%
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ request.getContextPath();
//System.out.println(basePath);
out.print(renderHtml("5",basePath,user.getName()));
%>
			</div>
		</div>
<% out.println(WBJSPCacheOut.out("@foot")); %>