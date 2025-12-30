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
h1 {
  font-size: 30px;
}
</style>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>User  Money Home page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
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
				<form action="\anishtestsnew/redwords/submitWord" method="post" modelAttribute="word" autocomplete="off">
					<table class="center" style="width:90%">
						<!-- <tr><td><b>Word Id:</b>&nbsp;&nbsp;&nbsp;${word.id}</td></tr> -->
						<tr><td><b>Listen Word:</b>&nbsp;&nbsp;&nbsp;<a href="\anishtestsnew/audio/redwords/${user_name_key}/${word.wordPath}.mp3">Listen Word</a></td></tr>
						<c:if test="${word.hasSentence}">
								<tr><td><b>Listen Sentence:</b>&nbsp;&nbsp;&nbsp;<a href="\anishtestsnew/audio/redwords/${user_name_key}/${word.sentencePath}.mp3">Listen Sentence</a></td></tr>
						</c:if>
						<tr><td colspan="2"><b>Questions Left:</b>&nbsp;&nbsp;&nbsp;${word.wordsLeft}</td></tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr><td colspan="2"><input type="text" id="ishantWrote" name="ishantWrote" value="${word.ishantWrote}"></td></tr>
						<tr><td colspan="2">&nbsp;
							<input type="hidden" id="personName" name="personName" value="${word.personName}" />
							<input type="hidden" id="id" name="id" value="${word.id}" />
							<input type="hidden" id="fileNo" name="fileNo" value="${word.fileNo}" />
							<input type="hidden" id="name" name="name" value="${word.name}" />
							<input type="hidden" id="id" name="id" value="${word.id}" />
						</td></tr>
						<tr><td colspan="2"><input type="submit" value="Save"/></td></tr>
					</table>
				</form>
				<br/>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
	</table>
</body>
</html>