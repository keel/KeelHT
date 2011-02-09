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
<p><span class="weight">KObj config: </span> [ <a href="act?act=console&amp;right=editIni&amp;target=kobj">edit json</a> ] [ <a href="#">add new</a> ] <input type="text" id="list_search" /><input type="button" value="search" /></p>
<% 
String subact = StringUtil.objToStrNotNull(data.getData("subact"));
//String save = StringUtil.objToStrNotNull(data.getData("save"));
Object od = data.getData("list");
if(od==null){
	out.print("list not exist.");
	return;
}
try{
	Map<String, Object> kobjMap = (Map<String, Object>)od;
	if(kobjMap.size()<=0){
		out.print("Empty.");
		return;
	}else{
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = kobjMap.keySet().iterator(); it.hasNext();) {
			String kobjName =  it.next();
			HashMap<String,Object> table = (HashMap<String,Object>)kobjMap.get(kobjName);
			sb.append("<p class='tb_list' ><a href='#' class='weight'>");
			sb.append(kobjName).append("</a> ");
			sb.append(table.get("intro"));
			sb.append(" - [ <a href='#'>show</a> | <a href='#'>modify</a> ]</p>");
		}
		out.print(sb.toString());
	}
}catch(Exception e){
	out.print("Error:"+e.getMessage());
	return;
}
 %>
