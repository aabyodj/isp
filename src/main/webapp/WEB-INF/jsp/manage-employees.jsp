<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.employee.manage" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.employee.manage" /></h1>
            <p><a href="/employees/new"><spring:message code="msg.employee.add" /></a>
            <form action="/employees/generate" method="POST">
                <input name="redirect" type="hidden" value="/employees">
                <ul>
                    <li>
                        <label for="quantity"><spring:message code="msg.employee.generate.prefix" /></label>
                        <input name="quantity" type="number" required min=1 value=10>
                    </li>
                    <li>
                        <label for="active"><spring:message code="msg.employee.generate.activated" /></label>
                        <span><input name="active" id="active" type="checkbox" checked> <spring:message code="msg.employee.generate.postfix" /></span>
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.employee.generate.submit" /></button>
            </form>
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th><spring:message code="msg.user.table.email" /></th>
                        <th><spring:message code="msg.employee.table.role" /></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="employee" items="${page.toList()}" varStatus="status">
                        <tr${employee.active ? ' class="active"' : ''}>
                            <td>${page.size * page.number + status.count}</td>
                            <td><c:out value="${employee.email}" /></td>
                            <td><spring:message code="${employee.role.messageKey}" /></td>
                            <td>
                                <c:if test="${employee.id != activeEmployee.id}">
                                    <a href="/employees/${employee.id}"><spring:message code="msg.employee.edit" /></a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
<%@ include file="/WEB-INF/jsp/inc/pagination-links.inc" %>
        </main>
    </body>
</html>
