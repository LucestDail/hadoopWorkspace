<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>항공사별 출발 / 도착 정보</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" 
  src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
<body>
<h3>항공사별 출발 / 도착 (정시/지연/조기), 운항거리 분석</h3>
<form action="${pageContext.request.contextPath}/CarrMultiServlet2" method="post" name="f">
	<select name="year">
		<core:forEach var="y" begin="1987" end="1988">
			<option <core:if test="${param.year==y}"> selected</core:if>>${y}</option>
		</core:forEach>
	</select>&nbsp;&nbsp;&nbsp;
	<input type = "radio" name = "kbn" value = "d" checked>출발&nbsp;&nbsp;
	<input type = "radio" name = "kbn" value = "a" <core:if test="${param.kbn=='d'}">checked="checked"</core:if>>도착&nbsp;&nbsp;
	<input type = "submit" value="분석">
</form>
<core:if test = "${!empty file}">
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
	arrcolor = new Array("rgb(255,0,0)","rgb(0,255,0)","rgb(0,0,255)");
	var kbn = "${param.kbn=='s'?'정시':param.kbn=='d'?'지연':'조기'}";
	var label = new Array(kbn + "출발건수", kbn + "도착 건수","운항거리(1000마일)");
	var config = {
			type : 'bar',
			data : {
				datasets : [
<core:forEach items="${list}" var="map" varStatus="stat">
					{ label : '${file}년' + label[${stat.index}],
					data : [<core:forEach items="${map}" var="m">"${m.value}",</core:forEach>],
					backgroundColor : [<core:forEach items="${map}" var="m">arrcolor[${stat.index}],</core:forEach>],
				},
</core:forEach>],
				labels : [<core:forEach items="${list[0]}" var="m">"${fn:split(m.key,'-')[1]}",</core:forEach>]
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