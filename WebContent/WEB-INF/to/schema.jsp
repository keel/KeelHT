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
KObjConfig kc = (KObjConfig)data.getData("schema_find");
if(kc == null){
	out.print("KObjConfig is null.");
	return;
}
%>
<div id="rightTop">
<span class="weight">KObj schema: <%=kc.getKobjName() %></span> 
[ <a href="act?act=console&amp;right=editIni&amp;ini=kobj">edit json</a> ] 
[ <a href="act?act=console&amp;right=kobj">list</a> ] 
<form id="f_search" action="act?act=console&amp;right=kobj" method="post">
<input type="text" id="search_key" name="search_key" /><input type="hidden" id="subact" name="subact" value="search" /><input type="submit" value="search" />
</form>
</div>
<div class="weight">Intro: - [ <a href="act?act=console&amp;right=kobj&amp;schema">edit</a> ]</div>
<div><%=kc.getIntro() %></div>
<div class="weight">Dao: - [ <a href="act?act=console&amp;right=kobj&amp;schema">edit</a> ]</div>
<div><%=kc.getDaoConfig().getDaoName() %></div>
<div class="weight">Columns: - [ <a href="act?act=console&amp;right=kobj&amp;schema">edit</a> ]</div>
<table id="schema_columns">
<tr><th>column</th><th>default</th><th>intro</th><th>len</th><th>validator</th></tr>
<%
KObjSchema ks = kc.getKobjSchema();
HashMap<String,KObjColumn> cols = ks.getKObjColumns();
StringBuilder sb = new StringBuilder();
for (Iterator<String> iterator = cols.keySet().iterator(); iterator.hasNext();) {
	String colKey = iterator.next();
	KObjColumn col = cols.get(colKey);
	sb.append("<tr><td>");
	sb.append(col.getCol());
	sb.append("</td><td>");
	sb.append(col.getDef());
	sb.append("</td><td>");
	sb.append(col.getIntro());
	sb.append("</td><td>");
	sb.append(col.getLen());
	sb.append("</td><td>");
	sb.append(col.getValidatorString());
	sb.append("</td></tr>\r\n");
}
out.println(sb);
%>
</table>
<div class="weight">Indexes: - [ <a href="act?act=console&amp;right=kobj&amp;schema">edit</a> ]</div>
<table id="schema_indexes">
<tr><th>column</th><th>asc</th><th>intro</th><th>type</th><th>unique</th></tr>
<%
HashMap<String,KObjIndex> indexes = ks.getIndexes();
sb = new StringBuilder();
for (Iterator<String> iterator = indexes.keySet().iterator(); iterator.hasNext();) {
	String colKey = iterator.next();
	KObjIndex index = indexes.get(colKey);
	sb.append("<tr><td>");
	sb.append(index.getCol());
	sb.append("</td><td>");
	sb.append(index.isAsc());
	sb.append("</td><td>");
	sb.append(index.getIntro());
	sb.append("</td><td>");
	sb.append(index.getType());
	sb.append("</td><td>");
	sb.append(index.isUnique());
	sb.append("</td></tr>\r\n");
}
out.println(sb);
%>
</table>

