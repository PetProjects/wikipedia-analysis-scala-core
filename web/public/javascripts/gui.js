$(document).ready(function(){
    $("#queryResults").hide();
    $(".input-group").hide();

    $("#queryBtn").on("click", function(){
        var $this = $(this);
        $this.button('loading');

        var queryStr = $("#queryInput").val().trim();
        if(queryStr.length == 0)
            alert('Please specify non-empty query, e.g. "50bc-50ad"');
        else {
            wikiq.queryTopTokens(queryStr,
                function(tokens){
                    if(tokens){
                        $("#queryResults").hide();
                        $("#queryResultsUl").empty();
                        for(var tKey in tokens) {
                            $( "<li>" + JSON.stringify(tokens[tKey]) + "</li>" ).appendTo( $("#queryResultsUl") );
                        }
                        $("#queryResults").show();
                    }
                    $this.button('reset');
                });
        }
    });

    $(".nav a").on("click", function(){
        $(".nav").find(".active").removeClass("active");
        $(this).parent().addClass("active");

        if($(this).parent().attr('id') == 'topTokens')
            $(".input-group").show()
        else
            $(".input-group").hide()
    });
});