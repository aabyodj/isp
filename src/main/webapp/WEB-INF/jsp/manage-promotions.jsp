<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <c:choose>
            <c:when test="${activeEmployee != null}">
                <title><spring:message code="msg.promotion.manage" /> | <spring:message code="msg.home.title" /></title>
            </c:when>
            <c:otherwise>
                <title><spring:message code="msg.promotion.view-all.title" /> | <spring:message code="msg.home.title" /></title>
            </c:otherwise>
        </c:choose>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <c:choose>
                <c:when test="${activeEmployee != null}">
                    <h1><spring:message code="msg.promotion.manage.h1" /></h1>
                    <p><a href="/promotions/new"><spring:message code="msg.promotion.add" /></a>
                    <form action="/promotions/generate" method="POST">
                        <input name="redirect" type="hidden" value="/promotions">
                        <ul>
                            <li>
                                <label for="quantity"><spring:message code="msg.promotion.generate.prefix" /></label>
                                <input id="quantity" name="quantity" type="number" required min=1 value=10>
                            </li>
                            <li>
                                <label for="active"><spring:message code="msg.promotion.generate.activated" /></label>
                                <span><input id="active" name="active" type="checkbox" checked> <spring:message code="msg.promotion.generate.postfix" /></span>
                            </li>
                        </ul>
                        <button type="submit"><spring:message code="msg.promotion.generate.submit" /></button>
                    </form>
                </c:when>
                <c:otherwise>
                    <h1><spring:message code="msg.promotion.view-all.h1" /></h1>
                </c:otherwise>
            </c:choose>
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th><spring:message code="msg.promotion.table.since" /></th>
                        <th><spring:message code="msg.promotion.table.until" /></th>
                        <th><spring:message code="msg.promotion.table.name" /></th>
                        <th><spring:message code="msg.promotion.table.description" /></th>
                        <c:if test="${activeEmployee != null}"><th></th></c:if>
                    </tr>
                </thead>
                <tbody>
                    <spring:message code="msg.promotion.since-ages" var="sinceAges" />
                    <spring:message code="msg.promotion.for-ages" var="forAges" />
                    <c:forEach var="promotion" items="${page.toList()}" varStatus="status">
                        <tr${promotion.active ? ' class="active"' : ''}>
                            <td>${page.number * page.size + status.count}</td>
                            <td>${promotion.activeSince != null ? promotion.activeSince : sinceAges}</td>
                            <td>${promotion.activeUntil != null ? promotion.activeUntil : forAges}</td>
                            <td><c:out value="${promotion.name}" /></td>
                            <td><c:out value="${promotion.description}" /></td>
                            <c:if test="${activeEmployee != null}">
                                <td>
                                    <a href="/promotions/${promotion.id}"><spring:message code="msg.promotion.edit" /></a>
                                    <c:if test="${promotion.active}">
                                        <a href="/promotions/${promotion.id}?stop"><spring:message code="msg.promotion.stop" /></a>
                                    </c:if>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    <c:if test="${page.isEmpty()}">
                        <tr>
                            <td colspan="${activeEmployee != null ? 6 : 5}"><spring:message code="msg.promotion.table.empty" /></td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
<%@ include file="/WEB-INF/jsp/inc/pagination-links.inc" %>
        </main>
    </body>
</html>
