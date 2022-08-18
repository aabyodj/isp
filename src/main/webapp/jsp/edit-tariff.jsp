<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${tariff.name}" />${tariff.id !=0 ? ' | Edit' : 'New'} tariff plan | Internet Service Provider</title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1>${tariff.id !=0 ? 'Edit' : 'New'} tariff plan</h1>
            <form action="?action=save_tariff" method="POST">
                <input type="hidden" name="id" value="${tariff.id}">
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="tariff-name">Tariff name</label>
                        <input type="text" name="name" required maxlength=15 placeholder="Tariff name"
                            value="<c:out value="${tariff.name}" />">
                    </li>
                    <li>
                        <label for="description">Tariff description</label>
                        <textarea name="description" required maxlength=50 placeholder="Tariff description"><c:out value="${tariff.description}" /></textarea>
                    </li>
                    <li>
                        <label for="bandwidth">Bandwidth, Kb/s (0 = unlimited)</label>
                        <input name="bandwidth" type="number" required min=0 step=1 value=${tariff.bandwidth}>
                    </li>
                    <li>
                        <label for="included-traffic">Included traffic, Mb (0 = unlimited)</label>
                        <input name="included-traffic" type="number" required min=0 step=1 value=${tariff.includedTraffic / (1024 * 1024)}
                    </li>
                    <li>
                        <label for="price">Monthly price</label>
                        <input type="number" name="price" required min="0.01" step="0.01" value="${tariff.price}">
                    </li>
                    <li>
                        <label for="active">Active</label>
                        <input name="active" type="checkbox"${tariff.active ? ' checked' : ''}>
                    </li>
                </ul>
                <input type="submit" value="Submit">
            </form>
        </main>
    </body>
</html>