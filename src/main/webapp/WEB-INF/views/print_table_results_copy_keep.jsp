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
	<table border="1" style="width:100%">
		<tr>
			<td style="vertical-align:top; table-layout:fixed; word-wrap:break-word; overflow: hidden; width:1%">
				<textarea id="test" name="test" rows="4" cols="50" style="width: 30px; height: 216px;">
					kishordffffffffffffffffffffffffffffffffffffffffffffff
				</textarea>
			</td>
			<td style="width:99%">
				<div>
					<c:if test="${dataExists == 'true'}">
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
									<!-- (${uniqueId})&nbsp;&nbsp;&nbsp;&nbsp; -->
									Total Test Questions: <b>${totalQuestions}</b>
									<br/><br/>
									Total Practice Questions: <b>${totalPracticeQuestions}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wrongs:
									<br/><br/>
									<b>Date:</b>
								</td>
							</tr>
						</table>
						<table border="1" style="width:100%;margin:0 auto auto">
							<tr>
								<td align="center"><b>${user_name_key_caps}</b></td>
								<td align="left" colspan="5">
									<b>Start Time:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<b>End Time:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<b>Total Time:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<b>Wrongs:</b>
								</td>
							</tr>
							<tr>
								<td width="16.6%" valign="top">
									<table border="0" style="width:100%;margin:0 auto auto">
										<c:forEach items="${data1}" var="current">
											<tr><td align="left">${current.value}</td></tr>
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
							<!-- <tr><td valign="top" colspan="6" align="right">${uniqueId}</td></tr>  -->
						</table>
				</div>				
			</td>
		</tr>
	</table>
</div>
</body>
</html>