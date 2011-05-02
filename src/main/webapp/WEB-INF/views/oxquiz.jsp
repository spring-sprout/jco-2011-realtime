<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JCO 2011 - Realtime Web Application :: OXQuiz</title>
    <link rel="stylesheet" type="text/css" href='<spring:url value="/css/style.css" />' />
    <script src='<spring:url value="/js/jquery-1.5.2.js" />'></script>
    <script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
    <script type="text/javascript">
        var streamHub = new StreamHub(),
            callBackFuns = {};

        // 관리자 명령 정의 - start
        var adminCommand = function() {
        	var topic = "adminCommand";
            return {
                currentQuizClose: function() {
                    streamHub.publish(topic, "{command:currentQuizClose}");
                },
                nextQuiz: function() {
                    streamHub.publish(topic, "{command:nextQuiz}");
                }
            };
        }();
        // 관리자 명령 정의 - end
     
        // 참가자 답 전송 - start
        var entryAnswerSubmitCommand = function() {
        	var topic = "entryAnswerSubmitCommand";
            return {
                O: function() {
                    streamHub.publish(topic, "{answer:O}");
                },
                X: function() {
                    streamHub.publish(topic, "{answer:X}");
                }
            };
        }();
        // 참가자 답 전송 - end
        
        // CallBack 정의 - start
        callBackFuns.currentQuizClose = function(message) {
        	console.log('O / X 화면 이동금지 명령에 서버에서 내려왔습니다.');
        };
        callBackFuns.nextQuiz = function(message) {
        	if(message.quiz === '') {
                console.log('더 이상 문제가 없습니다.');
            } else {
                console.log('서버에서 새로운 문제가 내려왔습니다.');
                console.log(message.quiz);
            }
        };
        callBackFuns.entryAnswerSubmit = function(message) {
        	console.log(message.entryId + '가 ' + message.answer + '를 선택했습니다.');
        };
     // CallBack 정의 - end
        
        $(document).ready(function(){
            streamHub.connect("http://localhost:7878/streamhub/");
            
            // 알림 청취
            streamHub.subscribe("notification", function(topic, notification) {
            	console.log(notification);
            	
            	// 알림 상태에 따라 적절한 콜백 호출 
            	if(callBackFuns[notification.state])
            		callBackFuns[notification.state](notification);
            });            
            
            // 참가
            streamHub.subscribe("entry", function(topic, message) {
            	if(message.state === 'entryIn') {
            		$('#entryList').append('<li id="' + message.entryId + '">Entry Id: ' + message.entryId + '</li>');
            	} else {
            		$('#' + message.entryId).remove();
            	}
            });
        });
    </script>
</head>
<body>
    <h2>Realtime Web Application :: OXQuiz</h2>
    <div>
        참가자 명단 :<br />
        <ul id="entryList">
        <c:forEach items="${entrys}" var="entry">
            <li id="${entry.uid}">Entry Id: ${entry.uid}</li>
        </c:forEach>        
        </ul>
    </div>
    <div>
        참가자 답 제출 :<br />
        <button onclick="entryAnswerSubmitCommand.O();">O</button>
        <button onclick="entryAnswerSubmitCommand.X();">X</button>
    </div>    
    <div>
        관리자 명령 :<br />
        <button onclick="adminCommand.currentQuizClose();">O / X 이동금지</button>
        <button onclick="adminCommand.nextQuiz();">다음 문제</button>
    </div>
    <h3>Examples</h3>
    <a href='<spring:url value="/examples/chat" />'>Realtime Chat</a>
</body>
</html>