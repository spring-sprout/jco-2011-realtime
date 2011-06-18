<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JCO 2011 - Realtime Web Application :: OXQuiz</title>
    <meta name="description" content="JCO 2011 ">
  	<meta name="author" content="SpringSprout">
    <link rel="stylesheet" type="text/css" href='<spring:url value="/css/style.css" />' />
    <script src='<spring:url value="/js/jquery-1.5.2.js" />'></script>
    <script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
    <script type="text/javascript">
        var loadTest = function() {
            var hubs = new Array();
            
            return {
                conn: function(cnt) {
                    for(var idx=0; idx<cnt; idx++) {
                        var hub = new StreamHub();
                        hub.connect("http://dev.springsprout.org:10010/streamhub/");
                        hub.subscribe("notification", function(topic, notification) {
                            if(notification.state === 'myEntryId')
                                $.get('<spring:url value="/entryConn" />', {entryId: notification.entryId});
                        });
                        hub.subscribe("entry", function(topic, message) {
                            console.log(message);
                        });
                        
                        hubs.push(hub);
                    }
                },
                answer: function() {
                    $.each(hubs, function(idx, hub) {
                        if(Math.floor(Math.random() * 2) == 0)
                            hub.publish('entryAnswerSubmitCommand', '{"answer":"yes"}');
                        else
                            hub.publish('entryAnswerSubmitCommand', '{"answer":"no"}');
                    });
                }
            };
        }();

        $(document).ready(function(){
            $('#conn1').click(function() { loadTest.conn(1); });
            $('#answer').click(function() { loadTest.answer(); });
        });
    </script>
</head>
<body>
<button id="conn1">접속 열기</button>
<button id="answer">답변 선택</button> 
</body>
</html>