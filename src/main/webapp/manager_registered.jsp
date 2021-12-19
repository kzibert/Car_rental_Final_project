<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Manager added</title>
</head>
<body>

<h2> Manager has been added! </h2>

<br>
<hr>

Please, choose on of the options: <br>
<a href="register_manager.jsp">Add more managers</a> <br> <br>
<a href="logged_admin.jsp">Back</a>

<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>