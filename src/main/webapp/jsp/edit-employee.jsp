<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${employee.email}" /><fmt:message key="${employee.id !=0 ? 'msg.employee.title.edit' : 'msg.employee.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="${employee.id !=0 ? 'msg.employee.h1.edit' : 'msg.employee.h1.new'}" /></h1>
            <form action="?action=save_employee" method="POST">
                <input name="redirect" type="hidden" value="${redirect}">
                <input type="hidden" name="id" value="${employee.id}">
                <ul>
                    <li>
                        <label for="email"><fmt:message key="msg.user.email" /></label>
                        <input name="email" id="email" type="email" required maxlength=50 placeholder="user@example.com"    <%-- TODO: use constant for maxlength --%>
                            value="<c:out value="${employee.email}" />">
                    </li>
                    <li>
                        <label for="password1"><fmt:message key="msg.user.password" /></label>
                        <input name="password1" id="password1" type="password"${employee.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="password2"><fmt:message key="msg.user.confirmPassword" /></label>
                        <input name="password2" id="password2" type="password"${employee.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="role"><fmt:message key="msg.employee.role" /></label>
                        <select name="role" id="role" required><c:forEach var="roleOption" items="${roles}">
                            <option value="${roleOption}"${roleOption == employee.role ? ' selected' : ''}>
                                <fmt:message key="${roleOption.messageKey}" />
                            </option>
                        </c:forEach></select>
                    </li>
                    <li>
                        <label for="active"><fmt:message key="msg.user.active" /></label>
                        <input name="active" id="active" type="checkbox"${employee.active ? ' checked' : ''}>
                    </li>
                </ul>
                <button type="submit"><fmt:message key="msg.user.submit" /></button>
            </form>
        </main>
    </body>
</html>