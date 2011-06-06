jQuery(function($) {
  var name = prompt('What is your name?');
  if(!name || !$.trim(name)){
    name = 'What the hell?';
  }
  
  var uuid = name+(new Date().getTime()),
      sendMsg = function(msg) {
        if($.trim(msg)){
          $.post('sendMsg',{'name':name,'msg':msg},function(data){
            if(!data.isSuccess){
              alert('Message sending failed.');
            }
          },'json');
        }
      },
      updateChat = function(data){
        if(data.state !== 'wait'){
          $('#chatWindow').append('<p><b>'+data.name+' &gt </b>'+data.msg+'</p>');
          $('#chatWindow p:last-child')[0].scrollIntoView(true);
        }
      },
      
      chunkedReader = (function(){
        var xhr =  new XMLHttpRequest(),
            chunkIdx = 0,
            responseText = '',
            isDone = false;
        
        xhr.open('GET', 'chatListener?uuid='+uuid, true);
        
        xhr.onreadystatechange = function(e) {
          if ((xhr.readyState == 3 || xhr.readyState == 4) && xhr.status == 200 && !isDone) {
             responseText = xhr.responseText;
             responseText = responseText.substring(chunkIdx);
             chunkIdx += responseText.length;
             if(responseText === ''){
               isDone = true;
             }
             if(isJSONString(responseText)){
               updateChat($.parseJSON(responseText));
             }
           }
          if(isDone){
            xhr.abort();
          }
        };
        xhr.send(null);
      })(),
      
      isJSONString = function( text ){
        if(!text){
          return false;
        }
        return (/^[\],:{}\s]*$/).test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
                          .replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
                          .replace(/(?:^|:|,)(?:\s*\[)+/g, '')); 
      };
  
  $('input.msg').keydown(function(e) {
    if (e.keyCode === 13 || e.ekyCode === 10) {
      sendMsg($(this).val());
      $(this).val('');
    }
  }).focus();
  
  $('div.inAction > button').click(function(){
    sendMsg($(this).prev().val());
    $(this).prev().val("").focus();
  });
  
});