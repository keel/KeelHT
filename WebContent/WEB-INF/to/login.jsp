<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>KHT console</title>
<link rel="stylesheet" type="text/css" href="css/style.css" />
</head>
<body>

<div id="wrapper">
	<div id="header">
		<h1>Console - login</h1>
	</div>
	
	<div id="mainBody">
<form action="act?act=console&amp;right=login" method="post" id="consoleLogin">
	<p><label for="form_name">Admin:</label><br />
	<input type="text" name="form_name" id="form_name" value="" class="wide300" /></p>
	<p><label for="form_pwd">Password:</label><br />
	<input type="password" name="form_pwd" id="form_pwd" value="" class="wide300" /></p>
	<p><input type="submit" value=" Login " /></p>
</form>
	</div>

	<div id="footer">
		<div id="copyright">
			Copyright:&copy;2010-2012 KEEL.SIKE All rights reserved. 
		</div>
	</div>
</div>

</body>
</html>