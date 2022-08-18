<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Manage promotions | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage discounts and promotions</h1>
            <a href="?action=new_promotion">Add new promotion</a>
            <table>
                <tr>
                    <th>Since</th>
                    <th>Until</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th></th>
                </tr>
                <c:forEach var="promotion" items="${promotions}">
                    <tr>
                        <td></td>
                        <td></td>
                        <td><c:out value="${promotion.name}" /></td>
                        <td><c:out value="${promotion.description}" /></td>
                        <td><a href="?action=edit_promotion&id=${promotion.id}">Edit</a></td>
                    </tr>
                </c:forEach>
                <c:if test="${promotions == null}">
                    <tr>
                        <td colspan=5>No promotions</td>
                    </tr>
                </c:if>
            </table>
        </main>
    </body>
</html>
