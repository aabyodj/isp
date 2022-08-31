<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${promotion.name}" /><fmt:message key="${promotion.id != null ? 'msg.promotion.title.edit' : 'msg.promotion.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="${promotion.id != null ? 'msg.promotion.h1.edit' : 'msg.promotion.h1.new'}" /></h1>
            <form action="?action=save_promotion" method="POST">
                <input type="hidden" name="id" value="${promotion.id}"><%-- TODO: make this null for new promotion --%>
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="promotion-name"><fmt:message key="msg.promotion.name" /></label>
                        <input name="name" id="promotion-name" type="text" required maxlength=25 placeholder="<fmt:message key="msg.promotion.name" />" <%-- TODO: define maxlength via a constant --%>
                            value="<c:out value="${promotion.name}" />">
                    </li>
                    <li>
                        <label for="description"><fmt:message key="msg.promotion.description" /></label>
                        <textarea name="description" id="description" required maxlength=100 placeholder="<fmt:message key="msg.promotion.description" />"><c:out value="${promotion.description}" /></textarea>
                    </li>
                    <li>
                        <label for="active-since"><fmt:message key="msg.promotion.since" /></label>
                        <input name="active-since" id="active-since" type="date" value="${promotion.activeSince.toLocalDate()}">
                    </li>
                    <li>
                        <label for="active-until"><fmt:message key="msg.promotion.until" /></label>
                        <input name="active-until" id="active-until" type="date" value="${promotion.activeUntil.toLocalDate()}">
                    </li>
                </ul>
                <button type="submit"><fmt:message key="msg.promotion.submit" /></button>
            </form>
        </main>
    </body>
</html>