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
            <p><a href="/customers/new">Add new customer</a>
            <form action="/customers/generate" method="POST">
                <input name="redirect" type="hidden" value="/customers">
                <ul>
                    <li>
                        <label for="quantity">Automatically generate</label>
                        <input name="quantity" type="number" required min=1 value=10>
                    </li>
                    <li>
                        <label for="active">activated</label>
                        <span><input name="active" type="checkbox" checked> customers.</span>
                    </li>
                </ul>
                <button type="submit">Generate</button>
            </form>
            <table>
                <tr>
                    <th></th>
                    <th>Email</th>
                    <th>Balance</th>
                    <th></th>
                </tr><c:forEach var="customer" items="${page.toList()}" varStatus="status">
                <tr${customer.active ? ' class="active"' : ''}>
                    <td>${page.size * page.number + status.count}</td>
                    <td><c:out value="${customer.email}" /></td>
                    <td${customer.balance < 0 ? ' class="negative"' : ''}>${customer.balance}</td>
                    <td><a href="/customers/${customer.id}">Edit</a></td>
                </tr></c:forEach>
                <c:if test="${page.isEmpty()}">
                    <tr>
                        <td colspan=3>No customers</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${page.totalPages > 1}">
                <p>
                    <c:if test="${page.hasPrevious()}">
                        <a href="?page=1">First</a>
                        <a href="?page=${page.number}">Prev</a>
                    </c:if>
                    ${page.number + 1}
                    <c:if test="${page.hasNext()}">
                        <a href="?page=${page.number + 2}">Next</a>
                        <a href="?page=${page.totalPages}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
