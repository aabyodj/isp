<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><c:out value="${employee.email}" /><spring:message code="${employee.id != null ? 'msg.employee.title.edit' : 'msg.employee.title.new'}" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="${employee.id != null ? 'msg.employee.h1.edit' : 'msg.employee.h1.new'}" /></h1>
            <form action="/employees" method="POST">
                <input name="redirect" type="hidden" value="${redirect}">
                <c:if test="${employee.id != null}"><input type="hidden" name="id" value="${employee.id}"></c:if>
                <ul>
                    <li>
                        <label for="email"><spring:message code="msg.user.email" /></label>
                        <input name="email" id="email" type="email" required maxlength=50 placeholder="user@example.com"    <%-- TODO: use constant for maxlength --%>
                            value="<c:out value="${employee.email}" />">
                    </li>
                    <li>
                        <label for="password1"><spring:message code="msg.user.password" /></label>
                        <input name="password1" id="password1" type="password"${employee.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="password2"><spring:message code="msg.user.confirmPassword" /></label>
                        <input name="password2" id="password2" type="password"${employee.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="role"><spring:message code="msg.employee.role" /></label>
                        <select name="role" id="role" required><c:forEach var="roleOption" items="${roles}">
                            <option value="${roleOption}"${roleOption == employee.role ? ' selected' : ''}>
                                <spring:message code="${roleOption.messageKey}" />
                            </option>
                        </c:forEach></select>
                    </li>
                    <li>
                        <label for="active"><spring:message code="msg.user.active" /></label>
                        <input name="active" id="active" type="checkbox"${employee.active ? ' checked' : ''}>
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.user.submit" /></button>
            </form>
        </main>
    </body>
</html>