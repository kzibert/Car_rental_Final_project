<%@ include file="directives.jspf" %>
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
    <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>


</body>
</html>