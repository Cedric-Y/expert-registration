<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户注册</title>
<%-- <script src="<%=request.getContextPath()%>/js/common.js"></script> --%>
</head>
<body>
	<h3>用户注册</h3>
	<h6><%=session.getAttribute("msg")%></h6>
	<form action="${pageContext.request.contextPath}/UserLogin?method=userRegSubmit" method="post">
		<table>
			<tbody>
				<tr>
					<td>登录用户名：</td>
					<td>
						<input name="loginname" type="text" />
						（由字母、数字或者下划线组成，且开头必须是字母，3到15位）
					</td>
				</tr>
				<tr>
					<td>邮箱地址：</td>
					<td>
						<input name="email" type="text" />
						（用于找回用户名或密码）
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
				<%--
				<tr>
					<td width="100px">验证码：</td>
					<td width="400px">
						<input name="checkCode" type="text" id="checkCode" title="验证码不区分大小写" size="8" ,maxlength="4" />
						<br/>
						<input type="hidden" id="checkCodeSrc"
							value="${pageContext.request.contextPath}/UserLogin?method=checkCode" />
						<img src="${pageContext.request.contextPath}/UserLogin?method=checkCode" id="checkCodeImg"
							align="middle" onclick="checkCodeReload()"> （点击图片,换一个） 
					</td>
				</tr>
				--%>
				<tr>
					<td colspan="2">
						<input type="hidden" name="token" value="<%=session.getAttribute("token")%>" />
						<input type="submit" value="确定" />
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>