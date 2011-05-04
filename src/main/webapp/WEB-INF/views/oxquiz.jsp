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
    <link rel="stylesheet" type="text/css" href='<spring:url value="/css/jquery.gritter.css" />' />
    
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
    <script type="text/javascript" src='<spring:url value="/js/jquery.gritter.min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
    <script type="text/javascript"><!--
        var streamHub = new StreamHub(),
        
        var SS = {
           me: null,
           gritter: null,
           initEventListener: function() {
        	   $('#yes').click(function(e) {
	               	console.log('before publish yes');
	               	streamHub.publish('entryAnswerSubmitCommand', '{"answer":"yes"}');
               }).css('cursor', 'pointer');
               $('#no').click(function(e) {
	               	console.log('before publish no');
	               	streamHub.publish('entryAnswerSubmitCommand', '{"answer":"no"}');
               }).css('cursor', 'pointer');
           },
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
			   console.log('in selectAnswer' + id + ' / ' + to);
		      var obj = $('#circle-' + id);
		      var target = $('#' +to+ ' .respondents');
		      if(target.find('#circle-' + id).length < 1) {
		    	  target.append(obj);
		    	  this.countWaiting();
		      }
		   },
		   notificateCloseQuiz: function() {
			   gritter = $.gritter.add({
					title: 'oxquiz',
					text: '5초후에 마감합니다.'
				});
			   setTimeout(function() {
				   SS.closeQuiz();
			   }, 5000);
		   },
		   closeQuiz: function() {
			   $('#yes').unbind('click').css('cursor', 'auto');
			   $('#no').unbind('click').css('cursor', 'auto');
		   },
		   notificateNextQuiz: function(title) {
			   gritter = $.gritter.add({
					title: 'oxquiz',
					text: '다음 문제로 이동합니다.'
				});
			   setTimeout(function() {
				   SS.nextQuestion(title);
			   }, 5000);
		   },
		   nextQuestion: function(title) {
		      $('header h2').html(title);
		      $('.circle').appendTo('#waiting');
		      this.initEventListener();
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
            
        $(document).ready(function(){
            streamHub.connect("http://localhost:7878/streamhub/");
            
            // 알림 청취
            streamHub.subscribe("notification", function(topic, notification) {
            	//console.log(notification);
				
            	if(notification.state === 'entryAnswerSubmit') {
            		console.log('from noti: ' + notification.entryId + ' / ' + notification.answer);
            		SS.selectAnswer(notification.entryId, notification.answer);
            	} else if(notification.state === 'myEntryId') {
            		SS.me = notification.entryId;
            		console.log('me : ' + notification.entryId);
            	} else if(notification.state === 'currentQuizClose') {
            		SS.notificateCloseQuiz();
            	} else if(notification.state === 'nextQuiz') {
            		SS.notificateNextQuiz();
            	}
            });            
            
            // 참가
            streamHub.subscribe("entry", function(topic, message) {
            	if(message.state === 'entryIn') {
            		console.log(message);
            		if(SS.me == message.entryId) {
	            		SS.addNewUser(message.entryId, 'me');
            		} else {
	            		SS.addNewUser(message.entryId, 'others');
            		}
            	} else if(message.state === 'entryOut') {
            		SS.removeUser(message.entryId);
            	}
            });
            
            SS.initEventListener();
        });
    --></script>
</body>
</html>