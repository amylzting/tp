package seedu.stock.logic.parser;

import static seedu.stock.commons.core.Messages.MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT;
import static seedu.stock.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.stock.logic.commands.CommandWords.ADD_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.BOOKMARK_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.DELETE_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.FIND_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.FIND_EXACT_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.NOTE_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.NOTE_DELETE_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.NOTE_VIEW_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.PRINT_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.SORT_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.STATISTICS_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.UNBOOKMARK_COMMAND_WORD;
import static seedu.stock.logic.commands.CommandWords.UPDATE_COMMAND_WORD;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_FILE_NAME;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_INCREMENT_QUANTITY;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_LIST_TYPE;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_LOW_QUANTITY;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_NEW_QUANTITY;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_NOTE_INDEX;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_QUANTITY;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SERIAL_NUMBER;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SORT_FIELD;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SORT_ORDER;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SOURCE;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_STATISTICS_TYPE;

import java.util.List;
import java.util.Random;

import seedu.stock.commons.util.SortUtil.Field;
import seedu.stock.commons.util.SortUtil.Order;
import seedu.stock.commons.util.SuggestionUtil;
import seedu.stock.logic.commands.AddCommand;
import seedu.stock.logic.commands.BookmarkCommand;
import seedu.stock.logic.commands.ClearCommand;
import seedu.stock.logic.commands.CommandWords;
import seedu.stock.logic.commands.DeleteCommand;
import seedu.stock.logic.commands.ExitCommand;
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
import seedu.stock.logic.parser.exceptions.ParseException;
import seedu.stock.model.stock.Location;
import seedu.stock.model.stock.Name;
import seedu.stock.model.stock.Note;
import seedu.stock.model.stock.Quantity;
import seedu.stock.model.stock.QuantityAdder;
import seedu.stock.model.stock.SerialNumber;
import seedu.stock.model.stock.Source;

public class SuggestionCommandParser implements Parser<SuggestionCommand> {

    public static final String MESSAGE_SUGGESTION = "Do you mean: \n";
    private String faultyCommandWord;
    private String commandWord;
    private String headerErrorMessage;
    private String bodyErrorMessage;

    /**
     * Constructs a new suggestion command parser.
     *
     * @param faultyCommandWord The command word to be corrected.
     */
    public SuggestionCommandParser(String faultyCommandWord) {
        this.faultyCommandWord = faultyCommandWord;
        commandWord = "";
        this.headerErrorMessage = "";
        this.bodyErrorMessage = "";
    }

    /**
     * Constructs a new suggestion command parser with error message from another parser.
     *
     * @param commandWord The command word to be executed if all went well.
     * @param errorMessage The parse error thrown from another parser.
     */
    public SuggestionCommandParser(String commandWord, String errorMessage) {
        String[] splitHeaderAndBody = errorMessage.split("\n", 2);
        this.commandWord = commandWord;
        faultyCommandWord = "";
        headerErrorMessage = splitHeaderAndBody[0];
        bodyErrorMessage = splitHeaderAndBody.length < 2 ? "" : splitHeaderAndBody[1];
    }

    /**
     * Parses {@code args} into a suggestion command.
     *
     * @param args The user input to be parsed.
     * @return A new suggestion command.
     * @throws ParseException If any parsing errors occurs.
     */
    @Override
    public SuggestionCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenizeAllPrefixes(args);
        List<String> allCommandWords = CommandWords.getAllCommandWords();
        StringBuilder toBeDisplayed = new StringBuilder();

        String suggestedCommandWord = faultyCommandWord;
        int bestEditDistanceSoFar = Integer.MAX_VALUE;
        for (String commandWord: allCommandWords) {
            int currentEditDistance = SuggestionUtil.minimumEditDistance(faultyCommandWord, commandWord);
            if (currentEditDistance < bestEditDistanceSoFar) {
                bestEditDistanceSoFar = currentEditDistance;
                suggestedCommandWord = commandWord;
            }
        }

