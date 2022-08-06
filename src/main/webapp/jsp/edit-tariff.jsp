<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${tariff.name}${tariff.id !=0 ? ' | Edit' : 'New'} tariff plan | Internet Service Provider</title>
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
                        <input type="text" name="name" required placeholder="Tariff name" value="${tariff.name}">
                    </li>
                    <li>
                        <label for="description">Tariff description</label>
                        <input type="text" name="description" required placeholder="Tariff description" value="${tariff.description}">
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