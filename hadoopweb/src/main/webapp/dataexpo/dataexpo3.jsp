<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>월별 출발 / 도착 지연 분석</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" 
  src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
</head>
<body>
<h3>월별 출발 / 도착 지연 분석</h3>
<form action="${pageContext.request.contextPath}/MonMultiServlet" method="post" name="f">
	<select name="year">
		<core:forEach var="y" begin="1987" end="1988">
			<option <core:if test="${param.year==y}"> selected</core:if>>${y}</option>
		</core:forEach>
	</select>
	<input type="submit" value="분석">
</form>
<core:if test = "${!empty file}">
	<div id = "canvas-holder" style="width:50%; height:300px;">
		<canvas id="chart" width="100%" height="100%"></canvas>
	</div>
	<script type="text/javascript">
	var randomColorFactor = function(){
		return Math.round(Math.random() * 255);
	}
	var randomColor = function(opacity){
		return "rgba("+randomColorFactor() + "," + randomColorFactor() + "," + randomColorFactor() + "," + (opacity||".3") + ")";
	}
	rcolor = randomColor(1);
	rcolor2 = randomColor(0.5);
	var config = {
			type : "bar",
			data : {
				datasets : [{
					label : "${file}년 출발 지연 건수",
					data : [<core:forEach items="${map1}" var="m">"${m.value}",</core:forEach>],
					backgroundColor : [<core:forEach items="${map1}" var="m">rcolor,</core:forEach>]
				},{
					label : "${file}년 도착 지연 건수",
					data : [<core:forEach items="${map2}" var="m">"${m.value}",</core:forEach>],
					backgroundColor : [<core:forEach items="${map2}" var="m">rcolor2,</core:forEach>]
				}],
				labels : [<core:forEach items="${map1}" var="m">"${m.key}월",</core:forEach>]
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