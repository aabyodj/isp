<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${promotion.name}${promotion.id !=0 ? ' | Edit' : 'New'} promotion | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${promotion.id !=0 ? 'Edit' : 'New'} promotion</h1>
            <form action="?action=save_promotion" method="POST">
                <input type="hidden" name="id" value="${promotion.id}">
                <ul>
                    <li>
                        <label for="tariff-name">Promotion name</label>
                        <input type="text" name="name" required maxlength=25 placeholder="Promotion name" value="${promotion.name}">
                    </li>
                    <li>
                        <label for="description">Promotion description</label>
                        <textarea name="description" required maxlength=100 placeholder="Promotion description">${promotion.description}</textarea>
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>