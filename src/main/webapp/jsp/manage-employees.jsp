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
            <a href="?action=new_employee">Add new employee</a>
            <table>
                <tr>
                    <th>Email</th>
                    <th>Role</th>
                    <th></th>
                </tr><c:forEach var="employee" items="${employees}">
                <tr>
                    <td><c:out value="${employee.email}" /></td>
                    <td><c:out value="${employee.role.toString().toLowerCase()}" /></td>
                    <td><a href="?action=edit_employee&id=${employee.id}">Edit employee</a></td>
                </tr></c:forEach>
            </table>
        </main>
    </body>
</html>
