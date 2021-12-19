<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Cars management</title>
</head>
<body>

<h2> Car add form </h2>
<form action="car_add" method="post">
Choose brand: <br>
    <select name="brand" required>
        <option selected disabled value="">--//--</option>
    <c:forEach items="${brands}" var="brand">
        <option value="${brand.id}">${brand.name}</option>
        </c:forEach>
    </select> <br>
Insert model: <br>
    <input name="model" placeholder="Model" pattern="[A-Za-z0-9]{1,100}" required> <br>
    Choose quality class: <br>
<select name="quality_class" placeholder="Quality class" required>
<option selected disabled value="">--//--</option>
        <option value="A">A</option>
        <option value="B">B</option>
        <option value="C">C</option>
        <option value="D">D</option>
        <option value="E">E</option>
    </select> <br>
    Insert price: <br>
    <input name="price" placeholder="Price" pattern="[0-9]{1,9}" required> <br>
Choose car status: <br>
    <select name="status" required>
            <option value="Available">Available</option>
            <option value="Damaged">Damaged</option>
    </select> <br>
<br>
    <input type="submit" value="Submit" />
</form>

<br>
<hr>

		Page ${page} of ${pageCount}

            	|

            	<c:choose>
            		<c:when test="${page - 1 > 0}">
            			<a href="car_manage?page=${page-1}&pageSize=${pageSize}">Previous</a>
            		</c:when>
            		<c:otherwise>
            			Previous
            		</c:otherwise>
            	</c:choose>


            	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
            		<c:choose>
            			<c:when test="${page == p}">${p}</c:when>
            			<c:otherwise>
            				<a href="car_manage?page=${p}&pageSize=${pageSize}">${p}</a>
            			</c:otherwise>
            		</c:choose>
            	</c:forEach>

            	<c:choose>
            		<c:when test="${page + 1 <= pageCount}">
            			<a href="car_manage?page=${page+1}&pageSize=${pageSize}">Next</a>
            		</c:when>
            		<c:otherwise>
            			Next
            		</c:otherwise>
            	</c:choose>

            	|

            	<form action="car_manage" style='display:inline;'>
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

<c:forEach items="${cars}" var="car">
	Brand: ${car.brand.name} <br>
	Model: ${car.model} <br>
	Quality class: ${car.qualityClass} <br>
	Price: ${car.price} UAH<br>
	Car status: ${car.carStatus} <br>

	<form action="car_edit" method="get" class="inLine">
	<input type="hidden" name="id" value="${car.id}">
	<input type="submit" value="Edit">
	</form>

    </form>
	<form action="car_delete" method="post" class="inLine">
	<input type="hidden" name="id" value="${car.id}">
	<input type="submit" value="Delete">
	</form>

	<hr>
</c:forEach>

<br>
<hr>

<a href="logged_admin.jsp">Back</a>

<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>