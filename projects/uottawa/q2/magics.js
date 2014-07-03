var letters = {};
var firstpass = /(\d+)(\D)/g;
var secondpass = /\d+\D(\D+)$/;
var divshow = 0;
var dolist = false;
var wordList;			//1
var nounsList;			//2
var verbsList;			//3
var adjectivesList;		//4
var adverbsList;			//5
var conjunctivesList;		//6
var definitearticlesList;	//7
var eightList;			//8
var pronounsList;			//9
var elevenList;			//11
var wordPool;

function shufflepool() {
    mypool = document.getElementById('pool');
    char_arr = mypool.innerHTML.split('');
    char_arr.sort(function(){return Math.random()<0.5?-1:1});
    mypool.innerHTML = char_arr.join('');
}

function cloneObj(o) {
     if(typeof(o) != 'object') return o;
     if(o == null) return o;

     var newO = new Object();

     for(var i in o) newO[i] = cloneObj(o[i]);
      return newO;
 }

function getimage() {
    document.getElementById('thepic').src = document.getElementById('picsrc').value;
}

function updatepool(event) {
    var charup = getChar(event);
    if(charup > 13 && charup <= 40 && charup != 32) {
        //Don't update for control keys, except for spacebar.
        return;
    }

    var errors = {
        "notinlist":false,
        "doesntmatch":false,
        "badlength":false,
        "puncseq":false
    }
    
    var messages = {
        "notinlist":"Word is not in list!",
        "doesntmatch":"Sentence doesn't match the Anacryptogram!",
        "badlength":"Word is an invalid length (must be 1-8 or 11 letters)",
        "puncseq":"Punctuation is out of order"
    }
    
    var sentelm = document.getElementById('sentence');
    var dosuggest = document.getElementById('suggest').checked;

    sent = sentelm.value;
    //preparse sent
    sent = sent.replace(/<word>/g,'1');
    //alert(sent);

    //Check to see if they match the anacryptogram
    validity = checkValid(sent, letters, true);
    templet = validity['letters'];
    if(validity['fail']) { errors['doesntmatch'] = true; }

    if(charup==13) {
        sentelm.value = sentelm.value.replace(/[\n\r]+/, '');
        shufflepool();
        document.getElementById('suggestions').innerHTML = genSuggest(templet);
        return;
    }

    var suggestions = '';
    var badwords = '';
    var hints = '';
    if(dolist) {
        //If we're using the list, go ahead and check and generate suggestions
        badwords = checkList(sent, templet, errors);
        hints = checkHints(sent);
        if (badwords == ''){
           hints += "<br /> All words are valid: <span style=\"color:green;font-size:130%;\">PASS</span>";
        } else {
           hints += "<br /> All words are valid: <span style=\"color:red;font-size:130%;\">FAIL</span>";
        }
        if(dosuggest) {
		suggestions = '<table><tr>';
		var regex = /\b[A-Za-z1-9]{1,}\b/g;
		var number = 0;
        	while(match = regex.exec(sent)) { //for every word
			//alert(match[0]);
            	if(match[0] == '1' || match[0] == '2' || match[0] == '3' || match[0] == '4' || match[0] == '5' || match[0] == '6' || match[0] == '7' || match[0] == '8' || match[0] == '9' || match[0] == '10' || match[0] == '11') {
				var words;
				if (match[0] == '1'){
					words = genSuggest(templet, wordList);
				} else if ((match[0] == '2')) {
					words = genSuggest(templet, nounsList);
				} else if ((match[0] == '3')) {
					words = genSuggest(templet, verbsList);
				} else if ((match[0] == '4')) {
					words = genSuggest(templet, adjectivesList);
				}else if ((match[0] == '5')) {
					words = genSuggest(templet, adverbsList);
				}else if ((match[0] == '6')) {
					words = genSuggest(templet, conjunctivesList);
				}else if ((match[0] == '7')) {
					words = genSuggest(templet, definitearticlesList);
				}else if ((match[0] == '8')) {
					words = genSuggest(templet, eightList);
				}else if ((match[0] == '9')) {
					words = genSuggest(templet, pronounsList);
				}else if ((match[0] == '10')) {
					words = genSuggest(templet, wordList);
				}else if ((match[0] == '11')) {
					words = genSuggest(templet, elevenList);
				}
				var regex2 = /\b[A-Za-z]{1,}\b/g;
				suggestions += '<td style = "background-color: #FAFFFF;">';
        			while(match2 = regex2.exec(words)) { //for every suggested word
					suggestions += '<span onClick="clickWord(' + number+ ', \'' + match2 + '\')">' + match2 + '</span><br />';
				}
				suggestions += '</td>';
			} else {
				suggestions += '<td>' + match[0] + '</td>';
			}
			suggestions += '</td>';
			number += 1;
		}
	suggestions += '</tr></table>';
            //suggestions = genSuggest(templet);
        }
    } else {
        //Do a poor man's length check
        var regex = /\b[A-Za-z]{9,}\b/g;
        while(match = regex.exec(sent)) {
            if(match[0].length != 11) {
                badwords = badwords + match[0] + ' ';
                errors['badlength'] = true;
            }
        }
    }

    var succeeded = true;
    var pool = '';
    var missedpool = '';
    var list = '';
    for(i in templet) {
        var letcount = templet[i];

        if(letcount > 0) { classname = 'pos'; }
        else if(letcount < 0) { classname = 'neg'; }
        else { classname = 'zero'; }
        list += '<span class="letcount ' + classname + '">' + i + ': ' + letcount + '</span>';

        for(r=0;r<letcount;r++) {
            pool += i;
        }
        if(letcount < 0) {
            for(r=0;r<(-letcount);r++) {
                missedpool += i;
            }
        }
        if(letcount != 0) {
            succeeded = false;
        }
    }
    /* A hint is added here*/
    if (missedpool == ''){
           hints += "<br /> All letters are valid: <span style=\"color:green;font-size:130%;\">PASS</span>";
    } else {
           hints += "<br /> All letters are valid: <span style=\"color:red;font-size:130%;\">FAIL</span>";
    }
    var errlist = [];
    for(e in errors) {
        if(errors[e]) { errlist.push(messages[e]); }
    }
    if(errlist.length == 0) { 
        if(succeeded) { msg = '<span style="color:green;">Match found!</span>'; }
        else { msg = 'Keep going...'; } 
    }
    else { msg = '<span style="color:red;font-size:80%;">' + errlist.join('<br/>') + '</span>'; }
    document.getElementById('missedpool').innerHTML = missedpool;
    document.getElementById('pool').innerHTML = pool;
    document.getElementById('list').innerHTML = list;
    document.getElementById('hints').innerHTML = hints;
    document.getElementById('badwords').innerHTML = badwords;
    document.getElementById('suggestions').innerHTML = suggestions;
    document.getElementById('result').innerHTML = msg;
}

