/*
//地区选择
//$.cnCity.cnCity("#cnCity");
//---------------
//选择生日
//$.cnBirth(1891,2011,"#cnBirth");
*/
$.cnCity = {
	province:["选择省份","江苏省","北京","天津","上海","重庆","广东省","浙江省","福建省","湖南省","湖北省","山东省","辽宁省","吉林省","云南省","四川省","安徽省","江西省","黑龙江省","河北省","陕西省","海南省","河南省","山西省","内蒙古","广西省","贵州省","宁夏省","青海省","新疆","西藏","甘肃省","台湾省","香港","澳门","国外"],
	city : [
    ["南京","徐州","连云港","淮安","盐城","扬州","南通","镇江","常州","无锡","苏州","泰州","宿迁","昆山","其他"],
    ["北京"],
    ["天津"],
    ["上海"],
    ["重庆"],
    ["广州","深圳","珠海","汕头","韶关","河源","梅州","惠州","汕尾","东莞","中山","江门","佛山","阳江","湛江","茂名","肇庆","清远","潮州","揭阳","云浮","其他"],
    ["杭州","宁波","温州","嘉兴","绍兴","金华","衢州","舟山","台州","丽水","湖州","其他"],
    ["福州","厦门","三明","莆田","泉州","漳州","南平","宁德","龙岩","其他"],
    ["长沙","株洲","湘潭","衡阳","邵阳","岳阳","常德","张家界","娄底","郴州","永州","怀化","益阳","湘西","其他"],
    ["武汉","黄石","襄樊","十堰","宜昌","荆州","鄂州","孝感","黄冈","咸宁","荆门","随州","天门","仙桃","潜江","神农架","恩施","其他"],
    ["济南","青岛","淄博","枣庄","东营","潍坊","烟台","威海","济宁","泰安","日照","莱芜","德州","滨州","临沂","荷泽","聊城","其他"],
    ["沈阳","铁岭","抚顺","大连","本溪","营口","锦州","盘锦","辽阳","鞍山","丹东","朝阳","阜新","其他"],
    ["长春","吉林","通化","四平","辽源","白城","延边","白山","松原","其他"],
    ["昆明","曲靖","大理","玉溪","丽江","楚雄","迪庆","文山","昭通","保山","其他"],
    ["成都","宜宾","泸州","内江","攀枝花","德阳","雅安","遂宁","南充","绵阳","广元","华鉴","乐山","其他"],
    ["合肥","芜湖","马鞍山","蚌埠","铜陵","淮北","淮南","亳州","巢湖","黄山","宿州","阜阳","六安","滁州","池州","安庆","其他"],
    ["南昌","九江","鹰潭","宜春","新余","萍乡","赣州","吉安","抚州","上饶","其他"],
    ["哈尔滨","佳木斯","牡丹江","大庆","齐齐哈尔","绥化","伊春","鹤岗","七台河","双鸭山","鸡西","黑河","其他"],
    ["石家庄","邯郸","保定","张家口","秦皇岛","邢台","唐山","廊坊","衡水","沧州","承德","其他"],
    ["西安","咸阳","宝鸡","铜川","渭南","延安","汉中","榆林","其他"],
    ["海口","三亚","琼海","儋州","其他"],
    ["郑州","洛阳","开封","鹤壁","焦作","许昌","驻马店","周口","新乡","安阳","濮阳","信阳","平顶山","三门峡","南阳","商丘","其他"],
    ["太原","大同","忻州","临汾","运城","长治","阳泉","晋城","其他"],
    ["呼和浩特","赤峰","包头","乌兰察布","锡林浩特","通辽","其他"],
    ["南宁","桂林","北海","柳州","玉林","百色","河池","钦州","梧州","其他"],
    ["贵阳","遵义","铜仁","六盘水","铜仁","安顺","其他"],
    ["银川","固原","吴忠","石嘴山","其他"],
    ["西宁","海东","海北","玉树","其他"],
    ["乌鲁木齐","石河子","哈密","阿克苏","昌吉","伊犁","吐鲁番","喀什","和田","其他"],
    ["拉萨","那曲","其他"],
    ["兰州","酒泉","临夏","张掖","嘉峪关","金昌","平凉","白银","武威","天水","其他"],
    ["台湾"],
    ["香港"],
    ["澳门"],
    ["海外"]
],
	cnCity:function(target,preVal){
	var select1 = $("<select name=\"cnLocal1\" id=\"cnLocal1\"></select>");
	var select2 = $("<select name=\"cnLocal2\" id=\"cnLocal2\"><option value=''>选择城市</option></select>");
	for (var i=0; i < this.province.length; i++) {
		var op = $("<option value=\""+this.province[i]+"\">"+this.province[i]+"</option>");
		select1.append(op);
	};
	select1.change(function(){
		var i = select1[0].selectedIndex-1;
		if(i>=0){
			select2.empty();
			var c = $.cnCity.city;
			for (var j=0; j < c[i].length; j++) {
				var text = c[i][j];
       			 $("<option value='"+text+"'>"+text+"</option>").appendTo(select2[0]);
			};
		}
	});
	if(preVal){var arr = preVal.split("-");
		if(arr.length==2){
		select1.val(arr[0]);select1.change();select2.val(arr[1]);}}
	$(target).append(select1).append(select2);
	}
};
$.cnBirth = function(fromYear,toYear,target,preVal){
	var y = $("<select name=\"cnYear\" id=\"cnYear\"></select>");
	var yy = toYear - fromYear +1;
	for (var i=0; i < yy; i++) {
		var year = fromYear+i;
		$("<option value='"+year+"'>"+year+"年</option>").appendTo(y);
	};
	var m = $("<select name=\"cnMonth\" id=\"cnMonth\"></select>");
	for (var i=1; i < 13; i++) {
		$("<option value='"+i+"'>"+i+"月</option>").appendTo(m);
	};
	var d = $("<select name=\"cnDay\" id=\"cnDay\"></select>");
	for (var i=1; i < 32; i++) {
		$("<option value='"+i+"'>"+i+"日</option>").appendTo(d);
	};	
	if(preVal){var arr = preVal.split("-");
		if(arr.length==3 ){
		y.val(arr[0]);m.val(arr[1]);d.val(arr[2]);}}
	$(target).append(y).append(m).append(d);
};
