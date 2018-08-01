system = require('system')
address = system.args[1];
var page = require('webpage').create();
var url = address;

page.onResourceReceived = function(response) {
    if (mItem = response.url.match(".*anti_content=(.*)&pdduid.*")) {
        console.log(mItem[1]);
    }
}

page.open(url, function (status) {
    var cookies = page.cookies;
    console.log('Listing cookies:');
    for(var i in cookies) {
        console.log(cookies[i].name + '=' + cookies[i].value);
    }
    //Page is loaded!
    if (status !== 'success') {
        console.log('Unable to post!');
    } else {
        window.setTimeout(function () {
            console.log(page.content);
            phantom.exit();
      }, 5000);
    }
  });



  
