<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Register manager</title>
</head>
<body>
<h3>Manager registration</h3>
<form action="register_manager" method="post" name="regform" onsubmit="return validation()">
<input name="email" placeholder="E-mail" pattern="^[a-zA-Z0-9\._%+-]+@[a-zA-Z0-9\.-]+\.[a-zA-Z]{2,6}$" required> <br>
<input type="password" name="password" placeholder="Create password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
<input type="password" name="password_check" placeholder="Repeat password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
<input name="name" placeholder="Name" required> <br>
<input name="surname" placeholder="Surname" required> <br>
<input type="submit" value="Register manager">
</form>
<br>
<hr>
<a href="logged_admin.jsp">Back</a>
<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

<script src="<c:url value="/js/validation.js"/>"></script>

</body>
</html>