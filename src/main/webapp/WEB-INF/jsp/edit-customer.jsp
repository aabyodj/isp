<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><c:out value="${customer.email}" /><spring:message code="${customer.id != null ? 'msg.customer.title.edit' : 'msg.customer.title.new'}" /></title>
    </head>
    <body>
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="${customer.id != null ? 'msg.customer.h1.edit' : 'msg.customer.h1.new'}" /></h1>
            <form:form method="POST" modelAttribute="customer">
                <input name="redirect" type="hidden" value="${redirect}">
                <form:input path="id" type="hidden" />
                <ul>
                    <li>
                        <form:label path="email"><spring:message code="msg.user.email" /></form:label>
                        <form:input path="email" type="email" required="true" maxlength="50" placeholder="user@example.com" errorCssClass="error" />
                        <form:errors path="email" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="password"><spring:message code="msg.user.password" /></form:label>
                        <c:choose>
                            <c:when test="${customer.id == null}">
                                <form:input path="password" type="password" required="true" errorCssClass="error" />
                            </c:when>
                            <c:otherwise>
                                <form:input path="password" type="password" errorCssClass="error" />
                            </c:otherwise>
                        </c:choose>
                        <form:errors path="password" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="passwordConfirmation"><spring:message code="msg.user.confirmPassword" /></form:label>
                        <c:choose>
                            <c:when test="${customer.id == null}">
                                <form:input path="passwordConfirmation" type="password" required="true" errorCssClass="error" />
                            </c:when>
                            <c:otherwise>
                                <form:input path="passwordConfirmation" type="password" errorCssClass="error" />
                            </c:otherwise>
                        </c:choose>
                        <form:errors path="passwordConfirmation" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="balance"><spring:message code="msg.customer.balance" /></form:label>
                        <form:input path="balance" type="number" required="true" step="0.01" />
                    </li>
                    <li>
                        <form:label path="permittedOverdraft"><spring:message code="msg.customer.permittedOverdraft" /></form:label>
                        <form:input path="permittedOverdraft" type="number" required="true" min="0" step="0.01" />
                    </li>
                    <li>
                        <form:label path="payoffDate"><spring:message code="msg.customer.payoffDate" /></form:label>
                        <form:input path="payoffDate" type="date" />
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
                        <form:label path="active"><spring:message code="msg.user.active" /></form:label>
                        <form:checkbox path="active" />
                    </li>
                </ul>
                <form:button type="submit"><spring:message code="msg.user.submit" /></form:button>
            </form:form>
        </main>
    </body>
</html>