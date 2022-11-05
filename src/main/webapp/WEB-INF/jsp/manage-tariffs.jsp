<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <c:choose>
            <c:when test="${activeEmployee != null}">
                <title><spring:message code="msg.tariff.manage" /> | <spring:message code="msg.home.title" /></title>
            </c:when>
            <c:otherwise>
                <title><spring:message code="msg.tariff.view-all.title" /> | <spring:message code="msg.home.title" /></title>
            </c:otherwise>
        </c:choose>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <c:choose>
                <c:when test="${activeEmployee != null}">
                    <h1><spring:message code="msg.tariff.manage.h1" /></h1>
                    <p><a href="/tariffs/new"><spring:message code="msg.tariff.add" /></a>
                    <form action="/tariffs/generate" method="POST">
                        <input name="redirect" type="hidden" value="/tariffs">
                        <ul>
                            <li>
                                <label for="quantity"><spring:message code="msg.tariff.generate.prefix" /></label>
                                <input name="quantity" type="number" required min=1 value=10>
                            </li>
                            <li>
                                <label for="active"><spring:message code="msg.tariff.generate.activated" /></label>
                                <span><input name="active" type="checkbox" checked> <spring:message code="msg.tariff.generate.postfix" /></span>
                            </li>
                        </ul>
                        <button type="submit"><spring:message code="msg.tariff.generate.submit" /></button>
                    </form>
                </c:when>
                <c:otherwise>
                    <h1><spring:message code="msg.tariff.view-all.h1" /></h1>
                </c:otherwise>
            </c:choose>
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th><spring:message code="msg.tariff.table.name" /></th>
                        <th><spring:message code="msg.tariff.table.description" /></th>
                        <th><spring:message code="msg.tariff.table.bandwidth" /></th>
                        <th><spring:message code="msg.tariff.table.included-traffic" /></th>
                        <th><spring:message code="msg.tariff.table.price" /></th>
                        <c:if test="${activeEmployee != null}"><th></th></c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="tariff" items="${page.toList()}" varStatus="status">
                        <tr${tariff.active ? ' class="active"' : ''}>
                            <td>${page.number * page.size + status.count}</td>
                            <td><c:out value="${tariff.name}" /></td>
                            <td><c:out value="${tariff.description}" /></td>
                            <td>${tariff.bandwidth}</td>
                            <td>${tariff.includedTraffic}</td>
                            <td><c:out value="${tariff.price}" /></td>
                            <c:if test="${activeEmployee != null}">
                                <td>
                                    <a href="/tariffs/${tariff.id}"><spring:message code="msg.tariff.edit" /></a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    <c:if test="${page.isEmpty()}">
                        <tr>
                            <td colspan="${activeEmployee != null ? 7 : 6}"><spring:message code="msg.tariff.table.empty" /></td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
<%@ include file="/WEB-INF/jsp/inc/pagination-links.inc" %>
        </main>
    </body>
</html>
