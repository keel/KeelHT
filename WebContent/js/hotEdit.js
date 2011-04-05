$.hotEditor = {
inputTextEditor : "<input type=\"text\" name=\"t\" class=\"hotEditInput\">",
textAreaEditor : "<textarea name=\"a\" class=\"hotEditTA\"></textarea>",
HENull : "HE#NULL",
checkBoxEditor : "<input type=\"checkbox\" name=\"c\" class=\"hotEditCheckbox\">",
selectEditor : "<select name=\"s\"> <option value=\"true\">true</option> <option value=\"false\">false</option> </select>"
}
/*
 * target:定位到editor目标,如:"#targetTB"
 * url:AJAX的提交URL
 * paras:提交的默认参数集，即不需要使用input来编辑的参数，提交AJAX时将带上此参数集，如:{schema_key:"kuser"}
 ** editParas:需要编辑输入的参数集，如:
 var eParas2 = {
	//val如存在则默认为此值，若无则为原key的text值,HE#NULL表示为空值
	//val:["aaa","HE#NULL","HE#NULL","HE#NULL","HE#NULL"], 
	//editParas.jsonToStr 设置后将使所有的输入形成一个json字符串,作为jsonToStr:json的键值对进入paras
	//jsonToStr:"schema_indexjson",
	target:["td:eq(0)","td:eq(1)","td:eq(2)","td:eq(3)","td:eq(4)"],
	key:["ta","tb","tc","td","te"],
	editor:[
		$.hotEditor.inputTextEditor,
		$.hotEditor.inputTextEditor,
		$.hotEditor.selectEditor,
		$.hotEditor.HENull,
		$.hotEditor.checkBoxEditor
	],
	bts:"td:eq(5)"
}
 ** 服务器返回数据，如:
 {
	re:"ok",
	d:[
		"re1","re2","re3",4,"re5"
 	]
 }
 */
$.hotEditor.act = function (target,url,paras,editParas,errMsg) {
	var $span = $("<span class='hotEditSpan'></span>");
	var $tar = $(target);
	var oldparas = $.extend({},paras);
	var $bt = $("<input type=\"button\" class=\"hotEditBT\" value=\"EDIT\" />");
	var $bt2 = $("<input type=\"button\" class=\"hotEditBT\" value=\"CANCEL\" />");
	//设置root
	if (!editParas.root) {editParas.root = $tar;};
	//检验editParas是否正确
	var eparasOK = false;
	var len = editParas.key.length;
	if (len === 1 && editParas.editor.length===1) {
		if (editParas.val) {
			eparasOK = (editParas.val.length === 1);
			editParas.hasVal = true;
		}else{eparasOK = true};
	}else if (editParas.key && editParas.editor && editParas.target) {
		eparasOK = (len === editParas.editor.length && len === editParas.target.length);
		if (editParas.val) {
			eparasOK = (editParas.val.length === len);
			editParas.hasVal = true;
		};
	};
	if(!editParas.bts){eparasOK = false;}
	if (!eparasOK) {alert("editParas error!");return false;};
	//初始化
	var tars = [];
	for (var i=0; i < len; i++) {
		if (!editParas.target) {
			tars[i] = $tar;
		}else{
			tars[i] = $tar.find(editParas.target[i]);
		};
		if(editParas.hasVal && (editParas.val[i] != "HE#NULL")){
			tars[i].oldVal = editParas.val[i];
			tars[i].span = $span.clone().text(tars[i].text());
		}else{
			tars[i].oldVal = tars[i].text();
			tars[i].span = $span.clone().text(tars[i].oldVal);
		};
		tars[i].text("");
		if (editParas.editor[i] ==="HE#NULL") {
			tars[i].ed = tars[i].span.clone();
		}else{
			tars[i].ed = $(editParas.editor[i]).attr("name",editParas.key[i]).val(tars[i].oldVal);
		};
		
		tars[i].append(tars[i].span).append(tars[i].ed.hide());
	};
	//放置按钮
	$tar.bt1 = $bt.clone();
	$tar.bt2 = $bt2.clone().hide();
	if($tar.find(editParas.bts).length === 0){
		$tar.append($tar.bt1).append($tar.bt2);
	}else{
		$tar.find(editParas.bts).append($tar.bt1).append($tar.bt2);
	};
	$tar.bt1.isSet = false;
	$tar.stateA = function(){
		for (var i=0; i < len; i++) {
			var w = tars[i].span.width()+5;
			if (w<6) {w=50;};
			tars[i].span.hide();
			tars[i].ed.show().width(w);
		}
		$tar.bt1.val("SET");
		$tar.bt1.isSet = true;
		$tar.bt2.show();
	};
	$tar.stateB = function(){
		for (var i=0; i < len; i++) {
			tars[i].span.show();
			tars[i].ed.hide();
		}
		$tar.bt1.val("EDIT").removeAttr("disabled");
		$tar.bt2.hide();
		$tar.bt1.isSet = false;
	};
	$tar.bt1.click(function  () {
		if (!$tar.bt1.isSet) {
			//原状态,转为可编辑
			$tar.stateA();
		}else{
			//编辑状态,提交更新动作
			$tar.bt1.val("loading").attr("disabled","disabled");
			var newParas = {};
			$tar.find("textarea,input:text,input:checkbox:checked,input:radio:checked,option:selected").each(function (i) {
				if(this.tagName.toLowerCase() === "option"){
					if($(this).parent().get(0).tagName.toLowerCase() === "select"){
						newParas[$(this).parent().get(0).name] = this.value;
					}
				}else{
					newParas[this.name] = this.value;
				}
			});
			//editParas.jsonToStr 设置后将使所有的输入形成一个json字符串,作为jsonToStr:json的键值对进入paras
			if (editParas.jsonToStr) {
				var jsonStr = $.toJSON(newParas);
				paras = $.extend({},oldparas);
				paras[editParas.jsonToStr] = jsonStr;
			}else{
				paras = $.extend(newParas,oldparas);
			};
			
			
			/*
			for(k in paras){
    			alert ("paras["+k +"] = "+paras[k]);
			}
			*/
			console.log(paras);
			
			$.post( url, paras ,function(data) {
				//alert("success:"+data);
				try{
					var re = $.parseJSON(data);
				}catch(e){
					$(errMsg).text("parseJSON err:"+data);
				}
				//根据返回值填充 结果
				if (re != null && re.re && re.re === "ok" && re.d && re.d.length === len) {
					//填充数据
					for (var i=0; i < len; i++) {
						if (re.d[i] != "HE#NULL") {
							tars[i].span.text(re.d[i]);
							tars[i].ed.val(re.d[i]);
						};
					}
					$tar.stateB();
					$(errMsg).text(re.re);
				}else{
					//显示错误
					$tar.stateB();
					if (re == null) {$(errMsg).text("re:null");}else{$(errMsg).text(re.re+":"+re.d);};
				};
			}).error(function() {$(errMsg).text("post failed.");$tar.stateB(); });
			
		};
	});
	$tar.bt2.click(function() {
		$tar.stateB();
	});
}
