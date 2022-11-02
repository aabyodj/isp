<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title>Manage tariffs | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <c:choose>
                <c:when test="${activeEmployee != null}">
                    <h1>Manage tariff plans</h1>
                    <p><a href="/tariffs/new">Add new tariff</a>
                    <form action="/tariffs/generate" method="POST">
                        <input name="redirect" type="hidden" value="/tariffs">
                        <ul>
                            <li>
                                <label for="quantity">Automatically generate</label>
                                <input name="quantity" type="number" required min=1 value=10>
                            </li>
                            <li>
                                <label for="active">activated</label>
                                <span><input name="active" type="checkbox" checked> tariffs.</span>
                            </li>
                        </ul>
                        <button type="submit">Generate</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <h1>View all tariff plans</h1>
                </c:otherwise>
            </c:choose>
            <table>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Bandwidth</th>
                    <th>Traffic limit</th>
                    <th>Price</th>
                    <c:if test="${activeEmployee != null}"><th></th></c:if>
                </tr>
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
                                <a href="/tariffs/${tariff.id}">Edit</a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                <c:if test="${page.isEmpty()}">
                    <tr>
                        <td colspan=6>No tariff plans</td>
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
