package seedu.stock.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.stock.commons.core.Messages.MESSAGE_SERIAL_NUMBER_NOT_FOUND;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SERIAL_NUMBER;
import static seedu.stock.logic.parser.CliSyntax.PREFIX_SERIAL_NUMBER_DESCRIPTION;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.stock.commons.util.SortUtil;
import seedu.stock.logic.commands.exceptions.CommandException;
import seedu.stock.model.Model;
import seedu.stock.model.stock.SerialNumber;
import seedu.stock.model.stock.Stock;

/**
 * Updates an existing stock in the stock book.
 */
public class UnbookmarkCommand extends Command {

    public static final String COMMAND_WORD = "unbookmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes bookmarks from bookmarked stocks "
            + "the given serial number. \n"
            + "Format: "
            + COMMAND_WORD + " "
            + PREFIX_SERIAL_NUMBER + PREFIX_SERIAL_NUMBER_DESCRIPTION + "...\n"
            + "Example: "
            + COMMAND_WORD + " "
            + PREFIX_SERIAL_NUMBER + "China3 ";


    public static final String MESSAGE_UNBOOKMARK_STOCK_SUCCESS = "Unbookmarked Stock: %1$s";
    public static final String MESSAGE_NOT_BOOKMARKED = "Stock with given serial number"
            + " is not bookmarked before:%1$s";

    private final Set<SerialNumber> targetSerialNumbers;

    public UnbookmarkCommand (Set<SerialNumber> targetSerialNumbers) {
        this.targetSerialNumbers = targetSerialNumbers;
    }

    /**
     * Executes the bookmark command and returns the result.
     *
     * @param model {@code Model} which the command should operate on.
     * @return The result of successful execution.
     * @throws CommandException If there are any errors.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredStockList(Model.PREDICATE_SHOW_ALL_STOCKS);
        List<Stock> lastShownStocks = model.getFilteredStockList();

        List<SerialNumber> serialNumbers = targetSerialNumbers.stream().collect(Collectors.toList());
        List<String> serials = serialNumbers.stream().map((serial) -> serial.toString().trim())
                .collect(Collectors.toCollection(ArrayList::new));
        List<Stock> stocksToUnbookmark = new ArrayList<>();
        List<String> stocksNotFound = new ArrayList<>();
        List<Stock> notUpdatedStocks = new ArrayList<>();
        List<Stock> updatedStocks = new ArrayList<>();

        // Find serials that are not found
        for (String currentSerialNumber : serials) {
            boolean noMatches = true;
            for (Stock currentStock : lastShownStocks) {
                String currentStockSerialNumber = currentStock.getSerialNumber().getSerialNumberAsString();
                if (currentSerialNumber.equals(currentStockSerialNumber)) {
                    noMatches = false;
                }
            }
            if (noMatches) {
                stocksNotFound.add(currentSerialNumber);
            }
        }

        // Find stocks to unbookmark
        for (Stock currentStock : lastShownStocks) {
            String currentStockSerialNumber = currentStock.getSerialNumber().getSerialNumberAsString();
            boolean anyMatches = false;

            for (String currentSerialNumber : serials) {
                if (currentSerialNumber.equals(currentStockSerialNumber)) {
                    anyMatches = true;
                }
            }

            if (anyMatches) {
                stocksToUnbookmark.add(currentStock);

            }
        }

        // Bookmark stocks
        for (Stock stockToUnbookmark: stocksToUnbookmark) {

            if (!stockToUnbookmark.getIsBookmarked()) {
                notUpdatedStocks.add(stockToUnbookmark);
            } else {
                stockToUnbookmark.setUnbookmarked();
                model.setStock(stockToUnbookmark, stockToUnbookmark);
                updatedStocks.add(stockToUnbookmark);
            }

        }

        model.sortFilteredStockList(SortUtil.generateGeneralComparator());

        if (stocksNotFound.size() == serialNumbers.size()) {
            return new CommandResult(String.format(MESSAGE_SERIAL_NUMBER_NOT_FOUND , arrayAsString(stocksNotFound)));
        } else if (notUpdatedStocks.size() == serialNumbers.size()) {
            return new CommandResult(String.format(MESSAGE_NOT_BOOKMARKED , stocksAsString(notUpdatedStocks)));
        } else if (updatedStocks.size() == serialNumbers.size()) {
            return new CommandResult(String.format(MESSAGE_UNBOOKMARK_STOCK_SUCCESS , stocksAsString(updatedStocks)));
        } else if (notUpdatedStocks.size() == 0 && stocksNotFound.size() > 0) {
            String result = String.format(MESSAGE_SERIAL_NUMBER_NOT_FOUND , arrayAsString(stocksNotFound))
                    + "\n" + String.format(MESSAGE_UNBOOKMARK_STOCK_SUCCESS , stocksAsString(updatedStocks));
            return new CommandResult(result);
        } else if (stocksNotFound.size() == 0 && notUpdatedStocks.size() > 0) {
            String result = String.format(MESSAGE_NOT_BOOKMARKED , stocksAsString(notUpdatedStocks))
                    + "\n" + String.format(MESSAGE_UNBOOKMARK_STOCK_SUCCESS , stocksAsString(updatedStocks));
            return new CommandResult(result);
        } else {
            String result = String.format(MESSAGE_NOT_BOOKMARKED , stocksAsString(notUpdatedStocks))
                    + "\n" + String.format(MESSAGE_SERIAL_NUMBER_NOT_FOUND , arrayAsString(stocksNotFound))
                    + "\n" + String.format(MESSAGE_UNBOOKMARK_STOCK_SUCCESS , stocksAsString(updatedStocks));
            return new CommandResult(result);
        }

    }

    /**
     * Displays the list of stocks in a clearer view, with each subsequent stock moved to the next line.
     *
     * @param stockList The list of stocks to be converted to String.
     * @return The String listing all stocks in the list.
     */
    private String stocksAsString(List<Stock> stockList) {
        String stocksAsString = "";
        for (int i = 0; i < stockList.size(); i++) {
            stocksAsString += "\n" + stockList.get(i).toString();
        }
        return stockList.size() == 0 ? "No stocks updated" : stocksAsString;
    }

    /**
     * Displays the list of serial numbers in a clearer view, with each subsequent serial number moved
     * to the next line.
     *
     * @param serialNumberList The list of serial numbers to convert to String.
     * @return The String depicting each serial number in the list.
     */
    public String serialNumbersAsString(List<SerialNumber> serialNumberList) {
        String serialNumbersAsString = "";
        for (int i = 0; i < serialNumberList.size(); i++) {
            serialNumbersAsString += "\n" + serialNumberList.get(i).toString();
        }
        return serialNumbersAsString;
    }

    /**
     * Displays the list of strings in a clearer view, with each subsequent string moved
     * to the next line.
     *
     * @param stringList The list of serial numbers to convert to String.
     * @return The String depicting each serial number in the list.
     */
    public String arrayAsString(List<String> stringList) {
        String serialNumbersAsString = "";
        for (int i = 0; i < stringList.size(); i++) {
            serialNumbersAsString += "\n" + stringList.get(i);
        }
        return serialNumbersAsString;
    }


}
