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
<div id="rightTop" class="weight">Edit config:</div>
<% 
String subact = StringUtil.objToStrNotNull(data.getData("subact"));
String save = StringUtil.objToStrNotNull(data.getData("save"));
if(subact.equals("save")){ 
	out.print(save);
} else{ %>
<form id="commenForm" action="act?act=console&amp;right=editIni" method="post">
<input type="hidden" id="subact" name="subact" value="save" />
<input type="hidden" id="ini" name="ini" value="kconfig" />
<textarea name="json" id="json" rows="25" cols="120" /><%=data.getData("json") %></textarea>
<p><input class="bt_submit" type="submit" value=" Save " /> [ <a href="act?act=console&amp;right=<%=data.getData("ini")%>">Back</a> ]</p>
</form>
<% } %>