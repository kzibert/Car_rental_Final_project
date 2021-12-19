<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Brands management</title>
</head>
<body>

<form action="brand_add" method="post">
	<input name="brand" placeholder="insert brand" pattern="[A-Za-z]{2,}" required>
	<input type="submit" value="Add brand">
</form> 
<br>
<hr>

<c:forEach items="${brands}" var="brand">
	<form action="deleteBrand" method="post">
	${brand.name}
	<input type="hidden" name="id" value="${brand.id}">
	<input type="submit" value="delete">
	</form>
</c:forEach>

<br>
<hr>

<a href="logged_admin.jsp">Back</a>

<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>