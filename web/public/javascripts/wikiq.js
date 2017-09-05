'use strict';

function Wikiq() {
    this.queryTopTokens = function(query, finishedCallback = function(a){}) {
        var queryUrl = '/v1/wikiq/tokens/' + query

        fetch(queryUrl)
          .then(function(response) {
            //alert(response.headers.get('Content-Type')); // application/json; charset=utf-8
            //alert(response.status); // 200

            return response.json();
           })
          .then(function(topTokens) {
//            alert(JSON.stringify(topTokens));
            finishedCallback(topTokens);
          })
          .catch( function(error){
            alert(error);
            finishedCallback(null);
          });
    };
}

var wikiq = new Wikiq();
