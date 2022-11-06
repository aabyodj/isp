<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><spring:message code="msg.user.my-account" /> | <spring:message code="msg.home.title" /></title>
    </head>
    <body id="my-account">
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1><spring:message code="msg.user.my-account" /></h1>
            <c:choose>
                <c:when test="${activeCustomer != null}">
                    <ul>
                        <li>
                            <p${activeCustomer.balance < 0 ? ' class="negative"' : ''}><spring:message code="msg.customer.balance" />: ${activeCustomer.balance}
                            <form action="/my_account/replenish_balance" method="POST">
                                <input name="redirect" type="hidden" value="/my_account">
                                <ul>
                                    <li>
                                        <label for="amount"><spring:message code="msg.customer.balance.replenish" /></label>
                                        <input name="amount" id=amount type="number" required min="0.01" step="0.01">
                                    </li>
                                </ul>
                                <button type="submit"><spring:message code="msg.customer.balance.replenish.submit" /></button>
                            </form>
                        </li>
                        <li>
                            <p><spring:message code="msg.customer.my-subscriptions" />:
                            <table>
                                <thead>
                                    <tr>
                                        <th><spring:message code="msg.subscription.table.since" /></th>
                                        <th><spring:message code="msg.subscription.table.until" /></th>
                                        <th><spring:message code="msg.subscription.table.tariff" /></th>
                                        <th><spring:message code="msg.subscription.table.price" /></th>
                                        <th><spring:message code="msg.subscription.table.bandwidth" /></th>
                                        <th><spring:message code="msg.subscription.table.traffic-consumed" /></th>
                                        <th><spring:message code="msg.subscription.table.traffic-left" /></th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="subscription" items="${subscriptions}">
                                        <tr${subscription.active ? ' class="active"' : ''}>
                                            <td>${subscription.activeSince}</td>
                                            <td>${subscription.activeUntil}</td>
                                            <td><c:out value="${subscription.tariff.name}" /></td>
                                            <td>${subscription.price}</td>
                                            <td>${subscription.tariff.bandwidth}</td>
                                            <td>${subscription.trafficConsumed}</td>
                                            <td>${subscription.trafficLeft}</td>
                                            <td><c:if test="${subscription.active}">
                                                <a href="/my_account/cancel_subscription?subscription_id=${subscription.id}"><spring:message code="msg.subscription.cancel" /></a></c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${subscriptions == null}">
                                        <tr><td colspan=8><spring:message code="msg.subscription.no-subscriptions" /></td></tr>
                                    </c:if>
                                </tbody>
                            </table>
                            <c:if test="${tariffs != null}">
                                <form action="/my_account/subscribe" method="POST">
                                    <input name="redirect" type="hidden" value="/my_account">
                                    <ul>
                                        <li>
                                            <label for="new-tariff"><spring:message code="msg.subscription.subscribe-another" /></label>
                                            <select name="tariff_id" id="new-tariff" required>
                                                <c:forEach var="tariff" items="${tariffs}">
                                                    <option value="${tariff.id}"><c:out value="${tariff.name}" /></option>
                                                </c:forEach>
                                            </select>
                                        </li>
                                    </ul>
                                    <button type="submit"><spring:message code="msg.subscription.subscribe" /></button>
                                </form>
                            </c:if>
                        </li>
                    </ul>
                </c:when>
            </c:choose>
            <form:form action="/my_account/update_credentials" method="POST" modelAttribute="credentials">
                <input name="redirect" type="hidden" value="/my_account">
                <form:input path="userId" type="hidden" />
                <ul>
                    <c:if test="${activeEmployee != null}">
                        <li>
                            <label><spring:message code="msg.employee.role" /></label>
                            <span><spring:message code="${activeEmployee.role.messageKey}" /></span>
                        </li>
                    </c:if>
                    <li>
                        <form:label path="email"><spring:message code="msg.user.email" /></form:label>
                        <form:input path="email" type="text" required="true" maxlength="50" cssErrorClass="error" />
                        <form:errors path="email" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="newPassword"><spring:message code="msg.user.new-password" /></form:label>
                        <form:input path="newPassword" type="password" cssErrorClass="error" />
                        <form:errors path="newPassword" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="newPasswordConfirmation"><spring:message code="msg.user.confirm-password" /></form:label>
                        <form:input path="newPasswordConfirmation" type="password" cssErrorClass="error" />
                        <form:errors path="newPasswordConfirmation" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="currentPassword"><spring:message code="msg.user.current-password" /></form:label>
                        <form:input path="currentPassword" type="password" required="true" cssErrorClass="error" />
                        <form:errors path="currentPassword" cssClass="error-message" />
                    </li>
                </ul>
                <form:button type="submit"><spring:message code="msg.user.update" /></form:button>
            </form:form>
        </main>
    </body>
</html>
