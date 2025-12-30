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
		<img src="<c:url value="/images/tests.jpg"/>" width="50" height="75">
		</div>
		<div class="second">Tests App</div>
	</div><br/><br/>
<hr/><br/>
<div class="bodypart">
	<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
	<div class="bodypart2">
		<table border="1" style="width:100%;margin:0 auto auto">
			<tr>
				<th>Anish</th>
				<th>Ishant</th>
			</tr>
			<tr>
                <td><a href="\anishtestsnew/buildXmls/ixl/anish">IXL Scripts</a></td>
                <td><a href="\anishtestsnew/buildXmls/ixl/ishant">IXL Scripts</a></td>
            </tr>
		</table>
	</div>
</div>
</body>
</html>