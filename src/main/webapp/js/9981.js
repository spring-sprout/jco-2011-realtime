jQuery(function($) {
  var jqxhr = $.ajax({
    url : "/jco.2011.realtime.oxquiz/chat",
    type : "get",
    dataType : "json",
//    dataFilter : function(data,type){
//      return '{\"data\":\"aaaddd\"}';
//    },
    success : function(data) {
      console.log(data);
      // sendMsg('yon',data.data);
    },
    error: function(xhr, errText, ex) {
      console.log(xhr);
      console.log(errText);
      console.log(ex);
      console.log('error');
    }
  });
  console.log(jqxhr);
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