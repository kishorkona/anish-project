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
					<td align="center" valign="top">
						<b>${user_name_key_caps}&nbsp;&nbsp;(${uniqueId})
						</br>&nbsp;&nbsp;</br>&nbsp;&nbsp;</b>
					</td>
					<td align="left" valign="top"><b>Start Time:</b></td>
					<td align="left" valign="top"><b>End Time:</b></td>
					<td align="left" valign="top"><b>Total Time:</b></td>
					<td align="left" valign="top"><b>Total Questions:</b></td>
					<td align="left" valign="top"><b>Wrongs:</b></td>
				</tr>
			</table>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr>
					<td width="100%" valign="top">
						<c:forEach items="${data1}" var="current">
							<table border="1" style="width:100%;margin:0 auto auto">
								<tr>
									<td align="left" width="10px" valign="top">${current.id}</td>
									<c:if test="${current.hasImage == '2'}">
										<td align="left" height="${current.rowHight}px" valign="top">${current.subject}</td>
			        					<td align="right" style="width: ${current.imageWidth}px; height: ${current.imageHeight}px; border: 1px solid black;">
											<img src="<c:url value="/images/alzebra/${current.imageName}.jpg"/>" width="100" height="500" style="width: 100%; height: 100%; object-fit: cover;">
										</td>
		        					</c:if>
									<c:if test="${current.hasImage == '3'}">
										<td align="left" colspan="2" height="${current.rowHight}px" valign="top">${current.id}.&nbsp;${current.subject}</td>
		        					</c:if>
									<c:if test="${current.hasImage == '1'}">
										<td align="right" style="width: ${current.imageWidth}px; height: ${current.imageHeight}px; border: 1px solid black;">
											<img src="<c:url value="/images/alzebra/${current.imageName}.jpg"/>" width="100" height="500" style="width: 100%; height: 100%; object-fit: cover;">
										</td>
		        					</c:if>
								</tr>
							</table>
						</c:forEach>
					</td>
					<!-- 
					<td width="50%" valign="top">
						<c:forEach items="${data2}" var="current">
							<table border="1" style="width:100%;margin:0 auto auto">
								<tr>
									<td align="left" width="10px" valign="top">${current.id}</td>
									<c:if test="${current.hasImage == '2'}">
										<td align="left" height="${current.rowHight}px" valign="top">${current.subject}</td>
			        					<td align="right" style="width: ${current.imageWidth}px; height: 100px; border: 1px solid black;">
											<img src="<c:url value="/images/alzebra/${current.imageName}.jpg"/>" width="500" height="500" style="width: 100%; height: 100%; object-fit: cover;">
										</td>
		        					</c:if>
									<c:if test="${current.hasImage == '3'}">
										<td align="left" colspan="2" height="${current.rowHight}px" valign="top">${current.id}.&nbsp;${current.subject}</td>
		        					</c:if>
									<c:if test="${current.hasImage == '1'}">
										<td align="left" colspan="2" height="${current.rowHight}px" valign="top">${current.id}.&nbsp;${current.subject}</td>
		        					</c:if>
								</tr>
							</table>
						</c:forEach>
					</td>
					-->
				</tr>
			</table>
			<table border="1" style="width:100%;margin:0 auto auto">
				<tr><td align="left" valign="top"><b>${user_name_key_caps}&nbsp;&nbsp;(${uniqueId})</td></tr>
			</table>
		</c:if>
	</div>
</div>
</body>
</html>