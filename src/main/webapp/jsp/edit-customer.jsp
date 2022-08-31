<%@ include file="/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/jsp/inc/html-head.inc" %>
        <title><c:out value="${customer.email}" /><fmt:message key="${customer.id !=0 ? 'msg.customer.title.edit' : 'msg.customer.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><fmt:message key="${customer.id !=0 ? 'msg.customer.h1.edit' : 'msg.customer.h1.new'}" /></h1>
            <form action="?action=save_customer" method="POST">
                <input type="hidden" name="id" value="${customer.id}">
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="email"><fmt:message key="msg.user.email" /></label>
                        <input name="email" id="email" type="email" required maxlength=50 placeholder="user@example.com"
                            value="<c:out value="${customer.email}" />">
                    </li>
                    <li>
                        <label for="password1"><fmt:message key="msg.user.password" /></label>
                        <input name="password1" id="password1" type="password"${customer.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="password2"><fmt:message key="msg.user.confirmPassword" /></label>
                        <input name="password2" id="password2" type="password"${customer.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="balance"><fmt:message key="msg.customer.balance" /></label>
                        <input name="balance" id="balance" type="number" required step="0.01" value="${customer.balance}">
                    </li>
                    <li>
                        <label for="permitted-overdraft"><fmt:message key="msg.customer.permittedOverdraft" /></label>
                        <input name="permitted-overdraft" id="permitted-overdraft" type="number" required min="0" step="0.01" value=${customer.permittedOverdraft}>
                    </li>
                    <li>
                        <label for="payoff-date"><fmt:message key="msg.customer.payoffDate" /></label>
                        <input name="payoff-date" id="payoff-date" type="date" value=${customer.payoffDate.toLocalDate()}>
                    </li>
                    <li>
                        <label for="tariff"><fmt:message key="msg.subscription.tariff" /></label>
                        <select name="tariff" id="tariff">
                            <option value="0"${activeTariff == null ? ' selected' : ''}>
                                <fmt:message key="msg.subscription.noTariff" />
                            </option>
                            <c:forEach var="tariff" items="${tariffs}">
                                <option value="${tariff.id}"${activeTariff.id == tariff.id ? ' selected' : ''}>
                                    <c:out value="${tariff.name}" />
                                </option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <label for="active"><fmt:message key="msg.user.active" /></label>
                        <input name="active" id="active" type="checkbox"${customer.active ? ' checked' : ''}>
                    </li>
                </ul>
                <button type="submit"><fmt:message key="msg.user.submit" /></button>
            </form>
        </main>
    </body>
</html>