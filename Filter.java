package getTweets;

import java.util.ArrayList;
import java.util.Arrays;

// You can download jar file from http://twitter4j.org/archive/twitter4j-4.0.4.zip
// and use lib/twitter4j-core-4.0.4.jar, lib/twitter4j-stream-4.0.4.jar.
import twitter4j.*;

// You can download jar file from http://ftp.gnu.org/gnu/gnuprologjava/gnuprologjava-0.2.6.zip
// and use gnuprologjava-0.2.6.jar.
import gnu.getopt.Getopt;

// get tweets via the Filter realtime Tweets API.
// https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter
public class Filter {

    public static void main(String[] args) throws TwitterException {
    	// print usage.
        if (args.length < 1) {
            System.out.println("Usage: java getTweets.Filter [-t track messages] [-f follow numerical user ids] [-g locations] [-l languages]");
            System.out.println("Get tweets in realtime. The track, follow, and locations fields should be considered to be combined with an OR operator.");
            System.out.println("example: java getTwerts.Filter -t \"word1,word2\" -f \"1234,5678\" -g \"132.2,29.9,146.2,39.0,138.4,33.5,146.1,46.20\"");
            System.exit(-1);
        }

        // get arguments from command line.
        ArrayList<String> track = new ArrayList<String>();
        ArrayList<Long> follow = new ArrayList<Long>();
        ArrayList<double[]> location = new ArrayList<double[]>();
        ArrayList<String> language = new ArrayList<String>();
        
        Getopt options = new Getopt("GetTweets", args, "t:f:g:l:");
        int c;
        while ( (c = options.getopt()) != -1) {
            switch (c) {
            case 't':
            	// get track options
                track.addAll(Arrays.asList(options.getOptarg().split(",")));
                break;
            case 'f':
            	// get follow options
                for (String id : options.getOptarg().split(",")) {
                    follow.add(Long.parseLong(id));
                }
                break;
            case 'g':
            	// get location options
            	Integer tIndex = 0;
            	double[] tLocation = new double[2];
                for (String id : options.getOptarg().split(",")) {
                	tLocation[tIndex] = Double.parseDouble(id);
                	if (tIndex == 1) {
                		location.add(tLocation.clone());
                	}
                    tIndex = (tIndex + 1) & 1;
                }
                break;
            case 'l':
            	// get language options
            	language.addAll(Arrays.asList(options.getOptarg().split(",")));
                break;
            default:
                System.err.println("unknown arguments.");
            }
        }
        // build options for FilterQuery.
        String[] trackArray = track.toArray(new String[track.size()]);
        long[] followArray = new long[follow.size()];
        for (int i = 0; i < follow.size(); i++) {
            followArray[i] = follow.get(i);
        }
        double[][] locationArray = new double[location.size()][2];
        for (int i = 0; i < location.size(); i++) {
        	locationArray[i] = location.get(i);
        }
        String[] languageArray = language.toArray(new String[language.size()]);

        // create listener for twitter4j.
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + ", " + status.getText().replace('\n', ' '));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        // get tweets.
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);
        FilterQuery filterQuery = new FilterQuery(0, followArray, trackArray, locationArray, languageArray);
        twitterStream.filter(filterQuery);
    }

}
