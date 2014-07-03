var ajax = new sack();

function whenCompleted(){
	var e = document.getElementById('sackdata'); 
	if (ajax.responseStatus){
		var string = "<p>Status Code: " + ajax.responseStatus[0] + "</p><p>Status Message: " + ajax.responseStatus[1] + "</p><p>URLString Sent: " + ajax.URLString + "</p>";
	} else {
		var string = "<p>URLString Sent: " + ajax.URLString + "</p>";
	}
	e.innerHTML = string;	
}

function loadList() {
    var statusspan = document.getElementById('status');
    wordList = ajax.response.split("\n"); 
    wordPool = wordList;
    wordList.sort();  //Sort for the binary search
    dolist=true; 
    statusspan.innerHTML = ''; 
    updatepool(); //alapl added this
}

function getWordlist(){
    var statusspan = document.getElementById('status');
	ajax.requestFile = document.getElementById('wordlist').value;
	ajax.method = 'GET';
	ajax.onLoading = function() { statusspan.innerHTML = 'Loading list...'; }
	ajax.onLoaded = function() {statusspan.innerHTML = 'List loaded...'; }
	ajax.onCompletion = loadList;
	ajax.runAJAX();
}

