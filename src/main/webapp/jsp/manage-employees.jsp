<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Manage employees | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage employees</h1>
            <p><a href="?action=new_employee">Add new employee</a>
            <form action="?action=generate_employees" method="POST">
                <input name="redirect" type="hidden" value="${'?'.concat(pageContext.request.queryString)}">
                <label for="quantity">Automatically generate</label>
                <input name="quantity" type="number" required min=1 value=10>
                <label for="active">activated</label>
                <input name="active" type="checkbox" checked>
                employees.
                <input type="submit" value="Generate">
            </form>
            <table>
                <tr>
                    <th></th>
                    <th>Email</th>
                    <th>Role</th>
                    <th></th>
                </tr><c:forEach var="employee" items="${employees}" varStatus="status">
                <tr${employee.active ? ' class="active"' : ''}>
                    <td>${pagination.offset + status.count}</td>
                    <td><c:out value="${employee.email}" /></td>
                    <td><c:out value="${employee.role.toString().toLowerCase()}" /></td>
                    <td>
                        <c:if test="${employee.id != activeEmployee.id}">
                            <a href="?action=edit_employee&id=${employee.id}">Edit</a>
                        </c:if>
                    </td>
                </tr></c:forEach>
            </table>
            <c:if test="${pagination.lastPageNumber > 0}">
                <p>
                    <c:if test="${pagination.pageNumber > 0}">
                        <a href="?action=manage_employees&page=0">First</a>
                        <a href="?action=manage_employees&page=${pagination.pageNumber - 1}">Prev</a>
                    </c:if>
                    ${pagination.pageNumber}
                    <c:if test="${pagination.pageNumber < pagination.lastPageNumber}">
                        <a href="?action=manage_employees&page=${pagination.pageNumber + 1}">Next</a>
                        <a href="?action=manage_employees&page=${pagination.lastPageNumber}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
