<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><fmt:message key="msg.customer.manage" /> | <fmt:message key="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="msg.customer.manage" /></h1>
            <p><a href="?action=new_customer">Add new customer</a>
            <form action="?action=generate_customers" method="POST">
                <input name="redirect" type="hidden" value="${'?'.concat(pageContext.request.queryString)}">
                <label for="quantity">Automatically generate</label>
                <input name="quantity" type="number" required min=1 value=10>
                <label for="active">activated</label>
                <input name="active" type="checkbox" checked>
                customers.
                <input type="submit" value="Generate">
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
                    <td><a href="?action=edit_customer&id=${customer.id}">Edit</a></td>
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
                        <a href="?action=manage_customers">First</a>
                        <a href="?action=manage_customers&page=${page.number}">Prev</a>
                    </c:if>
                    ${page.number + 1}
                    <c:if test="${page.hasNext()}">
                        <a href="?action=manage_customers&page=${page.number + 2}">Next</a>
                        <a href="?action=manage_customers&page=${page.totalPages}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
