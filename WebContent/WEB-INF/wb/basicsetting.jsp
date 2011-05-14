<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,java.net.URLEncoder,java.io.*,com.k99k.wb.acts.*" session="false" %>
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
out.println(WBJSPCacheOut.out("header1")); 
%>
<link rel="stylesheet" href="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.css" type="text/css" media="screen" />
<script src="<%=sPrefix %>/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="<%=sPrefix %>/js/jquery.city_date.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=sPrefix %>/fancybox/jquery.fancybox-1.3.4.pack.js"></script><script type="text/javascript">
$(function(){
	
	//放下地区选择
	$.cnCity.cnCity("#cnCity","<%= user.getProp("location")%>");
	//---------------
	//选择生日
	$.cnBirth(1891,2011,"#cnBirth","<%= user.getProp("birthday")%>");
	//--------

	//处理请求
	$.validator.dealAjax = {
		bt:$("#submitBT"),
		ok:function(data){
			$.fancybox(
			'<p class="fancyMsgBox1" >用户信息已保存</p>',
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
			'<p class="fancyMsgBox2" >保存失败,请检查输入的内容.</p>',
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
</script>
<% out.println(WBJSPCacheOut.out("@head_main")); %>
		<div class="indexh">
			<div class="tabon">基本设置</div>
			<div class="taboff"><a href="<%=prefix %>/settings/avatar">修改头像</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/changepwd">修改密码</a></div>
			<div class="taboff"><a href="<%=prefix %>/settings/checkmail">邮箱验证</a></div>
			<div class="taboff">个性模板</div>
		</div>
		<div class="mainBox">
			<form class="centerForm" name="settingForm" id="settingForm" action="<%=prefix %>/settings/basic/set" method="post">
				<table>
				<tr>
				<td class="txtRight" valign="top">帐号：</td>
				<td><%=user.getName() %></td>
				</tr>
				<tr>
				<td class="txtRight" valign="top"><span class="red bold">* </span>姓名：</td>
				<td><input type="text" name="uNick" value="<%=user.getProp("screen_name") %>" id="uNick"/></td>
				</tr>
				<tr>
				<td class="txtRight" valign="top"><span class="red bold">* </span>性别：<%String sex = user.getProp("sex").toString(); %></td>
				<td><input name="uSex" id="uSex" type="radio" value="2" <%if(sex.equals("2")){out.print("checked=\"checked\"");} %>> <label for="uSex">女</label>&nbsp;&nbsp;<input name="uSex" id="uSex2" type="radio" value="1" <%if(sex.equals("1")){out.print("checked=\"checked\"");} %>> <label for="uSex2">男</label></td>
				</tr>
				<tr>
				<td class="txtRight" valign="top"><span class="red bold">* </span>居住地：</td>
				<td id="cnCity"></td>
				</tr>
				<tr>
				<td class="txtRight" valign="top">生日：</td>
				<td id="cnBirth"></td>
				</tr>
				<tr>
				<td class="txtRight" valign="top">个人主页：<%String uUrl = user.getProp("user_url").toString(); %></td>
				<td><input type="text" name="uUrl" value="<%=(StringUtil.isStringWithLen(uUrl,1))?uUrl:"http://" %>" id="uUrl"/></td>
				</tr>
				<tr>
				<td class="txtRight" valign="top">个人介绍：</td>
				<td><textarea name="uIntro" id="uIntro"><%=user.getProp("description") %></textarea></td>
				</tr>
				<tr>
				<td></td>
				<td><input type="submit" name="submitBT" value=" 保存修改 " id="submitBT"/></td>
				</tr>
				</table>
			
			</form>
		</div>
<% out.println(WBJSPCacheOut.out("@foot")); %>