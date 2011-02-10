<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,java.util.*" %>
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
<div id="rightTop">
<span class="weight">KObj config: </span> [ <a href="act?act=console&amp;right=editIni&amp;ini=kobj">edit json</a> ] [ <a href="act?act=console&amp;right=kobj">list</a> ]
<form id="f_search" action="act?act=console&amp;right=kobj" method="post">
<input type="text" id="search_key" name="search_key" /><input type="hidden" id="subact" name="subact" value="search" /><input type="submit" value="search" />
</form>
</div>
<form id="addobj" action="act?act=console&amp;right=kobj" method="post">
<input type="hidden" id="subact" name="subact" value="update" />
<label for="update_key">key:</label><br />
<input type="text" id="update_key" name="update_key" /><br />
<label for="update_json">json:</label><br />
<textarea id="update_json" name="update_json" ></textarea><br />
<input type="submit" value="Add/Update" />
</form>
<% 
String subact = StringUtil.objToStrNotNull(data.getData("subact"));
//String save = StringUtil.objToStrNotNull(data.getData("save"));

%>
