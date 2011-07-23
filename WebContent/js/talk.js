var comms,commsLoading,commsTalk;
var checkNotify = true;

/* $form.readNew 为自定义方法 */
function talkForm($form){
    var $a = $form.find("textarea"),$countTxt = $form.find(".countTxt"),rests = $form.find(".restTxt"),sendbt = $form.find(".sendbt");
	$a.keyup(function(){
		var cc = 140-$(this).val().length;
		$(rests[1]).text("字").removeClass("red");
		if(cc >= 0){
			$countTxt.text(cc).removeClass("red");
			$(rests[0]).text("还能输入");
		}else{
			$countTxt.text(cc).addClass("red");
			$(rests[0]).text("超出");
		}
		
	});
	
	$form.submit(function(event) {
	   event.preventDefault(); 
	   checkNotify = false;
	   $form.find("input[type=submit]").attr("disabled","disabled");
       msg = $.trim($a.val()),
       url = $form.attr('action');
       if(msg.length <= 0){
       		$(rests[0]).text("");$countTxt.text("");
       		$(rests[1]).text("请输入内容").addClass("red").hide().fadeIn('slow');
       		return;
       }
       if(msg.length>140){
       		$countTxt.parent().hide().fadeIn('slow');
       		return;
       }
	var ok = "<div class='reOk'>消息发送成功！<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	var err = "<div class='reErr'>消息发送失败.您可能需要重新登录。<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	   $.post( url, $form.serialize(),function( data ) {
	         checkNotify = false;
	         var json;
	         if (data) {
	         	 console.log(data);
	         	json=$.parseJSON(data);
	         	if(json._id){
	         		console.log(json);
	         		abox("发微博",ok);
				$a.val("");
				setTimeout("$.fancybox.close();",1000);
				if($form.readNew){
					setTimeout($form.readNew,2000);
				}else{
					var s = talkLI(json,json.creatorId);
					$("#msgList").prepend(s);	
				}
	         	}else{
	         		abox("发表失败",err);
	         	}
	         };
			
	     }
	   ).error(function(){
			abox("发微博",err);
		});
	   $form.find("input[type=submit]").removeAttr("disabled");
	   checkNotify = true;
	});
};
function readComms(mid,cc,isON){
	if(isON){
		comms.ON = false;
	};
	if(comms.ON && (comms.mid==mid)){
		comms.appendTo($("#hideContent"));
		comms.ON = false;
		return;
	}else{
		comms.ON = true;
	}
	comms.mid = mid;
	commsTalk.readNew = "readComms("+mid+",1,true);";
	var $m = $("#m_"+mid);
	$("#comm_rt_id").val(mid);
	$("#comm_rt_userId").val($m.find(".r_userId").val());
	$("#comm_rt_name").val($m.find(".r_name").val());
	if(cc>0){
		commsLoading.appendTo($m.find(".msgBox")[0]).show();	
		$.getJSON($.prefix+"/msg/comms?mid="+mid+"&uid=0&r="+new Date(),function(data){
			var s = "";
			if (data.length > 1) {
				//更新评论数量
				$m.find(".commNUM").text(data[0].cc);
				for(var i = 1,j=data.length;i<j;i++){
					var d = data[i];
					s+="<li class='commsLi'><div style='float:left;padding:0 10px;'><img src='";
					s+=$.sPrefix;
					s+="/images/upload/";
					s+=d.creatorName;
					s+="_3.jpg' alt='' width='40' height='40' /></div> <div style='padding:5px;'>";
					s+="<a href=\"/";
					s += $.prefix;
					s += "/";
					s += d.creatorName;
					s += "\" title=\"";
					s += d.author_screen;
					s += "(@";
					s += d.creatorName;
					s += ")\"  target='_blank'>";
					s += d.author_screen;
					s += "</a> <span class='commSubInfo'>";
					s+=sentTime(d.createTime);
					s+=" 通过 ";
					s += d.source;
					s+="</span> <br />";
					s+=d.text;
					s+="</div></li>";
				}
			}
			comms.find("#commsUL").html(s);
			
			commsLoading.hide();
			if(data.length>=10){$("#forAllComms").show();}else{$("#forAllComms").hide()};
			comms.appendTo($m.find(".msgBox")[0]);
			return;
		}).error(function(){
			
		});
	}else{
		$("#forAllComms").hide();
	}
	commsLoading.hide();
	comms.find("#commsUL").html("");
	comms.appendTo($m.find(".msgBox")[0]);
	return;
};
function talkLI(d,userId,isOne){
	var s = "<li id='m_";
	s+=d._id;
	s+="'><div class=\"userPic\"><a href=\"";
	s += $.prefix;
	s += "/";
	s += d.creatorName;
	s += "\" class='icon'> <img src=\"";
	s += $.sPrefix;
	s += "/images/upload/";
	s += d.creatorName;
	s += "_3.jpg\" alt=\"";
	s += d.creatorName;
	s += "\" /></a></div>";
	s += "<div class=\"msgBox\"><div class=\"userName\" ><a target='_blank' href=\"";
	s += $.prefix;
	s += "/";
	s += d.creatorName;
	s += "\" title=\"";
	s += d.author_screen;
	s += "(@";
	s += d.creatorName;
	s += ")\" >";
	s += d.author_screen;
	s += "</a>";
	if(d.isRT){s+="&nbsp;&nbsp;转播:&nbsp;&nbsp;"};
	s += "</div><div class=\"msgCnt\">";
	s += d.text;
	s += "</div>";
	if (d.rtmsg) {
		var r = d.rtmsg;
		s += "<div class='replyBox'> <div class='msgBox'><a href=\"";
		s += $.prefix;
		s += "/";
		s += d.creatorName;
		s += "\" title=\"";
		s += d.author_screen;
		s += "(@";
		s += d.creatorName;
		s += ")\" >";
		s += d.author_screen;
		s += "</a>&nbsp;:&nbsp; <span class='msgCnt'>";
		s += r.text;
		s += "</span><div class=\"pubInfo\"><span class=\"fleft\"><a class=\"time\" target=\"_blank\" href=\"";
		s += $.prefix;
		s += "/m?mid=";
		s += r._id;
		s += "\" title=\"";
		s += new Date(r.createTime).format("yyyy-MM-dd hh:mm:ss");
		s += "\">";
		s += sentTime(r.createTime);
		s += "</a> 来自";
		s += r.source;
		s += "</span> &nbsp; <a href='";
		s += $.prefix;
		s += "/m?mid=";
		s += r._id;
		s += "' target='_blank'>原文转播与评论(";
		s += r.rt_comm_count;
		s += ")</a></div></div><div class='clear'></div> </div>";
	}else if(d.pic_url && d.pic_url.length>4){
		s+="<div class='picbox' id='pic_m_";
		s+=d._id;
		s+="'><a href=\"javascript:pic_in(";
		s+=d._id;
		s+=");\"><img src='";
		s+=$.sPrefix;
		s+="/images/upload/"
		s+=d.pic_url;
		s+="' alt='pic' /></a></div>";
	};
	s += "<div class=\"pubInfo\"><span class=\"fleft\">";
	if (isOne) {
		s += sentTime(d.createTime);
	}else{
		s+="<a class=\"time\" target=\"_blank\" href=\"";
		s += $.prefix;
		s += "/m?mid=";
		s += d._id;
		s += "\" title=\"";
		s += new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");
		s += "\">";
		s += sentTime(d.createTime);
		s += "</a> ";	
	};
	s+=" 来自";
	s += d.source;
	s += "</span><div class=\"funBox\">";
	if (d.creatorId == userId) {
		s += "<a href=\"javascript:delCheck(";
		s+=d._id;
		s+=");\" class=\"delMsg\">删除</a>&nbsp;&nbsp; |&nbsp;&nbsp;";
	};
	if (isOne) {}else{
		s += "<a href=\"javascript:reSend(";
		s+=d._id;
		s+=","
		s+=d.creatorId;
		s+=",'";
		s+=d.creatorName;
		s+="')\" class=\"relay\">转播</a>&nbsp;&nbsp; |&nbsp;&nbsp; <a href=\"javascript:readComms('";
		s+=d._id;
		s+="',";
		s+=d.rt_comm_count;
		s+=");\" class='comt'>评论(<span class='commNUM'>";
		s+=d.rt_comm_count;
		s+="</span>)</a> &nbsp;&nbsp;|&nbsp;&nbsp; ";
	};
	s+=" <a href=\"javascript:favCheck(";
	s+=d._id;
	s+=");\" class=\"comt\">收藏</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"javascript:void(0);\" class=\"alarm\">举报</a> </div></div></div>";
	s+="<input type='hidden' class='r_userId' value='";
	s+=d.creatorId;
	s+="' /><input type='hidden' class='r_name' value='";
	s+=d.creatorName;
	s+="' />";
	s+="</li>";
	return s;
};
function pic_in(mid){
　　var picm=$("#pic_m_"+mid),img = picm.find("img"),im=picm.get(0);
　　if(im.imgurl==undefined){im.imgurl=img.attr("src");}
	var imgURL = im.imgurl;
	var p = imgURL.lastIndexOf("\.");
	var picType = imgURL.substring(p-1,p);
	if(picType=="1"){
	　　var pic_2 = imgURL.substring(0,p-1)+"2"+imgURL.substring(p);
　　	img.attr("src",pic_2);im.imgurl= pic_2;
　　	picm.find(".imgCtrl").remove();picm.find("a").html("<img src=\""+pic_2+"\" alt=\"pic\">");
	} else{
	　　var pic_org = imgURL.substring(0,p-2)+imgURL.substring(p);
		var imgCtrl = "<div class='imgCtrl'><a href=\"javascript:pic_in("+mid+");\" class='imgbt hideimg'>收起</a><a href=\"javascript:rotaLeft("+mid+");\" class='imgbt rotaLeft'>左转</a><a href=\"javascript:rotaRight("+mid+");\" class='imgbt rotaRight'>右转</a><a href=\""+pic_org+"\" target='_blank' class='imgbt lookOrg'>查看原图</a></div>"
	　　var pic_1 = imgURL.substring(0,p-1)+"1"+imgURL.substring(p);
		img.attr("src",pic_1);im.imgurl= pic_1;
		picm.prepend($(imgCtrl));
	}
	return;
}
function rotaLeft(mid){
	$("#pic_m_"+mid).find("a").children(0).rotateLeft(90);
};
function rotaRight(mid){
	$("#pic_m_"+mid).find("a").children(0).rotateRight(90);
};
function favCheck(mid){
	var s = "确定收藏此消息？<a href=\"javascript:addfav("+mid+");\" class=\"aboxBT\">确定</a> <a href=\"javascript:$.fancybox.close();\" class=\"aboxBT\">取消</a>";
	abox("收藏微博",s);
}
function addfav(mid){
	var err = "<div class='reErr'>收藏失败！建议重新登录.<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	var ok = "<div class='reOk'>收藏成功！<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	$.getJSON($.prefix+"/favor/add?msgid="+mid+"&r="+new Date(),function(data){
		if (data==true) {
			aboxUpdate(ok,"收藏完成");
			setTimeout("$.fancybox.close();",1000);return;
		}else{aboxUpdate(err,"收藏失败");}
	}).error(function(){aboxUpdate(err,"收藏失败");});
}
function reSend(mid,rt_userId,rt_name){
	var reMsg = $("#m_"+mid+" > .msgBox > .msgCnt").text();
	var s = "<div style='padding:10px;'>转 @"+rt_name+" ："+reMsg+"</div>";
	s+="<form name=\"commForm\" id=\"reSendForm\" class=\"commForm\" action=\"/KHunter/talk\" method=\"post\"> <div class=\"commTalk\"> <textarea name=\"talk\" rows=\"4\" cols=\"40\"></textarea> <input type=\"hidden\" name=\"isRT\" value=\"true\" id=\"re_isRT\"><input type=\"hidden\" name=\"rt_id\" value=\"";
	s+=mid;
	s+="\" id=\"re_rt_id\"><input type=\"hidden\" name=\"rt_userId\" value=\"";
	s+=rt_userId;
	s+="\" id=\"re_rt_userId\"><input type=\"hidden\" name=\"rt_name\" value=\"";
	s+=rt_name;
	s+="\" id=\"re_rt_name\"><input type=\"hidden\" name=\"talk_state\" value=\"0\" id=\"re_talk_state\"></div>";
	s+="<div class=\"sendsub\" style=\"color: #999;text-align: right;padding:6px 0;\"> <span class=\"restTxt\">还能输入</span> <span class=\"countTxt\" style=\"font-size:20px;\">140</span> <span class=\"restTxt\">字</span> <input type=\"submit\" name=\"sendbt\" value=\"转播\" class=\"sendbt bt_re\"> <div class=\"clear\"></div> </div> </form>";
	abox("转播到我的微博",s);
	talkForm($("#reSendForm"));
};
function delM(mid){
	var err = "<div class='reErr'>删除失败！建议重新登录.<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	var ok = "<div class='reOk'>删除成功！<a href='javascript:$.fancybox.close();' class=\"aboxBT\">关闭</a></div>";
	$.getJSON($.prefix+"/talk/del?msgid="+mid+"&r="+new Date(),function(data){
		if (data==true) {
			aboxUpdate(ok,"删除完成");
			setTimeout("$.fancybox.close();",1000);return;
		}else{aboxUpdate(err,"删除失败");}
	}).error(function(){aboxUpdate(err,"删除失败");});
};
function delCheck(mid){
	var s = "是否删除？<a href=\"javascript:delM("+mid+");\" class=\"aboxBT\">确认</a> <a href=\"javascript:$.fancybox.close();\" class=\"aboxBT\">取消</a>";
	abox("确认删除",s);	
};
function abox(title,contentHtml){
	var s = "<div id=\"aboxDiv\" class=\"abox\"><div class=\"aboxTitle\">";
	s+=title;
	s+="</div><div class=\"aboxContent\">";
	s+=contentHtml;
	s+="</div></div>";
	$.fancybox({
		'autoDimensions'	: false,
		'width'         		: 'auto',
		'height'        		: 'auto',
		'transitionIn'		: 'none',
		'transitionOut'	: 'none',
		'content':s
	});
};
function aboxUpdate(html,title){
	if (html) { $("#aboxDiv").find(".aboxContent").html(html); };
	if (title) { $("#aboxDiv").find(".aboxTitle").html(title); };
}
function sentTime(ms){
　　var t = new Date(ms);
　　var now = new Date();
　　var dd = now.getTime()-new Date(now.getFullYear(),now.getMonth(),now.getDate(),0,0,0).getTime();
　　var showDate = dd+86400000;
　　var lastHour = 3600000;
　　var pas = now-t;
　　if (pas>=showDate) {
　　	return (t.format("yyyy-MM-dd hh:mm:ss"));
　　}else if(pas>=dd && pas<showDate){
　　	return ("昨天:"+t.format("hh:mm:ss"));
　　}else if(pas>=lastHour && pas<dd){
　　	return ("今天:"+t.format("hh:mm:ss"));
　　}else if(pas<600000){
　　	return ("刚刚");
　　}else{
　　	return (Math.floor(pas/60/1000)+"分钟前");
　　}
};