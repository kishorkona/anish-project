<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
table, th, td {
  border:1px solid black;
  border-collapse: collapse;
}
tr {
	height: 25px;
}
table.center {
  margin-left: auto; 
  margin-right: auto;
}
h1 {
  font-size: 30px;
}
</style>
<head>
<script> 
function setCurrentTime() {
	localStorage.setItem('answerTime', new Date());
	var currentDate = new Date(localStorage.getItem('answerTime'));
    var hours = Math.floor((currentDate.getTime() % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var minutes = Math.floor((currentDate.getTime() % (1000 * 60 * 60)) / (1000 * 60)); 
    var seconds = Math.floor((currentDate.getTime() % (1000 * 60)) / 1000); 
    var finalResult = hours + "h " + minutes + "m " + seconds + "s ";
    document.getElementById("answerTime").value=finalResult;
    document.getElementById("answer").focus();
}
var x = setInterval(function() { 
	var previousDate =new Date(localStorage.getItem('answerTime'));
	var newDate = new Date();
    var distence = newDate.getTime() - previousDate.getTime(); 
    var hours = Math.floor((distence % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var minutes = Math.floor((distence % (1000 * 60 * 60)) / (1000 * 60)); 
    var seconds = Math.floor((distence % (1000 * 60)) / 1000); 
    var finalResult = hours + "h " + minutes + "m " + seconds + "s ";
    document.getElementById("answerTime").value=finalResult;
}, 1000);
</script>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Double And Hlaf page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<html>
<body onload="setCurrentTime()">
	<jsp:include page="menu/menu.jsp" />
	<div class="header">
		<div class="first">
		<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75">
		</div>
		<div class="second">Tests App</div>
	</div>
	<br/><br/>
	<hr/><br/>
	<table style="width:100%;margin:0 auto auto">
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td style="width:30%;">
				<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
			</td>
			<c:if test="${idDone == 'true'}">
				<td style="width:70%;">Done</td>
			</c:if>
			<c:if test="${idDone != 'true'}">
				<td style="width:70%;">
					<form:form action="/anishtestsnew/tables/submitTable" method="post" modelAttribute="tableData" autocomplete="off">
						<table class="center" style="width:90%">
							<tr>
								<td>
									Id:&nbsp;&nbsp;&nbsp;${tableData.id}&nbsp;&nbsp;&nbsp; 
									Total Left:&nbsp;&nbsp;&nbsp;${tableData.totalQuestions}&nbsp;&nbsp;&nbsp;
									<div style="color:red">${tableData.value}</div>
								</td>
							</tr>
							<tr><td><form:input path="answer"/></td></tr>
							<tr><td><input type="submit" value="Save"/></td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td>
								<c:if test="${previousAnswerFlag == 'true' and previousAnswerStatus == 'Correct'}">
									<div style="color:green">Your Previous Question:&nbsp;&nbsp;${previousValue}=${previousAnswer}&nbsp;&nbsp;is&nbsp;&nbsp;${previousAnswerStatus}</div>
								</c:if>
								<c:if test="${previousAnswerFlag == 'true' and previousAnswerStatus == 'Wrong'}">
									<div style="color:red">Your Previous Question:&nbsp;&nbsp;${previousValue}=${previousAnswer}&nbsp;&nbsp;is&nbsp;&nbsp;${previousAnswerStatus}</div>
								</c:if>
							</td></tr>
						</table>
						<form:input type="hidden" path="childName" />
						<form:input type="hidden" path="answerTime" />
						<form:input type="hidden" path="id" />
						<form:input type="hidden" path="value" />
					</form:form>
					<br/>
				</td>
			</c:if>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
	</table>
</body>
</html>