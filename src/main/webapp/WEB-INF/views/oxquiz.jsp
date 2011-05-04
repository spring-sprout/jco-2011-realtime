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
  
    <script src='<spring:url value="/js/jquery-1.5.2.js" />'></script>
    <script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/jquery.gritter.min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
    <script type="text/javascript">
        var streamHub = new StreamHub();
        
        var SS = {
           me: null,
           gritter: null,
           initEventListener: function() {
        	   $('#yes').click(function(e) {
	               	streamHub.publish('entryAnswerSubmitCommand', '{"answer":"yes"}');
               }).css('cursor', 'pointer');
               $('#no').click(function(e) {
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
		      var obj = $('#circle-' + id);
		      var target = $('#' +to+ ' .respondents');
		      if(target.find('#circle-' + id).length < 1) {
		    	  target.append(obj);
		    	  this.countWaiting();
		      }
		   },
		   notificate: function(text) {
			   this.gritter = $.gritter.add({
					title: 'oxquiz',
					text: text,
					time: 500
				});
		   },
		   notificateCloseQuiz: function() {
			   this.notificate('5초후에 마감합니다.');
			   setTimeout(function() {
				   SS.notificate('4');
			   }, 1000);
			   setTimeout(function() {
				   SS.notificate('3');
			   }, 2000);
			   setTimeout(function() {
				   SS.notificate('2');
			   }, 3000);
			   setTimeout(function() {
				   SS.notificate('1');
			   }, 4000);
			   setTimeout(function() {
				   SS.notificate('마감합니다.');
			   }, 5000);
			   setTimeout(function() {
				   SS.closeQuiz();
			   }, 5000);
		   },
		   closeQuiz: function() {
			   $('#yes').unbind('click').css('cursor', 'auto');
			   $('#no').unbind('click').css('cursor', 'auto');
		   },
		   notificateNextQuiz: function(title) {
			   this.notificate('다음 문제로 이동합니다.');
			   setTimeout(function() {
				   SS.notificate('3');
			   }, 1000);
			   setTimeout(function() {
				   SS.notificate('2');
			   }, 2000);
			   setTimeout(function() {
				   SS.notificate('1');
			   }, 3000);
			   setTimeout(function() {
				   SS.nextQuestion(title);
			   }, 4000);
		   },
		   nextQuestion: function(title) {
		      $('header h2').html(title);
		      $('.circle').appendTo('#waiting');
		      this.initEventListener();
		   }
		}

        $(document).ready(function(){
            streamHub.connect("http://localhost:7878/streamhub/");
            
            // 알림 청취
            streamHub.subscribe("notification", function(topic, notification) {
            	//console.log(notification);
				
            	if(notification.state === 'entryAnswerSubmit') {
            		SS.selectAnswer(notification.entryId, notification.answer);
            	} else if(notification.state === 'myEntryId') {
            		SS.me = notification.entryId;
            	} else if(notification.state === 'currentQuizClose') {
            		SS.notificateCloseQuiz();
            	} else if(notification.state === 'nextQuiz') {
            		SS.notificateNextQuiz();
            	}
            });            
            
            // 참가
            streamHub.subscribe("entry", function(topic, message) {
            	if(message.state === 'entryIn') {
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
    </script>
</body>
</html>