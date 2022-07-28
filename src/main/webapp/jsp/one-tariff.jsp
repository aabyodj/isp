<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!doctype html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Internet Service Provider</title>
</head>
<body>
<h1><a href="${pageContext.request.contextPath}">Internet Service Provider</a></h1>
<h2>${tariff.name}</h2>
<div>${tariff.description}</div>
<span>Price: </span><span>${tariff.price}</span>
</body>
</html>