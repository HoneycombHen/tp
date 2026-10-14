package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INDEX_TOO_LARGE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagCommand;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_validArgs_returnsTagCommand() {
        TagCommand expectedCommand = new TagCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("friends"), new Tag("work")));

        assertParseSuccess(parser, "1 t/friends t/work", expectedCommand);
        assertParseSuccess(parser, "1 t/" + "a".repeat(20) + " t/c++",
                new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("a".repeat(20)), new Tag("c++"))));
    }

    @Test
    public void parse_duplicateTags_returnsTagCommandWithOneTag() {
        TagCommand expectedCommand = new TagCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java")));

        assertParseSuccess(parser, "1 t/java t/java", expectedCommand);
        assertParseSuccess(parser, "1 t/Java t/java", expectedCommand);
    }

    @Test
    public void parse_missingTags_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 work", String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTag_throwsTagConstraints() {
        assertParseFailure(parser, "1 t/" + "a".repeat(21), Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/foo bar", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/work!", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 t/", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_indexTooLarge_throwsParseException() {
        assertParseFailure(parser, "999999999999 t/x", MESSAGE_INDEX_TOO_LARGE);
    }

    @Test
    public void parse_invalidIndex_throwsInvalidPersonIndex() {
        assertParseFailure(parser, "0 t/x", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertParseFailure(parser, "-1 t/x", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertParseFailure(parser, "abc t/x", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
