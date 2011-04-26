<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Hello World Comet Application</title>
</head>
<body>
	<h1>Hello World Comet Application</h1>
	<input type="button" value="Start streaming" onclick="startStreaming()">
	<div id="streamingData"></div>
	<script src="/jco.2011.realtime.oxquiz/js/streamhub-min.js" type="text/javascript"></script>
	<script type="text/javascript">
	function topicUpdated(sTopic, oData) {
		var newDiv = document.createElement("DIV");
		newDiv.innerHTML = "Update for topic '" + sTopic + "' Response: '" + oData.Response + "'";
		document.getElementById('streamingData').appendChild(newDiv);
	}
	
	function startStreaming() {
		var hub = new StreamHub();
		hub.connect("http://localhost:8070/");
		hub.subscribe("HelloWorld", topicUpdated);
	}
	</script>
</body>
</html>