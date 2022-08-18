        <header>
            <h1><a href="${pageContext.request.contextPath}">Internet Service Provider</a></h1>
            <nav><c:choose>
            <c:when test="${activeCustomer != null}">
                <ul>
                    <li>
                        <p><c:out value="${activeCustomer.email}" /></p>
                        <ul>
                            <li${activeCustomer.balance < 0 ? ' class="negative"' : ''}>Balance: ${activeCustomer.balance}
                            <li><a href="?action=my_account">My account</a>
                            <li><a href="?action=logout">Log out</a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'MANAGER'}">
                <ul>
                    <li><a href="?action=manage_promotions">Manage promotions</a>
                    <li><a href="?action=manage_customers">Manage customers</a>
                    <li>
                        <p>${activeEmployee.email}</p>
                        <ul>
                            <li><a href="?action=my_account">My account</a>
                            <li><a href="?action=logout">Log out</a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:when test="${activeEmployee.role == 'ADMIN'}">
                <ul>
                    <li><a href="?action=manage_promotions">Manage promotions</a>
                    <li><a href="?action=manage_customers">Manage customers</a>
                    <li><a href="?action=manage_employees">Manage employees</a>
                    <li>
                        <p><c:out value="${activeEmployee.email}" /></p>
                        <ul>
                            <li><a href="?action=my_account">My account</a>
                            <li><a href="?action=logout">Log out</a>
                        </ul>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href="?action=login">Log in</a>
                </ul>
            </c:otherwise>
            </c:choose></nav>
        </header>