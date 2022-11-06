        <header>
            <h1><a href="/">Internet Service Provider</a></h1>
            <nav><c:choose>
            <c:when test="${activeCustomer != null}">
                <ul>
                    <li><a href="/promotions">Promotions</a>
                    <li><a href="/tariffs">Tariffs</a>
                    <li>
                        <p><c:out value="${activeCustomer.email}" /></p>
                        <ul>
                            <li${activeCustomer.balance < 0 ? ' class="negative"' : ''}><spring:message code="msg.customer.balance" />: ${activeCustomer.balance}
                            <li><a href="/my_account"><spring:message code="msg.user.my-account" /></a>
                            <li><a href="/logout"><spring:message code="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'MANAGER'}">
                <ul>
                    <li><a href="/promotions"><spring:message code="msg.promotion.manage" /></a>
                    <li><a href="/tariffs"><spring:message code="msg.tariff.manage" /></a>
                    <li><a href="/customers"><spring:message code="msg.customer.manage" /></a>
                    <li>
                        <p>${activeEmployee.email}</p>
                        <ul>
                            <li><a href="/my_account"><spring:message code="msg.user.my-account" /></a>
                            <li><a href="/logout"><spring:message code="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'ADMIN'}">
                <ul>
                    <li><a href="/promotions"><spring:message code="msg.promotion.manage" /></a>
                    <li><a href="/tariffs"><spring:message code="msg.tariff.manage" /></a>
                    <li><a href="/customers"><spring:message code="msg.customer.manage" /></a>
                    <li><a href="/employees"><spring:message code="msg.employee.manage" /></a>
                    <li>
                        <p><c:out value="${activeEmployee.email}" /></p>
                        <ul>
                            <li><a href="/my_account"><spring:message code="msg.user.my-account" /></a>
                            <li><a href="/logout"><spring:message code="msg.user.logout" /></a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href="/promotions">Promotions</a>
                    <li><a href="/tariffs">Tariffs</a>
                    <li><a href="/login"><spring:message code="msg.user.login" /></a>
                </ul>
            </c:otherwise>
            </c:choose></nav>
        </header>