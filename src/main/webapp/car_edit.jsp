<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Cars edit</title>
</head>
<body>

<h2> Car edit </h2>
<form action="car_update" method="post">
    <select name="brand" required>
    <c:forEach items="${brands}" var="brand">
        <option value="${brand.id}" ${brand.id == car.brand.id ? 'selected' : ''} >${brand.name}</option>



        </c:forEach>
    </select> <br>

    <input name="model" value="${car.model}" pattern="[A-Za-z0-9]{1,100}" required> <br>
    <select name="quality_class" placeholder="Quality class" required>
            <option ${car.qualityClass == 'A' ? 'selected' : ''} value="A">A</option>
            <option ${car.qualityClass == 'B' ? 'selected' : ''} value="B">B</option>
            <option ${car.qualityClass == 'C' ? 'selected' : ''} value="C">C</option>
            <option ${car.qualityClass == 'D' ? 'selected' : ''} value="D">D</option>
            <option ${car.qualityClass == 'E' ? 'selected' : ''} value="E">E</option>
        </select> <br>
    <input name="price" value="${car.price}" pattern="[0-9]{1,9}" required> <br>

    <select name="status" required>
            <option ${car.carStatus == 'Available' ? 'selected' : ''} value="Available">Available</option>
            <option ${car.carStatus == 'Damaged' ? 'selected' : ''} value="Damaged">Damaged</option>
    </select> <br>

    <input type="hidden" name="id" value="${car.id}"/>
    <input type="submit" value="Submit" />
</form>

<br>
<hr>

<a href="car_manage">Back</a>

<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>