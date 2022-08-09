<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><c:out value="${tariff.name}" />${tariff.id !=0 ? ' | Edit' : 'New'} tariff plan | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${tariff.id !=0 ? 'Edit' : 'New'} tariff plan</h1>
            <form action="?action=save_tariff" method="POST">
                <input type="hidden" name="id" value="${tariff.id}">
                <ul>
                    <li>
                        <label for="tariff-name">Tariff name</label>
                        <input type="text" name="name" required maxlength=15 placeholder="Tariff name"
                            value="<c:out value="${tariff.name}" />">
                    </li>
                    <li>
                        <label for="description">Tariff description</label>
                        <textarea name="description" required maxlength=50 placeholder="Tariff description"><c:out value="${tariff.description}" /></textarea>
                    </li>
                    <li>
                        <label for="price">Monthly price</label>
                        <input type="number" name="price" required min="0.01" step="0.01" value="${tariff.price}">
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>