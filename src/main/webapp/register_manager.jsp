<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Register manager</title>
</head>
<body>
<h3>Manager registration</h3>
<form action="register_manager" method="post">
<input name="email" placeholder="E-mail" pattern="^[a-zA-Z0-9\._%+-]+@[a-zA-Z0-9\.-]+\.[a-zA-Z]{2,6}$" required> <br>
<input type="password" name="password" placeholder="Password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
<input name="name" placeholder="Name" pattern="[a-zA-Zа-яА-ЯёЁ]{1,40}" required> <br>
<input name="surname" placeholder="Surname" pattern="[a-zA-Zа-яА-ЯёЁ]{1,40}" required> <br>
<input type="submit" value="Register manager">
</form>
<br>
<hr>
<a href="logged_admin.jsp">Back</a>
<br> <a href="index.html">Log out</a>
</body>
</html>