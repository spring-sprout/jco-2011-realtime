<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JCO 2011 - Realtime Web Application :: OXQuiz</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css" />
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.12/jquery-ui.min.js"></script>
    <script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
</head>
<body>
    <h2>Realtime Web Application :: OXQuiz</h2>
    <a href='<spring:url value="/examples/chat" />'>Realtime Chat</a>
</body>
</html>