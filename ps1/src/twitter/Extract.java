package twitter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Extract {

    
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            throw new IllegalArgumentException("Tweet list cannot be empty.");
        }

        Instant minTimestamp = tweets.get(0).getTimestamp();
        Instant maxTimestamp = tweets.get(0).getTimestamp();

       
        for (Tweet tweet : tweets) {
            if (tweet.getTimestamp().isBefore(minTimestamp)) {
                minTimestamp = tweet.getTimestamp();
            }
            if (tweet.getTimestamp().isAfter(maxTimestamp)) {
                maxTimestamp = tweet.getTimestamp();
            }
        }

       
        return new Timespan(minTimestamp, maxTimestamp);
    }

    
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();
        Pattern mentionPattern = Pattern.compile("(?<!\\w)@([A-Za-z0-9_]+)(?!\\w)");

        for (Tweet tweet : tweets) {
            Matcher matcher = mentionPattern.matcher(tweet.getText());
            while (matcher.find()) {
                mentionedUsers.add(matcher.group(1).toLowerCase());
            }
        }

        return mentionedUsers;
    }
}
