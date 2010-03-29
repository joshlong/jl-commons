package com.joshlong.commons.utils.parsers.tags.impl;

import java.util.Set;

public interface ITagParser {

    Set<String> getUniqueNormalizedTagsFromString(String tags);

    boolean isHashTag(String input);

    boolean isMultiTokenTag(String input);

}
