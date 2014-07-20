/*
    See http://alexandre.ml/projects/webfolder/ for instructions
*/

var scripts = [
    "//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js",
    "//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Converter.js",
    "//cdnjs.cloudflare.com/ajax/libs/handlebars.js/2.0.0-alpha.4/handlebars.min.js"
];

function loadScript(url, callback)
{
    // Adding the script tag to the head as suggested before
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;

    // Then bind the event to the callback function.
    // There are several events for cross browser compatibility.
    script.onreadystatechange = callback;
    script.onload = callback;

    // Fire the loading
    head.appendChild(script);
}

function load_recursive(index){
    if (index >= scripts.length){
        start();
    } else {
        loadScript(scripts[index], function(){
            load_recursive(index+1);
        });
    }
}
load_recursive(0);



function display(base, root){

    var title   = root.find('title').html();
    var content = $.converter.makeHtml( root.find('content').html() );

    content = $("<div></div>").append(content);
    content.find('pre > code').each(function(){
        // Inside a <pre><code> we don't want to replace these with entities!
        $(this).text( $(this).text().replace(/&amp;/g, '&').replace(/&gt;/g, '>').replace(/&lt;/g, '<') );
    });
    content = content.html();

    var context = {title: title, content: content};

    root.find('option').each(function() {
      context[$(this).html()] = 'true';
    });

    var template = Handlebars.compile(base);
    var html     = template(context);

    // classes on html head or body in base.html are ignored
    var body = html.split("<body")[1].split(">").slice(1).join(">").split("</body>")[0];
    var head = html.split("<head")[1].split(">").slice(1).join(">").split("</head>")[0];

    $.ajaxSetup({cache: true});
    $('head').html(head);
    $.ajaxSetup({cache: true});
    $('body').html(body);

    // Speed things up by ajax getting all subsequent pages
    $('a').click(function(e){
        var url = $(this).attr('href');

        function ext(filename){
            var a = filename.split(".");
            if( a.length === 1 || ( a[0] === "" && a.length === 2 ) ) {
                return "";
            }
            return a.pop().toLowerCase();
        }

        if ((url.substring(0,4) == "http") || (ext(url) && !(ext(url) in ["htm", "html"])) ){
            // We are treating this as a regular link
            return true;
        } else {
            // We ajaxify this link for speed
            e.preventDefault();
            $.get(url).done(function(data, textStatus, jqXHR){
                // In Chrome this lets me handle redirects properly, not in Firefox though
                if (jqXHR.getResponseHeader("TM-finalURL")){
                    url = jqXHR.getResponseHeader("TM-finalURL");
                }
                root = $("<html></html>").append(data);
                not_webfolder = true;
                root.find('script').each(function(){
                    if (this.src.indexOf("webfolder.js") > -1){
                        not_webfolder = false;
                    }
                });
                if (not_webfolder){
                    //just follow the link, this page doesn't use webfolder.js
                    window.location.href = url;
                    return;
                }
                state = {base: base, data: data};
                window.history.pushState(state, root.find('title'), url);
                display(base, root);
            });
            return false;
        }
        return true; // if we get here for some reason there's a problem, just follow the link

    });

}

function start() {

    $.get('/base.html').done(function(base){
        $.converter = new Markdown.Converter();

        state = {base: base, data: $('html').html()};
        window.history.replaceState(state , $('html').find('title').html());
        display(base, $('html') );

        // handle the back and forward buttons
        $(window).bind('popstate', function(event) {
            // if the event has our history data on it, load the page fragment with AJAX
            var state = event.originalEvent.state;
            if (state) {
                display(state.base, $("<html></html>").append(state.data));
            }
        });
    });

}