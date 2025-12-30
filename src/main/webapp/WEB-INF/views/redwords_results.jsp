<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<style>
table, th, td {
	border: 1px solid black;
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
	<br />
	<br />
	<hr />
	<br />
	<div class="bodypart">
		<div>
			<c:if test="${error!=null}">${error}</c:if>
			<table border="1" style="width: 50%; margin: 0 auto auto">
				<tr>
					<td align="left" style="color:green">&nbsp;&nbsp;&nbsp;Correct: <c:out value="${correctCnt}" /></td>
					<td align="left" style="color:red">&nbsp;&nbsp;&nbsp;Wrong: <c:out value="${wrongCnt}" /></td>
					<td align="left" style="color:blue">&nbsp;&nbsp;&nbsp;Not Answered: <c:out value="${notAnsweredCnt}"/></td>
				</tr>
			</table>
			<br/>
			<table border="1" style="width: 50%; margin: 0 auto auto">
				<tr>
					<th>Id</th>
					<th>Word Name</th>
					<th>You Answered</th>
					<th>Status</th>
					<th>Listen</th>
					<th>Sentence</th>
				</tr>
				<c:forEach items="${data}" var="w">
					<tr>
						<td align="center">&nbsp;&nbsp;&nbsp;<c:out value="${w.id}" /></td>
						<td align="left">&nbsp;&nbsp;&nbsp;<c:out value="${w.name}" /></td>
						<td align="left">&nbsp;&nbsp;&nbsp;<c:out value="${w.ishantWrote}" /></td>
						<td align="left">
							<c:if test="${w.rightOrWrong == 'Wrong'}"><p style="color:red">&nbsp;&nbsp;&nbsp;Wrong</p></c:if>
							<c:if test="${w.rightOrWrong == 'Not Answered'}">&nbsp;&nbsp;&nbsp;Not Answered</c:if>
							<c:if test="${w.rightOrWrong == 'Correct'}"><p style="color:green">&nbsp;&nbsp;&nbsp;Correct</p></c:if>
						</td>
						<td align="center">
							&nbsp;&nbsp;&nbsp;<a href="\anishtestsnew/audio/redwords/${user_name_key}/${w.wordPath}.mp3">Listen Word</a>
							<c:if test="${w.hasSentence}">
								&nbsp;&nbsp;&nbsp;<a href="\anishtestsnew/audio/redwords/${user_name_key}/${word.sentencePath}.mp3">Listen Sentence</a>
							</c:if>
						</td>
						<td align="left">&nbsp;&nbsp;&nbsp;<c:if test="${w.hasSentence}"><c:out value="${w.sentence}" /></c:if></td>
					</tr>
				</c:forEach>
			</table>
			<br /> <br /> <br />
		</div>
	</div>
</body>
</html>