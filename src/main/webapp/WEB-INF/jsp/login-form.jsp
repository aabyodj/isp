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
                <spring:message code="msg.user.login.hintEmail" /> <b>${defaultAdminEmail}</b>,
                <spring:message code="msg.user.login.hintPassword" /> <b>${defaultAdminPassword}</b>
            </p>
            <form:form action="/login/" method="POST" modelAttribute="credentials">
                <form:errors cssClass="error-message" element="p" />
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <form:label path="email"><spring:message code="msg.user.email" /></form:label>
                        <form:input path="email" type="email" required="true" placeholder="user@example.com" />
                    </li>
                    <li>
                        <form:label path="password"><spring:message code="msg.user.password" /></form:label>
                        <form:input path="password" type="password" required="true" />
                    </li>
                </ul>
                <form:button type="submit"><spring:message code="msg.user.login" /></form:button>
            </form:form>
        </main>
    </body>
</html>