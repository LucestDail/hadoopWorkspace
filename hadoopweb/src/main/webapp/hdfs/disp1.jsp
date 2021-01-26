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
	//하둡 서버에 저장된 파일 목록 조회하기
	String root = "C:/Users/dhtmd/myworkspace/hadoopWorkspace/hadoopstudy1";
	Path path = new Path(root);
	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(conf); // 하둡 시스템에 저장된 파일 정보
%>
<h2> 하둡을 이용한 파일 목록 조회</h2>
<%
	//path 로 지정된 폴더의 하위 파일 목록 반환
	RemoteIterator<LocatedFileStatus> flist = fs.listLocatedStatus(path);
	while(flist.hasNext()){
		LocatedFileStatus lfs = flist.next();//하둡 서버의 File 정보
		if(lfs.isDirectory()){// 폴더? 조건분기
			%>
			<a href="disp2.jsp?path=<%=lfs.getPath().getName()%>&&root=<%=root%>">d--<%=lfs.getPath().getName()%></a><br>
			<%}else{%>
			<a href="disp2.jsp?file=<%=lfs.getPath().getName()%>&&path=<%=root%>">---<%=lfs.getPath().getName()%></a><br>
			<%}
		} %>
</body>
</html>