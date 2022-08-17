<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Log in | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Log in</h1>
            <form action="?action=check_login" method="POST">
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="user-email">Your email</label>
                        <input type="email" name="email" required placeholder="user@example.com">
                    </li>
                    <li>
                        <label for="password">Your password</label>
                        <input type="password" name="password" required>
                    </li>
                </ul>
                <input type="submit" value="Proceed">
            </form>
        </main>
    </body>
</html>