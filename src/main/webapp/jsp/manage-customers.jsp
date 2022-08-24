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
            <table>
                <tr>
                    <th>Email</th>
                    <th>Balance</th>
                    <th></th>
                </tr><c:forEach var="customer" items="${customers}">
                <tr${customer.active ? ' class="active"' : ''}>
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
        </main>
    </body>
</html>
