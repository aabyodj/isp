<%@ include file="/WEB-INF/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.user.login" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.user.login" /></h1>
            <p>
                <spring:message code="msg.user.login.hintEmail" /> <b>${defaultAdminEmail}</b>,
                <spring:message code="msg.user.login.hintPassword" /> <b>${defaultAdminPassword}</b>
            </p>
            <form action="${pageContext.request.contextPath}/login/" method="POST">
                <c:if test="${wrongCredentials != null}">
                    <p class="error-message">Wrong email/password. Please try again.
                </c:if>
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="user-email"><spring:message code="msg.user.email" /></label>
                        <input name="email" id="user-email" type="email" required placeholder="user@example.com" value="<c:out value='${credentials.email}'/>">
                    </li>
                    <li>
                        <label for="password"><spring:message code="msg.user.password" /></label>
                        <input name="password" id="password" type="password" required>
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.user.login" /></button>
            </form>
        </main>
    </body>
</html>