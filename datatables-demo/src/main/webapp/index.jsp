<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>Datatables Demo</title>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/bootstrap.min.css" />
</head>
<body>
    <h1>Datatables Demo</h1>
    <div id="container">
        <ul>
            <li><a href="<%=contextPath%>/airports.jsp">Airports Demo</a></li>
            <li><a href="<%=contextPath%>/airports-column-filter.jsp">Airports Column Filter Demo</a></li>
        </ul>
    </div>
</body>
</html>