<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Manage customers | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage customers</h1>
            <p><a href="?action=new_customer">Add new customer</a>
            <form action="?action=generate_customers" method="POST">
                <input name="redirect" type="hidden" value="${'?'.concat(pageContext.request.queryString)}"
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
                </tr><c:forEach var="customer" items="${customers}" varStatus="status">
                <tr${customer.active ? ' class="active"' : ''}>
                    <td>${pagination.offset + status.count}</td>
                    <td><c:out value="${customer.email}" /></td>
                    <td${customer.balance < 0 ? ' class="negative"' : ''}>${customer.balance}</td>
                    <td><a href="?action=edit_customer&id=${customer.id}">Edit</a></td>
                </tr></c:forEach>
                <c:if test="${customers == null}">
                    <tr>
                        <td colspan=3>No customers</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${pagination.lastPageNumber > 0}">
                <p>
                    <c:if test="${pagination.pageNumber > 0}">
                        <a href="?action=manage_customers&page=0">First</a>
                        <a href="?action=manage_customers&page=${pagination.pageNumber - 1}">Prev</a>
                    </c:if>
                    ${pagination.pageNumber}
                    <c:if test="${pagination.pageNumber < pagination.lastPageNumber}">
                        <a href="?action=manage_customers&page=${pagination.pageNumber + 1}">Next</a>
                        <a href="?action=manage_customers&page=${pagination.lastPageNumber}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
