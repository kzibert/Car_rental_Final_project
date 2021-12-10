<%@ include file="directives.jspf" %>

<html>
<head>
	<title>New orders</title>
</head>
<body>
	<h3>New orders</h3> <br>

	Page ${page} of ${pageCount}

        	|

        	<c:choose>
        		<c:when test="${page - 1 > 0}">
        			<a href="manager_orders?page=${page-1}&pageSize=${pageSize}">Previous</a>
        		</c:when>
        		<c:otherwise>
        			Previous
        		</c:otherwise>
        	</c:choose>


        	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
        		<c:choose>
        			<c:when test="${page == p}">${p}</c:when>
        			<c:otherwise>
        				<a href="manager_orders?page=${p}&pageSize=${pageSize}">${p}</a>
        			</c:otherwise>
        		</c:choose>
        	</c:forEach>

        	<c:choose>
        		<c:when test="${page + 1 <= pageCount}">
        			<a href="manager_orders?page=${page+1}&pageSize=${pageSize}">Next</a>
        		</c:when>
        		<c:otherwise>
        			Next
        		</c:otherwise>
        	</c:choose>

        	|

        	<form action="manager_orders" style='display:inline;'>
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
    	Status: ${order.status} <br>
    	Car: ${order.brand.name} ${order.car.model}, (ID: ${order.car.id})<br>
    	Rent start date: ${order.rentStart} <br>
    	Rent end date: ${order.rentEnd} <br>

   <c:if test="${order.orderReceipt.paymentStatus == 1}">
   Payment status: Paid <br>

    	<form action="approve_order" method="post">
    	<input type="hidden" name="order_id" value="${order.id}">
    	    <input type="submit" value="Approve order">
    	    </form>

   </c:if>

   <c:if test="${order.orderReceipt.paymentStatus == 0}">
   Payment status: Not paid <br>
   </c:if>

    	<form action="cancel_order" method="post">
    	<input type="hidden" name="order_id" value="${order.id}">
    	<input name="comment" placeholder="Insert cancel comments" required>
             <input type="submit" value="Cancel order">
                	    </form>
    	<hr>
	</c:forEach>

    <br> <br>
    <a href="logged_manager.jsp">Back</a>
    <br> <br>
    <a href="logout">Log out</a>
</body>
</html>