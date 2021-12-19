<%@ include file="directives.jspf" %>
<%@ taglib uri="http://com.zibert" prefix="mylib" %>

<html>
<head>
	<title>Cancelled orders</title>
</head>
<body>
	<h3>Cancelled orders</h3> <br>

		Page ${page} of ${pageCount}

    	|

    	<c:choose>
    		<c:when test="${page - 1 > 0}">
    			<a href="cancelled_orders?page=${page-1}&pageSize=${pageSize}">Previous</a>
    		</c:when>
    		<c:otherwise>
    			Previous
    		</c:otherwise>
    	</c:choose>


    	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
    		<c:choose>
    			<c:when test="${page == p}">${p}</c:when>
    			<c:otherwise>
    				<a href="cancelled_orders?page=${p}&pageSize=${pageSize}">${p}</a>
    			</c:otherwise>
    		</c:choose>
    	</c:forEach>

    	<c:choose>
    		<c:when test="${page + 1 <= pageCount}">
    			<a href="cancelled_orders?page=${page+1}&pageSize=${pageSize}">Next</a>
    		</c:when>
    		<c:otherwise>
    			Next
    		</c:otherwise>
    	</c:choose>

    	|

    	<form action="cancelled_orders" style='display:inline;'>
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

    	<c:forEach items="${cancelledOrders}" var="order">
            Order number: ${order.id} <br>
        	Car: ${order.car.brand.name} ${order.car.model}, (ID: ${order.car.id}) <br>
        	Rent start date: <mylib:date date="${order.rentEnd}" /> <br>
        	Rent end date: <mylib:date date="${order.rentEnd}" /> <br> <br>
        	Reason of cancel: ${order.cancelComments} <br>
        	<hr>
    	</c:forEach>

    	<br>

        <br> <br>
        <a href="logged_manager.jsp">Back</a>
        <br> <br>
        <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>
</body>
</html>