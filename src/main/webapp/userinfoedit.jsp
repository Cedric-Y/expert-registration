<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户资料-编辑</title>
</head>
<body>
	<h6><%=session.getAttribute("msg")%></h6>
	<form action="${pageContext.request.contextPath}/controller/UserInfoServlet?method=userInfoEditSubmib" method="post">
		<table>
			<tbody>
				<tr>
					<td>姓名：</td>
					<td>
						<input name="name" type="text" />（必填）
					</td>
				</tr>
				<tr>
					<td>身份证：</td>
					<td>
						<input name="idcard" type="text" />（必填）
					</td>
				</tr>
				<tr>
					<td>密码：</td>
					<td>
						<input name="password1" type="password" />
						（至少是由字母和数字组成，且3到15位）
					</td>
				</tr>
				<tr>
					<td>确认密码：</td>
					<td>
						<input name="password2" type="password" />
						（至少是由字母和数字组成，且3到15位）
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="hidden" name="token" value="<%=session.getAttribute("token")%>">
						<input type="hidden" name="u_id" value="<%=session.getAttribute("u_id")%>">
						<input type="submit" value="确定" />
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>