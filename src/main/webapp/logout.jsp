<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		session.invalidate();	// session을 지워버림 => 로그아웃
		response.sendRedirect("login.jsp");
	
	%>
</body>
</html>