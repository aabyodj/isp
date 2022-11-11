        <header><sec:authorize access="hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')" var="isEmployee" />
            <h1><a href="/"><spring:message code="msg.home.h1" /></a></h1>
            <nav>
                <ul>
                    <c:choose>
                        <c:when test="${isEmployee}">
                            <li><a href="/promotions"><spring:message code="msg.promotion.manage" /></a>
                            <li><a href="/tariffs"><spring:message code="msg.tariff.manage" /></a>
                            <li><a href="/customers"><spring:message code="msg.customer.manage" /></a>
                            <sec:authorize access="hasAuthority('ROLE_ADMIN')">
                                <li><a href="/employees"><spring:message code="msg.employee.manage" /></a>
                            </sec:authorize>
                        </c:when>
                        <c:otherwise>
                            <li><a href="/promotions"><spring:message code="msg.promotion.view-all" /></a>
                            <li><a href="/tariffs"><spring:message code="msg.tariff.view-all" /></a>
                        </c:otherwise>
                    </c:choose>
                    <sec:authorize access="authenticated">
                        <li>
                            <p><c:out value="${activeUser.email}" /></p>
                            <ul>
                                <li>
<%@ include file="/WEB-INF/jsp/inc/language-selector.inc" %>
                                </li>
                                <sec:authorize access="hasAuthority('ROLE_CUSTOMER')">
                                    <li${activeCustomer.balance < 0 ? ' class="negative"' : ''}><spring:message code="msg.customer.balance" />: ${activeCustomer.balance}
                                </sec:authorize>
                                <c:if test="${isEmployee}">
                                    <li><spring:message code="msg.employee.role" />: <spring:message code="${activeEmployee.role.messageKey}" />
                                </c:if>
                                <li><a href="/my_account"><spring:message code="msg.user.my-account" /></a>
                                <li><%@ include file="/WEB-INF/jsp/inc/logout-button.inc" %>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="!authenticated">
                        <li>
<%@ include file="/WEB-INF/jsp/inc/language-selector.inc" %>
                        </li>
                        <li><a href="/login"><spring:message code="msg.user.login" /></a>
                    </sec:authorize>
                </ul>
            </nav>
        </header>