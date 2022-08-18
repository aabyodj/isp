<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
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
                            <c:if test="${promotion.activeUntil != null}">
                                <p>Active until ${promotion.activeUntil}
                            </c:if>
                        </a>
                    </li></c:forEach>
                </ul>
            </section>
            <c:if test="${tariffs != null || activeEmployee != null}">
                <section>
                    <h1>Our tariff plans</h1>
                    <c:if test="${activeEmployee != null}">
                        <a href="?action=new_tariff">Add new tariff plan</a>
                    </c:if>
                    <ul><c:forEach var="tariff" items="${tariffs}">
                        <li>
                            <a href="?action=view_tariff&id=${tariff.id}">
                                <h2><c:out value="${tariff.name}" /></h2>
                                <div><c:out value="${tariff.description}" /></div>
                                <p>Bandwidth: <span>${util.formatBandwidth(tariff.bandwidth)}</span>
                                <p>Included traffic: <span>${util.formatTraffic(tariff.includedTraffic)}</span>
                                <p>Price: <span>${tariff.price}</span>
                            </a>
                        </li></c:forEach>
                    </ul>
                </section>
            </c:if>
        </main>
    </body>
</html>
