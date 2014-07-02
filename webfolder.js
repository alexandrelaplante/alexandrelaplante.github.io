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
        code();
    } else {
        loadScript(scripts[index], function(){
            load_recursive(index+1);
        });
    }
}
load_recursive(0);


function code() {

    $.get('/base.html').done(function(base){

        var converter = new Markdown.Converter();
        var title   = $('title').html();
        var content = converter.makeHtml($('content').html() );
        var context = {title: title, content: content};

        $('option').each(function() {
          context[$(this).html()] = 'true';
        });

        var template = Handlebars.compile(base);
        var html     = template(context);

        // classes on html head or body in base.html are ignored
        var body = html.split("<body")[1].split(">").slice(1).join(">").split("</body>")[0];
        var head = html.split("<head")[1].split(">").slice(1).join(">").split("</head>")[0];

        $('head').html(head);
        $('body').html(body);
    });
}