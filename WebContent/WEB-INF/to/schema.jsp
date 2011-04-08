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
[ <a href="act?act=console&amp;right=editIni&amp;ini=kobj">edit json</a> | 
<a href="act?act=console&amp;right=kobj">list</a> |  
<a href="act?act=console&amp;right=kobj">query KObject</a> |
<a href="act?act=console&amp;right=kobj">add KObject</a> | 
<a href="act?act=console&amp;right=kobj&amp;subact=ini_save">save INI</a> ] 
</div>
<div id="re"></div>
<div class="weight">Intro:</div>
<div id="schema_intro"><%=kc.getIntro() %></div>
<div class="weight">Dao: </div>
<div id="schema_daojson"><%=JSONTool.writeJsonString(kc.getDaoConfig().toMap())%></div>
<div class="weight">Columns: - <span id="schema_col_add"></span></div>
<table id="schema_columns">
<tr><th>column</th><th>default</th><th>type</th><th>intro</th><th>len</th><th>validator</th><th>EDIT</th></tr>
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
	sb.append(col.getType());
	sb.append("</td><td>");
	sb.append(col.getIntro());
	sb.append("</td><td>");
	sb.append(col.getLen());
	sb.append("</td><td>");
	sb.append(col.getValidatorString());
	sb.append("</td><td></td></tr>\r\n");
}
out.println(sb);
%>
</table>
<div class="weight">Indexes: - <span id="schema_index_add"></span></div>
<table id="schema_indexes">
<tr><th>column</th><th>asc</th><th>intro</th><th>type</th><th>unique</th><th>EDIT</th></tr>
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
	sb.append("</td><td></td></tr>\r\n");
}
out.println(sb);
%>
</table>
<script type="text/javascript">
$(function(){
	//intro
	var p_intro = {
		preParas : {schema_key:"<%= kc.getKobjName()%>",schema_part:"intro"},
		url:"act?act=console&right=kobj&subact=schema_update",
		key:["schema_intro"],
		msg:"#re"
	};
	$.hotEditor.act(p_intro,"#schema_intro");
	//dao
	var p_dao = {
		key : ["schema_daojson"],
		preParas:{schema_key:"<%= kc.getKobjName()%>",schema_part:"dao"},
		url:"act?act=console&right=kobj&subact=schema_update",
		msg:"#re"
	};
	$.hotEditor.act(p_dao,"#schema_daojson");
	//col
	var p_cols = {
		subs:["td:eq(0)","td:eq(1)","td:eq(2)","td:eq(3)","td:eq(4)","td:eq(5)"],
		key : ["col","def","type","intro","len","validator"],
		url:"act?act=console&right=kobj&subact=schema_update",
		editor : [$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor],
		bts : "td:eq(6)",
		jsonTyps:["s","a","i","s","i","s"],
		jsonToStr:"schema_coljson",
		msg:"#re"
		,addTarget:">"
		//,addBT:"#schema_col_add"
	};
	$("#schema_columns tr:gt(0)").each(function (i) {
		$.hotEditor.act(p_cols,this);
	});
	//index
	var p_indexes = {
		subs:["td:eq(0)","td:eq(1)","td:eq(2)","td:eq(3)","td:eq(4)"],
		url:"act?act=console&right=kobj&subact=schema_update",
		key : ["col","asc","intro","type","unique"],
		editor : [$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor],
		bts : "td:eq(5)",
		jsonTyps:["s","b","s","s","b"],
		jsonToStr:"schema_indexjson",
		msg:"#re"
		,addTarget:">"
		//,addBT:"#schema_index_add"
	};
	$("#schema_indexes tr:gt(0)").each(function (i) {
		$.hotEditor.act(p_indexes,this);
	});

});
</script>