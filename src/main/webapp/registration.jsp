<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Sign up</title>
</head>
<body>
<h3>Registration</h3>
<form action="register" method="post" name="regform" onsubmit="return validation()">
<input name="email" placeholder="E-mail" pattern="^[a-zA-Z0-9\._%+-]+@[a-zA-Z0-9\.-]+\.[a-zA-Z]{2,6}$" required> <br>
<input type="password" name="password" placeholder="Create password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
<input type="password" name="password_check" placeholder="Repeat password" pattern="^(?=.*[0-9a-zA-Z])(?=\S+$).{8,30}$" required> <br>
<input name="name" placeholder="Name" required> <br>
<input name="surname" placeholder="Surname" required> <br>
<input type="submit" value="Sign up">
</form>

<script src="<c:url value="/js/validation.js"/>"></script>

<br>
<a href="auth">Already have an account?</a>

</body>
</html>