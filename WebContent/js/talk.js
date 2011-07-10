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
	s+="');\" class='comt'>评论(";
	s+=d.rt_comm_count;
	s+=")</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"/p/t/39552051902918\" class=\"comt\">收藏</a> &nbsp;&nbsp;|&nbsp;&nbsp; <a href=\"#\" class=\"alarm\">举报</a> </div></div></div>";
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