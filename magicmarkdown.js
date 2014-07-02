var base = '';

$.get('base.html').done(function(data){
    base = data;
});

var content = $('content').html();
var title   = $('title').html();

$.getScript( "handlebars-v1.3.0.js", function( data, textStatus, jqxhr ) {
    var source   = base;
    var template = Handlebars.compile(base);
    var context = {title: title, content: content}
    var html    = template(context);

    // classes on html head or body in base.html are ignored
    var body = html.split("<body")[1].split(">").slice(1).join(">").split("</body>")[0];
    var head = html.split("<head")[1].split(">").slice(1).join(">").split("</head>")[0];

    $('body').html(body);
    $('head').html(head);

});