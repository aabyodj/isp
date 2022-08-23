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
                <input name="redirect" type="hidden" value="${'?'.concat(pageContext.request.queryString)}"
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
                <c:forEach var="promotion" items="${promotions}" varStatus="status">
                    <tr${promotion.isActiveOn(now) ? ' class="active"' : ''}>
                        <td>${pagination.offset + status.count}</td>
                        <td>${promotion.activeSince != null ? promotion.activeSince : 'Since the Big Bang'}</td>
                        <td>${promotion.activeUntil != null ? promotion.activeUntil : 'Until stopped'}</td>
                        <td><c:out value="${promotion.name}" /></td>
                        <td><c:out value="${promotion.description}" /></td>
                        <td>
                            <a href="?action=edit_promotion&id=${promotion.id}">Edit</a>
                            <c:if test="${promotion.isActiveOn(now)}">
                                <a href="?action=stop_promotion&id=${promotion.id}">Stop</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${promotions == null}">
                    <tr>
                        <td colspan=5>No promotions</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${pagination.lastPageNumber > 0}">
                <p>
                    <c:if test="${pagination.pageNumber > 0}">
                        <a href="?action=manage_promotions&page=0">First</a>
                        <a href="?action=manage_promotions&page=${pagination.pageNumber - 1}">Prev</a>
                    </c:if>
                    ${pagination.pageNumber}
                    <c:if test="${pagination.pageNumber < pagination.lastPageNumber}">
                        <a href="?action=manage_promotions&page=${pagination.pageNumber + 1}">Next</a>
                        <a href="?action=manage_promotions&page=${pagination.lastPageNumber}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
