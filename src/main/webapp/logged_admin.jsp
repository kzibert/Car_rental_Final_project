<%@ include file="directives.jspf" %>
<%@ taglib uri="http://com.zibert" prefix="mylib" %>

<html>
<head>
	<title>Main</title>
</head>
<body>
	You have logged in successfully as: ${user.name} (<mylib:role user="${user}" />)
<br>
<hr>
	<a href="brand_list">Add/delete car brands</a> <br> <br>
    <a href="car_manage">Car management</a> <br> <br>
    <a href="user_manage">User management</a> <br> <br>
    <a href="register_manager.jsp">Register new manager</a> <br> <br>

    <br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>


</body>
</html>