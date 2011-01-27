<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*" %>
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
<p>config:</p>
<% 
String subact = (data.getData("subact")==null)?"":data.getData("subact").toString();
String save = (data.getData("save")==null)?"":data.getData("save").toString();
if(subact.equals("save")){ 
	out.print(save);
%>

<%} else{ %>
<form id="commenForm" action="act?act=console&amp;right=editIni&amp;subact=save" method="post">
<textarea name="json" id="json" rows="15" cols="120" /><%=data.getData("json") %></textarea>
<p><input class="submit" type="submit" value=" Save " /></p>
</form>
<% } %>