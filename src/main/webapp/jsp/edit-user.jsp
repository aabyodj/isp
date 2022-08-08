<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${user.email}${user.id !=0 ? ' | Edit' : 'New'} user account | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${user.id !=0 ? 'Edit' : 'New'} user account</h1>
            <form action="?action=save_user" method="POST">
                <input type="hidden" name="id" value="${user.id}">
                <ul>
                    <li>
                        <label for="user-email">User email</label>
                        <input type="email" name="email" required placeholder="user@example.com" value="${user.email}">
                    </li>
                    <li>
                        <label for="role">User role</label>
                        <select name="role" required><c:forEach var="roleOption" items="${userRoles}">
                            <option value="${roleOption}"${roleOption == user.role ? ' selected' : ''}>${roleOption.toString().toLowerCase()}</option>
                        </c:forEach></select>
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>