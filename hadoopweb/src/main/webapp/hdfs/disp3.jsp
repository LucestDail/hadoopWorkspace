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
	String path = request.getParameter("path");
	String file = request.getParameter("file");
%>
<%=path %><br>
<%=file %>
</body>
</html>