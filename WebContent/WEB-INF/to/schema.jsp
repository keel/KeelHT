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
<div class="weight">Intro: - [ <a href="act?act=console&amp;right=kobj&amp;schema">edit</a> ]</div>
<div id="schema_intro"><%=kc.getIntro() %></div>
<div class="weight">Dao: - [ <a href="act?act=console&amp;right=kobj&amp;schema">edit</a> ]</div>
<div><%=kc.getDaoConfig().getDaoName() %></div>
<div class="weight">Columns: - [ <a href="act?act=console&amp;right=kobj&amp;schema">add</a> ]</div>
<table id="schema_columns">
<tr><th>act</th><th>column</th><th>default</th><th>intro</th><th>len</th><th>validator</th></tr>
<%
KObjSchema ks = kc.getKobjSchema();
HashMap<String,KObjColumn> cols = ks.getKObjColumns();
StringBuilder sb = new StringBuilder();
for (Iterator<String> iterator = cols.keySet().iterator(); iterator.hasNext();) {
	String colKey = iterator.next();
	KObjColumn col = cols.get(colKey);
	sb.append("<tr><td><a href=\"act?act=console&amp;right=kobj&amp;schema\">edit</a></td><td>");
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
<div class="weight">Indexes: - [ <a href="act?act=console&amp;right=kobj&amp;schema">add</a> ]</div>
<table id="schema_indexes">
<tr><th>act</th><th>column</th><th>asc</th><th>intro</th><th>type</th><th>unique</th></tr>
<%
HashMap<String,KObjIndex> indexes = ks.getIndexes();
sb = new StringBuilder();
for (Iterator<String> iterator = indexes.keySet().iterator(); iterator.hasNext();) {
	String colKey = iterator.next();
	KObjIndex index = indexes.get(colKey);
	sb.append("<tr><td><a href=\"act?act=console&amp;right=kobj&amp;schema\">edit</a></td><td>");
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
<script type="text/javascript">
//AJAX热编辑
function hotEdit(target,url,paras){
	var $span = $("<span></span>");
	var $inForm = $("<form style='display: inline;' action=''><input type=\"text\" name=\""+$(target).attr("id")+"\" class=\"hotEditInput\"></form>");
	var $bt = $("<input type=\"button\" class=\"hotEditBT\" value=\"EDIT\" />");
	var $bt2 = $("<input type=\"button\" class=\"hotEditBT\" value=\"CANCEL\" />");
	var $target = $(target);
	var oldVal = $target.text();
	//清空原对象
	$target.text("");
	$span.appendTo(target).text(oldVal);
	$inForm.appendTo(target).hide();
	$bt.appendTo(target);
	$bt2.appendTo(target).hide();
	$bt.isSet = false;
	$bt.click(function(){
		if(!$bt.isSet){
			//显示表单并填充input
			var val = $span.text();
			$inForm.show().attr('action',url).find("input[type='text']").val(val);
			$bt.isSet = true;
			$(this).val("SET");
			$bt2.show();
			$span.hide();
		}else{
			//提交
			//var req = $inForm.serialize();
			//$.extend(paras, req);
			$inForm.find("textarea,input:text,input:checkbox:checked,input:radio:checked,option:selected").each(function (i) {
				if(this.tagName.toLowerCase() === "option"){
					if($(this).parent().tagName.toLowerCase() === "select"){
						paras[$(this).parent().name] = this.value;
					}
				}else{
					paras[this.name] = this.value;
				}
				//alert(this.name+":"+this.value);
			});
			//alert(paras);
			$bt.isSet = false;
			$bt2.hide();
			$.post( url, paras ,function( data ) {
				//根据返回值填充 结果
				$span.text(data).show();
				$inForm.hide();
				$bt.val("EDIT");
			    //$bt.removeAttr("disabled");
			}).error(function() { alert("error"); });
		}
	});
	$bt2.click(function(){
		$bt.isSet = false;
		$span.show();
		$inForm.hide();
		$bt.val("EDIT");
		$(this).hide();
	});
}
$(function(){
	hotEdit("#schema_intro","act?act=console&right=kobj&subact=schema_update",{schema_key:"kuser",schema_part : "intro"});
});
</script>