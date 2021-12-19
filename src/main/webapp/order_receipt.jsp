<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Order receipt</title>
</head>
<body>

<h2> Order receipt </h2>
    Car: ${order.car.brand.name} ${order.car.model} <br>
    Price: ${order.orderReceipt.cost} UAH <br>
    Period: ${days} days <br> <hr>
<form action="order_payment" method="post">
<input type="hidden" name="order_id" value="${order.id}">
Enter your card number: <br>
    <input name="card" placeholder="1234-5678-9012-3456" pattern="[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}" required> <br>
    Expire date: <input name="expire" placeholder="mm/yy" pattern="[0-9]{2}/[0-9]{2}" required><br>
    CCV: <input name="ccv" placeholder="123" pattern="[0-9]{3}" required> <br> <br>
    <input type="submit" value="Pay" />
</form>


<br>
<hr>


<a href="${back}">Back</a> <br>

<form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>