<%@ include file="directives.jspf" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://com.zibert" prefix="mylib" %>

<html>
<head>
	<title><fmt:message key="label.orders"/></title>
</head>
<body>
	<h2><fmt:message key="label.orders"/></h2>

<hr>
	
			<fmt:message key="label.page"/> ${page} <fmt:message key="label.page_of_page"/> ${pageCount}
    
                	|
    
                	<c:choose>
                		<c:when test="${page - 1 > 0}">
                			<a href="user_orders?page=${page-1}&pageSize=${pageSize}"><fmt:message key="label.previous_page"/></a>
                		</c:when>
                		<c:otherwise>
                			<fmt:message key="label.previous_page"/>
                		</c:otherwise>
                	</c:choose>
    
    
                	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
                		<c:choose>
                			<c:when test="${page == p}">${p}</c:when>
                			<c:otherwise>
                				<a href="user_orders?page=${p}&pageSize=${pageSize}">${p}</a>
                			</c:otherwise>
                		</c:choose>
                	</c:forEach>
    
                	<c:choose>
                		<c:when test="${page + 1 <= pageCount}">
                			<a href="user_orders?page=${page+1}&pageSize=${pageSize}"><fmt:message key="label.next_page"/></a>
                		</c:when>
                		<c:otherwise>
                			<fmt:message key="label.next_page"/>
                		</c:otherwise>
                	</c:choose>
    
                	|
    
                	<form action="user_orders" style='display:inline;'>
                		<select name="page">
                			<c:forEach begin="1" end="${pageCount}" var="p">
                				<option value="${p}" ${p == param.page ? 'selected' : ''}>${p}</option>
                			</c:forEach>
                		</select>
                		<input name="pageSize" value="${pageSize}" type="hidden" />
                		<input type="submit" value="<fmt:message key='form.submit_page_choice'/>"/>
                	</form>
    
    <br>
    <hr>

    <c:set var="Paid">
        <fmt:message key="label.payment_status_paid"/>
    </c:set>
    <c:set var="Not_paid">
            <fmt:message key="label.payment_status_not_paid"/>
    </c:set>

    <c:set var="Order_received">
            <fmt:message key="label.order_received"/>
    </c:set>
    <c:set var="Order_approved">
            <fmt:message key="label.order_approved"/>
    </c:set>
    <c:set var="Order_closed">
            <fmt:message key="label.order_closed"/>
    </c:set>
    <c:set var="Order_cancelled">
            <fmt:message key="label.order_cancelled"/>
    </c:set>

	<c:forEach items="${orders}" var="order">
    	<fmt:message key="label.order_number"/>: ${order.id} <br>
    	<fmt:message key="label.order_status"/>:
    	    ${order.status == 'Order received' ? Order_received : ''}
    	    ${order.status == 'Order approved' ? Order_approved : ''}
    	    ${order.status == 'Order closed' ? Order_closed : ''}
    	    ${order.status == 'Order cancelled' ? Order_cancelled : ''} <br>
    	<fmt:message key="label.car"/>: ${order.car.brand.name} ${order.car.model} <br>
    	<fmt:message key="label.rent_start_date"/>: <mylib:date date="${order.rentStart}" /> <br>
    	<fmt:message key="label.rent_end_date"/>: <mylib:date date="${order.rentEnd}" /> <br>
    	<fmt:message key="label.rent_cost"/>: ${order.orderReceipt.cost} <fmt:message key="label.uah"/> <br>
    	<fmt:message key="label.payment_status"/>: ${order.orderReceipt.paymentStatus == 1 ? Paid : Not_paid} <br>

    <c:if test="${order.damageReceipt.damageCost > 0}">
        <br>
        <fmt:message key="label.the_car_was_damaged"/> <br>
        <fmt:message key="label.damage_cost"/>: ${order.damageReceipt.damageCost} <fmt:message key="label.uah"/> <br>
        <fmt:message key="label.damage_payment_status"/>: ${order.damageReceipt.damagePaymentStatus == 1 ? Paid : Not_paid} <br>
    </c:if>


    <c:if test="${order.orderReceipt.paymentStatus == 0 && order.status != 'Order closed' && order.status != 'Order cancelled'}">
    <br>
        <form action="show_order_receipt" method="get">
        <input type="hidden" name="order_id" value="${order.id}"/>
        <input type="submit" value="<fmt:message key='form.pay_order_button'/>" />
        </form>
    </c:if>

    <c:if test="${order.damageReceipt.damageCost > 0 && order.damageReceipt.damagePaymentStatus != 1}">
    <br>
       	<form action="show_damage_receipt" method="get">
        <input type="hidden" name="order_id" value="${order.id}"/>
        <input type="submit" value="<fmt:message key='form.pay_damage_button'/>" />
        </form>
    </c:if>

    	<hr>
    </c:forEach>



	<br>
    <a href="user_main.jsp"><fmt:message key="link.back"/></a>
    <br>
        <form action="logout" method="post" style="position: fixed; top: 3; right: 100">
        <input type="submit" value="<fmt:message key="link.logout"/>">
        </form>


<hr>


	<form action="changeLocale.jsp" method="post">
		<fmt:message key="label.set_locale"/>:

		<select name="locale">
			<c:forEach items="${applicationScope.locales}" var="locale">
				<c:set var="selected" value="${locale.key == currentLocale ? 'selected' : currentLocale == null && locale.key == 'en' ? 'selected' : ''}"/>
				<option value="${locale.key}" ${selected}>${locale.value}</option>
			</c:forEach>
		</select>

		<input type="submit" value="<fmt:message key='form.submit_save_locale'/>">

	</form>



</body>
</html>