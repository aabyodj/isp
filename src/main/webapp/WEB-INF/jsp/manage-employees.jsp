<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title>Manage employees | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1>Manage employees</h1>
            <p><a href="/employees/new">Add new employee</a>
            <form action="/employees/generate" method="POST">
                <input name="redirect" type="hidden" value="/employees">
                <ul>
                    <li>
                        <label for="quantity">Automatically generate</label>
                        <input name="quantity" type="number" required min=1 value=10>
                    </li>
                    <li>
                        <label for="active">activated</label>
                        <span><input name="active" type="checkbox" checked> employees.</span>
                    </li>
                </ul>
                <button type="submit">Generate</button>
            </form>
            <table>
                <tr>
                    <th></th>
                    <th>Email</th>
                    <th>Role</th>
                    <th></th>
                </tr><c:forEach var="employee" items="${page.toList()}" varStatus="status">
                <tr${employee.active ? ' class="active"' : ''}>
                    <td>${page.size * page.number + status.count}</td>
                    <td><c:out value="${employee.email}" /></td>
                    <td><c:out value="${employee.role.toString().toLowerCase()}" /></td>
                    <td>
                        <c:if test="${employee.id != activeEmployee.id}">
                            <a href="/employees/${employee.id}">Edit</a>
                        </c:if>
                    </td>
                </tr></c:forEach>
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
