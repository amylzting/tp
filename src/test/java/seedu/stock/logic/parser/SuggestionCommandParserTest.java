package seedu.stock.logic.parser;

import static seedu.stock.commons.core.Messages.MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT;
import static seedu.stock.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.stock.logic.commands.CommandTestUtil.FILE_NAME_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.INVALID_LIST_TYPE_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.INVALID_STATISTICS_TYPE_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.LOCATION_DESC_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.LOCATION_DESC_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.LOW_QUANTITY_DESC_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.LOW_QUANTITY_DESC_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.NAME_DESC_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.NAME_DESC_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.NOTE_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.NOTE_INDEX_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.QUANTITY_DESC_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.QUANTITY_DESC_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.SERIAL_NUMBER_DESC_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.SERIAL_NUMBER_DESC_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.SORT_FIELD_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.SORT_ORDER_DESCENDING_DESC;
import static seedu.stock.logic.commands.CommandTestUtil.SOURCE_DESC_APPLE;
import static seedu.stock.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.stock.logic.parser.SuggestionCommandParser.MESSAGE_SUGGESTION;

import org.junit.jupiter.api.Test;

import seedu.stock.logic.commands.AddCommand;
import seedu.stock.logic.commands.BookmarkCommand;
import seedu.stock.logic.commands.ClearCommand;
import seedu.stock.logic.commands.CommandWords;
import seedu.stock.logic.commands.DeleteCommand;
import seedu.stock.logic.commands.FindCommand;
import seedu.stock.logic.commands.FindExactCommand;
import seedu.stock.logic.commands.HelpCommand;
import seedu.stock.logic.commands.ListCommand;
import seedu.stock.logic.commands.NoteCommand;
import seedu.stock.logic.commands.NoteDeleteCommand;
import seedu.stock.logic.commands.NoteViewCommand;
import seedu.stock.logic.commands.PrintCommand;
import seedu.stock.logic.commands.SortCommand;
import seedu.stock.logic.commands.StatisticsCommand;
import seedu.stock.logic.commands.SuggestionCommand;
import seedu.stock.logic.commands.TabCommand;
import seedu.stock.logic.commands.UnbookmarkCommand;
import seedu.stock.logic.commands.UpdateCommand;

