<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><fmt:message key="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <section id="about">
                <h1><fmt:message key="msg.home.about.h1" /></h1>
                <p><fmt:message key="msg.home.about.content" />
            </section>
            <c:if test="${promotions != null || activeEmployee != null}">
                <section id="promotions">
                    <h1><fmt:message key="msg.home.promotions.h1" /></h1>
                    <c:if test="${activeEmployee != null}">
                        <p><a href="?action=new_promotion"><fmt:message key="msg.promotion.add" /></a>
                    </c:if>
                    <ul><c:forEach var="promotion" items="${promotions}">
                        <li>
                            <h2><c:out value="${promotion.name}" /></h2>
                            <div><c:out value="${promotion.description}" /></div>
                            <c:if test="${promotion.activeUntil != null}">
                                <p><fmt:message key="msg.promotion.until" /> ${promotion.activeUntil.toLocalDate()}</p>
                            </c:if>
                            <c:if test="${activeEmployee != null}">
                                <a href="?action=edit_promotion&id=${promotion.id}"><fmt:message key="msg.promotion.edit" /></a>
                                <a href="?action=stop_promotion&id=${promotion.id}"><fmt:message key="msg.promotion.stop" /></a>
                            </c:if>
                        </li></c:forEach>
                    </ul>
                </section>
            </c:if>
            <c:if test="${tariffs != null || activeEmployee != null}">
                <section>
                    <h1><fmt:message key="msg.home.tariffs.h1" /></h1>
                    <c:if test="${activeEmployee != null}">
                        <p><a href="?action=new_tariff"><fmt:message key="msg.tariff.add" /></a>
                    </c:if>
                    <ul><c:forEach var="tariff" items="${tariffs}">
                        <li>
                            <h2><c:out value="${tariff.name}" /></h2>
                            <div><c:out value="${tariff.description}" /></div>
                            <p><fmt:message key="msg.tariff.bandwidth" />: <span>${tariff.bandwidth}</span>
                            <p><fmt:message key="msg.tariff.traffic" />: <span>${tariff.includedTraffic}</span>
                            <p><fmt:message key="msg.tariff.price" />: <span>${tariff.price}</span></p>
                            <c:choose>
                                <c:when test="${activeEmployee != null}">
                                    <a href="?action=edit_tariff&id=${tariff.id}"><fmt:message key="msg.tariff.edit" /></a>
                                </c:when>
                                <c:otherwise>
                                    <a href="?action=subscribe&tariff_id=${tariff.id}&redirect="><fmt:message key="msg.subscription.subscribe" /></a><%-- TODO: redirect to user account --%>
                                </c:otherwise>
                            </c:choose>
                        </li></c:forEach>
                    </ul>
                </section>
            </c:if>
        </main>
    </body>
</html>
