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
String kobj_key = data.getData("key").toString();
KObjConfig kc = (KObjConfig)data.getData("kc");
KObjSchema ks = kc.getKobjSchema();
//直接命令,为query或add
String direct_act = data.getData("direct_act").toString();
ArrayList<KObjColumn> cols = ks.getColList();
%>
<div id="rightTop">
<span class="weight">KObj [<%=kobj_key %>] actions: </span>
<input type="button" id="act_query" value="Query" /> <input type="button" id="act_add" value="Add" /> 
</div>
<div id="query_form">
Query:<br />
<select name="q_select">
<%
StringBuilder sb = new StringBuilder();
for(Iterator<KObjColumn> it = cols.iterator(); it.hasNext();){
	KObjColumn col = it.next();
	String colName = col.getCol();
	sb.append("<option value='"+colName+"'>"+colName+"</option>");
}
String colSelect = sb.toString();
out.print(colSelect);
%></select>
 : <input type="text" name="q_select_val" id="q_select_val" />
<br /><br />Fields:<br />
<%
sb = new StringBuilder();
for(Iterator<KObjColumn> it = cols.iterator(); it.hasNext();){
	KObjColumn col = it.next();
	String colName = col.getCol();
	sb.append("<input type=\"checkbox\" name=\"q_field\" value=\""+colName+"\" />"+colName+"<br />");
}
String colCheckBox = sb.toString();
out.print(colCheckBox);
%>
<br />Skip: <input type="text" id="q_skip" value="0" /><br />
<br />Len: <input type="text" id="q_len" value="20" /><br />
<br />Order by:<select name="q_order"><%=colSelect %></select> <select name="q_order"><option value="1">ASC</option><option value="-1">DESC</option></select><br />
<br />Hint:<br />
<%
HashMap<String,KObjIndex> indexes = ks.getIndexes();
sb = new StringBuilder();
for(Iterator<String> it = indexes.keySet().iterator(); it.hasNext();){
	String indexKey = it.next();
	sb.append("<input type=\"checkbox\" name=\"q_hint\" value=\""+indexKey+"\" />"+indexKey+"<br />");
}
String indexesCheckBox = sb.toString();
out.print(indexesCheckBox);
%>
<p>
<input type="button" id="q_submit" value="Query" />
</p>
</div>
<script type="text/javascript">
(function(){
	var key = <%=kobj_key%>;
	//query
	$("#act_query").click(function(){
		
	});
});
</script>

