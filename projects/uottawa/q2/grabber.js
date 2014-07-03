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

function loadList(whichone) {
    //alert('loadList '+ whichone);
    var statusspan = document.getElementById('status');
    if (whichone == 'joel'){
          //alert('word list should be initialized');
	    wordList = ajax.response.split("\n"); 
          wordList.sort();  //Sort for the binary search
    } else if (whichone == 'nouns') {
          nounsList = ajax.response.split("\n");
          nounsList.sort();  //Sort for the binary search
    } else if (whichone == 'verbs') {
          verbsList = ajax.response.split("\n");
          verbsList.sort();  //Sort for the binary search
    } else if (whichone == 'adjectives') {
          adjectivesList = ajax.response.split("\n");
          adjectivesList.sort();  //Sort for the binary search
    } else if (whichone == 'adverbs') {
          adverbsList = ajax.response.split("\n");
          adverbsList.sort();  //Sort for the binary search
    } else if (whichone == 'conjunctives') {
          conjunctivesList = ajax.response.split("\n");
          conjunctivesList.sort();  //Sort for the binary search
    } else if (whichone == 'definitearticles') {
          definitearticlesList = ajax.response.split("\n");
          definitearticlesList.sort();  //Sort for the binary search
    } else if (whichone == 'eight') {
          eightList = ajax.response.split("\n");
          eightList.sort();  //Sort for the binary search
    } else if (whichone == 'pronouns') {
          pronounsList = ajax.response.split("\n");
          pronounsList.sort();  //Sort for the binary search
    } else if (whichone == 'eleven') {
          elevenList = ajax.response.split("\n");
          elevenList.sort();  //Sort for the binary search
    }
    wordPool = wordList;

    dolist=true; 
    statusspan.innerHTML = ''; 
    updatepool(); //alapl added this
}

function getWordlist(whichone){
//alert('getWordlist ' + whichone);
    var statusspan = document.getElementById('status');
	ajax.requestFile =  whichone + '.txt';
	ajax.method = 'GET';
	ajax.onLoading = function() { statusspan.innerHTML = 'Loading '+ whichone + '...'; }
	ajax.onLoaded = function() {statusspan.innerHTML = whichone + ' loaded...'; }
	ajax.onCompletion = function() { loadList(whichone); };
	ajax.runAJAX();
}