        if (suggestedCommandWord.equals(faultyCommandWord) && commandWord.equals("")) {
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

        if (!commandWord.equals("")) {
            suggestedCommandWord = commandWord;
        }

        if (headerErrorMessage.equals("")) {
            toBeDisplayed.append(MESSAGE_UNKNOWN_COMMAND + "\n" + MESSAGE_SUGGESTION);
        } else {
            toBeDisplayed.append(headerErrorMessage + "\n" + MESSAGE_SUGGESTION);
        }

        switch (suggestedCommandWord) {
        case ExitCommand.COMMAND_WORD:
            generateExitSuggestion(toBeDisplayed, argMultimap);
            break;

        case HelpCommand.COMMAND_WORD:
            generateHelpSuggestion(toBeDisplayed, argMultimap);
            break;

        case ListCommand.COMMAND_WORD:
            generateListSuggestion(toBeDisplayed, argMultimap);
            break;

        case AddCommand.COMMAND_WORD:
            generateAddSuggestion(toBeDisplayed, argMultimap);
            break;

        case ClearCommand.COMMAND_WORD:
            generateClearSuggestion(toBeDisplayed);
            break;

        case DeleteCommand.COMMAND_WORD:
            toBeDisplayed = new StringBuilder();
            toBeDisplayed.append(MESSAGE_INVALID_COMMAND_SERIAL_NUMBER_FORMAT + "\n" + MESSAGE_SUGGESTION);
            generateDeleteSuggestion(toBeDisplayed, argMultimap);
            break;

        case StatisticsCommand.COMMAND_WORD:
            generateStatisticsSuggestion(toBeDisplayed, argMultimap);
            break;

        case TabCommand.COMMAND_WORD:
            generateTabSuggestion(toBeDisplayed);
            break;

        case FindCommand.COMMAND_WORD:
            generateFindSuggestion(toBeDisplayed, argMultimap);
            break;

        case UpdateCommand.COMMAND_WORD:
            generateUpdateSuggestion(toBeDisplayed, argMultimap);
            break;

        case FindExactCommand.COMMAND_WORD:
            generateFindExactSuggestion(toBeDisplayed, argMultimap);
            break;

        case SortCommand.COMMAND_WORD:
            generateSortSuggestion(toBeDisplayed, argMultimap);
            break;

        case NoteCommand.COMMAND_WORD:
            generateNoteSuggestion(toBeDisplayed, argMultimap);
            break;

        case NoteDeleteCommand.COMMAND_WORD:
            generateDeleteNoteSuggestion(toBeDisplayed, argMultimap);
            break;

        case NoteViewCommand.COMMAND_WORD:
            generateNoteViewSuggestion(toBeDisplayed, argMultimap);
            break;

        case PrintCommand.COMMAND_WORD:
            generatePrintSuggestion(toBeDisplayed, argMultimap);
            break;

        case BookmarkCommand.COMMAND_WORD:
            generateBookmarkSuggestion(toBeDisplayed, argMultimap);
            break;

        case UnbookmarkCommand.COMMAND_WORD:
            generateUnbookmarkSuggestion(toBeDisplayed, argMultimap);
            break;

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }

        return new SuggestionCommand(toBeDisplayed.toString());
    }

