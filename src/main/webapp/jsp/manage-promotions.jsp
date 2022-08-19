<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Manage promotions | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage discounts and promotions</h1>
            <a href="?action=new_promotion">Add new promotion</a>
            <table>
                <tr>
                    <th>Since</th>
                    <th>Until</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th></th>
                </tr>
                <c:forEach var="promotion" items="${promotions}">
                    <tr${promotion.isActiveOn(now) ? ' class="active"' : ''}>
                        <td>${util.formatDate(promotion.activeSince, 'Since the Big Bang')}</td>
                        <td>${util.formatDate(promotion.activeUntil, 'Until stopped')}</td>
                        <td><c:out value="${promotion.name}" /></td>
                        <td><c:out value="${promotion.description}" /></td>
                        <td>
                            <a href="?action=edit_promotion&id=${promotion.id}">Edit</a>
                            <c:if test="${promotion.isActiveOn(now)}">
                                <a href="?action=stop_promotion&id=${promotion.id}">Stop</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${promotions == null}">
                    <tr>
                        <td colspan=5>No promotions</td>
                    </tr>
                </c:if>
            </table>
        </main>
    </body>
</html>
