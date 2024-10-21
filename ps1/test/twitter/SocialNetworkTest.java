package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * Partitions:
     * - `guessFollowsGraph`:
     *   - Empty list of tweets
     *   - List of tweets with no mentions
     *   - Single tweet with one mention
     *   - Single tweet with multiple mentions
     *   - Multiple tweets from the same author
     *   - Multiple tweets from different authors
     * 
     * - `influencers`:
     *   - Empty graph
     *   - Graph with a single user having no followers
     *   - Graph with multiple users having varying numbers of followers
     *   - Graph with users having the same number of followers
     */

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // guessFollowsGraph Tests
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "I love programming!", 1000)
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph as there are no mentions", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "@bob how are you?", 1000)
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("graph should contain one user following another", followsGraph.containsKey("alice"));
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
    }
    
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "@bob @charlie let's meet!", 1000)
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("graph should contain one user following multiple others", followsGraph.containsKey("alice"));
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("alice should follow charlie", followsGraph.get("alice").contains("charlie"));
    }
    
    @Test
    public void testGuessFollowsGraphMultipleTweetsSameAuthor() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "@bob hey!", 1000),
            new Tweet(2, "alice", "@charlie how are you?", 1001)
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("graph should contain alice following multiple others", followsGraph.containsKey("alice"));
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("alice should follow charlie", followsGraph.get("alice").contains("charlie"));
    }
    
    @Test
    public void testGuessFollowsGraphMultipleTweetsDifferentAuthors() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alice", "@bob hey!", 1000),
            new Tweet(2, "bob", "@charlie how are you?", 1001)
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("graph should contain alice and bob as keys", followsGraph.containsKey("alice"));
        assertTrue("graph should contain bob as key", followsGraph.containsKey("bob"));
        assertTrue("alice should follow bob", followsGraph.get("alice").contains("bob"));
        assertTrue("bob should follow charlie", followsGraph.get("bob").contains("charlie"));
    }

    // influencers Tests

    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list since no one has followers", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersSingleUserWithFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("expected bob to be the only influencer", Arrays.asList("bob"), influencers);
    }

    @Test
    public void testInfluencersMultipleUsers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob", "charlie")));
        followsGraph.put("bob", new HashSet<>(Arrays.asList("charlie")));
        followsGraph.put("charlie", new HashSet<>());

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("charlie should be the top influencer", "charlie", influencers.get(0));
        assertEquals("bob should be the next influencer", "bob", influencers.get(1));
    }
    
    @Test
    public void testInfluencersTie() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        followsGraph.put("charlie", new HashSet<>(Arrays.asList("bob")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("bob should be the influencer, regardless of order", influencers.contains("bob"));
    }
}

