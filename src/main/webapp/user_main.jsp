<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://com.zibert" prefix="mylib" %>

<html>
<head>
	<title>Main</title>
</head>
<body>
	Hello ${user.name} ${user.surname}! You've logged as <mylib:role user="${user}" />
	<hr>
	<br> <br>
    <a href="choose_car">Rent a car</a>
	<br> <br>
    <a href="user_orders">My orders</a>
    <br> <br>
    <a href="logout">Log out</a>
</body>
</html>