    /**
     * Generates suggestion for faulty clear command.
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     */
    private void generateClearSuggestion(StringBuilder toBeDisplayed) {
        toBeDisplayed.append(CommandWords.CLEAR_COMMAND_WORD);

        generateBodyMessage(toBeDisplayed, ClearCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty tab command.
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     */
    private void generateTabSuggestion(StringBuilder toBeDisplayed) {
        toBeDisplayed.append(CommandWords.TAB_COMMAND_WORD);

        generateBodyMessage(toBeDisplayed, TabCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty sort command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateSortSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(SORT_COMMAND_WORD);
        Prefix fieldPrefix = PREFIX_SORT_FIELD;
        Prefix orderPrefix = PREFIX_SORT_ORDER;

        if (!argMultimap.getValue(orderPrefix).isPresent()) {
            toBeDisplayed.append(" " + orderPrefix + CliSyntax.getDefaultDescription(orderPrefix));
        } else {
            String description = argMultimap.getValue(orderPrefix).get();
            String suggestedDescription = description;
            int bestEditDistanceSoFar = Integer.MAX_VALUE;
            for (Order order : Order.values()) {
                String orderDescription = order.toString().toLowerCase();
                int currentEditDistance = SuggestionUtil.minimumEditDistance(description, orderDescription);
                if (currentEditDistance < bestEditDistanceSoFar) {
                    bestEditDistanceSoFar = currentEditDistance;
                    suggestedDescription = orderDescription;
                }
            }
            toBeDisplayed.append(" " + orderPrefix + suggestedDescription);
        }

        if (!argMultimap.getValue(fieldPrefix).isPresent()) {
            toBeDisplayed.append(" " + fieldPrefix + CliSyntax.getDefaultDescription(fieldPrefix));
        } else {
            String description = argMultimap.getValue(fieldPrefix).get();
            String suggestedDescription = description;
            int bestEditDistanceSoFar = Integer.MAX_VALUE;
            for (Field field : Field.values()) {
                String fieldDescription = field.toString().toLowerCase();
                int currentEditDistance = SuggestionUtil.minimumEditDistance(description, fieldDescription);
                if (currentEditDistance < bestEditDistanceSoFar) {
                    bestEditDistanceSoFar = currentEditDistance;
                    suggestedDescription = fieldDescription;
                }
            }
            toBeDisplayed.append(" " + fieldPrefix + suggestedDescription);
        }

        generateBodyMessage(toBeDisplayed, SortCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty bookmark command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateBookmarkSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(BOOKMARK_COMMAND_WORD);
        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);

        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
        }

        generateBodyMessage(toBeDisplayed, BookmarkCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty unbookmark command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateUnbookmarkSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(UNBOOKMARK_COMMAND_WORD);
        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);

        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
        }

        generateBodyMessage(toBeDisplayed, UnbookmarkCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty find exact command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateFindExactSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        List<Prefix> allowedPrefixes = ParserUtil.generateListOfPrefixes(PREFIX_NAME, PREFIX_SOURCE,
                PREFIX_SERIAL_NUMBER, PREFIX_LOCATION);
        toBeDisplayed.append(FIND_EXACT_COMMAND_WORD);

        for (int i = 0; i < allowedPrefixes.size(); i++) {
            Prefix currentPrefix = allowedPrefixes.get(i);
            if (!argMultimap.getValue(currentPrefix).isPresent()) {
                continue;
            }
            String currentParameter = argMultimap.getValue(currentPrefix).get();
            if (checkIfParameterValid(currentPrefix, currentParameter)) {
                toBeDisplayed.append(" " + currentPrefix + currentParameter);
            } else {
                toBeDisplayed.append(" " + currentPrefix + CliSyntax.getDefaultDescription(currentPrefix));
            }
        }

        generateBodyMessage(toBeDisplayed, FindExactCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty update command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateUpdateSuggestion(StringBuilder toBeDisplayed,
            ArgumentMultimap argMultimap) throws ParseException {
        List<Prefix> allowedPrefixes = ParserUtil.generateListOfPrefixes(PREFIX_SERIAL_NUMBER,
                PREFIX_INCREMENT_QUANTITY, PREFIX_NEW_QUANTITY, PREFIX_NAME, PREFIX_SOURCE, PREFIX_LOCATION,
                PREFIX_LOW_QUANTITY);
        toBeDisplayed.append(UPDATE_COMMAND_WORD);
        boolean isIncrementQuantityPresent = argMultimap.getValue(PREFIX_INCREMENT_QUANTITY).isPresent();
        boolean isNewQuantityPresent = argMultimap.getValue(PREFIX_NEW_QUANTITY).isPresent();

        if (isIncrementQuantityPresent == isNewQuantityPresent) {
            // both present or both not present
            int removeRng = new Random().nextInt(2);
            if (removeRng == 0) {
                allowedPrefixes.remove(PREFIX_INCREMENT_QUANTITY);
            } else {
                allowedPrefixes.remove(PREFIX_NEW_QUANTITY);
            }
        }

        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);
        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
        }

        for (int i = 1; i < allowedPrefixes.size(); i++) {
            Prefix currentPrefix = allowedPrefixes.get(i);
            boolean isPresent = argMultimap.getValue(currentPrefix).isPresent();
            String description = "";
            if (isPresent) {
                description = argMultimap.getValue(currentPrefix).get();
            }
            if (!checkIfParameterValid(currentPrefix, description)) {
                description = "";
            }
            boolean isEmpty = description.equals("");
            if (!isEmpty) {
                toBeDisplayed.append(" " + currentPrefix + description);
            } else if (isEmpty && isPresent) {
                toBeDisplayed.append(" " + currentPrefix + CliSyntax.getDefaultDescription(currentPrefix));
            }
        }

        generateBodyMessage(toBeDisplayed, UpdateCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty find command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateFindSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        List<Prefix> allowedPrefixes = ParserUtil.generateListOfPrefixes(PREFIX_NAME, PREFIX_SOURCE,
                PREFIX_SERIAL_NUMBER, PREFIX_LOCATION);
        toBeDisplayed.append(FIND_COMMAND_WORD);

        for (int i = 0; i < allowedPrefixes.size(); i++) {
            Prefix currentPrefix = allowedPrefixes.get(i);
            if (!argMultimap.getValue(currentPrefix).isPresent()) {
                continue;
            }
            String currentParameter = argMultimap.getValue(currentPrefix).get();
            if (checkIfParameterValid(currentPrefix, currentParameter)) {
                toBeDisplayed.append(" " + currentPrefix + currentParameter);
            } else {
                toBeDisplayed.append(" " + currentPrefix + CliSyntax.getDefaultDescription(currentPrefix));
            }
        }

        generateBodyMessage(toBeDisplayed, FindCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty note command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateNoteSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        List<Prefix> allowedPrefixes = ParserUtil.generateListOfPrefixes(
                PREFIX_SERIAL_NUMBER, PREFIX_NOTE);
        toBeDisplayed.append(NOTE_COMMAND_WORD);

        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);
        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
        }

        for (int i = 1; i < allowedPrefixes.size(); i++) {
            Prefix currentPrefix = allowedPrefixes.get(i);
            String description = "";
            if (argMultimap.getValue(currentPrefix).isPresent()) {
                description = argMultimap.getValue(currentPrefix).get();
            }
            if (!checkIfParameterValid(currentPrefix, description)) {
                description = "";
            }
            boolean isEmpty = description.equals("");
            if (isEmpty) {
                toBeDisplayed.append(" " + currentPrefix + CliSyntax.getDefaultDescription(currentPrefix));
            } else {
                toBeDisplayed.append(" " + currentPrefix + description);
            }
        }
        generateBodyMessage(toBeDisplayed, NoteCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty deletenote command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateDeleteNoteSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        List<Prefix> allowedPrefixes = ParserUtil.generateListOfPrefixes(
                PREFIX_SERIAL_NUMBER, PREFIX_NOTE_INDEX);
        toBeDisplayed.append(NOTE_DELETE_COMMAND_WORD);

        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);
        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER));
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
        }