public class SuggestionCommandParserTest {
    @Test
    public void parse_exitCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = "";
        SuggestionCommandParser parser = new SuggestionCommandParser("exi");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.EXIT_COMMAND_WORD;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = LOCATION_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = "";
        parser = new SuggestionCommandParser("exit", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.EXIT_COMMAND_WORD;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = LOCATION_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = "";
        SuggestionCommandParser parser = new SuggestionCommandParser("clea");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.CLEAR_COMMAND_WORD + "\n" + ClearCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = "";
        parser = new SuggestionCommandParser("clear", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.CLEAR_COMMAND_WORD + "\n" + ClearCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = SOURCE_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tabCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = "";
        SuggestionCommandParser parser = new SuggestionCommandParser("ta");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.TAB_COMMAND_WORD + "\n" + TabCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = SOURCE_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = "";
        parser = new SuggestionCommandParser("tab", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.TAB_COMMAND_WORD + "\n" + TabCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = LOCATION_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_helpCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = "";
        SuggestionCommandParser parser = new SuggestionCommandParser("hel");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.HELP_COMMAND_WORD + "\n" + HelpCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = SOURCE_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = "";
        parser = new SuggestionCommandParser("help", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.HELP_COMMAND_WORD + "\n" + HelpCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = QUANTITY_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_listCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = INVALID_LIST_TYPE_DESC;
        SuggestionCommandParser parser = new SuggestionCommandParser("lis");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.LIST_COMMAND_WORD + " lt/all"
                + "\n" + ListCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = INVALID_LIST_TYPE_DESC + LOW_QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = INVALID_LIST_TYPE_DESC;
        parser = new SuggestionCommandParser("list", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.LIST_COMMAND_WORD + " lt/all" + "\n" + ListCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = INVALID_LIST_TYPE_DESC + NAME_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_addCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = NAME_DESC_APPLE + SOURCE_DESC_APPLE + QUANTITY_DESC_APPLE + LOCATION_DESC_APPLE
                + LOW_QUANTITY_DESC_APPLE;
        SuggestionCommandParser parser = new SuggestionCommandParser("ad");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.ADD_COMMAND_WORD + userInput + "\n" + AddCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = NAME_DESC_APPLE + SOURCE_DESC_APPLE + QUANTITY_DESC_APPLE + LOCATION_DESC_APPLE
                + LOW_QUANTITY_DESC_APPLE;
        parser = new SuggestionCommandParser("add", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.ADD_COMMAND_WORD + userInput + "\n" + AddCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_deleteCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes (single)
        String userInput = SERIAL_NUMBER_DESC_APPLE;
        SuggestionCommandParser parser = new SuggestionCommandParser("delet");
        String expectedSuggestionMessage = MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT + "\n"
                + MESSAGE_SUGGESTION + CommandWords.DELETE_COMMAND_WORD + userInput
                + "\n" + DeleteCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_BANANA;
        expectedSuggestionMessage = MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT + "\n"
                + MESSAGE_SUGGESTION + CommandWords.DELETE_COMMAND_WORD + SERIAL_NUMBER_DESC_APPLE
                + SERIAL_NUMBER_DESC_BANANA + "\n" + DeleteCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (single)
        userInput = SERIAL_NUMBER_DESC_APPLE;
        parser = new SuggestionCommandParser("delete", "error message");
        expectedSuggestionMessage = MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT + "\n"
                + MESSAGE_SUGGESTION + CommandWords.DELETE_COMMAND_WORD + userInput
                + "\n" + DeleteCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_BANANA;
        expectedSuggestionMessage = MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT + "\n"
                + MESSAGE_SUGGESTION + CommandWords.DELETE_COMMAND_WORD + SERIAL_NUMBER_DESC_APPLE
                + SERIAL_NUMBER_DESC_BANANA + "\n" + DeleteCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_statisticsCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = INVALID_STATISTICS_TYPE_DESC;
        SuggestionCommandParser parser = new SuggestionCommandParser("stat");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.STATISTICS_COMMAND_WORD + " st/source"
                + "\n" + StatisticsCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + LOW_QUANTITY_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = INVALID_STATISTICS_TYPE_DESC;
        parser = new SuggestionCommandParser("stats", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.STATISTICS_COMMAND_WORD + " st/source"
                + "\n" + StatisticsCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + LOW_QUANTITY_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_findCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = NAME_DESC_APPLE;
        SuggestionCommandParser parser = new SuggestionCommandParser("fin");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.FIND_COMMAND_WORD + userInput
                + "\n" + FindCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = NAME_DESC_APPLE;
        parser = new SuggestionCommandParser("find", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.FIND_COMMAND_WORD + userInput
                + "\n" + FindCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_updateCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes (single)
        String userInput = SERIAL_NUMBER_DESC_APPLE;
        SuggestionCommandParser parser = new SuggestionCommandParser("upda");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UPDATE_COMMAND_WORD + userInput
                + "\n" + UpdateCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_BANANA;
        expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UPDATE_COMMAND_WORD + SERIAL_NUMBER_DESC_APPLE
                + SERIAL_NUMBER_DESC_BANANA + "\n" + UpdateCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (single)
        userInput = SERIAL_NUMBER_DESC_APPLE;
        parser = new SuggestionCommandParser("update", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UPDATE_COMMAND_WORD + userInput
                + "\n" + UpdateCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_BANANA;
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UPDATE_COMMAND_WORD + SERIAL_NUMBER_DESC_APPLE
                + SERIAL_NUMBER_DESC_BANANA + "\n" + UpdateCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_findExactCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = SERIAL_NUMBER_DESC_APPLE;
        SuggestionCommandParser parser = new SuggestionCommandParser("findexac");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.FIND_EXACT_COMMAND_WORD + userInput
                + "\n" + FindExactCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + INVALID_LIST_TYPE_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = SERIAL_NUMBER_DESC_APPLE;
        parser = new SuggestionCommandParser("findexact", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.FIND_EXACT_COMMAND_WORD + userInput
                + "\n" + FindExactCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + INVALID_LIST_TYPE_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_sortCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = SORT_ORDER_DESCENDING_DESC + SORT_FIELD_DESC;
        SuggestionCommandParser parser = new SuggestionCommandParser("sor");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.SORT_COMMAND_WORD + userInput
                + "\n" + SortCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = SORT_ORDER_DESCENDING_DESC + SORT_FIELD_DESC;
        parser = new SuggestionCommandParser("sort", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.SORT_COMMAND_WORD + userInput
                + "\n" + SortCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noteCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = SERIAL_NUMBER_DESC_APPLE + NOTE_DESC;
        SuggestionCommandParser parser = new SuggestionCommandParser("not");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.NOTE_COMMAND_WORD + userInput
                + "\n" + NoteCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + SOURCE_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = SERIAL_NUMBER_DESC_APPLE + NOTE_DESC;
        parser = new SuggestionCommandParser("note", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.NOTE_COMMAND_WORD + userInput
                + "\n" + NoteCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + LOCATION_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noteDeleteCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = SERIAL_NUMBER_DESC_APPLE + NOTE_INDEX_DESC;
        SuggestionCommandParser parser = new SuggestionCommandParser("notedele");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.NOTE_DELETE_COMMAND_WORD + userInput
                + "\n" + NoteDeleteCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + QUANTITY_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = SERIAL_NUMBER_DESC_APPLE + NOTE_INDEX_DESC;
        parser = new SuggestionCommandParser("notedelete", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.NOTE_DELETE_COMMAND_WORD + userInput
                + "\n" + NoteDeleteCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + NAME_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noteViewCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = SERIAL_NUMBER_DESC_APPLE;
        SuggestionCommandParser parser = new SuggestionCommandParser("notevie");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.NOTE_VIEW_COMMAND_WORD + userInput
                + "\n" + NoteViewCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + LOCATION_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = SERIAL_NUMBER_DESC_APPLE;
        parser = new SuggestionCommandParser("noteview", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.NOTE_VIEW_COMMAND_WORD + userInput
                + "\n" + NoteViewCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + NAME_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_printCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes
        String userInput = FILE_NAME_DESC;
        SuggestionCommandParser parser = new SuggestionCommandParser("prin");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.PRINT_COMMAND_WORD + userInput
                + "\n" + PrintCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + LOW_QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes
        userInput = FILE_NAME_DESC;
        parser = new SuggestionCommandParser("print", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.PRINT_COMMAND_WORD + userInput
                + "\n" + PrintCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + LOCATION_DESC_BANANA;
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_bookmarkCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes (single)
        String userInput = SERIAL_NUMBER_DESC_BANANA;
        SuggestionCommandParser parser = new SuggestionCommandParser("bookma");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.BOOKMARK_COMMAND_WORD + userInput
                + "\n" + BookmarkCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + LOW_QUANTITY_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_APPLE;
        expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.BOOKMARK_COMMAND_WORD + SERIAL_NUMBER_DESC_BANANA
                + SERIAL_NUMBER_DESC_APPLE + "\n" + BookmarkCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (single)
        userInput = SERIAL_NUMBER_DESC_BANANA;
        parser = new SuggestionCommandParser("bookmark", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.BOOKMARK_COMMAND_WORD + userInput
                + "\n" + BookmarkCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_APPLE;
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.BOOKMARK_COMMAND_WORD + SERIAL_NUMBER_DESC_BANANA
                + SERIAL_NUMBER_DESC_APPLE + "\n" + BookmarkCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_unbookmarkCommandSuggestion_success() {
        // EP: incorrect command word with valid prefixes (single)
        String userInput = SERIAL_NUMBER_DESC_BANANA;
        SuggestionCommandParser parser = new SuggestionCommandParser("unbookma");
        String expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UNBOOKMARK_COMMAND_WORD + userInput
                + "\n" + UnbookmarkCommand.MESSAGE_USAGE;
        SuggestionCommand expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with invalid prefixes
        userInput = userInput + NAME_DESC_APPLE;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: incorrect command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_APPLE;
        expectedSuggestionMessage = MESSAGE_UNKNOWN_COMMAND + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UNBOOKMARK_COMMAND_WORD + SERIAL_NUMBER_DESC_BANANA
                + SERIAL_NUMBER_DESC_APPLE + "\n" + UnbookmarkCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (single)
        userInput = SERIAL_NUMBER_DESC_BANANA;
        parser = new SuggestionCommandParser("unbookmark", "error message");
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UNBOOKMARK_COMMAND_WORD + userInput
                + "\n" + UnbookmarkCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with invalid prefixes
        userInput = userInput + FILE_NAME_DESC;
        assertParseSuccess(parser, userInput, expectedCommand);

        // EP: correct command word with valid prefixes (multiple)
        userInput = userInput + SERIAL_NUMBER_DESC_APPLE;
        expectedSuggestionMessage = "error message" + "\n"
                + MESSAGE_SUGGESTION + CommandWords.UNBOOKMARK_COMMAND_WORD + SERIAL_NUMBER_DESC_BANANA
                + SERIAL_NUMBER_DESC_APPLE + "\n" + UnbookmarkCommand.MESSAGE_USAGE;
        expectedCommand = new SuggestionCommand(expectedSuggestionMessage);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
