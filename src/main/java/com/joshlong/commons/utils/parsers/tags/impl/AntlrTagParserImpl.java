package com.joshlong.commons.utils.parsers.tags.impl;

import com.joshlong.commons.utils.parsers.tags.impl.antlr.TagsLexer;
import com.joshlong.commons.utils.parsers.tags.impl.antlr.TagsParser;
import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AntlrTagParserImpl implements ITagParser {

    private String normalizedTagString(String input) {
        input = input == null ? "" : input.trim();
        String[] ends = {"'", "\""};
        for (String endPointString : ends) {
            if (input.startsWith(endPointString) && input.endsWith(endPointString)) {
                // then we know its string that still has the surrounding ' .. ' and " .. "s to them
                if (input.length() > 1) { // afer all, "'" would match both conditions
                    String inputString = input.substring(1);
                    inputString = inputString.substring(0, inputString.length() - 1);
                    return inputString;
                }
            }
        }
        return input;
    }

    private List<String> extractTagsFromString(String in) throws Throwable {
        InputStream stringToInputStream = IOUtils.toInputStream(in);
        //test :: "cow 'cows' 'dogs and cat' \"monkeys and cats\" #cows ");
        Reader inputStreamReader = new InputStreamReader(stringToInputStream);
        TagsLexer tagsLexer = new TagsLexer(new ANTLRReaderStream(inputStreamReader));
        CommonTokenStream tokens = new CommonTokenStream(tagsLexer);
        TagsParser pa = new TagsParser(tokens);
        List<String> tags = pa.getParsedTags();
        IOUtils.closeQuietly(stringToInputStream);
        IOUtils.closeQuietly(inputStreamReader);
        return tags;
    }

    private List<String> normalizeListOfTags(List<String> input) {
        CollectionUtils.transform(input, new Transformer() {
            public Object transform(Object object) {
                String inputString = (String) object;
                return normalizedTagString(inputString);
            }
        });
        return input;
    }

    public Set<String> getUniqueNormalizedTagsFromString(String tags) {
        try {
            List<String> tagsStringList = normalizeListOfTags(extractTagsFromString(tags));
            Set<String> uniqueStrings = new HashSet<String>(tagsStringList);
            return uniqueStrings;
        }
        catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public boolean isHashTag(String input) {
        return normalizedTagString(input).startsWith("#");
    }

    public boolean isMultiTokenTag(String input) {
        return normalizedTagString(input).indexOf(" ") != -1;
    }

    public static void main(String[] a) {
        ITagParser parser = new AntlrTagParserImpl();
        for (String tag : parser.getUniqueNormalizedTagsFromString("cow #cow 'cows and dogs' \"cows and dogs\" dogs")) {
            System.out.println("tag=" + tag);
        }
        System.out.println("is hash tag: " + parser.isHashTag("#foo"));  // this is the kind of tag that 'twitter' uses 
        System.out.println("is multi token tag: " + parser.isMultiTokenTag("cow and dog"));
    }

}
