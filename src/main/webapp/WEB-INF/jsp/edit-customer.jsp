<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><c:out value="${customer.email}" /><spring:message code="${customer.id != null ? 'msg.customer.title.edit' : 'msg.customer.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><spring:message code="${customer.id != null ? 'msg.customer.h1.edit' : 'msg.customer.h1.new'}" /></h1>
            <form action="/customers" method="POST">
                <c:if test="${customer.id != null}"><input type="hidden" name="id" value="${customer.id}"></c:if>
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <label for="email"><spring:message code="msg.user.email" /></label>
                        <input name="email" id="email" type="email" required maxlength=50 placeholder="user@example.com"
                            value="<c:out value="${customer.email}" />">
                    </li>
                    <li>
                        <label for="password1"><spring:message code="msg.user.password" /></label>
                        <input name="password1" id="password1" type="password"${customer.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="password2"><spring:message code="msg.user.confirmPassword" /></label>
                        <input name="password2" id="password2" type="password"${customer.id == 0 ? ' required' : ''}>
                    </li>
                    <li>
                        <label for="balance"><spring:message code="msg.customer.balance" /></label>
                        <input name="balance" id="balance" type="number" required step="0.01" value="${customer.balance}">
                    </li>
                    <li>
                        <label for="permitted-overdraft"><spring:message code="msg.customer.permittedOverdraft" /></label>
                        <input name="permitted-overdraft" id="permitted-overdraft" type="number" required min="0" step="0.01" value="${customer.permittedOverdraft}">
                    </li>
                    <li>
                        <label for="payoff-date"><spring:message code="msg.customer.payoffDate" /></label>
                        <input name="payoff-date" id="payoff-date" type="date" value="${customer.payoffDate.toLocalDate()}">
                    </li>
                    <li>
                        <label for="tariff"><spring:message code="msg.subscription.tariff" /></label>
                        <select name="tariff" id="tariff">
                            <option value="none" ${activeTariff == null ? ' selected' : ''}>
                                <spring:message code="msg.subscription.noTariff" />
                            </option>
                            <c:forEach var="tariff" items="${tariffs}">
                                <option value="${tariff.id}"${activeTariff.id == tariff.id ? ' selected' : ''}>
                                    <c:out value="${tariff.name}" />
                                </option>
                            </c:forEach>
                        </select>
                    </li>
                    <li>
                        <label for="active"><spring:message code="msg.user.active" /></label>
                        <input name="active" id="active" type="checkbox"${customer.active ? ' checked' : ''}>
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.user.submit" /></button>
            </form>
        </main>
    </body>
</html>