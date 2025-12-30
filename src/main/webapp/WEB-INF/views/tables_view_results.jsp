<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
</style>
<body>
	<jsp:include page="menu/menu.jsp" />
	<div class="header">
		<div class="first">
		<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75">
		</div>
		<div class="second">Tests App</div>
	</div><br/><br/>
<hr/><br/>
<div class="bodypart">
	<div>
		<c:if test="${error!=null}">${error}</c:if>
		<table border="1" style="width:50%;margin:0 auto auto">
			<tr>
				<td><p style="color:green">Correct: <b><c:out value="${totalCorrect}"/></b></p></td>
				<td><p style="color:red">Wrong: <b><c:out value="${totalWrong}"/></b></p></td>
				<td><p>Not Answered: <b><c:out value="${totalNotAnswered}"/></b></p></td>
			</tr>
		</table>
		<br/>
		<table border="1" style="width:50%;margin:0 auto auto">
			<tr>
				<th>Id</th>
				<th>Question</th>
				<th>You Answered</th>
				<th>Result</th>
				<th>Time Taken</th>
			</tr>
			<c:forEach items="${data}" var="current">
		        <tr>
		        	<td align="center"><c:out value="${current.id}"/></td>
		        	<td align="center"><c:out value="${current.value}"/></td>
		        	<td align="center"><c:out value="${current.answer}"/></td>
		        	<c:if test="${current.result == 'Correct'}">
		        		<td align="center"><p style="color:green">${current.result}</p></td>
		        	</c:if>
		        	<c:if test="${current.result == 'Wrong'}">
		        		<td align="center"><p style="color:red">${current.result}</p></td>
		        	</c:if>
		        	<c:if test="${current.result == 'Not Answered'}">
		        		<td align="center"><p>${current.result}</p></td>
		        	</c:if>
		        	<td align="center"><c:out value="${current.answerTime}"/></td>
		        </tr>
			</c:forEach>
		</table>
		<br/><br/><br/>
	</div>
</div>
</body>
</html>