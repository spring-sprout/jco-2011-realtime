<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Comet Chat with StreamHub</title>
</head>
<body>
	<input type="text" id="chatText" onkeypress="checkForEnter(event)">
	<input type="button" value="Chat" onclick="chat()">
	
	<div id="chatMessages"></div>

	<script src="/jco.2011.realtime.oxquiz/js/streamhub-min.js" type="text/javascript"></script>
	<script type="text/javascript">
	function chatUpdated(topic, data) {
		var div = document.createElement("DIV");
		div.innerHTML = data.client + ": " + data.message;
		document.getElementById('chatMessages').appendChild(div);
	}
	
	function chat() {
	   var message = document.getElementById('chatText').value;
	   var json = "{'message':'" + escapeQuotes(message) + "'}";
	   hub.publish("chat", json);
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
	
	var hub;
	window.onload = function() {
		hub = new StreamHub();
		hub.connect("http://localhost:7878/streamhub/");
		hub.subscribe("chat", chatUpdated);
	}
	</script>
</body>
</html>