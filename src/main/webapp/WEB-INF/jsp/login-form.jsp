<%@ include file="/WEB-INF/jsp/inc/html-start-error.inc" %><%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.user.login" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.user.login" /></h1>
            <p>
                <spring:message code="msg.user.login.hint.email" /> <b><spring:eval expression="T(by.aab.isp.Const).DEFAULT_ADMIN_EMAIL" /></b>,
                <spring:message code="msg.user.login.hint.password" /> <b><spring:eval expression="T(by.aab.isp.Const).DEFAULT_ADMIN_PASSWORD" /></b>
            </p>
            <form:form action="/login/" method="POST" modelAttribute="credentials">
                <c:if test="${loggedOut != null}"><p class="success-message"><spring:message code="msg.user.logout.message" /></p></c:if>
                <c:if test="${updated != null}"><p class="success-message"><spring:message code="msg.user.update.message" /></p></c:if>
                <form:errors cssClass="error-message" element="p" />
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <form:label path="email"><spring:message code="msg.user.email" /></form:label>
                        <form:input path="email" type="email" required="true" placeholder="user@example.com" />
                    </li>
                    <li>
                        <form:label path="password"><spring:message code="msg.user.password" /></form:label>
                        <form:password path="password" required="true" />
                    </li>
                </ul>
                <form:button type="submit"><spring:message code="msg.user.login" /></form:button>
            </form:form>
        </main>
    </body>
</html>