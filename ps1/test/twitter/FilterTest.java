package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "I love programming", d3);
    
    @Test
    public void testWrittenBySingleUser() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertEquals("expected 2 tweets by alyssa", 2, writtenBy.size());
        assertTrue("expected tweet1 and tweet3", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
    }
    
    @Test
    public void testWrittenByNoTweetsByUser() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "charlie");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }
    
    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "ALYSSA");
        assertEquals("expected 2 tweets by alyssa", 2, writtenBy.size());
        assertTrue("expected tweet1 and tweet3", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
    }

    @Test
    public void testInTimespanAllTweets() {
        Timespan span = new Timespan(Instant.parse("2016-02-17T09:00:00Z"), Instant.parse("2016-02-17T12:30:00Z"));
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertEquals("expected all 3 tweets in timespan", 3, inTimespan.size());
    }

    @Test
    public void testInTimespanSomeTweets() {
        Timespan span = new Timespan(Instant.parse("2016-02-17T10:30:00Z"), Instant.parse("2016-02-17T11:30:00Z"));
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertEquals("expected 1 tweet in timespan", 1, inTimespan.size());
        assertTrue("expected tweet2", inTimespan.contains(tweet2));
    }

    @Test
    public void testInTimespanEdgeCases() {
        Timespan span = new Timespan(d1, d2);
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertEquals("expected 2 tweets in timespan", 2, inTimespan.size());
        assertTrue("expected tweet1 and tweet2", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testContainingSingleWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("talk"));
        assertEquals("expected 2 tweets containing 'talk'", 2, containing.size());
        assertTrue("expected tweet1 and tweet2", containing.containsAll(Arrays.asList(tweet1, tweet2)));
    }
    
    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("TALK"));
        assertEquals("expected 2 tweets containing 'TALK'", 2, containing.size());
        assertTrue("expected tweet1 and tweet2", containing.containsAll(Arrays.asList(tweet1, tweet2)));
    }
    
    @Test
    public void testContainingNoMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("python"));
        assertTrue("expected empty list", containing.isEmpty());
    }
}
