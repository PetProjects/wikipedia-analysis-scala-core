'use strict';

function Wikiq() {
    this.queryTopTokens = function(query, finishedCallback = function(a){}, debug = false) {
      if(! debug) {
        var queryUrl = '/v1/wikiq/tokens/' + query

        fetch(queryUrl)
          .then(function(response) {
            return response.json();
           })
          .then(function(topTokens) {
            finishedCallback(topTokens);
          })
          .catch( function(error){
            alert(error);
            finishedCallback(null);
          });

        }
        else {

          var fakeResult = [
            {token:"school",count:641942},
            {token:"state",count:298815},
            {token:"city",count:286115},
            {token:"district",count:259278},
            {token:"roman",count:238781},
            {token:"first",count:196863},
            {token:"war",count:192564},
            {token:"census",count:181558},
            {token:"pennsylvania",count:178716},
            {token:"population",count:159137}
          ];

          setTimeout(function(){ finishedCallback(fakeResult); }, 2500);
        }
    };
}

var wikiq = new Wikiq();