function checkValid(sent, letters, clone) {
    var returnval = {'letters':0,'fail':0}
    if(clone) { myletters = cloneObj(letters); }
    else { myletters = letters; }

    var failboat = false;
    for(i=0;i<sent.length;i++) {
        var curchar = sent.charAt(i);
        if(curchar != ' ') {
            if(myletters[curchar]) {
                myletters[curchar]--;
                if(myletters[curchar] < 0) { failboat = true; }
            } else {
                myletters[curchar] = -1;
                failboat = true;
            }
        }
    }

    returnval['letters'] = myletters;
    returnval['fail'] = failboat;
    return returnval;
}

function genletters() {
    letters = {};
    var input = document.getElementById('anacryptogram').value;
    while(match = firstpass.exec(input)) {
        letters[match[2]] = match[1];
    }
    if(match = secondpass.exec(input)) {
        var tail = match[1].split('');
        for(i=0;i<tail.length;i++) {
            if(letters[tail[i]]) {
                letters[tail[i]]++;
            } else {
                letters[tail[i]] = 1;
            }
        }

    }
    updatepool();
}

function getChar(e){
    var characterCode;

    try {
        if(e && e.which){
            e = e;
            characterCode = e.which; //character code is contained in NN4's which property
        } else {
            e = event;
            characterCode = e.keyCode; //character code is contained in IE's keyCode property
        }

        return characterCode;
    }
    catch(err) {
        return null;
    }
}

function toggleDiv(name, link) {
    var div = document.getElementById(name);
    var linky = document.getElementById(link);
    if(divshow) {
        div.style.display='none';
        linky.innerHTML = 'Show old updates'; 
        divshow=0;
    }
    else {
        div.style.display='';
        linky.innerHTML = 'Hide old updates'; 
        divshow=1;
    }
}

function toggleList(chk) {
    var div = document.getElementById('listopts');
    if(chk.checked) {
        div.style.display='';
    }
    else {
        div.style.display='none';
        dolist = false;
    }
}

function maybeSort(chk) {
    if(!chk.checked) {
        wordPool.sort();
    }
}
