<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>항공사별 항공기 지연 데이터 분석</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" 
  src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js"></script>
</head>
<body>
<h3>항공사별 항공기 지연 데이터 분석</h3>
<form action = "${pageContext.request.contextPath}/CarrDelayServlet" method="post" name="f">
	<select name="year">
		<core:forEach var = "y" begin = "1987" end = "1988">
			<option <core:if test = "${param.year == y}">selected</core:if>>${y}</option>
		</core:forEach>
	</select>
	<input type = "radio" name = "kbn" value = "a" checked = "checked"> 도착 지연&nbsp;
	<input type = "radio" name = "kbn" value = "d" <core:if test="${param.kbn=='d'}">checked="checked"</core:if>>출발 지연&nbsp;<br>
	<input type = "radio" name = "graph" value = "bar" checked = "checked">막대 그래프&nbsp;
	<input type = "radio" name = "graph" value = "pie"
		<core:if test = "${param.graph=='pie'}">checked="checked"</core:if>>파이 그래프&nbsp;
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
		rcolor = randomColor(1);
		var config = {
				type : '${param.graph}',
				data : {
					datasets : [{
						label : "${file}${(param.kbn=='a')?'도착':'출발'}지연정보",
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