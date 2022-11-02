        <header>
            <h1><a href="${pageContext.request.contextPath}/">Internet Service Provider</a></h1>
            <nav><c:choose>
            <c:when test="${activeCustomer != null}">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion">Promotions</a>
                    <li><a href="${pageContext.request.contextPath}/tariff">Tariffs</a>
                    <li>
                        <p><c:out value="${activeCustomer.email}" /></p>
                        <ul>
                            <li${activeCustomer.balance < 0 ? ' class="negative"' : ''}><spring:message code="msg.customer.balance" />: ${activeCustomer.balance}
                            <li><a href="${pageContext.request.contextPath}/my_account"><spring:message code="msg.user.myAccount" /></a>
                            <li><a href="${pageContext.request.contextPath}/logout"><spring:message code="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'MANAGER'}">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion"><spring:message code="msg.promotion.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/tariff"><spring:message code="msg.tariff.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/customer"><spring:message code="msg.customer.manage" /></a>
                    <li>
                        <p>${activeEmployee.email}</p>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/my_account"><spring:message code="msg.user.myAccount" /></a>
                            <li><a href="${pageContext.request.contextPath}/logout"><spring:message code="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'ADMIN'}">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion"><spring:message code="msg.promotion.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/tariff"><spring:message code="msg.tariff.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/customer"><spring:message code="msg.customer.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/employee"><spring:message code="msg.employee.manage" /></a>
                    <li>
                        <p><c:out value="${activeEmployee.email}" /></p>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/my_account"><spring:message code="msg.user.myAccount" /></a>
                            <li><a href="${pageContext.request.contextPath}/logout"><spring:message code="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion">Promotions</a>
                    <li><a href="${pageContext.request.contextPath}/tariff">Tariffs</a>
                    <li><a href="${pageContext.request.contextPath}/login"><spring:message code="msg.user.login" /></a>
                </ul>
            </c:otherwise>
            </c:choose></nav>
        </header>