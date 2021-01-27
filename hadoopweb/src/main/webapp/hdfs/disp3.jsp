<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.apache.hadoop.fs.FileSystem"%>
<%@page import="org.apache.hadoop.conf.Configuration"%>
<%@page import="org.apache.hadoop.fs.Path"%>
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
	String path = request.getParameter("root");
	String file = request.getParameter("file");
	String start = request.getParameter("start");
%>
<form>
	조회를 원하는 라인 : <input type = "text" name = "start" value = "${param.start}">
	<input type = "hidden" name = "file" value = "${param.file}">
	<input type = "hidden" naem = "path" value = "${param.path}">
	<input type = "submit" value = "찾기">
<%
	Path pt = new Path(path + "/" + file);
	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(conf);
	System.out.println(pt);
	BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt),"UTF-8"));
%>
<h3><%= path + "/" + file %> 내용 보기</h3>
<%
	int cnt = 0;
if(start == null || start.equals("")){
	cnt = 1;
}else{
	cnt = Integer.parseInt(start);
}
for(int a = 1; a < cnt; a++){
	br.readLine();
}
int i = 0;
String line = null;
while((line = br.readLine()) != null && 1 < 10){
	out.println(String.format("%10d",(i + cnt)) + " : " + line + "<br>");
	i++;
}
if(line == null){
	%>
	<h3>파일의 끝</h3>
	<%
	}else{
		%>
		<a href = "disp3.jsp?file=<%=file%>&&path=<%=path%>&&start=<%=cnt+10%>">다음 페이지</a>
		<%
}
%>
&nbsp;&nbsp;
<a href="javascript:history.go(-1)">앞페이지</a>
<a href="disp1.jsp">처음페이지로 이동</a>
</form>
</body>
</html>