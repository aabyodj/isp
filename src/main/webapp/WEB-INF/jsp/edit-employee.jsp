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
            <form:form method="POST" modelAttribute="employee">
                <input name="redirect" type="hidden" value="${redirect}">
                <form:input path="id" type="hidden" />
                <ul>
                    <li>
                        <form:label path="email"><spring:message code="msg.user.email" /></form:label>
                        <form:input path="email" type="email" required="true" maxlength="50" placeholder="user@example.com" errorCssClass="error" /><%-- TODO: use constant for maxlength --%>
                        <form:errors path="email" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="password"><spring:message code="msg.user.password" /></form:label>
                        <c:choose>
                            <c:when test="${employee.id == null}">
                                <form:input path="password" type="password" required="true" errorCssClass="error "/>
                            </c:when>
                            <c:otherwise>
                                <form:input path="password" type="password" errorCssClass="error "/>
                            </c:otherwise>
                        </c:choose>
                        <form:errors path="password" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="passwordConfirmation"><spring:message code="msg.user.confirmPassword" /></form:label>
                        <c:choose>
                            <c:when test="${employee.id == null}">
                                <form:input path="passwordConfirmation" type="password" required="true" errorCssClass="error" />
                            </c:when>
                            <c:otherwise>
                                <form:input path="passwordConfirmation" type="password" errorCssClass="error" />
                            </c:otherwise>
                        </c:choose>
                        <form:errors path="passwordConfirmation" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="role"><spring:message code="msg.employee.role" /></form:label>
                        <form:select path="role" required="true" items="${roles}" itemValue="name" itemLabel="label" />
                    </li>
                    <li>
                        <form:label path="active"><spring:message code="msg.user.active" /></form:label>
                        <form:checkbox path="active" />
                    </li>
                </ul>
                <form:button type="submit"><spring:message code="msg.user.submit" /></form:button>
            </form:form>
        </main>
    </body>
</html>