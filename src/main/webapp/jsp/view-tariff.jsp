<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/header.inc" %>
        <h1>${tariff.name}</h1>
        <div>${tariff.description}</div>
        <span>Price: </span><span>${tariff.price}</span>
    </body>
</html>