<%@page import="org.apache.hadoop.fs.LocatedFileStatus"%>
<%@page import="org.apache.hadoop.fs.RemoteIterator"%>
<%@page import="org.apache.hadoop.fs.FileSystem"%>
<%@page import="org.apache.hadoop.conf.Configuration"%>
<%@page import="org.apache.hadoop.fs.Path"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>하둡을 이용하여 파일 목록 보기</title>
</head>
<body>
<%
	String root = request.getParameter("root");
	String param = request.getParameter("path");
	String target = root + "/" + param;
	Path path = new Path(target);
	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(conf); 
%>
<h2> 하둡을 이용한 파일 목록 조회 : <%=target%></h2>
<%
	RemoteIterator<LocatedFileStatus> flist = fs.listLocatedStatus(path);
	while(flist.hasNext()){
		LocatedFileStatus lfs = flist.next();
		if(lfs.isDirectory()){
			%>
			<a href="disp2.jsp?path=<%=lfs.getPath().getName()%>&&root=<%=target%>">d--<%=lfs.getPath().getName()%></a><br>
			<%}else{%>
			<a href="disp3.jsp?file=<%=lfs.getPath().getName()%>&&root=<%=target%>">---<%=lfs.getPath().getName()%></a><br>
			<%}
		} %>
</body>
</html>