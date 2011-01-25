<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Test jsp out</title>
</head>
<body>
<%
Object o = request.getAttribute("jspAttr");
if(o != null ){
	HTUser user = (HTUser)o;
	out.print(user.getName() + " , " + user.getImei() + " , " + user.getEmail());
}else{
	out.print("attr is null.");
}

%>
</body>
</html>