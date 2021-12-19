<%@ include file="directives.jspf" %>
<html>
<head>
	<title>Blocked</title>
</head>
<body>
	User ${user.name} ${user.surname} is blocked. Please, contact admin for more information.

	<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>