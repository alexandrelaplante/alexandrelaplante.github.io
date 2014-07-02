/*
Usage

index.html:

    <!DOCTYPE html>
    <html>
    <title>Hello World</title>
    <content>

    # Hello World

    ## Heading 1

    Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore
    et dolore magna aliqua.

    ## Heading 2

    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
    aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
    cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in
    culpa qui officia deserunt mollit anim id est laborum.

    </content>
    <script src="webfolder.js"></script>
    </html>

base.html:

    <html>
        <head>
            <title>{{title}}</title>
        </head>
        <body>
            <p> Header to every page </p>
            {{{content}}}
            <p> Footer to every page </p>
        </body>
    </html>

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

    $.get('base.html').done(function(base){

        var content = $('content').html();
        var title   = $('title').html();
        var converter = new Markdown.Converter();
        content = converter.makeHtml(content);

        var template = Handlebars.compile(base);
        var context = {title: title, content: content}
        var html    = template(context);

        // classes on html head or body in base.html are ignored
        var body = html.split("<body")[1].split(">").slice(1).join(">").split("</body>")[0];
        var head = html.split("<head")[1].split(">").slice(1).join(">").split("</head>")[0];

        $('head').html(head);
        $('body').html(body);
    });
}