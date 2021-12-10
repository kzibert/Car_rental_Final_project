<%@ include file="directives.jspf" %>

<html>
<head>
	<title>User management</title>
</head>
<body>

<c:forEach items="${users}" var="user">
<form action="user_status_update" method="get">
<input type="hidden" name="id" value="${user.id}">
${user.name} ${user.surname}, ${user.email}
<select name="status" default value="${user.status}">
            <option value="0" ${user.status == 0 ? 'selected' : ''}>Unblocked</option>
            <option value="1" ${user.status == 1 ? 'selected' : ''} >Blocked</option>
    </select>
	<input type="submit" value="Update status">
</form>
</c:forEach>

<br>
<hr>

<a href="logged_admin.jsp">Back</a>

<br> <a href="index.html">Log out</a>

</body>
</html>