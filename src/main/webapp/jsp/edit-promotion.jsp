<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${promotion.name}" />${promotion.id !=0 ? ' | Edit' : 'New'} promotion | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${promotion.id !=0 ? 'Edit' : 'New'} promotion</h1>
            <form action="?action=save_promotion" method="POST">
                <input type="hidden" name="id" value="${promotion.id}">
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="promotion-name">Promotion name</label>
                        <input type="text" name="name" required maxlength=25 placeholder="Promotion name"
                            value="<c:out value="${promotion.name}" />">
                    </li>
                    <li>
                        <label for="description">Promotion description</label>
                        <textarea name="description" required maxlength=100 placeholder="Promotion description"><c:out value="${promotion.description}" /></textarea>
                    </li>
                    <li>
                        <label for="active-since">Active since</label>
                        <input name="active-since" type="date" value="${activeSince}">
                    </li>
                    <li>
                        <label for="active-until">Active until</label>
                        <input name="active-until" type="date" value="${activeUntil}">
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>