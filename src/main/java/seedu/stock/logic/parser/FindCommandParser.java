package seedu.stock.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.stock.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.stock.logic.parser.CliSyntax.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.stock.logic.commands.FindCommand;
import seedu.stock.logic.parser.exceptions.ParseException;
import seedu.stock.model.stock.Stock;
import seedu.stock.model.stock.predicates.LocationContainsKeywordsPredicate;
import seedu.stock.model.stock.predicates.NameContainsKeywordsPredicate;
import seedu.stock.model.stock.predicates.SerialNumberContainsKeywordsPredicate;
import seedu.stock.model.stock.predicates.SourceContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private final static Prefix[] allPossiblePrefixes = getAllPossiblePrefixesAsArray();
    private final static Prefix[] validPrefixesForFind = { PREFIX_NAME, PREFIX_LOCATION,
            PREFIX_SOURCE, PREFIX_SERIAL_NUMBER };
    private final static Prefix[] invalidPrefixesForFind =
            getInvalidPrefixesForCommand(validPrefixesForFind);

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, allPossiblePrefixes);

        // Check if command format is correct
        if (!isAnyPrefixPresent(argMultimap, validPrefixesForFind)
                || isAnyPrefixPresent(argMultimap, invalidPrefixesForFind)
                || isDuplicatePrefixPresent(argMultimap, validPrefixesForFind)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // Get the predicates to test to find stocks wanted
        List<Predicate<Stock>> predicatesToTest =
                parsePrefixAndKeywords(argMultimap, validPrefixesForFind);

        return new FindCommand(predicatesToTest);
    }

    /**
     * Returns true if any one of the prefixes does not contain
     * an empty {@code Optional} value in the given {@code ArgumentMultimap}.
     * @param argumentMultimap map of prefix to keywords entered by user
     * @param prefixes prefixes to parse
     * @return boolean true if a prefix specified is present
     */
    private static boolean isAnyPrefixPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix ->
                argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if duplicate prefixes are present when parsing command.
     * @param argumentMultimap map of prefix to keywords entered by user
     * @param prefixes prefixes to parse
     * @return boolean true if duplicate prefix is present
     */
    private static boolean isDuplicatePrefixPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {

        // Check for duplicate prefixes
        for (Prefix prefix: prefixes) {
            if (argumentMultimap.getAllValues(prefix).size() >= 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of predicates to filter stocks based on user's search fields and terms.
     * @param argumentMultimap map of prefix to keywords entered by user
     * @param prefixes prefixes to parse
     * @return list of predicates to filter stocks
     */
    private static List<Predicate<Stock>> parsePrefixAndKeywords(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes)
                .filter(prefix -> argumentMultimap.getValue(prefix).isPresent())
                .map(prefix -> getPredicate(prefix, argumentMultimap.getValue(prefix).get()))
                .collect(Collectors.toList());
    }

    /**
     * Returns a field predicate to test whether a {@code Stock}'s {@code field} matches or contains
     * any of the keywords given.
     * @param prefix prefix for field
     * @param keywordsToFind keywords to match with the stock's field
     * @return predicate filter stocks based on field
     */
    private static Predicate<Stock> getPredicate(Prefix prefix, String keywordsToFind) {
        final Predicate<Stock> fieldContainsKeywordsPredicate;
        String trimmedKeywordsToFind = keywordsToFind.trim();
        String[] keywords = trimmedKeywordsToFind.split("\\s+");

        switch(prefix.getPrefix()) {
        case "n/":
            // predicate to test name field of stock
            fieldContainsKeywordsPredicate =
                    new NameContainsKeywordsPredicate(Arrays.asList(keywords));
            break;

        case "sn/":
            // predicate to test serial number field of stock
            fieldContainsKeywordsPredicate =
                    new SerialNumberContainsKeywordsPredicate(Arrays.asList(keywords));
            break;

        case "s/":
            // predicate to test source field of stock
            fieldContainsKeywordsPredicate =
                    new SourceContainsKeywordsPredicate(Arrays.asList(keywords));
            break;

        case "l/":
            // predicate to test location stored field of stock
            fieldContainsKeywordsPredicate =
                    new LocationContainsKeywordsPredicate(Arrays.asList(keywords));
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + prefix);
        }

        return fieldContainsKeywordsPredicate;
    }

}
