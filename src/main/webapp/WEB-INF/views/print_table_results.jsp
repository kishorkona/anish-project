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
		<c:if test="${dataExists == 'true'}">
			<!-- 
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr>
					<td valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${testData1}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${testData2}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${testData3}" var="current">
								<tr><td align="left">${current.value}&nbsp;&nbsp;&nbsp;</td></tr>
							</c:forEach>
						</table>
					</td>
					<td valign="top" align="left">
						Total Questions: <b>${totalQuestions}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wrongs:
						<br/><br/>
						<b>[${uniqueId1}]</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Date:</b>
						<br/><br/>
						<b>##### ${user_name_key_caps} ###</b>
					</td>
				</tr>
			</table>
			 -->
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr>
					<td align="left" colspan="6">
						<b>${user_name_key_caps}</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>Start Time:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>End Time:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<b>Total Time:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td align="left" colspan="6">
						<b>[${uniqueId1}]</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Date:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Total Questions: <b>${totalQuestions}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wrongs:
					</td>
				</tr>
				<tr>
					<td width="16.6%" valign="top">
						<table border="1" style="width:100%;margin:0 auto auto">
							<c:forEach items="${data1}" var="current" varStatus="loopStatus">
								<tr>
									<td align="left">
										${current.value}
										<c:if test="${loopStatus.count == 30}">
											<br><b>[${uniqueId1}]</b>
										</c:if>										
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${data2}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${data3}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${data4}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${data5}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${data6}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
		</c:if>
		<br/>
		<table border="1" style="width:100%;margin:0 auto auto">
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
			</table>
			<br/>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr>
					<td align="left" colspan="6">
						<b>Practice Tables:</b>
					</td>
				</tr>
				<tr>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${practiceData1}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${practiceData2}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${practiceData3}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${practiceData4}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${practiceData5}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${practiceData6}" var="current">
								<tr><td align="left">${current.value}</td></tr>
							</c:forEach>
						</table>
					</td>
				</tr>
				<tr><td valign="top" colspan="6" align="left"><b>[${uniqueId1}]</b></td></tr>
			</table>
	</div>
</div>
</body>
</html>