<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
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
            <form action="/login" method="POST">
                <sec:csrfInput />
                <c:if test="${logout != null}"><p class="success-message"><spring:message code="msg.user.logout.message" /></p></c:if>
                <c:if test="${updated != null}"><p class="success-message"><spring:message code="msg.user.update.message" /></p></c:if>
                <c:if test="${error != null}"><p class="error-message"><spring:message code="msg.user.login.error" /><p></c:if>
                <ul>
                    <li>
                        <label for="email"><spring:message code="msg.user.email" /></label>
                        <input name="email" id="email" type="email" required placeholder="user@example.com" value="${email}" />
                    </li>
                    <li>
                        <label for="password"><spring:message code="msg.user.password" /></label>
                        <input name="password" id="password" type="password" required />
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.user.login" /></button>
            </form>
        </main>
    </body>
</html>