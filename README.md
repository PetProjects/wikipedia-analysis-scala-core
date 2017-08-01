# Wikipedia time trending CLI tools

## A set of CLI tools written in scala for processing Wikipedia dump files.

**_WikiPrep_** is used for preliminary plain text extraction from Wikipedia dump files, consuming [Attardi](https://github.com/attardi/wikiextractor) git tool.

**_WikiIndex_** is used for dates extraction and building an index for quering based on it.

**_WikiQuery_** is used for various time-based queries to the Wikipedia corpus such as 
  - get the most trending topics for a time period since 1940 till 1950
  - get the most interesting articles on the VI century
  - get certain topic mentioning distribution accross AD era
