<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Closed orders</title>
</head>
<body>
	<h3>Closed orders</h3> <br>



		Page ${page} of ${pageCount}

    	|

    	<c:choose>
    		<c:when test="${page - 1 > 0}">
    			<a href="closed_orders?page=${page-1}&pageSize=${pageSize}">Previous</a>
    		</c:when>
    		<c:otherwise>
    			Previous
    		</c:otherwise>
    	</c:choose>


    	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
    		<c:choose>
    			<c:when test="${page == p}">${p}</c:when>
    			<c:otherwise>
    				<a href="closed_orders?page=${p}&pageSize=${pageSize}">${p}</a>
    			</c:otherwise>
    		</c:choose>
    	</c:forEach>

    	<c:choose>
    		<c:when test="${page + 1 <= pageCount}">
    			<a href="closed_orders?page=${page+1}&pageSize=${pageSize}">Next</a>
    		</c:when>
    		<c:otherwise>
    			Next
    		</c:otherwise>
    	</c:choose>

    	|

    	<form action="closed_orders" style='display:inline;'>
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

    	<c:forEach items="${closedOrders}" var="order">
            Order number: ${order.id} <br>
        	Car: ${order.brand.name} ${order.car.model}, (ID: ${order.car.id}) <br>
        	Rent start date: ${order.rentStart} <br>
        	Rent end date: ${order.rentEnd} <br>

        	<c:if test="${order.damageReceipt.damageCost > 0}">
        	<br>
                    The car was damaged! <br>
                    Damage cost: ${order.damageReceipt.damageCost} UAH <br>
                    Damage payment status: ${order.damageReceipt.damagePaymentStatus == 1 ? 'Paid' : 'Not paid'} <br>
                </c:if>

        	<hr>
    	</c:forEach>

    	<br>






        <br> <br>
        <a href="logged_manager.jsp">Back</a>
        <br> <br>
        <a href="logout">Log out</a>
</body>
</html>