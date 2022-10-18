<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Manage promotions | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage discounts and promotions</h1>
            <p><a href="?action=new_promotion">Add new promotion</a>
            <form action="?action=generate_promotions" method="POST">
                <input name="redirect" type="hidden" value="${'?'.concat(pageContext.request.queryString)}">
                <label for="quantity">Automatically generate</label>
                <input name="quantity" type="number" required min=1 value=10>
                <label for="active">activated</label>
                <input name="active" type="checkbox" checked>
                promotions.
                <input type="submit" value="Generate">
            </form>
            <table>
                <tr>
                    <th></th>
                    <th>Since</th>
                    <th>Until</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th></th>
                </tr>
                <c:forEach var="promotion" items="${page.toList()}" varStatus="status">
                    <tr${promotion.active ? ' class="active"' : ''}>
                        <td>${page.number * page.size + status.count}</td>
                        <td>${promotion.activeSince != null ? promotion.activeSince : 'Since the Big Bang'}</td>
                        <td>${promotion.activeUntil != null ? promotion.activeUntil : 'Until stopped'}</td>
                        <td><c:out value="${promotion.name}" /></td>
                        <td><c:out value="${promotion.description}" /></td>
                        <td>
                            <a href="?action=edit_promotion&id=${promotion.id}">Edit</a>
                            <c:if test="${promotion.active}">
                                <a href="?action=stop_promotion&id=${promotion.id}">Stop</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${page.isEmpty()}">
                    <tr>
                        <td colspan=5>No promotions</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${page.totalPages > 1}">
                <p>
                    <c:if test="${page.hasPrevious()}">
                        <a href="?action=manage_promotions">First</a>
                        <a href="?action=manage_promotions&page=${page.number}">Prev</a>
                    </c:if>
                    ${page.number + 1}
                    <c:if test="${page.hasNext()}">
                        <a href="?action=manage_promotions&page=${page.number + 2}">Next</a>
                        <a href="?action=manage_promotions&page=${page.totalPages}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
