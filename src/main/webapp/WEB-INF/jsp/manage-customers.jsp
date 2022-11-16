<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.customer.manage" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.customer.manage" /></h1>
            <p><a href="/customers/new"><spring:message code="msg.customer.add" /></a>
            <form action="/customers/generate" method="POST">
                <sec:csrfInput />
                <input name="redirect" type="hidden" value="/customers">
                <ul>
                    <li>
                        <label for="quantity"><spring:message code="msg.customer.generate.prefix" /></label>
                        <input name="quantity" id="quantity" type="number" required min=1 value=10>
                    </li>
                    <li>
                        <label for="active"><spring:message code="msg.customer.generate.activated" /></label>
                        <span><input name="active" id="active" type="checkbox" checked> <spring:message code="msg.customer.generate.postfix" /></span>
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.customer.generate.submit" /></button>
            </form>
            <table>
                <tr>
                    <th></th>
                    <th><spring:message code="msg.user.table.email" /></th>
                    <th><spring:message code="msg.customer.table.balance" /></th>
                    <th></th>
                </tr><c:forEach var="customer" items="${page.toList()}" varStatus="status">
                <tr${customer.active ? ' class="active"' : ''}>
                    <td>${page.size * page.number + status.count}</td>
                    <td><c:out value="${customer.email}" /></td>
                    <td${customer.balance < 0 ? ' class="negative"' : ''}>${customer.balance}</td>
                    <td><a href="/customers/${customer.id}"><spring:message code="msg.customer.edit" /></a></td>
                </tr></c:forEach>
                <c:if test="${page.isEmpty()}">
                    <tr>
                        <td colspan=4><spring:message code="msg.customer.table.empty" /></td>
                    </tr>
                </c:if>
            </table>
<%@ include file="/WEB-INF/jsp/inc/pagination-links.inc" %>
        </main>
    </body>
</html>
