<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<style>
table, th {
  border:1px solid black;
  border-collapse: collapse;
}
tr {
	height: 35px;
}
table.center {
  margin-left: auto; 
  margin-right: auto;
}
table td {
    border: 1px solid black;
    border-width: 1px 1px 0 0;
}
</style>
<body>
<div class="bodypart">
	<div>
		<c:if test="${doubleDataExists == 'true'}">
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr>
					<td align="center"><b>${user_name_key_caps}</b></td>
					<td align="left" colspan="5">
						<b>Date:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>Time Given:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>Time Taken:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>Answered:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>Wrongs:</b>
					</td>
				</tr>
				<tr>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${doubleData1}" var="current">
								<tr><td align="left">${current.printValue}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${doubleData2}" var="current">
								<tr><td align="left">${current.printValue}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${doubleData3}" var="current">
								<tr><td align="left">${current.printValue}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${doubleData4}" var="current">
								<tr><td align="left">${current.printValue}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${doubleData5}" var="current">
								<tr><td align="left">${current.printValue}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${doubleData6}" var="current">
								<tr><td align="left">${current.printValue}</td></tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<c:if test="${user_name_key == 'anish'}">
					<tr><td valign="top" colspan="6" align="right">${uniqueId}</td></tr>
				</c:if>
			</table>
		</c:if>
	</div>
</div>
</body>
</html>