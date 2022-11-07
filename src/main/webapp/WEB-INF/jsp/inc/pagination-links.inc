<c:if test="${page.totalPages > 1}">
    <p>
        <c:if test="${page.hasPrevious()}">
            <a href="?page=1"><spring:message code="msg.page.first" /></a> &lt;&lt;
            <a href="?page=${page.number}"><spring:message code="msg.page.prev" /></a> &lt;
        </c:if>
        ${page.number + 1}
        <c:if test="${page.hasNext()}">
            &gt; <a href="?page=${page.number + 2}"><spring:message code="msg.page.next" /></a>
            &gt;&gt; <a href="?page=${page.totalPages}"><spring:message code="msg.page.last" /></a>
        </c:if>
    </p>
</c:if>