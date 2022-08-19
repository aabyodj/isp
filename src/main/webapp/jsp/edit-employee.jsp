<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${employee.email}" />${employee.id !=0 ? ' | Edit' : 'New'} employee account | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${employee.id !=0 ? 'Edit' : 'New'} employee account</h1>
            <form action="?action=save_employee" method="POST">
                <input name="redirect" type="hidden" value="${redirect}">
                <input type="hidden" name="id" value="${employee.id}">
                <ul>
                    <li>
                        <label for="email">Email</label>
                        <input type="email" name="email" required maxlength=25 placeholder="user@example.com"
                            value="<c:out value="${employee.email}" />">
                    </li>
                    <li>
                        <label for="role">User role</label>
                        <select name="role" required><c:forEach var="roleOption" items="${roles}">
                            <option value="${roleOption}"${roleOption == employee.role ? ' selected' : ''}>${roleOption.toString().toLowerCase()}</option>
                        </c:forEach></select>
                    </li>
                    <li>
                        <label for="active">Active</label>
                        <input name="active" type="checkbox"${employee.active ? ' checked' : ''}>
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>