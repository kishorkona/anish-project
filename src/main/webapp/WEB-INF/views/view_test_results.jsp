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
<head>
<script> 

</script>
</head>
<body>
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
			<td style="width:70%;">
				<c:if test="${error!=null}">${error}</c:if>
				<table border="1" style="width:50%;margin:0 auto auto">
					<tr>
						<th align="left"><a href="\anishtestsnew/tests/view/${other_name_key}">View ${other_name} IXL Tests</a></th>
						<th colspan="2">${name}</th>
					</tr>
					<tr>
						<th>Test Name</th>
						<th>Test Id</th>
						<th>Completed</th>	
					</tr>
					<c:forEach items="${data}" var="current">
						<tr>
							<td align="center">
								<a href="\anishtestsnew/ixl/test/results/${current.questionId}/${user_name_key}"><c:out value="${current.questionName}"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="\anishtestsnew/ixl/test/resultsVerify/${current.questionId}/${user_name_key}"><c:out value="${current.questionName}"/>-Verify</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<c:if test="${current.resultsClean}">
									<a href="\anishtestsnew/ixl/test/clean/${current.questionId}/${user_name_key}">clean</a>
								</c:if>
								<c:if test="${current.existingTests}">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<p style="color:red">Current Test</p>
								</c:if>
							</td>
							<td align="center">${current.questionId}</td>
							<td align="center"><c:out value="${current.totalCurrentQuestions}"/></td>
						</tr>
					</c:forEach>
					<c:if test="${redWordsExists!=null && redWordsExists == 'true'}">
						<c:forEach items="${redWords}" var="word">
							<tr>
								<td align="center">
									<a href="\anishtestsnew/redwords/results/${user_name_key}/${word.fileNo}"><c:out value="${word.testName}"/></a>
								</td>
								<td align="center">${word.fileNo}</a></td>
								<td align="center"><c:out value="${word.totalWords}"/></td>
							</tr>
						</c:forEach>
					</c:if>
					<tr>
						<td align="center" colspan="3">
							<a href="\anishtestsnew/tests/alzebra/${user_name_key}">Print Geometry Tests</a>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="3">
							<a href="\anishtestsnew/tables/generateTableTest/${uniqueId}/${user_name_key}">Print Table With Doubles Tests</a>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="3">
							<a href="\anishtestsnew/unit/${user_name_key}">Print Unit Conversion Tests</a>
						</td>
					</tr>					
				</table>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
	</table>
</body>
</html>