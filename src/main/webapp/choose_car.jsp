<%@ include file="directives.jspf" %>

<html>
<head>
	<title>Car rental</title>
</head>
<body>
<h2>${user.name}, we are glad to see you on Car Rental! </h2>
	 <hr> <br>
	 <form action="choose_car" method="get" class="inLine">
	 <label for="start">Rent start date:</label> <br>
        <input required type="date" id="start" name="rent-start"
               min="${today}" max="2025-12-31" value="${rentBeg == null ? '' : rentBeg}">
         <br>
         <label for="start">Rent end date:</label> <br>
            <input required type="date" id="start" name="rent-end"
                   min="${today}" max="2025-12-31" value="${rentFin == null ? '' : rentFin}"> <br>
                   <em>${dateInputError}</em> <br>
         <input type="submit" value="Search">
        </form>

<c:if test="${dateInputError != 'Rent end date should be bigger than rent start date'}">

<a href="choose_car"><button class="inLine">Clear dates</button></a>
<hr>

<form action="choose_car" method="get" class="inLine">
<input type="hidden" name="sort" value="brand"/>
<c:if test="${rentBeg != null}">
<input type="hidden" name="rent-start" value="${rentBeg}"/>
<input type="hidden" name="rent-end" value="${rentFin}"/>
</c:if>
<select name="brand">
        <option selected disabled>--//--</option>
    <c:forEach items="${brands}" var="brands">
        <option value="${brands.id}" ${brand == brands.id ? 'selected' : ''}>${brands.name}</option>
        </c:forEach>
    </select>
<input type="submit" value="Filter by brand" />
</form>

<form action="choose_car" method="get" class="inLine">
<input type="hidden" name="sort" value="class"/>
<c:if test="${rentBeg != null}">
<input type="hidden" name="rent-start" value="${rentBeg}"/>
<input type="hidden" name="rent-end" value="${rentFin}"/>
</c:if>
<select name="quality_class">
        <option selected disabled>--//--</option>
    <c:forEach items="${q_cars}" var="q_car">
        <option value="${q_car}" ${quality_class == q_car ? 'selected' : ''}>${q_car}</option>
        </c:forEach>
    </select>
<input type="submit" value="Filter by quality class" />
</form>

<a href="choose_car?sort=price${rentBeg == null ? '' : '&rent-start='.concat(rentBeg).concat('&rent-end=').concat(rentFin)}" class="inLine"><button>Sort by price</button></a>
 <a href="choose_car?sort=model${rentBeg == null ? '' : '&rent-start='.concat(rentBeg).concat('&rent-end=').concat(rentFin)}" class="inLine"><button>Sort by model name</button></a>
 <a href="choose_car?${rentBeg == null ? '' : '&rent-start='.concat(rentBeg).concat('&rent-end=').concat(rentFin)}" class="inLine"><button>Undo filters</button></a>

<br>
<hr>

			Page ${page} of ${pageCount}
    
                	|
    
                	<c:choose>
                		<c:when test="${page - 1 > 0}">
                			<a href="choose_car?sort=${sort}&page=${page-1}&pageSize=${pageSize}${quality_class == null ? '' : '&quality_class='.concat(quality_class)}${brand == null ? '' : '&brand='.concat(brand)}${rentBeg == null ? '' : '&rent-start='.concat(rentBeg)}${rentFin == null ? '' : '&rent-end='.concat(rentFin)}">Previous</a>
                		</c:when>
                		<c:otherwise>
                			Previous
                		</c:otherwise>
                	</c:choose>
    
    
                	<c:forEach var="p" begin="${minPossiblePage}" end="${maxPossiblePage}">
                		<c:choose>
                			<c:when test="${page == p}">${p}</c:when>
                			<c:otherwise>
                				<a href="choose_car?sort=${sort}&page=${page-1}&pageSize=${pageSize}${quality_class == null ? '' : '&quality_class='.concat(quality_class)}${brand == null ? '' : '&brand='.concat(brand)}${rentBeg == null ? '' : '&rent-start='.concat(rentBeg)}${rentFin == null ? '' : '&rent-end='.concat(rentFin)}">${p}</a>
                			</c:otherwise>
                		</c:choose>
                	</c:forEach>
    
                	<c:choose>
                		<c:when test="${page + 1 <= pageCount}">
                			<a href="choose_car?${sort == null ? '' : 'sort='.concat(sort)}&page=${page+1}&pageSize=${pageSize}${quality_class == null ? '' : '&quality_class='.concat(quality_class)}${brand == null ? '' : '&brand='.concat(brand)}${rentBeg == null ? '' : '&rent-start='.concat(rentBeg)}${rentFin == null ? '' : '&rent-end='.concat(rentFin)}">Next</a>
                		</c:when>
                		<c:otherwise>
                			Next
                		</c:otherwise>
                	</c:choose>
    
                	|
    
                	<form action="choose_car" style='display:inline;'>
                		<select name="page">
                			<c:forEach begin="1" end="${pageCount}" var="p">
                				<option value="${p}" ${p == param.page ? 'selected' : ''}>${p}</option>
                			</c:forEach>
                		</select>
                		<input name="pageSize" value="${pageSize}" type="hidden" />
                		<c:if test="${sort != null}">
                        <input name="sort" value="${sort}" type="hidden" />
                        </c:if>
                		<c:if test="${quality_class != null}">
                		<input name="quality_class" value="${quality_class}" type="hidden" />
                		</c:if>
                		<c:if test="${brand != null}">
                        <input name="brand" value="${brand}" type="hidden" />
                        </c:if>
                        <c:if test="${rentBeg != null}">
                        <input name="rent-start" value="${rentBeg}" type="hidden" />
                        </c:if>
                        <c:if test="${rentFin != null}">
                        <input name="rent-end" value="${rentFin}" type="hidden" />
                        </c:if>
                		<input type="submit" value="Go"/>
                	</form>
    
<br>
<hr>


<c:forEach items="${cars}" var="car">
	Brand: ${car.brand.name} <br>
	Model: ${car.model} <br>
	Quality class: ${car.qualityClass} <br>
	Price: ${car.price} UAH/day<br>
	<a href="rent?id=${car.id}${rentBeg == null ? '' : '&rent-start='.concat(rentBeg)}${rentFin == null ? '' : '&rent-end='.concat(rentFin)}"><button>Order now</button></a>
	<hr>
</c:forEach>
</c:if>


<br>
<hr>

<a href="user_main.jsp">Main</a> <br>
<br> <form action="logout" method="post" class="logout">
    <input type="submit" value="Log out">
    </form>

</body>
</html>