<%@ include file="directives.jspf" %>
<%@ page session="true" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
	<title>Rent order</title>
</head>
<body>
<h2> Please, fill order form below: </h2>
<form action="rent_order" method="post">
<input type="hidden" name="id" value="${id}"/>
Please, insert passport data: <br>
    <input name="passport" value="${passport}" placeholder="AA123456" pattern="[A-Z]{2}[0-9]{6}" required> <br>
Please, check if you need a driver: <br>
    <input type="checkbox" value="1" name="driver" ${driver == 1 ? 'checked' : ''}/>yes, please <br>
   <label for="start">Rent start date:</label> <br>
   <input required type="date" id="start" name="rent-start"
          min="${today}" max="2025-12-31" value="${rentBeg}">
    <br>
    <label for="start">Rent end date:</label> <br>
       <input required type="date" id="start" name="rent-end"
              min="${today}" max="2025-12-31" value="${rentFin}">
    <br>
    <em>${dateInputError}</em> <br>
    <br>

    <input type="submit" value="Rent the car" />

</form>


<br>
<em>${busy}</em>

<br>
<hr>

<a href="user_main.jsp">Main</a> <br>
<a href="choose_car">Back</a> <br>
<a href="logout">Log out</a>



</body>
</html>