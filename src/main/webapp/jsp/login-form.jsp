<%@ include file="/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Log in | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Log in</h1>
            <form action="?action=check_login" method="POST">
                <input name="redirect" type="hidden" value="${redirect != null ? redirect : '?'.concat(pageContext.request.queryString)}">
                <ul>
                    <li>
                        <label for="user-email">Your email</label>
                        <input type="email" name="email" required placeholder="user@example.com">
                    </li>
                    <li>
                        <label for="password">Your password</label>
                        <input type="password" name="password" required>
                    </li>
                </ul>
                <input type="submit" value="Proceed">
            </form>
        </main>
    </body>
</html>