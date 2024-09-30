package twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> result = new ArrayList<>();
        
        for (Tweet tweet : tweets) {
            if (tweet.getAuthor().equalsIgnoreCase(username)) {
                result.add(tweet);
            }
        }
        
        return result;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> result = new ArrayList<>();
        
        for (Tweet tweet : tweets) {
            if (!tweet.getTimestamp().isBefore(timespan.getStart()) && 
                !tweet.getTimestamp().isAfter(timespan.getEnd())) {
                result.add(tweet);
            }
        }
        
        return result;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> result = new ArrayList<>();
        List<String> lowerCaseWords = new ArrayList<>();
        
      
        for (String word : words) {
            lowerCaseWords.add(word.toLowerCase(Locale.ROOT));
        }

       
        for (Tweet tweet : tweets) {
            String tweetText = tweet.getText().toLowerCase(Locale.ROOT);
            for (String word : lowerCaseWords) {
                if (tweetText.contains(word)) {
                    result.add(tweet);
                    break;
                }
            }
        }
        
        return result;
    }
}

