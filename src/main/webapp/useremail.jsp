<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>填写邮箱地址</title>	
	<script src="<%=request.getContextPath()%>/js/common.js"></script> 
</head>
<body>
	<h3>填写邮箱地址</h3>
	<h6><%=session.getAttribute("msg")%></h6>
	<form action="${pageContext.request.contextPath}/UserLogin?method=userSendEmailSubmit" method="post">
		<table>
			<tbody>
				<tr>
					<td>请输入注册时填写的邮箱地址：</td>
					<td colspan="2">
						<input name="email" type="text" />
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
						<input type="hidden" name="reset" value="<%=session.getAttribute("reset")%>" />
						<input type="submit" value="发送验证邮件" />
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>