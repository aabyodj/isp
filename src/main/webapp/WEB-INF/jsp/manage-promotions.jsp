<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title>Manage promotions | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <c:choose>
                <c:when test="${activeEmployee != null}">
                    <h1>Manage discounts and promotions</h1>
                    <p><a href="${pageContext.request.contextPath}/promotion/new">Add a new promotion</a>
                    <form action="${pageContext.request.contextPath}/promotion/generate" method="POST">
                        <input name="redirect" type="hidden" value="/promotion">
                        <label for="quantity">Automatically generate</label>
                        <input name="quantity" type="number" required min=1 value=10>
                        <label for="active">activated</label>
                        <input name="active" type="checkbox" checked>
                        promotions.
                        <input type="submit" value="Generate">
                    </form>
                </c:when>
                <c:otherwise>
                    <h1>View all promotions</h1>
                </c:otherwise>
            </c:choose>
            <table>
                <tr>
                    <th></th>
                    <th>Since</th>
                    <th>Until</th>
                    <th>Name</th>
                    <th>Description</th>
                    <c:if test="${activeEmployee != null}"><th></th></c:if>
                </tr>
                <c:forEach var="promotion" items="${page.toList()}" varStatus="status">
                    <tr${promotion.active ? ' class="active"' : ''}>
                        <td>${page.number * page.size + status.count}</td>
                        <td>${promotion.activeSince != null ? promotion.activeSince : 'Since the Big Bang'}</td>
                        <td>${promotion.activeUntil != null ? promotion.activeUntil : 'Until stopped'}</td>
                        <td><c:out value="${promotion.name}" /></td>
                        <td><c:out value="${promotion.description}" /></td>
                        <c:if test="${activeEmployee != null}">
                            <td>
                                <a href="${pageContext.request.contextPath}/promotion/${promotion.id}">Edit</a>
                                <c:if test="${promotion.active}">
                                    <a href="${pageContext.request.contextPath}/promotion/${promotion.id}?stop">Stop</a>
                                </c:if>
                            </td>
                        </c:if>
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
