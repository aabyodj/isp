<%@ include file="/WEB-INF/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><fmt:message key="msg.user.login" /> | <fmt:message key="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="msg.user.login" /></h1>
            <p>
                <fmt:message key="msg.user.login.hintEmail" /> <b>${defaultAdminEmail}</b>,
                <fmt:message key="msg.user.login.hintPassword" /> <b>${defaultAdminPassword}</b>
            </p>
            <form action="${pageContext.request.contextPath}/login/" method="POST">
                <input name="redirect" type="hidden" value="${redirect != null ? redirect : '?'.concat(pageContext.request.queryString)}">
                <ul>
                    <li>
                        <label for="user-email"><fmt:message key="msg.user.email" /></label>
                        <input name="email" id="user-email" type="email" required placeholder="user@example.com">
                    </li>
                    <li>
                        <label for="password"><fmt:message key="msg.user.password" /></label>
                        <input name="password" id="password" type="password" required>
                    </li>
                </ul>
                <button type="submit"><fmt:message key="msg.user.login" /></button>
            </form>
        </main>
    </body>
</html>