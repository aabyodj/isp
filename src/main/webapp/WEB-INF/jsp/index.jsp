<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <section id="about">
                <h1><spring:message code="msg.home.about.h1" /></h1>
                <p><spring:message code="msg.home.about.content" />
            </section>
            <c:if test="${promotions != null || activeEmployee != null}">
                <section id="promotions">
                    <h1><spring:message code="msg.home.promotions.h1" /></h1>
                    <c:if test="${activeEmployee != null}">
                        <p><a href="/promotions/new?redirect=/"><spring:message code="msg.promotion.add" /></a>
                    </c:if>
                    <ul><c:forEach var="promotion" items="${promotions}">
                        <li>
                            <h2><c:out value="${promotion.name}" /></h2>
                            <div><c:out value="${promotion.description}" /></div>
                            <c:if test="${promotion.activeUntil != null}">
                                <p><spring:message code="msg.promotion.until" /> ${promotion.activeUntil.toLocalDate()}</p>
                            </c:if>
                            <c:if test="${activeEmployee != null}">
                                <a href="/promotions/${promotion.id}?redirect=/"><spring:message code="msg.promotion.edit" /></a>
                                <a href="/promotions/${promotion.id}?stop&redirect=/"><spring:message code="msg.promotion.stop" /></a>
                            </c:if>
                        </li></c:forEach>
                    </ul>
                </section>
            </c:if>
            <c:if test="${tariffs != null || activeEmployee != null}">
                <section>
                    <h1><spring:message code="msg.home.tariffs.h1" /></h1>
                    <c:if test="${activeEmployee != null}">
                        <p><a href="/tariffs/new?redirect=/"><spring:message code="msg.tariff.add" /></a>
                    </c:if>
                    <ul><c:forEach var="tariff" items="${tariffs}">
                        <li>
                            <h2><c:out value="${tariff.name}" /></h2>
                            <div><c:out value="${tariff.description}" /></div>
                            <p><spring:message code="msg.tariff.bandwidth" />: <span>${tariff.bandwidth}</span>
                            <p><spring:message code="msg.tariff.traffic" />: <span>${tariff.includedTraffic}</span>
                            <p><spring:message code="msg.tariff.price" />: <span>${tariff.price}</span></p>
                            <c:if test="${activeEmployee != null}">
                                <a href="/tariffs/${tariff.id}?redirect=/"><spring:message code="msg.tariff.edit" /></a>
                            </c:if>
                        </li></c:forEach>
                    </ul>
                </section>
            </c:if>
        </main>
    </body>
</html>