        for (int i = 1; i < allowedPrefixes.size(); i++) {
            Prefix currentPrefix = allowedPrefixes.get(i);
            String description = "";
            if (argMultimap.getValue(currentPrefix).isPresent()) {
                description = argMultimap.getValue(currentPrefix).get();
            }
            if (!checkIfParameterValid(currentPrefix, description)) {
                description = "";
            }
            boolean isEmpty = description.equals("");
            if (isEmpty) {
                toBeDisplayed.append(" " + currentPrefix + CliSyntax.getDefaultDescription(currentPrefix));
            } else {
                toBeDisplayed.append(" " + currentPrefix + description);
            }
        }

        generateBodyMessage(toBeDisplayed, NoteDeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty noteview command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateNoteViewSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(NOTE_VIEW_COMMAND_WORD);

        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);
        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER));
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
            break;
        }

        generateBodyMessage(toBeDisplayed, NoteViewCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty delete command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateDeleteSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(DELETE_COMMAND_WORD);

        String defaultDescriptionSerialNumber = CliSyntax.getDefaultDescription(PREFIX_SERIAL_NUMBER);
        List<String> keywords = argMultimap.getAllValues(PREFIX_SERIAL_NUMBER);
        if (!argMultimap.getValue(PREFIX_SERIAL_NUMBER).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
        }
        for (String serialNumber : keywords) {
            if (checkIfParameterValid(PREFIX_SERIAL_NUMBER, serialNumber)) {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + serialNumber);
            } else {
                toBeDisplayed.append(" " + PREFIX_SERIAL_NUMBER + defaultDescriptionSerialNumber);
            }
        }

        generateBodyMessage(toBeDisplayed, DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty statistics command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateStatisticsSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(STATISTICS_COMMAND_WORD);

        Prefix typePrefix = PREFIX_STATISTICS_TYPE;
        String[] typeDescriptions = new String[]{"source", "source-qd-"};

        if (!argMultimap.getValue(typePrefix).isPresent()) {
            toBeDisplayed.append(" " + typePrefix + CliSyntax.getDefaultDescription(typePrefix));
        } else {
            String description = argMultimap.getValue(typePrefix).get();
            String suggestedDescription = description;
            int bestEditDistanceSoFar = Integer.MAX_VALUE;
            for (String typeDescription : typeDescriptions) {
                int currentEditDistance = SuggestionUtil.minimumEditDistance(description, typeDescription);
                if (currentEditDistance < bestEditDistanceSoFar) {
                    bestEditDistanceSoFar = currentEditDistance;
                    suggestedDescription = typeDescription;
                }
            }
            if (suggestedDescription.equals(typeDescriptions[0])) {
                toBeDisplayed.append(" " + typePrefix + suggestedDescription);
            } else {
                toBeDisplayed.append(" " + typePrefix + suggestedDescription + "<source-company>");
            }
        }

        generateBodyMessage(toBeDisplayed, StatisticsCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty add command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateAddSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        List<Prefix> allowedPrefixes = ParserUtil.generateListOfPrefixes(PREFIX_NAME, PREFIX_SOURCE,
                 PREFIX_QUANTITY, PREFIX_LOCATION, PREFIX_LOW_QUANTITY);
        toBeDisplayed.append(ADD_COMMAND_WORD);

        for (int i = 0; i < allowedPrefixes.size(); i++) {
            Prefix currentPrefix = allowedPrefixes.get(i);
            String description = "";
            if (argMultimap.getValue(currentPrefix).isPresent()) {
                description = argMultimap.getValue(currentPrefix).get();
            }
            if (!checkIfParameterValid(currentPrefix, description)) {
                description = "";
            }
            boolean isEmpty = description.equals("");
            if (isEmpty) {
                toBeDisplayed.append(" " + currentPrefix + CliSyntax.getDefaultDescription(currentPrefix));
            } else {
                toBeDisplayed.append(" " + currentPrefix + description);
            }
        }

        generateBodyMessage(toBeDisplayed, AddCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty list command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateListSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(CommandWords.LIST_COMMAND_WORD);

        Prefix typePrefix = PREFIX_LIST_TYPE;
        String[] typeDescriptions = new String[]{"all", "bookmark", "low"};

        if (!argMultimap.getValue(typePrefix).isPresent()) {
            toBeDisplayed.append(" " + typePrefix + CliSyntax.getDefaultDescription(typePrefix));
        } else {
            String description = argMultimap.getValue(typePrefix).get();
            String suggestedDescription = description;
            int bestEditDistanceSoFar = Integer.MAX_VALUE;
            for (String typeDescription : typeDescriptions) {
                int currentEditDistance = SuggestionUtil.minimumEditDistance(description, typeDescription);
                if (currentEditDistance < bestEditDistanceSoFar) {
                    bestEditDistanceSoFar = currentEditDistance;
                    suggestedDescription = typeDescription;
                }
            }
            toBeDisplayed.append(" " + typePrefix + suggestedDescription);
        }

        generateBodyMessage(toBeDisplayed, ListCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty help command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateHelpSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(CommandWords.HELP_COMMAND_WORD);

        generateBodyMessage(toBeDisplayed, HelpCommand.MESSAGE_USAGE);
    }

    /**
     * Generates suggestion for faulty exit command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generateExitSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(CommandWords.EXIT_COMMAND_WORD);
    }

    /**
     * Generates suggestion for faulty print command.
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param argMultimap The parsed user input fields.
     */
    private void generatePrintSuggestion(StringBuilder toBeDisplayed, ArgumentMultimap argMultimap) {
        toBeDisplayed.append(PRINT_COMMAND_WORD);

        String defaultFileNameDescription = CliSyntax.getDefaultDescription(PREFIX_FILE_NAME);
        if (!argMultimap.getValue(PREFIX_FILE_NAME).isPresent()) {
            toBeDisplayed.append(" " + PREFIX_FILE_NAME + defaultFileNameDescription);
        }
        List<String> keywords = argMultimap.getAllValues(PREFIX_FILE_NAME);
        for (String fileName : keywords) {
            if (checkIfParameterValid(PREFIX_FILE_NAME, fileName)) {
                toBeDisplayed.append(" " + PREFIX_FILE_NAME + fileName);
            } else {
                toBeDisplayed.append(" " + PREFIX_FILE_NAME + defaultFileNameDescription);
            }
            break;
        }

        generateBodyMessage(toBeDisplayed, PrintCommand.MESSAGE_USAGE);
    }

    /**
     * Generates the suggestion body message to be displayed to the user
     *
     * @param toBeDisplayed The accumulated suggestion to be displayed to the user.
     * @param messageUsage The body message.
     */
    private void generateBodyMessage(StringBuilder toBeDisplayed, String messageUsage) {
        if (!bodyErrorMessage.equals("")) {
            toBeDisplayed.append("\n" + bodyErrorMessage);
        } else {
            toBeDisplayed.append("\n" + messageUsage);
        }
    }

    /**
     * Checks if the given input by user is valid.
     *
     * @param prefix The prefix the parameter is in.
     * @param parameter The user input value.
     * @return A boolean which indicates if the user input is valid.
     */
    private boolean checkIfParameterValid(Prefix prefix, String parameter) {
        if (prefix.equals(PREFIX_NAME)) {
            return Name.isValidName(parameter);
        } else if (prefix.equals(PREFIX_SOURCE)) {
            return Source.isValidSource(parameter);
        } else if (prefix.equals(PREFIX_QUANTITY)) {
            return Quantity.isValidQuantity(parameter);
        } else if (prefix.equals(PREFIX_LOW_QUANTITY)) {
            return Quantity.isValidQuantity(parameter);
        } else if (prefix.equals(PREFIX_LOCATION)) {
            return Location.isValidLocation(parameter);
        } else if (prefix.equals(PREFIX_LIST_TYPE)) {
            return true;
        } else if (prefix.equals(PREFIX_SERIAL_NUMBER)) {
            return SerialNumber.isValidSerialNumber(parameter);
        } else if (prefix.equals(PREFIX_NEW_QUANTITY)) {
            return Quantity.isValidQuantity(parameter);
        } else if (prefix.equals(PREFIX_INCREMENT_QUANTITY)) {
            return QuantityAdder.isValidValue(parameter);
        } else if (prefix.equals(PREFIX_NOTE)) {
            return Note.isValidNote(parameter);
        } else if (prefix.equals(PREFIX_NOTE_INDEX)) {
            return parameter.matches("[0-9]+");
        } else if (prefix.equals(PREFIX_STATISTICS_TYPE)) {
            return true;
        } else if (prefix.equals(PREFIX_SORT_FIELD)) {
            return true;
        } else if (prefix.equals(PREFIX_SORT_ORDER)) {
            return true;
        } else if (prefix.equals(PREFIX_FILE_NAME)) {
            return parameter.matches(PrintCommandParser.VALIDATION_REGEX);
        } else {
            return false;
        }
    }
}
