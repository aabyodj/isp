<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title>My account | Internet Service Provider</title>
    </head>
    <body id="my-account">
<%@ include file="/WEB-INF/jsp/inc/page-header.inc" %>
        <main>
            <h1>My account</h1>
            <c:choose>
                <c:when test="${activeCustomer != null}">
                    <ul>
                        <li>
                            <p${activeCustomer.balance < 0 ? ' class="negative"' : ''}>Balance: ${activeCustomer.balance}
                            <form action="/my_account/replenish_balance" method="POST">
                                <input name="redirect" type="hidden" value="/my_account">
                                <label for="amount">Replenish balance by:</label>
                                <input name="amount" type="number" required min="0.01" step="0.01">
                                <input type="submit" value="Replenish">
                            </form>
                        </li>
                        <li>
                            <p>My subscriptions:
                            <table>
                                <tr>
                                    <th>Since</th>
                                    <th>Until</th>
                                    <th>Tariff</th>
                                    <th>Price</th>
                                    <th>Bandwidth</th>
                                    <th>Traffic consumed</th>
                                    <th>Traffic left</th>
                                    <th></th>
                                </tr>
                                <c:if test="${subscriptions == null}">
                                    <tr><td colspan=8>No subscriptions</td></tr>
                                </c:if>
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
                                            <a href="/my_account/cancel_subscription?subscription_id=${subscription.id}">Cancel</a></c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <c:if test="${tariffs != null}">
                                <form action="/my_account/subscribe" method="POST">
                                    <input name="redirect" type="hidden" value="/my_account">
                                    <label for="new-tariff">Subscribe to another tariff:</label>
                                    <select name="tariff_id" required>
                                        <c:forEach var="tariff" items="${tariffs}">
                                            <option value="${tariff.id}"><c:out value="${tariff.name}" /></option>
                                        </c:forEach>
                                    </select>
                                    <input type="submit" value="Subscribe">
                                </form>
                            </c:if>
                        </li>
                    </ul>
                </c:when>
                <c:when test="${activeEmployee != null}">
                    <ul><li>Role: ${activeEmployee.role.name().toLowerCase()}</ul>
                </c:when>
            </c:choose>
            <form:form action="/my_account/update_credentials" method="POST" modelAttribute="credentials">
                <input name="redirect" type="hidden" value="/my_account">
                <form:input path="userId" type="hidden" />
                <ul>
                    <li>
                        <form:label path="email">Email:</form:label>
                        <form:input path="email" type="text" required="true" maxlength="25" cssErrorClass="error" />
                        <form:errors path="email" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="newPassword">New password:</form:label>
                        <form:input path="newPassword" type="password" cssErrorClass="error" />
                        <form:errors path="newPassword" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="newPasswordConfirmation">Confirm new password:</form:label>
                        <form:input path="newPasswordConfirmation" type="password" cssErrorClass="error" />
                        <form:errors path="newPasswordConfirmation" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="currentPassword">Current password:</form:label>
                        <form:input path="currentPassword" type="password" required="true" cssErrorClass="error" />
                        <form:errors path="currentPassword" cssClass="error-message" />
                    </li>
                </ul>
                <form:button type="submit">Update</form:button>
            </form:form>
        </main>
    </body>
</html>
