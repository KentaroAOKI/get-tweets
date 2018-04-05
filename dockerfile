FROM ubuntu:16.04

# example
# docker run -t twitterfilter java -Dfile.encoding=UTF-8 -cp ./:gnuprologjava-0.2.6.jar:twitter4j-core-4.0.4.jar:twitter4j-stream-4.0.4.jar getTweets.Filter -t "@ms_rinna" -f "3274075003" -g "132.2,29.9,146.2,39.0,138.4,33.5,146.1,46.20"

# Define working directory.
WORKDIR /data
COPY gettweetsfilter.sh /data
COPY twitter4j.properties /data
COPY Filter.java /data

# Install java application.
RUN buildDeps='curl unzip' \
  && apt-get update \
  && apt-get install -y openjdk-8-jdk $buildDeps \
  && mkdir work \
  && cd work \
  && curl -O http://twitter4j.org/archive/twitter4j-4.0.4.zip \
  && unzip -o twitter4j-4.0.4.zip \
  && cp ./lib/twitter4j-core-4.0.4.jar ../ \
  && cp ./lib/twitter4j-stream-4.0.4.jar ../ \
  && curl -O http://ftp.gnu.org/gnu/gnuprologjava/gnuprologjava-0.2.6.zip \
  && unzip -o gnuprologjava-0.2.6.zip \
  && cp gnuprologjava-0.2.6.jar ../ \
  && cd .. \
  && javac -cp ./:twitter4j-core-4.0.4.jar:twitter4j-stream-4.0.4.jar:gnuprologjava-0.2.6.jar -d ./ Filter.java \
  && sed 's/\r$//' gettweetsfilter.sh > _gettweetsfilter.sh \
  && chmod 755 _gettweetsfilter.sh \
  && rm -rf ./work \
  && rm -rf /var/lib/apt/lists/* \
  && apt-get purge -y --auto-remove $buildDeps

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

ENTRYPOINT ["/data/_gettweetsfilter.sh"]
