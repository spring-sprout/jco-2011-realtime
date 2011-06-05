jQuery(function($) {
  var xhr =  new XMLHttpRequest();
  var startIdx = 0;
  xhr.open('GET', 'chat', true);
  xhr.onreadystatechange = function(e) {
    console.log(xhr.readyState)
     if (xhr.readyState == 3 && xhr.status == 200) {
       console.log(e);
     }
  };
  xhr.onprogress = function(e){
      console.log(e.target.responseText.substring(startIdx,e.loaded));
      startIdx += (e.loaded-startIdx);
  };
  xhr.send(null);

  var sendMsg = function(who, msg) {
    if (msg) {
      $('div#chatWindow').append('<p> ' + who + ' &gt; ' + msg + '</p>').find(
          'p:last-child')[0].scrollIntoView(true);
    }
  };

  $('input.msg').keydown(function(e) {
    if (e.keyCode === 13 || e.ekyCode === 10) {
      sendMsg('me', $(this).val());
      $(this).val('');
    }
  }).focus();
});