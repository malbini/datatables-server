<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>Airports Column Filter</title>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/dataTables.bootstrap.css" />
    <script src="<%=contextPath%>/js/jquery-1.11.2.min.js"></script>
    <script src="<%=contextPath%>/js/bootstrap.min.js"></script>
    <script src="<%=contextPath%>/js/jquery.dataTables.min.js"></script>
    <script src="<%=contextPath%>/js/dataTables.bootstrap.js"></script>
    <script src="<%=contextPath%>/js/airports-column-filter.js"></script>
</head>
<body>
<h1>Airports Column Filter</h1>
<div id="container">
    <div class="col-lg-12">
        <table id="airports-tab" class="table table-condensed"></table>
    </div>
    <footer>
        Full data available at <a href="http://ourairports.com/data">http://ourairports.com/data</a>
    </footer>
</div>
</body>
</html>