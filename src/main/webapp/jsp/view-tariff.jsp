<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${tariff.name}" />Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <h1><c:out value="${tariff.name}" /></h1>
        <div><c:out value="${tariff.description}" /></div>
        <span>Price: </span><span>${tariff.price}</span>
    </body>
</html>