<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Rented cars</title>
</head>
<body>
	<h3>Rented cars</h3> <br>

		Page ${page} of ${pageCount}

            	|

            	<c:choose>
            		<c:when test="${page - 1 > 0}">
            			<a href="rented_cars?page=${page-1}&pageSize=${pageSize}">Previous</a>
            		</c:when>
            		<c:otherwise>
            			Previous
            		</c:otherwise>
            	</c:choose>


            	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
            		<c:choose>
            			<c:when test="${page == p}">${p}</c:when>
            			<c:otherwise>
            				<a href="rented_cars?page=${p}&pageSize=${pageSize}">${p}</a>
            			</c:otherwise>
            		</c:choose>
            	</c:forEach>

            	<c:choose>
            		<c:when test="${page + 1 <= pageCount}">
            			<a href="rented_cars?page=${page+1}&pageSize=${pageSize}">Next</a>
            		</c:when>
            		<c:otherwise>
            			Next
            		</c:otherwise>
            	</c:choose>

            	|

            	<form action="rented_cars" style='display:inline;'>
            		<select name="page">
            			<c:forEach begin="1" end="${pageCount}" var="p">
            				<option value="${p}" ${p == param.page ? 'selected' : ''}>${p}</option>
            			</c:forEach>
            		</select>
            		<input name="pageSize" value="${pageSize}" type="hidden" />
            		<input type="submit" value="Go"/>
            	</form>

<br>
<hr>


    	<c:forEach items="${orders}" var="order">
            Order number: ${order.id} <br>
        	Car: ${order.brand.name} ${order.car.model}, (ID: ${order.car.id}) <br>
        	Rent start date: ${order.rentStart} <br>
        	Rent end date: ${order.rentEnd} <br>
        	<form action="car_back" method="post">
        	<input type="hidden" name="order_id" value="${order.id}">
        	<input name="damage_cost" placeholder="Insert damage cost if any" pattern="[0-9]{1,}" > <br>
        	    <input type="submit" value="Car is back">
        	    </form>
        	<hr>
    	</c:forEach>


        <br> <br>
        <a href="logged_manager.jsp">Back</a>
        <br> <br>
        <a href="logout">Log out</a>
</body>
</html>