<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>重置密码</title>	
	<script src="<%=request.getContextPath()%>/js/common.js"></script> 
</head>
<body>
	<h3>重置密码</h3>
	<h6><%=session.getAttribute("msg")%></h6>
	<form action="${pageContext.request.contextPath}/UserLogin?method=userResetPasswordSubmit" method="post">
		<table>
			<tbody>
				<tr>
					<td>新密码：</td>
					<td>
						<input name="password1" type="password" />
					</td>
					<td>
						（至少是由字母和数字组成，且3到15位）
					</td>
				</tr>
				<tr>
					<td>确认密码：</td>
					<td>
						<input name="password2" type="password" />
					</td>
					<td>
						（至少是由字母和数字组成，且3到15位）
					</td>
				</tr>
				<tr>
					<td>验证码：</td>
					<td>
						<input name="checkCode" type="text" id="checkCode" title="验证码不区分大小写" size="8" ,maxlength="4" />
					</td>
					<td>
						<input type="hidden" id="checkCodeSrc" value="${pageContext.request.contextPath}/UserLogin?method=checkCode"/>
						<img src="${pageContext.request.contextPath}/UserLogin?method=checkCode" 
							id="checkCodeImg" align="middle" onclick="checkCodeReload()">
							（点击图片,换一个）
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<input type="hidden" name="token" value="<%=session.getAttribute("token")%>" />
						<input type="submit" value="重置" />
					</td>
				</tr>
			</tbody>
		</table>

		<hr />
		<a href="${pageContext.request.contextPath}/UserLogin?method=userLoginForm">登录</a>
	</form>
</body>
</html>