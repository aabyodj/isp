        <header>
            <h1><a href="${pageContext.request.contextPath}">Internet Service Provider</a></h1>
            <nav><c:choose>
            <c:when test="${activeUser == null}">
                <ul>
                    <li><a href="?action=login">Log in</a>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href="?action=manage_users">Manage users</a>
                    <li><a href="?action=logout">Log out</a>
                </ul>
            </c:otherwise>
            </c:choose></nav>
        </header>