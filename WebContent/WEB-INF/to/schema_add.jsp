<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,com.k99k.tools.*,java.util.*" %>
<div id="rightTop">
<span class="weight">KObj schema add</span> 
[ <a href="act?act=console&amp;right=editIni&amp;ini=kobj">edit json</a> | 
<a href="act?act=console&amp;right=kobj">list</a> |  
<a href="act?act=console&amp;right=kobj">query KObject</a> |
<a href="act?act=console&amp;right=kobj">add KObject</a> | 
<a href="act?act=console&amp;right=kobj&amp;subact=ini_save">save INI</a> ] 
</div>
<div id="re"></div>
<div class="weight">KObj name:</div>
<div id="schema_name"></div>
<div class="weight">Intro:</div>
<div id="schema_intro"></div>
<div class="weight">Dao: </div>
<div id="schema_daojson">{"tableName":"HTItem","newDaoName":"mongoItemDao","daoName":"mongoDao","props":{"id":11,"type":"single"}}</div>
<div class="weight">Columns: - <span id="schema_col_add"></span></div>
<table id="schema_columns">
<tr><th>column</th><th>default</th><th>type</th><th>intro</th><th>len</th><th>validator</th><th>EDIT</th></tr>
<tr><td>col</td><td>default</td><td>type</td><td>intro</td><td>0</td><td></td><td></td></tr>
</table>
<div class="weight">Indexes: - <span id="schema_index_add"></span></div>
<table id="schema_indexes">
<tr><th>column</th><th>asc</th><th>intro</th><th>type</th><th>unique</th><th>EDIT</th></tr>
<tr><td>col</td><td>false</td><td>intro</td><td>type</td><td>false</td><td></td></tr>
</table>
<script type="text/javascript">
$(function(){
	var kobjName = "";
	//intro
	var p_intro = {
		preParas : {schema_key:kobjName,schema_part:"intro"},
		url:"act?act=console&right=kobj&subact=schema_update",
		key:["schema_intro"],
		msg:"#re"
	};
	$.hotEditor.act(p_intro,"#schema_intro");
	//dao
	var p_dao = {
		key : ["schema_daojson"],
		preParas:{schema_key:kobjName,schema_part:"dao"},
		url:"act?act=console&right=kobj&subact=schema_update",
		editor:[$.hotEditor.textAreaEditor],
		msg:"#re"
	};
	$.hotEditor.act(p_dao,"#schema_daojson");
	//col
	var p_cols = {
		preParas:{schema_key:kobjName,schema_part:"col_edit"},
		subs:["td:eq(0)","td:eq(1)","td:eq(2)","td:eq(3)","td:eq(4)","td:eq(5)"],
		key : ["col","def","type","intro","len","validator"],
		url:"act?act=console&right=kobj&subact=schema_update",
		editor : [$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor],
		bts : "td:eq(6)",
		jsonTyps:["s","a","i","s","i","s"],
		jsonToStr:"schema_coljson",
		msg:"#re"
		,addTarget:">"
		,delBT:">"
		,delPreParas:{schema_key:kobjName,schema_part:"col_del"}
		//,addBT:"#schema_col_add"
	};
	$("#schema_columns tr:gt(0)").each(function (i) {
		$.hotEditor.act(p_cols,this);
	});
	//index
	var p_indexes = {
		preParas:{schema_key:kobjName,schema_part:"index_edit"},
		subs:["td:eq(0)","td:eq(1)","td:eq(2)","td:eq(3)","td:eq(4)"],
		url:"act?act=console&right=kobj&subact=schema_update",
		key : ["col","asc","intro","type","unique"],
		editor : [$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor,$.hotEditor.inputTextEditor],
		bts : "td:eq(5)",
		jsonTyps:["s","b","s","s","b"],
		jsonToStr:"schema_indexjson",
		msg:"#re"
		,addTarget:">"
		,delBT:">"
		,delPreParas:{schema_key:kobjName,schema_part:"index_del"}
		//,addBT:"#schema_index_add"
	};
	$("#schema_indexes tr:gt(0)").each(function (i) {
		$.hotEditor.act(p_indexes,this);
	});

});
</script>