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
	<form action="${pageContext.request.contextPath}/UserInfo?method=userInfoEditSubmit" method="post" enctype="multipart/form-data">
		<table>
			<tbody>
				<tr>
					<td>姓名：</td>
					<td>
						<input name="name" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>身份证：</td>
					<td>
						<input name="idcard" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>政治面貌：</td>
					<td>
						<input name="political_status" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>电话：</td>
					<td>
						<input name="phone" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>一寸照：</td>
					<td>
						<input name="photo" type="file" />
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>最高学历：</td>
					<td>
						<input name="education" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>毕业院校：</td>
					<td>
						<input name="college" type="text" />
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>从事行业类型：</td>
					<td>
						<input name="profession_kind" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>从事行业：</td>
					<td>
						<input name="profession" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>工作单位：</td>
					<td>
						<input name="company" type="text" />
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>单位地址：</td>
					<td>
						<input name="company_address" type="text" />
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>工作电子邮箱：</td>
					<td>
						<input name="email" type="text" />
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td>个人简历：</td>
					<td>
						<input name="resume" type="file" />
						<%-- <input name="resume" type="text" /> --%>
					</td>
					<td>
						（必填）
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<input type="hidden" name="token" value="<%=session.getAttribute("token")%>" />
						<input type="hidden" name="reg_id" value="<%=session.getAttribute("reg_id")%>" />
						<input type="hidden" name="u_id" value="<%=session.getAttribute("u_id")%>" />
					</td>
				</tr>
			</tbody>
		</table>
		
		<table>
			<tbody>
				<tr>
					<td>
						<table>
							<thead>
								<tr>
									<th colspan="2">职称证书1</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>职称证书名称</td>
									<td>
										<input name="job_title_name1" type="text" />
									</td>
								</tr>
								<tr>
									<td>职称证书级别</td>
									<td>
										<input name="job_title_level1" type="text" />
									</td>
								</tr>
								<tr>
									<td>获取证书时间</td>
									<td>
										<input name="job_title_date1" type="text" />
										（2017-01-01）
									</td>
								</tr>
								<tr>
									<td>发证机构</td>
									<td>
										<input name="job_title_organization1" type="text" />
									</td>
								</tr>
							</tbody>
						</table>
					</td>
					<td>
						<table>
							<thead>
								<tr>
									<th colspan="2">职称证书2</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>职称证书名称</td>
									<td>
										<input name="job_title_name2" type="text" />
									</td>
								</tr>
								<tr>
									<td>职称证书级别</td>
									<td>
										<input name="job_title_level2" type="text" />
									</td>
								</tr>
								<tr>
									<td>获取证书时间</td>
									<td>
										<input name="job_title_date2" type="text" />
										（2017-01-01）
									</td>
								</tr>
								<tr>
									<td>发证机构</td>
									<td>
										<input name="job_title_organization2" type="text" />
									</td>
								</tr>
							</tbody>
						</table>
					</td>					
				</tr>
			</tbody>
		</table>
		<input type="submit" value="确定" />
	</form>
	<hr/>
	<a href="${pageContext.request.contextPath}/UserInfo?method=userInfoForm">个人信息</a>
	<a href="${pageContext.request.contextPath}/UserInfo?method=userLogin">退出登录</a>
</body>
</html>