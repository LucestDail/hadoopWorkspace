<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CCTV 분석</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" 
  src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
<body>
<h3>관리목적별, 설치목적별, 설치년도별 cctv 데이터 분석</h3>
<form action="${pageContext.request.contextPath}/OshServlet" method="post" name="f">
<br>
	<input type = "radio" name = "kbn" value = "mgrgov" checked>관리지역별 cctv 갯수<br>
	<input type = "radio" name = "kbn" value = "reason" <core:if test="${param.kbn=='reason'}">checked="checked"</core:if>>설치목적별 cctv 갯수<br>
	<input type = "radio" name = "kbn" value = "year" <core:if test="${param.kbn=='year'}">checked="checked"</core:if>>설치년도별 cctv 갯수<br>
	<input type = "submit" value="데이터분석">
</form>
<core:if test = "${!empty map}">
	<div id = "canvas-holder" style="width:50% height:300px;">
		<canvas id = "chart" width="100%" height="100%"></canvas>
	</div>
	<script type="text/javascript">
	var randomColorFactor = function(){
		return Math.round(Math.random() * 255);
	}
	var randomColor = function(opacity){
		return "rgba("+randomColorFactor() + "," + randomColorFactor() + "," + randomColorFactor() + "," + (opacity||"0.3") + ")";
	}
	rcolor = randomColor(1);
	var config = {
			type : 'bar',
			data : {
				datasets : [{
					label :"${param.kbn=='mgrgov'?'관리지역':param.kbn=='reason'?'설치목적':'설치년도'}별 CCTV 갯수",
					data : [<core:forEach items="${map}" var="m">"${m.value}",</core:forEach>],
					backgroundColor : [<core:forEach items="${map}" var="m">randomColor(1),</core:forEach>]
				}],
				labels : [<core:forEach items="${map}" var="m">"${m.key}",</core:forEach>]
			},
			options : {responsive : true}
	};
	window.onload = function(){
		var ctx = document.getElementById("chart").getContext("2d");
		new Chart(ctx,config);
	}
	</script>
</core:if>
</body>
</html>