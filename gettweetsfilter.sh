#!/bin/sh
java -Dfile.encoding=UTF-8 -cp ./:gnuprologjava-0.2.6.jar:twitter4j-core-4.0.4.jar:twitter4j-stream-4.0.4.jar getTweets.Filter $@
