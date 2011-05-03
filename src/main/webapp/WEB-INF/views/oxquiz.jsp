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
    <link rel="stylesheet" type="text/css" href='<spring:url value="/css/style.css?v=2" />' />
    
</head>
<body>
    <h2>Realtime Web Application :: OXQuiz</h2>
    <div id="container">
    <header>
		<h2>Comet이라는 용어는 Alex Russell이 만들었다.</h2> 
    </header>
    
    <div id="main">
		<div id="yes">
		   <div class="background">O</div>
		   <div class="respondents">
		   </div>
		</div>
		<div id="no">
		   <div class="background">X</div>
         <div class="respondents">
         </div>
		</div>
    </div>
    
    <footer>
       <div>답변 대기중인 사람들(<span id="watingNum">0</span>) : </div>
       <div id="waiting">
       		<c:forEach items="${entrys}" var="entry">
	            <div id="circle-${entry.uid}" class="circle others"></div>
	        </c:forEach>
	   </div>
    </footer>
  </div>
  
  
  
    <div style="margin-top:300px; clear:both;">
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
    
    <script src='<spring:url value="/js/jquery-1.5.2.js" />'></script>
    <script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
    <script type="text/javascript"><!--
        var streamHub = new StreamHub(),
            callBackFuns = {};
        
        var SS = {
           me: null,
           addNewUser: function(id, type) {
        	   $('<div/>').attr({
        		   'id':'circle-'+id,
        		   'class':'circle '+ type
        		}).appendTo('#waiting');
        	   this.countWaiting();
           },
           removeUser: function(id) {
        	 $('#circle-'+id).remove();
        	 this.countWaiting();
           },
           countWaiting: function() {
        	   $('#watingNum').html($('#waiting').children().length);
           },
		   selectAnswer: function(id, to) {
		      var obj = $('#circle-' + id);
		      var target = $('#' +to+ ' .respondents');
		      if(target.find('#circle-' + id).length < 1) {
		    	  target.append(obj);
		    	  streamHub.publish('notification', '{"answer":"' + to + '"}');
		    	  this.countWaiting();
		      }
		   },
		   
		   nextQuestion: function(title) {
		      $('header h2').html(title);
		      $('.circle').appendTo('#waiting');
		   }
		}

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
        	var hasMe = false;
            streamHub.connect("http://localhost:7878/streamhub/");
            
            // 알림 청취
            streamHub.subscribe("notification", function(topic, notification) {
            	console.log(notification);
				
            	if(message.state === 'entryAnswerSubmit') {
            		
            	}
            });            
            
            // 참가
            streamHub.subscribe("entry", function(topic, message) {
            	if(message.state === 'entryIn') {
            		if(hasMe) {
	            		SS.addNewUser(message.entryId, 'other');
            		} else {
            			SS.me = message.entryId;
	            		SS.addNewUser(message.entryId, 'me');
            		}
            	} else if(message.state === 'entryOut') {
            		SS.removeUser(message.entryId);
            	}
            });
            
            // event listener
            $('#yes').click(function() {
            	SS.selectAnswer(SS.me, 'yes');
            });
            $('#no').click(function() {
            	SS.selectAnswer(SS.me, 'no');
            });
        });
    --></script>
</body>
</html>