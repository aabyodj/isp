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
<%@ include file="/WEB-INF/jsp/inc/pagination-links.inc" %>
        </main>
    </body>
</html>
