<%@ include file="/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><fmt:message key="msg.error400.title" /></title>
    </head>
    <body>
<%@ include file="/jsp/inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="msg.error400.h1" /></h1>
            <p><c:out value="${pageContext.exception.message}" />
        </main>
    </body>
</html>