<%@ include file="/WEB-INF/jsp/inc/html-start.inc" %>
<html>
    <head>
<%@ include file="/WEB-INF/jsp/inc/html-head.inc" %>
        <title><c:out value="${tariff.name}" /><spring:message code="${tariff.id != null ? 'msg.tariff.title.edit' : 'msg.tariff.title.new'}" /></title>
    </head>
    <body>
<%@ include file="inc/page-header.inc" %>
        <main>
            <h1><spring:message code="${tariff.id != null ? 'msg.tariff.h1.edit' : 'msg.tariff.h1.new'}" /></h1>
            <form:form method="POST" modelAttribute="tariff">
                <form:input type="hidden" path="id" />
                <input name="redirect" type="hidden" value="${redirect}">
                <ul>
                    <li>
                        <form:label path="name"><spring:message code="msg.tariff.name" /></form:label><spring:message code="msg.tariff.name" var="namePlaceholder" />
                        <form:input path="name" type="text" required="true" maxlength="15" placeholder="${namePlaceholder}" cssErrorClass="error" />
                        <form:errors path="name" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="description"><spring:message code="msg.tariff.description" /></form:label><spring:message code="msg.tariff.description" var="descriptionPlaceholder" />
                        <form:textarea path="description" required="true" maxlength="50" placeholder="${descriptionPlaceholder}" cssErrorClass="error" />
                        <form:errors path="description" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="bandwidth"><spring:message code="msg.tariff.bandwidthLabel" /></form:label>
                        <form:input path="bandwidth" type="number" required="true" min="0" step="1" cssErrorClass="error" />
                        <form:errors path="bandwidth" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="includedTraffic"><spring:message code="msg.tariff.trafficLabel" /></form:label>
                        <form:input path="includedTraffic" type="number" required="true" min="0" step="1" cssErrorClass="error" />
                        <form:errors path="includedTraffic" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="price"><spring:message code="msg.tariff.priceLabel" /></form:label>
                        <form:input path="price" type="number" required="true" min="0.01" step="0.01" cssErrorClass="error" />
                        <form:errors path="price" cssClass="error-message" />
                    </li>
                    <li>
                        <form:label path="active"><spring:message code="msg.tariff.active" /></form:label>
                        <form:checkbox path="active" />
                    </li>
                </ul>
                <form:button type="submit"><spring:message code="msg.tariff.submit" /></form:button>
            </form:form>
        </main>
    </body>
</html>