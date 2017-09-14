$(document).ready(function(){
    $(".tabDiv").hide();
    $(".input-group").hide();
    $("#homeDiv").show();

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
                        $("#topTokensDiv").hide();
                        $("#queryResultsTable > tbody").empty();

                        for(var tKey in tokens) {
                            $( "<tr><td>" + tokens[tKey].token + "</td><td>" + tokens[tKey].count + "</td></tr>" ).appendTo( $("#queryResultsTable > tbody") );
                        }
                        $("#topTokensDiv").show();
                    }
                    $this.button('reset');
                }, false /*debug*/ );
        }
    });

    $(".nav a").on("click", function(){
        $(".nav").find(".active").removeClass("active");
        $(this).parent().addClass("active");
        $(".tabDiv").hide();


        var tabId = $(this).parent().attr('id');
        if(tabId == 'topTokens') {
            $(".input-group").show();
            $("#queryResultsDiv").show();
        }
        else {
            $(".input-group").hide();

            if(tabId == "home")
                $("#homeDiv").show();
            else
                $("#topArticlesDiv").show();
        }
    });
});