package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class ExtractTest {

    @Test
    public void testGetTimespanMultipleTweets() {
        
        Tweet tweet1 = new Tweet(1, "user1", "Tweet 1", Instant.parse("2023-09-01T10:00:00Z"));
        Tweet tweet2 = new Tweet(2, "user2", "Tweet 2", Instant.parse("2023-09-01T11:00:00Z"));
        Tweet tweet3 = new Tweet(3, "user3", "Tweet 3", Instant.parse("2023-09-01T09:00:00Z"));
        
       
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        
        
        Timespan timespan = Extract.getTimespan(tweets);
        
        assertEquals(Instant.parse("2023-09-01T09:00:00Z"), timespan.getStart());
        assertEquals(Instant.parse("2023-09-01T11:00:00Z"), timespan.getEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTimespanEmptyList() {
       
        List<Tweet> tweets = Arrays.asList();
        Extract.getTimespan(tweets);
    }

    @Test
    public void testGetMentionedUsersMultipleTweets() {
        
        Tweet tweet1 = new Tweet(1, "user1", "Hello @Alice", Instant.now());
        Tweet tweet2 = new Tweet(2, "user2", "Hi @Bob", Instant.now());
        Tweet tweet3 = new Tweet(3, "user3", "No mention here", Instant.now());
        
        
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);
        
        
        Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);
        
       
        Set<String> expected = new HashSet<>(Arrays.asList("alice", "bob"));
        
        
        assertEquals(expected, mentionedUsers);
    }

    @Test
    public void testGetMentionedUsersWithNoMentions() {
       
        Tweet tweet1 = new Tweet(1, "user1", "Just a regular tweet", Instant.now());
        
       
        List<Tweet> tweets = Arrays.asList(tweet1);
        
       
    }

    @Test
    public void testGetMentionedUsersIgnoresEmail() {
        
        Tweet tweet1 = new Tweet(1, "user1", "Contact me at example@domain.com", Instant.now());
       
        List<Tweet> tweets = Arrays.asList(tweet1);
        
        
        Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);
        
        
        assertTrue(mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersWithComplexText() {
        
        Tweet tweet1 = new Tweet(1, "user1", "Hello @Charlie! How's @Dave? Email me: john@doe.com", Instant.now());
        
       
        List<Tweet> tweets = Arrays.asList(tweet1);
        
        
        Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);
        
        Set<String> expected = new HashSet<>(Arrays.asList("charlie", "dave"));
        
     
        assertEquals(expected, mentionedUsers);
    }
}