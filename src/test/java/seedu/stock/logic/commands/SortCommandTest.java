package seedu.stock.logic.commands;

import static seedu.stock.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.stock.logic.commands.SortCommand.MESSAGE_SORT_STOCK_SUCCESS;
import static seedu.stock.testutil.TypicalStocks.getTypicalSerialNumberSetsBook;
import static seedu.stock.testutil.TypicalStocks.getTypicalStockBook;
import static seedu.stock.testutil.TypicalStocks.getTypicalStocks;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.stock.commons.util.SortUtil;
import seedu.stock.model.Model;
import seedu.stock.model.ModelManager;
import seedu.stock.model.SerialNumberSetsBook;
import seedu.stock.model.StockBook;
import seedu.stock.model.UserPrefs;
import seedu.stock.model.stock.Stock;

public class SortCommandTest {

    private SerialNumberSetsBook serialNumbers = getTypicalSerialNumberSetsBook();
    private Model model = new ModelManager(getTypicalStockBook(), new UserPrefs(), serialNumbers);

    @Test
    public void execute_ascendingOrder_success() {
        SortCommand sortCommand = new SortCommand(SortUtil.Field.SERIALNUMBER, false);
        List<Stock> sortedStocks = getTypicalStocks();
        Comparator<Stock> serialNumberComparator = SortUtil.generateComparator(SortUtil.Field.SERIALNUMBER);
        sortedStocks.sort(serialNumberComparator);
        StockBook sortedStockBook = new StockBook();
        sortedStockBook.setStocks(sortedStocks);

        String expectedMessage =
                String.format(MESSAGE_SORT_STOCK_SUCCESS, SortUtil.getFieldDescription(SortUtil.Field.SERIALNUMBER));
        Model expectedModel = new ModelManager(sortedStockBook, new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));

        assertCommandSuccess(sortCommand, model, expectedMessage, expectedModel);
    }
}
