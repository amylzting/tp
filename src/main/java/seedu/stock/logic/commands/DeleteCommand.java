package seedu.stock.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.stock.commons.core.Messages;
import seedu.stock.logic.commands.exceptions.CommandException;
import seedu.stock.model.Model;
import seedu.stock.model.stock.SerialNumber;
import seedu.stock.model.stock.Stock;

/**
 * Deletes a stock identified using it's displayed serial number from the stock book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the stock identified by the EXACT serial number used in the displayed.\n"
            + "Parameters: SERIAL NUMBER (must be a valid serial number)\n"
            + "Example: " + COMMAND_WORD + " sn/Kc company1";

    public static final String MESSAGE_DELETE_STOCK_SUCCESS = "All serial number(s) are found.\n"
                                                                    + "Deleted Stock(s): %1$s";
    public static final String MESSAGE_DELETE_STOCK_SOME_SUCCESS = "Some serial number(s) are not found.\n"
                                                                    + "Deleted Stock(s): %1$s";

    private final List<SerialNumber> targetSerialNumbers;

    /**
     * Constructor for a new delete command.
     *
     * @param targetSerialNumbers The list of target serial numbers to delete.
     */
    public DeleteCommand(List<SerialNumber> targetSerialNumbers) {
        assert(targetSerialNumbers.size() > 0);
        this.targetSerialNumbers = targetSerialNumbers;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Stock> lastShownList = model.getFilteredStockList();
        List<Stock> stocksDeleted = new ArrayList<>();
        List<SerialNumber> unknownSerialNumbers = new ArrayList<>();
        for (int j = 0; j < targetSerialNumbers.size(); j++) {
            SerialNumber targetSerialNumber = targetSerialNumbers.get(j);
            String targetSerialNumberInString = targetSerialNumber.getSerialNumberAsString();
            boolean isStockDeleted = false;

            //attempts to map serial number to stock and delete the related stock.
            for (int i = 0; i < lastShownList.size(); i++) {
                Stock currentStock = lastShownList.get(i);
                String currentStockSerialNumberInString = currentStock.getSerialNumber()
                                                                        .getSerialNumberAsString();
                if (currentStockSerialNumberInString.equals(targetSerialNumberInString)) {
                    isStockDeleted = true;
                    stocksDeleted.add(currentStock);
                    model.deleteStock(currentStock);
                }
            }

            //if given serial number does not map to any stock, it does not exist.
            if (!isStockDeleted) {
                unknownSerialNumbers.add(targetSerialNumber);
            }
        }

        //deletion of multiple stocks is only successful if the number of deleted stocks is equals
        //to number of serial numbers provided, ensuring all given serial numbers are used.
        if (stocksDeleted.size() == targetSerialNumbers.size()) {
            return new CommandResult(String.format(MESSAGE_DELETE_STOCK_SUCCESS, stocksAsString(stocksDeleted)));
        } else if (stocksDeleted.size() > 0){
            String serialNumbersNotFound = String.format(Messages.MESSAGE_SOME_SERIAL_NUMBER_NOT_FOUND,
                    serialNumberListAsString(unknownSerialNumbers));
            return new CommandResult(String.format(MESSAGE_DELETE_STOCK_SOME_SUCCESS, stocksAsString(stocksDeleted))
                                            + "\n" + serialNumbersNotFound);
        } else {
            String serialNumbersNotFound = String.format(Messages.MESSAGE_SERIAL_NUMBER_NOT_FOUND,
                    serialNumberListAsString(unknownSerialNumbers));
            throw new CommandException(serialNumbersNotFound);
        }
    }

    /**
     * Displays the list of stocks in a clearer view, with each subsequent stock moved to the next line.
     *
     * @param stockList The list of stocks to convert to String.
     * @return The String depicting each stock in the list.
     */
    public String stocksAsString(List<Stock> stockList) {
        String stocksAsString = "";
        for (int i = 0; i < stockList.size(); i++) {
            stocksAsString += "\n" + stockList.get(i).toString();
        }
        return stockList.size() == 0 ? "No stocks deleted" : stocksAsString;
    }

    /**
     * Displays the list of serial numbers in a clearer view, with each subsequent serial number moved
     * to the next line.
     *
     * @param serialNumberList The list of serial numbers to convert to String.
     * @return The String depicting each serial number in the list.
     */
    public String serialNumberListAsString(List<SerialNumber> serialNumberList) {
        String serialNumbersAsString = "";
        for (int i = 0; i < serialNumberList.size(); i++) {
            serialNumbersAsString += "\n" + serialNumberList.get(i).toString();
        }
        return serialNumbersAsString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetSerialNumbers.equals(((DeleteCommand) other).targetSerialNumbers)); // state check
    }
}
