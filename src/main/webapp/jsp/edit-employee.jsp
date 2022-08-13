<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><c:out value="${employee.email}" />${employee.id !=0 ? ' | Edit' : 'New'} employee account | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${employee.id !=0 ? 'Edit' : 'New'} employee account</h1>
            <form action="?action=save_employee" method="POST">
                <input type="hidden" name="id" value="${employee.id}">
                <ul>
                    <li>
                        <label for="email">Email</label>
                        <input type="email" name="email" required maxlength=25 placeholder="user@example.com"
                            value="<c:out value="${employee.email}" />">
                    </li>
                    <li>
                        <label for="role">User role</label>
                        <select name="role" required><c:forEach var="roleOption" items="${roles}">
                            <option value="${roleOption}"${roleOption == employee.role ? ' selected' : ''}>${roleOption.toString().toLowerCase()}</option>
                        </c:forEach></select>
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>