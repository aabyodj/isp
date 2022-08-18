<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Manage tariffs | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage tariff plans</h1>
            <a href="?action=new_tariff">Add new tariff</a>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Bandwidth</th>
                    <th>Traffic limit</th>
                    <th>Price</th>
                    <th></th>
                </tr>
                <c:forEach var="tariff" items="${tariffs}">
                    <tr>
                        <td><c:out value="${tariff.name}" /></td>
                        <td><c:out value="${tariff.description}" /></td>
                        <td></td>
                        <td></td>
                        <td><c:out value="${tariff.price}" /></td>
                        <td><a href="?action=edit_tariff&id=${tariff.id}">Edit</a></td>
                    </tr>
                </c:forEach>
                <c:if test="${tariffs == null}">
                    <tr>
                        <td colspan=6>No tariff plans</td>
                    </tr>
                </c:if>
            </table>
        </main>
    </body>
</html>
