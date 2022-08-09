<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <section>
                <h1>Welcome!</h1>
                <p>We have been providing broadband internet access for last 10 years! Our customers don't even know what does the word "offline" mean.
            </section>
            <section>
                <h1>Discounts and promotions</h1>
                <a href="?action=new_promotion">Add new promotion</a>
                <ul><c:forEach var="promotion" items="${promotions}">
                    <li>
                        <a href="?action=edit_promotion&id=${promotion.id}">
                            <h2><c:out value="${promotion.name}" /></h2>
                            <div><c:out value="${promotion.description}" /></div>
                        </a>
                    </li></c:forEach>
                </ul>
            </section>
            <section>
                <h1>Our tariff plans</h1>
                <a href="?action=new_tariff">Add new tariff plan</a>
                <ul><c:forEach var="tariff" items="${tariffs}">
                    <li>
                        <a href="?action=view_tariff&id=${tariff.id}">
                            <h2><c:out value="${tariff.name}" /></h2>
                            <div><c:out value="${tariff.description}" /></div>
                            <span>Price: </span><span>${tariff.price}</span>
                        </a>
                    </li></c:forEach>
                </ul>
            </section>
        </main>
    </body>
</html>
