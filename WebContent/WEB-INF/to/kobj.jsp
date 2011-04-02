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
<span class="weight">KObj config: </span> [ <a href="act?act=console&amp;right=editIni&amp;ini=kobj">edit json</a> ] [ <a href="act?act=console&amp;right=schema_add">add schema</a> ]
<form id="f_search" action="act?act=console&amp;right=kobj" method="post">
<input type="text" id="search_key" name="search_key" /><input type="hidden" id="subact" name="subact" value="search" /><input type="submit" value="search" />
</form>
</div>
<% 
String subact = StringUtil.objToStrNotNull(data.getData("subact"));
//String save = StringUtil.objToStrNotNull(data.getData("save"));
Object od = data.getData("list");
if(od==null){
	out.print("list not exist.");
	return;
}
try{
	HashMap<String, KObjConfig> kcMap = (HashMap<String, KObjConfig>)od;
	if(kcMap.size()<=0){
		out.print("Empty.");
		return;
	}else{
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = kcMap.keySet().iterator(); it.hasNext();) {
			String kobjName =  it.next();
			KObjConfig kc = kcMap.get(kobjName);
			//HashMap<String,Object> table = (HashMap<String,Object>)kobjMap.get(kobjName);
			sb.append("<p class='tb_list' ><span class='orangeBold'>");
			sb.append(kobjName).append("</span> ");
			//sb.append(kc.getIntro());
			sb.append(" [ <a href='act?act=console&amp;right=kobj&amp;subact=schema_find&amp;schema_key=")
			.append(kobjName)
			.append("'>schema</a> | <a href='act?act=console&amp;right=kobj&amp;subact=query&amp;schema_key=");
			sb.append(kobjName);
			sb.append("'>query KObject</a>  | <a href='act?act=console&amp;right=kobj&amp;subact=kobj_add&amp;schema_key=");
			sb.append(kobjName);
			sb.append("'>add KObject</a> ] - ");
			sb.append(kc.getIntro());
			sb.append("</p>\r\n");
			
		}
		out.print(sb.toString());
	}
}catch(Exception e){
	out.print("Error:"+e.getMessage());
	return;
}
 %>
