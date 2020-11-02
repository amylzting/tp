package seedu.stock.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.stock.logic.commands.CommandTestUtil.VALID_LOCATION_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.VALID_NAME_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.VALID_NAME_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.VALID_SERIAL_NUMBER_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.VALID_SERIAL_NUMBER_BANANA;
import static seedu.stock.logic.commands.CommandTestUtil.VALID_SOURCE_APPLE;
import static seedu.stock.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.stock.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.stock.testutil.TypicalSerialNumberSets.getTypicalSerialNumberSetsBook;
import static seedu.stock.testutil.TypicalStocks.getTypicalStockBook;

import org.junit.jupiter.api.Test;

import seedu.stock.commons.core.index.Index;
import seedu.stock.logic.commands.UpdateCommand.UpdateStockDescriptor;
import seedu.stock.model.Model;
import seedu.stock.model.ModelManager;
import seedu.stock.model.SerialNumberSetsBook;
import seedu.stock.model.StockBook;
import seedu.stock.model.UserPrefs;
import seedu.stock.model.stock.Stock;
import seedu.stock.testutil.StockBuilder;
import seedu.stock.testutil.TypicalStocks;
import seedu.stock.testutil.UpdateStockDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
  and unit tests for EditCommand.
 */
public class UpdateCommandTest {

    private SerialNumberSetsBook serialNumbers = getTypicalSerialNumberSetsBook();
    private Model model = new ModelManager(getTypicalStockBook(), new UserPrefs(), serialNumbers);

