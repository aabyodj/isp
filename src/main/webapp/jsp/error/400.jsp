<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Bad request | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="/jsp/inc/page-header.inc" %>
        <main>
            <h1>Bad request</h1>
            <p><c:out value="${pageContext.exception.message}" />
        </main>
    </body>
</html>