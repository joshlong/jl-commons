package com.joshlong.commons.utils.parsers.tags.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;

import org.jmock.lib.legacy.ClassImposteriser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * AntlrTagParserImpl Tester.
 *
 * @author <a href="mailto:josh@joshlong.com">Josh Long</a>
 */
public class AntlrTagParserImplTest {
    private Mockery context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

    private AntlrTagParserImpl antlrTagParser;

    @Before
    public void setUp() throws Exception {
        this.antlrTagParser = new AntlrTagParserImpl();
    }

    @After
    public void tearDown() throws Exception {
        this.antlrTagParser = null;
    }

    /**
     * Method: getUniqueNormalizedTagsFromString(String tags)
     */
    @Test
    public void testGetUniqueNormalizedTagsFromStringWhileThrowingException()
        throws Exception {
        Set<String> result = this.antlrTagParser.getUniqueNormalizedTagsFromString("cat");
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() > 0);
        Assert.assertEquals(result.iterator().next(), "cat");
    }

    /**
     * Method: getUniqueNormalizedTagsFromString(String tags)
     */
    @Test
    public void testGetUniqueNormalizedTagsFromString()
        throws Exception {
        Set<String> result = this.antlrTagParser.getUniqueNormalizedTagsFromString("cat");
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.size() > 0);
        Assert.assertEquals(result.iterator().next(), "cat");
    }

    /**
     * Method: isHashTag(String input)
     */
    @Test
    public void testIsHashTag() throws Exception {
        Assert.assertTrue(this.antlrTagParser.isHashTag("#foo"));
        Assert.assertFalse(this.antlrTagParser.isHashTag("foo"));
    }

    /**
     * Method: isMultiTokenTag(String input)
     */
    @Test
    public void testIsMultiTokenTag() throws Exception {
        Assert.assertTrue(this.antlrTagParser.isMultiTokenTag("dog cat"));
        Assert.assertFalse(this.antlrTagParser.isMultiTokenTag("cat"));
    }

    /**
     * Method: normalizedTagString(String input)
     */
    @Test
    public void testNormalizedTagString() throws Exception {
        Assert.assertTrue(this.antlrTagParser.normalizedTagString("  'cat'  ").equals("cat"));
        Assert.assertEquals(this.antlrTagParser.normalizedTagString(""), "");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString(null), "");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString(null), "");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("aa"), "aa");
    }

    @Test
    public void testNormalizedListOfTags() throws Throwable {
        List<String> inpps = Arrays.asList("a", "b");
        List<String> results = this.antlrTagParser.normalizeListOfTags(inpps);
        Assert.assertTrue(results.containsAll(inpps));

        List<String> r2 = Arrays.asList("'a' ", " 'b' ");

        Assert.assertTrue(this.antlrTagParser.normalizeListOfTags(r2).containsAll(inpps));
    }

    /**
     * Method: normalizeListOfTags(List<String> input)
     */
    @Test(expected = RuntimeException.class)
    public void testNormalizeListOfTagsAndThrowNPE() throws Exception {
        this.antlrTagParser.getUniqueNormalizedTagsFromString(null);
    }

    @Test
    public void testNormalizedTagStringWithQuotes() throws Throwable {
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("''"), "");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("\"\""), "");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("'deer'"), "deer");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("\"deer\""), "deer");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("'"), "'");
        Assert.assertEquals(this.antlrTagParser.normalizedTagString("'cow"), "'cow");
    }

    /**
     * Method: normalizeListOfTags(List<String> input)
     */
    @Test
    public void testNormalizeListOfTags() throws Exception {
        List<String> listOftags = Arrays.asList("cat", "dog", "'cat'", "#cats", "#dog");
        List<String> normalizedListOfTags = this.antlrTagParser.normalizeListOfTags(listOftags);
        Assert.assertNotNull(normalizedListOfTags);
        Assert.assertTrue(normalizedListOfTags.size() == listOftags.size());
    }
}
