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
						<th align="right">Test Name&nbsp;&nbsp;&nbsp;</th>
						<th align="left">&nbsp;&nbsp;&nbsp;Test Id</th>
						<th align="left">&nbsp;&nbsp;&nbsp;Question To Complete</th>	
					</tr>
					<!--  This is for IXL -->
					<c:forEach items="${data}" var="current">
						<tr>
							<td align="right">
								<c:out value="${current.questionName}"/>&nbsp;&nbsp;&nbsp;
								<c:if test="${current.existingTests}">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<p style="color:red">Current Test</p>
								</c:if>
							</td>
							<td align="left">
								&nbsp;&nbsp;&nbsp;<a href="\anishtestsnew/tests/getTestLink/${current.questionId}/${user_name_key}">${current.questionId}</a>
							</td>
							<td align="left">&nbsp;&nbsp;&nbsp;<c:out value="${current.totalCurrentQuestions}"/></td>
						</tr>
					</c:forEach>
					<!--  This is for Redwords
					<c:if test="${redWordsExists!=null && redWordsExists == 'true'}">
						<c:forEach items="${redWords}" var="word">
							<tr>
								<td align="center"><c:out value="${word.testName}"/></td>
								<td align="center">
									<a href="\anishtestsnew/redwords/getTestLink/${user_name_key}/${word.fileNo}">${word.fileNo}</a>
								</td>
								<td align="center"><c:out value="${word.totalWords}"/></td>
							</tr>
						</c:forEach>
					</c:if>
					-->
					<!--
					<c:if test="${user_name_key == 'ishant'}">
						<tr>
							<td align="center"><a href="\anishtestsnew/doubleHalf/showQuestions/${user_name_key}">Double And Half</a></td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>					
					</c:if>
					<tr>
						<td align="center"><a href="\anishtestsnew/tables/startTest/${user_name_key}">Tables</a></td>
						<td>&nbsp;</td>
						<td align="center">${tableQuestionLeft}</td>
					</tr>
					-->
				</table>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
	</table>
</body>
</html>