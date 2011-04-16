<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*" %>
<%
Object o = request.getAttribute("jspAttr");
HttpActionMsg data = null;
if(o != null ){
	data = (HttpActionMsg)o;
}else{
	out.print("attr is null.");
	return;
}
%>
<div id="rightTop" class="weight">Reload:</div>
<% 
String subact = StringUtil.objToStrNotNull(data.getData("subact"));
String sub = StringUtil.objToStrNotNull(data.getData("sub"));
String re = StringUtil.objToStrNotNull(data.getData("re"));
String re_name = StringUtil.objToStrNotNull(data.getData("re_name"));
//显示结果
if(!re.equals("")){ 
	out.print(re);
} 
//确认
else if(subact.equals("confirm")){
%>
<form id="confirmForm" action="act?act=console&amp;right=reload" method="post">
Reload <%=sub %> <%=re_name %>  ? 
<input type="hidden" name="subact" value="<%=sub %>" />
<input type="hidden" name="reload_name" value="<%=re_name%>" />
<p><input type="submit" value=" Yes " /> [ <a href="act?act=console&amp;right=reload&amp;subact=show">Cancel</a> ]</p>
</form>
<% }
//显示reload菜单
else{ %>
<form id="reloadForm" action="act?act=console&amp;right=reload" method="post">
<ul>
<li class="hasReName"><input type="radio" name="sub" value="action" />action : <span></span></li>
<li class="hasReName"><input type="radio" name="sub" value="dao" />dao : <span></span></li>
<li class="hasReName"><input type="radio" name="sub" value="kobj" />kobj : <span></span></li>
<li><input type="radio" name="sub" value="allactions" />All Actions</li>
<li><input type="radio" name="sub" value="alldaos" />All Daos</li>
<li><input type="radio" name="sub" value="allkobjs" />All Kobjs</li>
<li><input type="radio" name="sub" value="system" />System</li>
</ul>
<input type="hidden" name="subact" value="confirm" />
<input type="submit" value=" RELOAD " />
</form>
<script type="text/javascript">
$(function(){
var re_name = $("<input type=\"text\" name=\"re_name\" />");
$("#reloadForm input:radio").click(function(){
	$(".hasReName span").html("");
	var li = $(this).parent();
	if(li.attr("class") === "hasReName"){
		li.find("span").html(re_name);
	}
});
});
</script>
<% }%>
<hr />
<p>
&gt;&gt; <a href="act?act=console&amp;right=reload&amp;subact=stop">STOP all requert for maintenance</a><br />
&gt;&gt; <a href="act?act=console&amp;right=reload&amp;subact=start&amp;keelcontrolsall=true">Come back from maintenance</a>
</p>