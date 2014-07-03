var comparator = function(a, b){ return (a < b) ? -1 : (a > b); }; 
Array.prototype.binarySearch = function binarySearch(find, comparator) {
    var low = 0, high = this.length - 1, i, comparison;
    while (low <= high) {
        i = parseInt((low + high) / 2, 10);
        comparison = comparator(this[i], find);
        if (comparison < 0) { low = i + 1; continue; };
        if (comparison > 0) { high = i - 1; continue; };
        return i;
    }
    return null;
}

Array.prototype.shuffle = function() {
    // n is the number of items remaining to be shuffled.
    for(n = this.length; n > 1; n--) {
        // Pick a random element to swap with the nth element.
        k = Math.floor(Math.random()*n);  // 0 <= k <= n-1 (0-based array)
        // Swap array elements.
        tmp = this[k];
        this[k] = this[n-1];
        this[n-1] = tmp;
    }
}

function genSuggest(usedletters, list) {
    var maxwords = document.getElementById('maxwords').value;
    var shuffle = document.getElementById('doshuffle').checked;
    
    if(shuffle) { wordPool.shuffle(); }
    
    count = 0;
    sugWords = '';
    for(curword=0;curword<list.length;curword++) {
        if(count > maxwords) { break; }
        /* start checking if a word should be in the list of available words */
        validity = checkValid(list[curword],usedletters,true);
        if(!validity['fail']) {
            count++;
            sugWords = sugWords + list[curword] + ' ';
        }
    }

    return sugWords;
}

function clickWord(num, word) {
	var sentelm = document.getElementById('sentence');
      var sent = sentelm.value;
	var newsent = '';
      var number = 0;
	var regex = /\b[A-Za-z1-9]+\b/g;
        	while(match = regex.exec(sent)) { //for every word
			if (number == num){
				newsent += word + " ";
			} else {
				//alert(match[0]);
				newsent += match[0] + " ";
			}
			number += 1;
		}
	//alert(newsent);
	sentelm.value = newsent;
	updatepool(this);

    //alert("word number " + num + " is " + word);
}

function checkHints(phrase) {
        var hints = '';
        var h11 = "11 letter words: <span style=\"color:red;font-size:130%;\">0</span>";
        var h8 = "8 letter words: <span style=\"color:red;font-size:130%;\">0</span>";
        var regex = /\b[A-Za-z]{1,}\b/g;
        var count11 = 0;
        var count8 = 0;
        var lastwordlength = 0;
        var sidebyside = false;
        while(match = regex.exec(phrase)) {
            if(match[0].length == 11) {
              count11++;
              if (lastwordlength == 8) { sidebyside = true; }
              //lastwordlength = '11';
              if (count11 == 1) {
                h11 = "11 letter words: <span style=\"color:green;font-size:130%;\"> " + count11 + "</span>";
              } else {
                h11 = "11 letter words: <span style=\"color:red;font-size:130%;\"> " + count11 + "</span>";
              }
            } else if(match[0].length == 8) {
              count8++;
              if (lastwordlength == 11) { sidebyside = true; }
              //lastwordlength = '8';
              h8 = "8 letter words: <span style=\"color:green;font-size:130%;\"> " + count8 + "</span>";
            } else {
              //lastwordlength = '0';
            }
            lastwordlength = match[0].length;
        }


        if (phrase.substring(0,2) == "I "){
            hints += "Starts with \"I\": <span style=\"color:green;font-size:130%;\">PASS</span>";
        } else {
            hints += "Starts with \"I\": <span style=\"color:red;font-size:130%;\">FAIL</span>";
        }

        if (phrase.indexOf("!!") != -1){
            hints += " <br /> Contains \"!!\": <span style=\"color:green;font-size:130%;\">PASS</span>";
        } else {
            hints += "<br /> Contains \"!!\": <span style=\"color:red;font-size:130%;\">FAIL</span>";
        }

        if ((phrase.indexOf("!") < phrase.indexOf(":"))
         || (phrase.indexOf("!") < phrase.indexOf(","))
         || (phrase.indexOf(",") < phrase.indexOf(":"))
                                                       ){
          hints += " <br /> :,!! in the right order: <span style=\"color:red;font-size:130%;\">FAIL</span>";
        } else {
          hints += " <br /> :,!! in the right order: <span style=\"color:green;font-size:130%;\">PASS</span>";
        }

        hints += "<br />" + h8 + "<br />" + h11;

        if (sidebyside) {
          hints += "<br />8 and 11 letter words adjacent: <span style=\"color:green;font-size:130%;\">PASS</span>";
        } else {
          hints += "<br />8 and 11 letter words adjacent: <span style=\"color:red;font-size:130%;\">FAIL</span>";
        }


        return hints;
}

function checkList(setn, usedletters, errors) {
    var regex = /(\b[A-Za-z]+\b)./g;  //The dot ignores the last word, cause they're still typing it
    var badwords = '';
    while(match = regex.exec(sent)) {
        if(match[1].length < 9 || match[1].length == 11) {
            if(checkValid(usedletters,match[1],true)) { 
                //It's a valid length and word, check The List
                if(wordList.binarySearch(match[1].toLowerCase(), comparator) == null) {
                    badwords = badwords + match[1] + ' ';
                    errors['notinlist'] = true;
                }
            } else {
                //If it's a valid length, but not word, say so
                badwords = badwords + match[1] + ' ';
                errors['doesntmatch'] = true;
            }
        } else {
            badwords = badwords + match[1] + ' ';
            errors['badlength'] = true;
        }
    }
    return badwords;
}
