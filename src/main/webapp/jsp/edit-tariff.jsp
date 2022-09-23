<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${tariff.name}" /><fmt:message key="${tariff.id != null ? 'msg.tariff.title.edit' : 'msg.tariff.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="${tariff.id != null ? 'msg.tariff.h1.edit' : 'msg.tariff.h1.new'}" /></h1>
            <form action="?action=save_tariff" method="POST">
                <input type="hidden" name="id" value="${tariff.id}">
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="tariff-name"><fmt:message key="msg.tariff.name" /></label>
                        <input name="name" id="tariff-name" type="text" required maxlength=15 placeholder="<fmt:message key="msg.tariff.name" />"
                            value="<c:out value="${tariff.name}" />">
                    </li>
                    <li>
                        <label for="description"><fmt:message key="msg.tariff.description" /></label>
                        <textarea name="description" id="description" required maxlength=50 placeholder="<fmt:message key="msg.tariff.description" />"><c:out value="${tariff.description}" /></textarea>
                    </li>
                    <li>
                        <label for="bandwidth"><fmt:message key="msg.tariff.bandwidthLabel" /></label>
                        <input name="bandwidth" id="bandwidth" type="number" required min=0 step=1 value="${tariff.bandwidth}">
                    </li>
                    <li>
                        <label for="included-traffic"><fmt:message key="msg.tariff.trafficLabel" /></label>
                        <input name="included-traffic" id="included-traffic" type="number" required min=0 step=1 value="${tariff.includedTraffic / (1024 * 1024)}">
                    </li>
                    <li>
                        <label for="price"><fmt:message key="msg.tariff.priceLabel" /></label>
                        <input name="price" id="price" type="number" required min="0.01" step="0.01" value="${tariff.price}">
                    </li>
                    <li>
                        <label for="active"><fmt:message key="msg.tariff.active" /></label>
                        <input name="active" id="active" type="checkbox"${tariff.active ? ' checked' : ''}>
                    </li>
                </ul>
                <button type="submit"><fmt:message key="msg.tariff.submit" /></button>
            </form>
        </main>
    </body>
</html>