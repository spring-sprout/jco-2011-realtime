<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Comet Chat with StreamHub</title>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.12/jquery-ui.min.js"></script>
	<script type="text/javascript" src='<spring:url value="/js/streamhub-min.js" />'></script>	
	<script type="text/javascript">
	    var chatRoomName = "ChatRoom";
	
	    function chatUpdated(topic, data) {
	        var chatMessagesBox = document.getElementById('chatMessages');
	        if (!!data.chat) {
	            chatMessagesBox.value += "\n" + data.user + ": " + data.chat;
	        } else if (!!data.notification) {
	            chatMessagesBox.value += "\n>>> " + data.notification;
	        }
	        chatMessagesBox.scrollTop = chatMessagesBox.scrollHeight;
	    }
	    
	    function chat() {
	        var message = document.getElementById('chatText').value;
	        var json = "{'chat':'" + escapeQuotes(message) + "'}";
	        hub.publish(chatRoomName, json);
	    }
	    
	    function changeUserName() {
	        var userName = document.getElementById('userName').value;
	        if (userName != null && userName.length > 0) {
	            var userNameJson = "{'user':'" + escapeQuotes(userName) + "'}";
	            hub.publish(chatRoomName, userNameJson);
	        }
	    }
	    
	    function escapeQuotes(sString) {
	        return sString.replace(/(\')/gi, "\\$1").replace(/(\\\\\')/gi, "\\'");
	    }
	    
	    function checkForEnter(e){
	        var e = e || event;
	        var key = e.keyCode || e.charCode;
	        
	        if(key == 13){
	            chat();
	        }
	        
	        return true;
	    }
	</script>
	<script type="text/javascript">
	    var hub = new StreamHub();
	    var sServerUrl = "http://192.168.1.7:7878/streamhub/";
	    hub.connect(sServerUrl);
	    hub.subscribe(chatRoomName, chatUpdated);
	</script>
</head>
<body>
<h1>Chat Demo</h1>
Pick a Username: 
<input type="text" id="userName">
<input type="button" value="Change" onclick="changeUserName()"><br />
<textarea id="chatMessages" rows="20" cols="60"></textarea><br />
Message: 
<input type="text" id="chatText" onkeypress="checkForEnter(event)">
<input type="button" value="Chat" onclick="chat()">
<p class="demo-description">This example uses pure Javascript and HTML.</p>	
</body>
</html>