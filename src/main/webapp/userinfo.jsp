<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户资料</title>
</head>
<body>
	<h3>用户资料</h3>
	<hr />
	<h5>基本资料</h5>
	<p>
		<%
			out.print(request.getAttribute("reg"));
		%>
	</p>
	<hr />
	<hr />
	<h5>职称</h5>
	<p>
		<%
			out.print(request.getAttribute("jobtitles"));
		%>
	</p>
	<hr />
	<a href="${pageContext.request.contextPath}/UserInfo?method=userInfoEditForm">编辑</a>
</body>
</html>