<%@ include file="/WEB-INF/jsp/inc/html-start-error.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.error.404.title" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.error.404.h1" /></h1>
            <p><spring:message code="msg.error.404.content" arguments="${pageContext.errorData.requestURI}" />
        </main>
    </body>
</html>