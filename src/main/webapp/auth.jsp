<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Sign in</title>
</head>
<body>
<h3>Log in here:</h3>
	<form action="auth" method="post">
	<input name="email" placeholder="E-mail" pattern="^[a-zA-Z0-9\._%+-]+@[a-zA-Z0-9\.-]+\.[a-zA-Z]{2,6}$" required> <br>
	<input type="password" name="password" placeholder="Password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
	<input type="submit" value="Sign in">
	</form>

	<br>

	<a href="registration.jsp">Don't have an account?</a>
</body>
</html>