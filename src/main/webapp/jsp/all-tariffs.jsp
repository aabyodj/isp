<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!doctype html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Internet Service Provider</title>
</head>
<body>
<h1><a href="${pageContext.request.contextPath}">Internet Service Provider</a></h1>
<h2>All tariff plans</h2>
<ul><c:forEach var="tariff" items="${tariffs}">
	<li>
		<section>
			<a href="?command=show_tariff&id=<c:out value="${tariff.id}" />">
	    		<h1>${tariff.name}</h1>
	    		<div>${tariff.description}</div>
	    		<span>Price: </span><span>${tariff.price}</span>
	    	</a>
	    </section>
	</li>
</c:forEach></ul>
</body>
</html>