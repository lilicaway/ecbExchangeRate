<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en" ng-app="ecbRates">
<head>
<link href="/app/bootstrap.css" rel='stylesheet' type='text/css' />
<link href="/app/app.css" rel='stylesheet' type='text/css' />
</head>
<body>
    <div class="container">
        <p>Coming soon exchange rate system based on ECB data</p>
        <img src="/resources/ECB_logo.png" />
        <div ng-view></div>
    </div>
    <script type="text/javascript" src="/app/bundle.js"></script>
</body>
</html>
