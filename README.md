# gettweets

Get tweets in realtime stream using twitter statuses/filter api.  
https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter  

## Installing gettweets.

```
git clone https://github.com/KentaroAOKI/gettweets.git
docker build -t gettweets gettweets
```

## Commands

The keyword to track is "hello". Keyword phrases are specified in a comma-separated list.
```
docker run -t gettweets -t hello
```
Specify the location and get tweets.
```
docker run -t gettweets -g "132.2,29.9,146.2,39.0,138.4,33.5,146.1,46.20"
```
