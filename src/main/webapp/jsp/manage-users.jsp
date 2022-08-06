<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Manage users | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage users</h1>
            <a href="?action=new_user">Add new user</a>
            <table>
                <tr>
                    <th>Email</th>
                    <th>Role</th>
                    <th></th>
                </tr><c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.email}</td>
                    <td>${user.role.toString().toLowerCase()}</td>
                    <td><a href="?action=edit_user&user_id=${user.id}">Edit user</a></td>
                </tr></c:forEach>
            </table>
        </main>
    </body>
</html>
