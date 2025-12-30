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
						<b>[${uniqueId}]</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Date:</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Total Questions: <b>${totalQuestions}</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Wrongs:
					</td>
				</tr>
			</table>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr><td align="center" colspan="4"><b>Units</b></td></tr>
				<tr>
					<td align="left">Units => Customary Units Conversion: <b>C</b></td>
					<td align="left">Units => Metric Units Conversion: <b>M</b></td>
					<td align="left" colspan="2">Units => Customary to Metric Units Conversion: <b>B</b></td>
				</tr>
				<tr>
					<td align="left">Type => Length: <b>L</b></td>
					<td align="left">Type => Capacity: <b>C</b></td>
					<td align="left">Type => Weight/Mass: <b>W</b></td>
					<td align="left">Type => Temperature: <b>T</b></td>
				</tr>
			</table>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr>
					<td width="16.6%" valign="top">
						<table border="1" style="width:100%;margin:0 auto auto">
							<c:forEach items="${unitData1}" var="current" varStatus="loopStatus">
								<tr>
									<td align="left">
										[${current.id}]&nbsp;&nbsp;${current.questonName1}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${current.questonName1Answer}
										<c:if test="${not empty current.questonName2}">
											<br/>${current.questonName2}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${current.questonName2Answer}
										</c:if>
										<c:if test="${loopStatus.count == 17}">
											<br/><b>[${uniqueId}]</b>
										</c:if>										
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="1" style="width:100%;margin:0 auto auto">
							<c:forEach items="${unitData2}" var="current">
								<tr>
									<td align="left">
										[${current.id}]&nbsp;&nbsp;${current.questonName1}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${current.questonName1Answer}
										<c:if test="${not empty current.questonName2}">
											<br/>${current.questonName2}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${current.questonName2Answer}
										</c:if>										
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr><td colspan="6" align="center"><b>Perfect Cube</b></td></tr>
				<tr>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${cubeData1}" var="current">
								<tr><td align="left">${current.value} =</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${cubeData2}" var="current">
								<tr><td align="left">${current.value} =</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${cubeData3}" var="current">
								<tr><td align="left">${current.value} =</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${cubeData4}" var="current">
								<tr><td align="left">${current.value} =</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${cubeData5}" var="current">
								<tr><td align="left">${current.value} =</td></tr>
							</c:forEach>
						</table>
					</td>
					<td width="16.6%" valign="top">
						<table border="0" style="width:100%;margin:0 auto auto">
							<c:forEach items="${cubeData6}" var="current">
								<tr><td align="left">${current.value} =</td></tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</table>

			<table border="1" style="width:100%;margin:0 auto auto">
                <tr><td colspan="6" align="center"><b>Answer Questions</b></td></tr>
                <tr>
                    <td width="16.6%" valign="top">
                        <table border="0" style="width:100%;margin:0 auto auto">
                            <c:forEach items="${part1Questions}" var="current">
            				    <tr><td align="left">${current.question}&nbsp;:&nbsp; </td></tr>
            			    </c:forEach>
            		    </table>
                    </td>
                    <td width="16.6%" valign="top">
                        <table border="0" style="width:100%;margin:0 auto auto">
                            <c:forEach items="${part2Questions}" var="current">
            				    <tr><td align="left">${current.question}&nbsp;:&nbsp;</td></tr>
            			    </c:forEach>
            		    </table>
                    </td>
                    <td width="16.6%" valign="top">
                        <table border="0" style="width:100%;margin:0 auto auto">
                            <c:forEach items="${part3Questions}" var="current">
            				    <tr><td align="left">${current.question}&nbsp;:&nbsp;</td></tr>
            			    </c:forEach>
            		    </table>
                    </td>
                </tr>
            </table>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr><td align="left"><b>[${uniqueId}]</b></td></tr>
			</table>
		</c:if>
	</div>
</div>
</body>
</html>