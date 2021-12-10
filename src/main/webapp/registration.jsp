<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title>Sign up</title>
</head>
<body>
<h3>Registration</h3>
<form action="register" method="post">
<input name="email" placeholder="E-mail" pattern="^[a-zA-Z0-9\._%+-]+@[a-zA-Z0-9\.-]+\.[a-zA-Z]{2,6}$" required> <br>
<input type="password" name="password" placeholder="Password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
<input name="name" placeholder="Name" pattern="[a-zA-Zа-яА-ЯёЁ]{1,40}" required> <br>
<input name="surname" placeholder="Surname" pattern="[a-zA-Zа-яА-ЯёЁ]{1,40}" required> <br>
<input type="submit" value="Sign up">
</form>

<br>
<a href="login_page">Already have an account?</a>

</body>
</html>