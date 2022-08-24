<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title>Manage tariffs | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>Manage tariff plans</h1>
            <p><a href="?action=new_tariff">Add new tariff</a>
            <form action="?action=generate_tariffs" method="POST">
                <input name="redirect" type="hidden" value="${'?'.concat(pageContext.request.queryString)}"
                <label for="quantity">Automatically generate</label>
                <input name="quantity" type="number" required min=1 value=10>
                <label for="active">activated</label>
                <input name="active" type="checkbox" checked>
                tariffs.
                <input type="submit" value="Generate">
            </form>
            <table>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Bandwidth</th>
                    <th>Traffic limit</th>
                    <th>Price</th>
                    <th></th>
                </tr>
                <c:forEach var="tariff" items="${tariffs}" varStatus="status">
                    <tr${tariff.active ? ' class="active"' : ''}>
                        <td>${pagination.offset + status.count}</td>
                        <td><c:out value="${tariff.name}" /></td>
                        <td><c:out value="${tariff.description}" /></td>
                        <td>${util.formatBandwidth(tariff.bandwidth)}</td>
                        <td>${util.formatTraffic(tariff.includedTraffic)}</td>
                        <td><c:out value="${tariff.price}" /></td>
                        <td>
                            <a href="?action=edit_tariff&id=${tariff.id}">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${tariffs == null}">
                    <tr>
                        <td colspan=6>No tariff plans</td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${pagination.lastPageNumber > 0}">
                <p>
                    <c:if test="${pagination.pageNumber > 0}">
                        <a href="?action=manage_tariffs&page=0">First</a>
                        <a href="?action=manage_tariffs&page=${pagination.pageNumber - 1}">Prev</a>
                    </c:if>
                    ${pagination.pageNumber}
                    <c:if test="${pagination.pageNumber < pagination.lastPageNumber}">
                        <a href="?action=manage_tariffs&page=${pagination.pageNumber + 1}">Next</a>
                        <a href="?action=manage_tariffs&page=${pagination.lastPageNumber}">Last</a>
                    </c:if>
                </p>
            </c:if>
        </main>
    </body>
</html>
