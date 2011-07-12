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
	   $.post( url, $form.serialize(),function( data ) {
	         checkNotify = false;
			$.fancybox(
			'<p class="fancyMsgBox1" >消息发送成功!</p>',
			{	'autoDimensions'	: false,
				'width'         	: 300,
				'height'        	: 'auto',
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'hideOnContentClick': true
			});
			$a.val("");
			setTimeout("$.fancybox.close();",1000);
			if($form.readNew){
				setTimeout($form.readNew,2000);
			}
	     }
	   ).error(function(){
			$.fancybox(
				'<p class="fancyMsgBox2" >消息发送失败.您可能需要重新登录。</p>',
				{	'autoDimensions'	: false,
					'width'         	: 300,
					'height'        	: 'auto',
					'transitionIn'		: 'none',
					'transitionOut'		: 'none',
					'hideOnContentClick': true
			});
		});
	   $form.find("input[type=submit]").removeAttr("disabled");
	   checkNotify = true;
	});
};
function readComms(mid,cc,prefix,sPrefix,isON){
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
	commsTalk.readNew = "readComms("+mid+",1,'"+prefix+"','"+sPrefix+"',true);";
	var $m = $("#m_"+mid);
	$("#comm_rt_id").val(mid);
	$("#comm_rt_userId").val($m.find(".r_userId").val());
	$("#comm_rt_name").val($m.find(".r_name").val());
	if(cc>0){
		commsLoading.appendTo($m.find(".msgBox")[0]).show();	
		$.getJSON(prefix+"/msg/comms?mid="+mid+"&uid=0&r="+new Date(),function(data){
			var s = "";
			if (data.length > 1) {
				//更新评论数量
				$m.find(".commNUM").text(data[0].cc);
				for(var i = 1,j=data.length;i<j;i++){
					var d = data[i];
					s+="<li class='commsLi'><div style='float:left;padding:0 10px;'><img src='";
					s+=sPrefix;
					s+="/images/upload/";
					s+=d.user_name;
					s+="_3.jpg' alt='' width='40' height='40' /></div> <div style='padding:5px;'>";
					s+="<a href=\"/";
					s += prefix;
					s += "/";
					s += d.user_name;
					s += "\" title=\"";
					s += d.user_screen;
					s += "(@";
					s += d.user_name;
					s += ")\"  target='_blank'>";
					s += d.user_screen;
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
	return false;
};
function talkLI(d,prefix,sPrefix,userId){
	var s = "<li id='m_";
	s+=d._id;
	s+="'><div class=\"userPic\"><a href=\"";
	s += prefix;
	s += "/";
	s += d.creatorName;
	s += "\" class='icon'> <img src=\"";
	s += sPrefix;
	s += "/images/upload/";
	s += d.creatorName;
	s += "_3.jpg\" alt=\"";
	s += d.creatorName;
	s += "\" /></a></div>";
	s += "<div class=\"msgBox\"><div class=\"userName\" ><a href=\"/";
	s += prefix;
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
		s += prefix;
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
		s += prefix;
		s += "/m?mid=";
		s += r._id;
		s += "\" title=\"";
		s += new Date(r.createTime).format("yyyy-MM-dd hh:mm:ss");
		s += "\">";
		s += sentTime(r.createTime);
		s += "</a> 来自";
		s += r.source;
		s += "</span> &nbsp; <a href='";
		s += prefix;
		s += "/m?mid=";
		s += r._id;
		s += "' target='_blank'>原文转播与评论(";
		s += r.rt_comm_count;
		s += ")</a></div></div><div class='clear'></div> </div>";
	}
	s += "<div class=\"pubInfo\"><span class=\"fleft\"><a class=\"time\" target=\"_blank\" href=\"";
	s += prefix;
	s += "/m?mid=";
	s += d._id;
	s += "\" title=\"";
	s += new Date(d.createTime).format("yyyy-MM-dd hh:mm:ss");
	s += "\">";
	s += sentTime(d.createTime);
	s += "</a> 来自";
	s += d.source;
	s += "</span><div class=\"funBox\">";
	if (d.creatorId == userId) {
		s += "<a href=\"#\" class=\"delMsg\">删除</a>&nbsp;&nbsp; |&nbsp;&nbsp;";
	}
	s += "<a href=\"#\" class=\"relay\">转播</a>&nbsp;&nbsp; |&nbsp;&nbsp; <a href=\"javascript:readComms('";
	s+=d._id;
	s+="',";
	s+=d.rt_comm_count;
	s+=",'";
	s+=prefix;
	s+="','";
	s+=sPrefix;
	s+="');\" class='comt'>评论(<span class='commNUM'>";
	s+=d.rt_comm_count;
	s+="</span>)</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"/p/t/39552051902918\" class=\"comt\">收藏</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"#\" class=\"alarm\">举报</a> </div></div></div>";
	s+="<input type='hidden' class='r_userId' value='";
	s+=d.creatorId;
	s+="' /><input type='hidden' class='r_name' value='";
	s+=d.creatorName;
	s+="' />";
	s+="</li>";
	return s;
};

function sentTime(ms){
　　	var t = new Date(ms);
　　var now = new Date();
　　var showDate = 172800000;
　　var showYestoday = 	86400000;
　　var lastHour = 3600000;
　　var pas = now-t;
　　if (pas>=showDate) {
　　return (t.format("yyyy-MM-dd hh:mm:ss"));
　　}else if(pas>=showYestoday && pas<showDate){
　　	return ("昨天:"+t.format("hh:mm:ss"));
　　}else if(pas>=lastHour && pas<showYestoday){
　　return ("今天:"+t.format("hh:mm:ss"));
　　}else{
　　return (Math.floor(pas/60/1000)+"分钟前");
　　}
};