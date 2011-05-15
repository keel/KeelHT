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
	out.print("attr is null.");
	return;
}
KObject user = (KObject)data.getData("wbUser");
String uEmail = user.getProp("email").toString();
out.println(WBJSPCacheOut.out("header1")); %>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<script type="text/javascript">
$(function(){
	//处理请求
	$.validator.dealAjax = {
		bt:$("#submitBT"),
		ok:function(data){
			$("#orgEmail").val(data);
			$.fancybox(
			'<p class="fancyMsgBox1" >新邮箱已保存!</p>',
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
			'<p class="fancyMsgBox2" >邮箱修改失败,请确认输入正确.</p>',
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
			newEmail:{
		        required:true,
		        email:true
		    }
	    }
	});
	//发送验证邮件
	$("#verifyMail").click(function(){
		$.post("<%=prefix %>/settings/checkmail/verify", "verify=verify" ,function(data) {
			$.fancybox(
			'<p class="fancyMsgBox1" >验证邮件已发送,请到您的邮箱接收验证邮件,点击其中的链接进行验证.</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
		}).error(function() {
			$.fancybox(
					'<p class="fancyMsgBox2" >验证邮件发送失败,请稍后再试.</p>',
					{	'autoDimensions'	: false,
						'width'         	: 300,
						'height'        	: 'auto',
						'transitionIn'		: 'none',
						'transitionOut'		: 'none',
						'hideOnContentClick': true
					});
		});
	});
		
});
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div class="indexh">
			<div class="taboff"><a href="<%=prefix %>/settings/basic">基本设置</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/avatar">修改头像</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/changepwd">修改密码</a></div>
			<div class="tabon">邮箱验证</div>
			<div class="taboff">个性模板</div>
		</div>
		<div class="mainBox">
			<div>
			<%if(user.getState() == 0){%>
			<div style="width:450px;border:1px solid #CCC;background:#eee;padding:20px;">您的 Email 还未通过验证，请到<%=uEmail %> 查收验证信，并点击其中的链接完成验证； 如果没有收到，可以重发验证信。
			<br /><input type="button" value="发送验证邮件" id="verifyMail" />
			</div>
			<%} %>
			</div>
			<form class="centerForm" name="settingForm" id="settingForm" action="<%=prefix %>/settings/checkmail/modify" method="post">
				<div>
				<label for="orgEmail">旧Email地址：</label><br>
				<input type="text" name="orgEmail" value="<%=uEmail%>" id="orgEmail" disabled="disabled" />
				</div>
				<div>
				<label for="newEmail">新Email地址：</label><br>
				<input type="text" name="newEmail" value="" id="newEmail" />
				</div>
				<div>
				<input type="submit" name="submitBT" value=" 修改邮箱 " id="submitBT" />
				</div>
			</form>
		</div>
<% out.println(WBJSPCacheOut.out("@foot")); %>