    @Test
    public void execute_onlyQuantityUpdated_success() {
        Stock updatedStock = new StockBuilder(TypicalStocks.BANANA).withQuantity("2101").build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(updatedStock).build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));
        expectedModel.setStock(model.getFilteredStockList().get(1), updatedStock);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlySourceUpdated_success() {
        Stock updatedStock = new StockBuilder(TypicalStocks.BANANA).withSource("Value$").build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(updatedStock).build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));
        expectedModel.setStock(model.getFilteredStockList().get(1), updatedStock);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyLocationUpdated_success() {
        Stock updatedStock = new StockBuilder(TypicalStocks.BANANA).withLocation("Discount section").build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(updatedStock).build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));
        expectedModel.setStock(model.getFilteredStockList().get(1), updatedStock);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyNameUpdated_success() {
        Stock updatedStock = new StockBuilder(TypicalStocks.BANANA).withName("mochi").build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(updatedStock).build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));
        expectedModel.setStock(model.getFilteredStockList().get(1), updatedStock);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Stock updatedStock = new StockBuilder(TypicalStocks.BANANA).build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(updatedStock).build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));
        expectedModel.setStock(model.getFilteredStockList().get(1), updatedStock);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastStock = Index.fromOneBased(model.getFilteredStockList().size());
        Stock lastStock = model.getFilteredStockList().get(indexLastStock.getZeroBased());

        StockBuilder stockInList = new StockBuilder(lastStock);
        Stock updatedStock = stockInList.withName(VALID_NAME_APPLE).withSource(VALID_SOURCE_APPLE)
                .withLocation(VALID_LOCATION_APPLE).build();

        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder().withName(VALID_NAME_APPLE)
                .withSource(VALID_SOURCE_APPLE).withLocation(VALID_LOCATION_APPLE)
                .withSerialNumber(updatedStock.getSerialNumber().toString()).build();
        UpdateCommand editCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                model.getSerialNumberSetsBook());
        expectedModel.setStock(lastStock, updatedStock);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        Stock updatedStock = model.getFilteredStockList().get(0);
        UpdateStockDescriptor serialNumberOnly = new UpdateStockDescriptorBuilder()
                .withSerialNumber(updatedStock.getSerialNumber().toString()).build();
        UpdateCommand updateCommand = new UpdateCommand(serialNumberOnly);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                model.getSerialNumberSetsBook());

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        Stock stockInFilteredList = model.getFilteredStockList().get(0);
        Stock updatedStock = new StockBuilder(stockInFilteredList).withName(VALID_NAME_BANANA).build();
        UpdateCommand updateCommand = new UpdateCommand(new UpdateStockDescriptorBuilder().withName(VALID_NAME_BANANA)
                .withSerialNumber(stockInFilteredList.getSerialNumber().toString()).build());

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS, "\n" + updatedStock);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                model.getSerialNumberSetsBook());
        expectedModel.setStock(model.getFilteredStockList().get(0), updatedStock);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleStocksUpdated_success() {
        Stock updatedStockApple = new StockBuilder(TypicalStocks.APPLE).withName("Ice Cream")
                .withSource("Magnum").withLocation("Freezer One").withSerialNumber(VALID_SERIAL_NUMBER_APPLE)
                .withQuantity("2000").build();
        Stock updatedStockBanana = new StockBuilder(TypicalStocks.BANANA).withName("Ice Cream")
                .withSource("Magnum").withLocation("Freezer One").withSerialNumber(VALID_SERIAL_NUMBER_BANANA)
                .withQuantity("2103").build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder()
                .withName("Ice Cream").withSource("Magnum")
                .withLocation("Freezer One").withSerialNumber(VALID_SERIAL_NUMBER_APPLE, VALID_SERIAL_NUMBER_BANANA)
                .build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        String expectedMessage = String.format(UpdateCommand.MESSAGE_UPDATE_STOCK_SUCCESS,
                "\n" + updatedStockApple + "\n" + updatedStockBanana);

        Model expectedModel = new ModelManager(new StockBook(model.getStockBook()), new UserPrefs(),
                new SerialNumberSetsBook(model.getSerialNumberSetsBook()));
        expectedModel.setStock(model.getFilteredStockList().get(0), updatedStockApple);
        expectedModel.setStock(model.getFilteredStockList().get(1), updatedStockBanana);

        assertCommandSuccess(updateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_serialNumberNotFound_failure() {
        Stock updatedStock = new StockBuilder().withSerialNumber("NotFound1").build();
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(updatedStock).build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        assertCommandFailure(updateCommand, model, UpdateCommand.MESSAGE_SERIAL_NUMBER_NOT_FOUND);
    }

    @Test
    public void execute_someSerialNumberNotFound_failure() {
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder()
                .withSerialNumber(VALID_SERIAL_NUMBER_APPLE, "Not Found1").build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        assertCommandFailure(updateCommand, model, UpdateCommand.MESSAGE_SERIAL_NUMBER_NOT_FOUND);
    }

    @Test
    public void execute_allSerialNumberNotFound_failure() {
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder()
                .withSerialNumber("Unknown1", "Not Found1").build();
        UpdateCommand updateCommand = new UpdateCommand(descriptor);

        assertCommandFailure(updateCommand, model, UpdateCommand.MESSAGE_SERIAL_NUMBER_NOT_FOUND);
    }

    @Test
    public void equals() {
        UpdateStockDescriptor descriptor = new UpdateStockDescriptorBuilder(new StockBuilder().build()).build();
        UpdateStockDescriptor differentDescriptor = new UpdateStockDescriptorBuilder()
                .withName(VALID_NAME_APPLE).withSerialNumber(VALID_SERIAL_NUMBER_APPLE).build();
        final UpdateCommand standardCommand = new UpdateCommand(descriptor);

        // EP: same values -> returns true
        UpdateStockDescriptor copyDescriptor = new UpdateStockDescriptor(descriptor);
        UpdateCommand commandWithSameValues = new UpdateCommand(copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // EP: same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // EP: null -> returns false
        assertFalse(standardCommand.equals(null));

        // EP: different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // EP: different descriptor -> returns false
        assertFalse(standardCommand.equals(new UpdateCommand(differentDescriptor)));
    }
}
