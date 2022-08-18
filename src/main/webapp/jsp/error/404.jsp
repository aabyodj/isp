<%@ include file="/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Not found | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="/jsp/inc/page-header.inc" %>
        <main>
            <h1>Not found</h1>
            <p><c:out value="${pageContext.exception.message}" />
        </main>
    </body>
</html>