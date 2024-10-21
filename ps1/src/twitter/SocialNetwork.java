package twitter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        
        // Iterate through each tweet
        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
            Set<String> mentions = Extract.getMentionedUsers(List.of(tweet));
            
            // If the author is not yet in the graph, add them with an empty set
            followsGraph.putIfAbsent(author, new HashSet<>());
            
            // Add each mentioned user to the set of users the author follows
            for (String mention : mentions) {
                String normalizedMention = mention.toLowerCase();
                if (!normalizedMention.equals(author)) { // Avoid self-following
                    followsGraph.get(author).add(normalizedMention);
                }
            }
        }
        
        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();
        
        // Count followers for each user
        for (String user : followsGraph.keySet()) {
            // Get the set of users the current user follows
            for (String followed : followsGraph.get(user)) {
                followerCount.put(followed, followerCount.getOrDefault(followed, 0) + 1);
            }
        }
        
        // Create a list of users from the map
        List<String> influencers = new ArrayList<>(followerCount.keySet());
        
        // Sort the list based on the follower count in descending order
        influencers.sort((user1, user2) -> followerCount.get(user2) - followerCount.get(user1));
        
        return influencers;
    }
}

