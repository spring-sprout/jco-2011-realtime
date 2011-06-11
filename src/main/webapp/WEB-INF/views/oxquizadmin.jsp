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
    
</head>
<body>
    <h2>Realtime Web Application :: OXQuiz</h2>
    <div id="container">
    <header>
		<h2>Comet이라는 용어를 들어봤다.</h2> 
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
  <div>
  	<input type="button" id="closeBtn" value="답변 선택 마감하기"/>
  	<input type="button" id="nextQuestion" value="다음 문제로 이동하기"/>
  </div>
  
    <script src='<spring:url value="/js/jquery-1.5.2.js" />'></script>
    <script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>  
    <script type="text/javascript" src='<spring:url value="/js/oxquiz.js" />'></script>
    <script type="text/javascript">
        var streamHub = new StreamHub();
        
        jQuery.fn.center = function () {
            this.css("position","absolute");
            this.css("top", ( $(window).height() - this.height() ) / 2+$(window).scrollTop() + "px");
            this.css("left", ( $(window).width() - this.width() ) / 2+$(window).scrollLeft() + "px");
            return this;
        }
        
        var SS = {
           me: null,
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
		    	  var targetOffset;
		    	  var objOffset = obj.offset();
		    	  if ($.isEmptyObject(target.find('.circle'))) {
		    		  targetOffset = target.find('.circle').last().offset();
		    	  } else {
		    		  targetOffset = target.offset();
		    	  }
		    	  obj.animate({
	    		    left: targetOffset.left- obj.offset().left,
	    		    top: targetOffset.top - obj.offset().top
	    		  }, 500, function() {
	    		    obj.css({
	    		    	'left':0
	    		       ,'top':0
	    		    })
		       	    target.append(obj);
	    		  });
		    	  this.countWaiting();
		      }
		   },
		   notificate: function(timer, text, callback) {
			   if ($('#countdown').size() < 1) {
				   $(document.body).append('<div id="countdown"/>');
				   $('#countdown').center();
				   $('#countdown').css({'opacity': '0.8'});
			   }
			   if (text) {
				   $('#countdown').html('<div style="font-size:0.15em; margin-top:100px;">'+ text + '</div>');
			   } else {
				   $('#countdown').text(timer--);
			   }
			   if (timer > 0) {
				   setTimeout(function() {
					   SS.notificate(timer, null, callback);
				   }, 1000);
			   } else {
				   setTimeout(function() {
					   $('#countdown').remove();
					   callback();
				   }, 1000);
				   
			   }
		   },
		   notificateCloseQuiz: function() {
			   this.notificate(5, '5초후에 마감합니다.', SS.closeQuiz);
		   },
		   closeQuiz: function() {
			   $('#yes').unbind('click').css('cursor', 'auto');
			   $('#no').unbind('click').css('cursor', 'auto');
		   },
		   notificateNextQuiz: function(title) {
			   this.notificate(3, '다음 문제로 이동합니다.', function() {SS.nextQuestion(title);});
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
            		SS.notificateNextQuiz(notification.quiz);
            	}
            });            
            
            SS.initEventListener();
            
            $('#closeBtn').click(function() {
                streamHub.publish("adminCommand", "{command:currentQuizClose}");
            });
            $('#nextQuestion').click(function() {
                streamHub.publish("adminCommand", "{command:nextQuiz}");
            });
        });
    </script>
</body>
</html>