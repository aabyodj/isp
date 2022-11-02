<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %><%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><c:out value="${promotion.name}" /><spring:message code="${promotion.id != null ? 'msg.promotion.title.edit' : 'msg.promotion.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><spring:message code="${promotion.id != null ? 'msg.promotion.h1.edit' : 'msg.promotion.h1.new'}" /></h1>
            <form:form method="POST" modelAttribute="promotion">
                <c:if test="${promotion.id != null}"><input type="hidden" name="id" value="${promotion.id}"></c:if>
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <form:label path="name"><spring:message code="msg.promotion.name" /></form:label><spring:message code="msg.promotion.name" var="namePlaceholder" />
                        <form:input path="name" type="text" required="true" maxlength="25" placeholder="${namePlaceholder}" cssErrorClass="error" />
                        <form:errors path="name" cssClass="error-message" element="label" for="name" />
                    </li>
                    <li>
                        <form:label path="description"><spring:message code="msg.promotion.description" /></form:label><spring:message code="msg.promotion.description" var="descriptionPlaceholder" />
                        <form:textarea path="description" required="true" maxlength="100" placeholder="${descriptionPlaceholder}" cssErrorClass="error"></form:textarea>
                        <form:errors path="description" cssClass="error-message" element="label" for="description" />
                    </li>
                    <li>
                        <label for="active-since"><spring:message code="msg.promotion.since" /></label>
                        <form:input path="activeSince" id="active-since" type="date" />
                    </li>
                    <li>
                        <label for="active-until"><spring:message code="msg.promotion.until" /></label>
                        <form:input path="activeUntil" id="active-until" type="date" />
                    </li>
                </ul>
                <button type="submit"><spring:message code="msg.promotion.submit" /></button>
            </form:form>
        </main>
    </body>
</html>