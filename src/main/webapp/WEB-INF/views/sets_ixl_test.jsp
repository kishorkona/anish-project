<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
	//document.getElementById("submitBtn").disabled=true;
	localStorage.setItem('questionDate', new Date());
	var currentDate = new Date(localStorage.getItem('questionDate'));
    var hours = Math.floor((currentDate.getTime() % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var minutes = Math.floor((currentDate.getTime() % (1000 * 60 * 60)) / (1000 * 60)); 
    var seconds = Math.floor((currentDate.getTime() % (1000 * 60)) / 1000); 
    var finalResult = hours + "h " + minutes + "m " + seconds + "s ";
    document.getElementById("questionDate").value=finalResult;
    
    // Setting TestTimer
    if(document.getElementById("testResetFlag").value=='true') {
    	localStorage.removeItem('testDate');
    	localStorage.setItem('testDate', new Date());
	}
    var testPreviousDate =new Date(localStorage.getItem('testDate'));
	var testNewDate = new Date();
    var testDistence = testNewDate.getTime() - testPreviousDate.getTime(); 
    var testHours = Math.floor((testDistence % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var testMinutes = Math.floor((testDistence % (1000 * 60 * 60)) / (1000 * 60)); 
    var testSeconds = Math.floor((testDistence % (1000 * 60)) / 1000); 
    var testFinalResult = testHours + "h " + testMinutes + "m " + testSeconds + "s ";
    document.getElementById("testDate").innerHTML=testFinalResult;
    document.getElementById("testTime").value=testFinalResult;
}
function enableSubmitBtn() {
	var value = document.getElementById("questionType").value;
	if(value == 1 ||value == 2 ||value == 3) {
		document.getElementById("submitBtn").disabled=false;
	}
}
var x = setInterval(function() { 
	var previousDate =new Date(localStorage.getItem('questionDate'));
	var newDate = new Date();
    var distence = newDate.getTime() - previousDate.getTime(); 
    var hours = Math.floor((distence % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var minutes = Math.floor((distence % (1000 * 60 * 60)) / (1000 * 60)); 
    var seconds = Math.floor((distence % (1000 * 60)) / 1000); 
    var finalResult = hours + "h " + minutes + "m " + seconds + "s ";
    document.getElementById("questionDate").innerHTML=finalResult;
	document.getElementById("questionTime").value=finalResult;

 	// Setting TestTimer
    var testPreviousDate =new Date(localStorage.getItem('testDate'));
	var testNewDate = new Date();
    var testDistence = testNewDate.getTime() - testPreviousDate.getTime(); 
    var testHours = Math.floor((testDistence % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var testMinutes = Math.floor((testDistence % (1000 * 60 * 60)) / (1000 * 60)); 
    var testSeconds = Math.floor((testDistence % (1000 * 60)) / 1000); 
    var testFinalResult = testHours + "h " + testMinutes + "m " + testSeconds + "s ";
    document.getElementById("testDate").innerHTML=testFinalResult;
    document.getElementById("testTime").value=testFinalResult;
    
    //alert("1:"+document.getElementById("questionDate").value);
    //alert("2:"+document.getElementById("testDate").value);
}, 1000);
</script>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>User  Money Home page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
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
		<form:form action="/anishtestsnew/sets/tests/nextQuestion" method="post" modelAttribute="question" autocomplete="off">	
			<table style="width:100%;margin:0 auto auto">
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td style="width:30%;">
						<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
					</td>
					<td style="width:70%;">
						<table class="center" style="width:90%">
							<tr>
								<td><b>Subject:</b> ${question.subject}</td>
								<td><b>Grade:</b> ${question.grade}&nbsp;&nbsp; <b>Question No:</b>${question.id}</td>
							</tr>
							<tr><td colspan="2"><br/></td></tr>
							<tr>
								<td><b>Questions Left:</b>  ${question.totalCurrentQuestions}</td>
								<td><b>Section:</b> ${question.sectionId}.${question.subSectionId} &nbsp;&nbsp;&nbsp; <b>Section Name:</b> ${question.sectionName}</td>
							</tr>
							<tr><td colspan="2"><br/></td></tr>
							<tr>
								<td colspan="2">
									<b>TestId:</b>&nbsp;${question.questionId}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Test Name:</b>&nbsp;${question.questionName}&nbsp;&nbsp;&nbsp;
									<c:if test="${question.wordProbleum}">
										<div style="color:red">Need Rough Work</div>
									</c:if>
								</td>
							</tr>
							<tr><td colspan="2"><br/></td></tr>
							<tr><td colspan="2"><a href="${question.url}" target="_blank"><b>${question.url}</b></a></td></tr>
							<tr><td colspan="2"><br/></td></tr>
							<tr><td colspan="2">
							</td></tr><tr><td colspan="2"><br/></td></tr>
							<tr><td  colspan="2"align="right"><input type="submit" id="submitBtn" value="Next Question"/></td></tr>
						</table>
						<br/>
						<table class="center" style="width:90%">
							<tr>
								<td align="center">Question Time:<h1><div id="questionDate"></div></h1></td>
								<td align="center">
									Test Time:<h1><div id="testDate"></div></h1>
								</td>
							</tr>
						</table>
						<input type="hidden" id="testResetFlag" value="${testResetFlag}" />
						<input type="hidden" id="testTime" value="${testResetFlag}" />
						
						<form:input type="hidden" path="questionTime" />
						<form:input type="hidden" path="questionId" />
						<form:input type="hidden" path="sectionId" />
						<form:input type="hidden" path="subSectionId" />
						<form:input type="hidden" path="userName"  />
						<form:input type="hidden" path="id" />
						<form:input type="hidden" path="totalCurrentQuestions" />
						<form:input type="hidden" path="sectionName" />
						<form:input type="hidden" path="questionId" />
						<form:input type="hidden" path="questionName" />
						<form:input type="hidden" path="subject" />
						<form:input type="hidden" path="grade" />
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				</tr>
			</table>
		</form:form>
	</body>
</html>