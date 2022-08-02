<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!doctype html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/header.inc" %>
        <main>
            <section>
                <h1>Welcome!</h1>
                <p>We have been providing broadband internet access for last 10 years! Our customers don't even know what does the word "offline" mean.
            </section>
            <section>
                <h1>Our tariff plans</h1>
                <ul><c:forEach var="tariff" items="${tariffs}">
                    <li>
                        <section>
                            <a href="?action=show_tariff&id=${tariff.id}">
                                <h1>${tariff.name}</h1>
                                <div>${tariff.description}</div>
                                <span>Price: </span><span>${tariff.price}</span>
                            </a>
                        </section>
                    </li></c:forEach>
                </ul>
            </section>
        </main>
    </body>
</html>
