<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>Brands management</title>
</head>
<body>

<form action="brand_add" method="get">
	<input name="brand" placeholder="insert brand" pattern="[A-Za-z]{1,}">
	<input type="submit" value="Add brand">
</form> 
<br>
<hr>

<c:forEach items="${brands}" var="brand">
	${brand.name}
	<a href="deleteBrand?id=${brand.id}">delete</a>
	<br>
</c:forEach>

<br>
<hr>

<a href="logged_admin.jsp">Back</a>

<br> <a href="index.html">Log out</a>

</body>
</html>