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
		<table border="1" style="width:90%;margin:0 auto auto">
            <tr>
                <td>Start Time: <c:if test="${startTime != null}">&nbsp;&nbsp;&nbsp;<c:out value="${startTime}"/></c:if></td>
                <td>End Time: <c:if test="${endTime != null}">&nbsp;&nbsp;&nbsp;<c:out value="${endTime}"/></c:if></td>
                <td>Total Time: <c:if test="${totalTimeSpent != null}">&nbsp;&nbsp;&nbsp;<c:out value="${totalTimeSpent}"/></c:if></td>
            </tr>
            </table>
        <br/>
		<table border="1" style="width:90%;margin:0 auto auto">
			<tr>
				<th>Test Id</th>
				<th>Test Name</th>
				<th>Question No</th>
				<th>Subject</th>
				<th>Grade</th>
				<th>Section</th>
				<th>Duration</th>
				<th>Section Name</th>
				<th>Sub Section Name</th>
				<th>Check</th>
				<th>Level</th>
			</tr>
			<c:forEach items="${data}" var="current">
		        <tr>
		        	<td align="center"><c:out value="${current.questionId}"/></td>
		        	<td align="center"><c:out value="${current.questionName}"/></td>
		        	<td align="center"><c:out value="${current.id}"/></td>
		        	<td align="center"><c:out value="${current.subject}"/></td>
		        	<td align="center"><c:out value="${current.grade}"/></td>
		        	<td align="left"><c:out value="${current.sectionId}"/>.<c:out value="${current.subSectionId}"/></td>
		        	<td align="left"><c:out value="${current.sectionName}"/></td>
		        	<td align="center"><c:if test="${current.questionDuration != null}"><c:out value="${current.questionDuration}"/></c:if></td>
		        	<c:if test="${current.wordProbleum}">
			        	<td align="left" style="color:red">
			        		<c:out value="${current.grade}"/>.<c:out value="${current.subSectionName}"/>&nbsp;
			        		<a href="\anishtestsnew/ixl/updateResultStatus/${current.questionId}/${user_name_key}/1/${current.id}">Need Practice</a>&nbsp;/
			        		<a href="\anishtestsnew/ixl/updateResultStatus/${current.questionId}/${user_name_key}/2/${current.id}">Good</a>&nbsp;/
			        		<a href="\anishtestsnew/ixl/updateResultStatus/${current.questionId}/${user_name_key}/3/${current.id}">Skiped</a>
			        		&nbsp;|&nbsp;
			        		<a href="${current.url}" target="_blank">View</a>&nbsp;&nbsp;&nbsp;<a href="${current.resultsUrl}" target="_blank">Results</a>&nbsp;
			        	</td>
					</c:if>
		        	<c:if test="${!current.wordProbleum}">
			        	<td align="left">
			        		<c:out value="${current.grade}"/>.<c:out value="${current.subSectionName}"/>&nbsp;
			        		<a href="\anishtestsnew/ixl/updateResultStatus/${current.questionId}/${user_name_key}/1/${current.id}">Need Practice</a>&nbsp;/
			        		<a href="\anishtestsnew/ixl/updateResultStatus/${current.questionId}/${user_name_key}/2/${current.id}">Good</a>&nbsp;/
			        		<a href="\anishtestsnew/ixl/updateResultStatus/${current.questionId}/${user_name_key}/3/${current.id}">Skiped</a>
			        		&nbsp;|&nbsp;
			        		<a href="${current.url}" target="_blank">View</a>&nbsp;&nbsp;&nbsp;<a href="${current.resultsUrl}" target="_blank">Results</a>&nbsp;
			        	</td>
					</c:if>		        	
		        	<td align="left">
		        		<c:if test="${current.verified == '0'}">
			        		<p>Check</p>
			        	</c:if>
			        	<c:if test="${current.verified == '1'}">
			        		<c:if test="${current.status == '1'}">
				        		<p style="color:red"><b>Need Practice</b></p>
				        	</c:if>
				        	<c:if test="${current.status == '2'}">
				        		<p style="color:green"><b>Good</b></p>
				        	</c:if>
				        	<c:if test="${current.status == '3'}">
				        		<p style="color:blue"><b>Skiped</b></p>
				        	</c:if>
			        	</c:if>
		        	</td>
		        	<td align="center">
		        		<c:if test="${current.questionStatus == '0'}">
				        		<p style="color:red"><b>Difficult</b></p>
						</c:if>
						<c:if test="${current.questionStatus == '1'}">
							<p style="color:green"><b>Current</b></p>
						</c:if>
						<c:if test="${current.questionStatus == '3'}">
							<p style="color:blue"><b>Need Practice</b></p>
						</c:if>
		        	</td>
		        </tr>
			</c:forEach>
		</table>
		<br/><br/><br/>
	</div>
</div>
</body>
</html>