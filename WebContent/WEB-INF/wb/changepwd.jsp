<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,java.net.URLEncoder,java.io.*,com.k99k.wb.acts.*" session="false" %>
<%!//编辑页面中包含 camera.swf 的 HTML 代码
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
out.println(WBJSPCacheOut.out("header1")); 
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
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
	
	//处理请求
	$.validator.dealAjax = {
		bt:$("#submitBT"),
		ok:function(data){
			$.fancybox(
			'<p class="fancyMsgBox1" >新密码已保存!</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
		},
		err:function(){
			$.fancybox(
			'<p class="fancyMsgBox2" >密码修改失败!请确认原密码输入正确.</p>',
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
	$('#settingForm').validate({
	    // 设置验证规则
	    rules: {
			orgPwd:{
		        required:true,
		        pwdCheck:true,
		        rangelength:[6,30]
		    },
		    newPwd:{
		        required:true,
		        pwdCheck:true,
		        rangelength:[6,30]
		    },
		    newPwd2:{
		    	equalTo: "#newPwd"
		    }
	    }
	});

});
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div class="indexh">
			<div class="taboff"><a href="<%=prefix %>/settings/basic">基本设置</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/avatar">修改头像</a></div>
			<div class="tabon">修改密码</div>
			<div class="taboff"><a href="<%=prefix %>/settings/checkmail">邮箱验证</a></div>
			<div class="taboff">个性模板</div>
		</div>
		<div class="mainBox">
			<form class="centerForm" name="settingForm" id="settingForm" action="<%=prefix %>/settings/changepwd/set" method="post">
				<div>
				<label for="orgPwd">原始密码：</label><br>
				<input type="password" name="orgPwd" value="" id="orgPwd" />
				</div>
				<div>
				<label for="newPwd">新的密码：</label><br>
				<input type="password" name="newPwd" value="" id="newPwd" />
				</div>
				<div>
				<label for="newPwd2">确认密码：</label><br>
				<input type="password" name="newPwd2" value="" id="newPwd2" />
				</div>
				<div>
				<input type="submit" name="submitBT" value=" 确认修改 " id="submitBT"/>
				</div>
			</form>
		</div>
<% out.println(WBJSPCacheOut.out("@foot")); %>