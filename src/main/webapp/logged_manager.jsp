<%@ include file="directives.jspf" %>
<%@ taglib uri="http://com.zibert" prefix="mylib" %>

<html>
<head>
	<title>Main</title>
</head>
<body>
	You have logged in successfully as: ${user.name} (<mylib:role user="${user}" />) <br>

	<br> <a href="manager_orders">New orders</a> <br> <br>

	<a href="rented_cars">Orders in process</a> <br> <br>

	<a href="closed_orders">Closed orders</a> <br> <br>

	<a href="cancelled_orders">Cancelled orders</a> <br>

	<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>
</body>
</html>