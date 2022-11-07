<%@ include file="/WEB-INF/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.error.403.title" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.error.403.h1" /></h1>
            <p><spring:message code="msg.error.403.content" />
        </main>
    </body>
</html>