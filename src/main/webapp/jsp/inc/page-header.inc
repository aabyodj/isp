        <header>
            <h1><a href="${pageContext.request.contextPath}">Internet Service Provider</a></h1>
            <nav><c:choose>
            <c:when test="${activeUser == null}">
                <ul>
                    <li><a href="?action=login">Log in</a>
                </ul>
            </c:when>
            <c:when test="${activeUser.role == 'ADMIN'}">
                <ul>
                    <li><a href="?action=manage_customers">Manage customers</a>
                    <li><a href="?action=manage_employees">Manage employees</a>
                    <li>
                        <p><c:out value="${activeUser.email}" /></p>
                        <ul>
                            <li><a href="?action=logout">Log out</a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeUser.role == 'MANAGER'}">
                <ul>
                    <li><a href="?action=manage_customers">Manage customers</a>
                    <li>
                        <p>${activeUser.email}</p>
                        <ul>
                            <li><a href="?action=logout">Log out</a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li>
                        <p><c:out value="${activeUser.email}" /></p>
                        <ul>
                            <li>Balance: ${account.balance}
                            <c:if test="${account.tariff != null}"><li>Tariff: <c:out value="${account.tariff.name}" />
                            </c:if><li><a href="?action=logout">Log out</a>
                        </ul>
                    </li>
                </ul>
            </c:otherwise>
            </c:choose></nav>
        </header>