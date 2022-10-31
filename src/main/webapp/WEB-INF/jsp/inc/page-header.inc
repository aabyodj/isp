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
                            <li${activeCustomer.balance < 0 ? ' class="negative"' : ''}><fmt:message key="msg.customer.balance" />: ${activeCustomer.balance}
                            <li><a href="${pageContext.request.contextPath}/my_account"><fmt:message key="msg.user.myAccount" /></a>
                            <li><a href="${pageContext.request.contextPath}/logout"><fmt:message key="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'MANAGER'}">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion"><fmt:message key="msg.promotion.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/tariff"><fmt:message key="msg.tariff.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/customer"><fmt:message key="msg.customer.manage" /></a>
                    <li>
                        <p>${activeEmployee.email}</p>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/my_account"><fmt:message key="msg.user.myAccount" /></a>
                            <li><a href="${pageContext.request.contextPath}/logout"><fmt:message key="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'ADMIN'}">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion"><fmt:message key="msg.promotion.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/tariff"><fmt:message key="msg.tariff.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/customer"><fmt:message key="msg.customer.manage" /></a>
                    <li><a href="${pageContext.request.contextPath}/employee"><fmt:message key="msg.employee.manage" /></a>
                    <li>
                        <p><c:out value="${activeEmployee.email}" /></p>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/my_account"><fmt:message key="msg.user.myAccount" /></a>
                            <li><a href="${pageContext.request.contextPath}/logout"><fmt:message key="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/promotion">Promotions</a>
                    <li><a href="${pageContext.request.contextPath}/tariff">Tariffs</a>
                    <li><a href="${pageContext.request.contextPath}/login"><fmt:message key="msg.user.login" /></a>
                </ul>
            </c:otherwise>
            </c:choose></nav>
        </header>