package seedu.stock.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.stock.model.Model.PREDICATE_SHOW_ALL_STOCKS;
import static seedu.stock.testutil.Assert.assertThrows;
import static seedu.stock.testutil.TypicalStocks.APPLE;
import static seedu.stock.testutil.TypicalStocks.BANANA;
import static seedu.stock.testutil.TypicalStocks.ORANGE;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.stock.commons.core.GuiSettings;
import seedu.stock.model.stock.AccumulatedQuantity;
import seedu.stock.model.stock.SerialNumberSet;
import seedu.stock.model.stock.Source;
import seedu.stock.model.stock.predicates.NameContainsKeywordsPredicate;
import seedu.stock.testutil.SerialNumberSetsBookBuilder;
import seedu.stock.testutil.StockBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new StockBook(), new StockBook(modelManager.getStockBook()));
        assertEquals(new SerialNumberSetsBook(),
                new SerialNumberSetsBook(modelManager.getSerialNumberSetsBook()));
    }

    @Test
    public void setStockBook() {
        StockBook newStockBook = new StockBook();
        newStockBook.addStock(ORANGE);
        modelManager.addStock(APPLE);
        modelManager.addStock(BANANA);
        modelManager.setStockBook(newStockBook);
        assertTrue(modelManager.hasStock(ORANGE));
        assertFalse(modelManager.hasStock(BANANA));
        assertFalse(modelManager.hasStock(APPLE));
    }

    @Test
    public void deleteStock() {
        modelManager.addStock(APPLE);
        assertTrue(modelManager.hasStock(APPLE));
        modelManager.deleteStock(APPLE);
        assertFalse(modelManager.hasStock(APPLE));
    }

    @Test
    public void setStock() {
        modelManager.addStock(APPLE);
        modelManager.setStock(APPLE, ORANGE);
        assertTrue(modelManager.hasStock(ORANGE));
        assertFalse(modelManager.hasStock(APPLE));
    }

    @Test
    public void setSerialNumberSetsBook() {
        SerialNumberSet newSerialNumberSet = new SerialNumberSet(new Source("alpha"), new AccumulatedQuantity("4"));
        SerialNumberSetsBook newSerialNumberSetsBook = new SerialNumberSetsBook();
        newSerialNumberSetsBook.addSerialNumberSet(newSerialNumberSet);
        modelManager.setSerialNumberSetsBook(newSerialNumberSetsBook);
        assertEquals(newSerialNumberSetsBook, modelManager.getSerialNumberSetsBook());
    }

    @Test
    public void hasSerialNumberSet() {
        SerialNumberSet newSerialNumberSet = new SerialNumberSet(new Source("alpha"), new AccumulatedQuantity("4"));
        assertFalse(modelManager.hasSerialNumberSet(newSerialNumberSet));
        modelManager.addSerialNumberSet(newSerialNumberSet);
        assertTrue(modelManager.hasSerialNumberSet(newSerialNumberSet));
    }

    @Test
    public void deleteSerialNumberSet() {
        SerialNumberSet newSerialNumberSet = new SerialNumberSet(new Source("alpha"), new AccumulatedQuantity("4"));
        modelManager.addSerialNumberSet(newSerialNumberSet);
        assertTrue(modelManager.hasSerialNumberSet(newSerialNumberSet));
        modelManager.deleteSerialNumberSet(newSerialNumberSet);
        assertFalse(modelManager.hasSerialNumberSet(newSerialNumberSet));
    }

    @Test
    public void updateSerialNumberSet() {
        SerialNumberSet newSerialNumberSet = new SerialNumberSet(new Source("alpha"), new AccumulatedQuantity("4"));
        modelManager.addSerialNumberSet(newSerialNumberSet);
        modelManager.updateSerialNumberSet(new Source("alpha"));
        assertEquals(modelManager.getSerialNumberSetsBook().getSerialNumberSetsList().get(0).getAccumulatedQuantity(),
                new AccumulatedQuantity("5"));
    }

    @Test
    public void generateNextSerialNumber() {
        SerialNumberSet newSerialNumberSet = new SerialNumberSet(new Source("alpha"), new AccumulatedQuantity("4"));
        modelManager.addSerialNumberSet(newSerialNumberSet);
        String actualSerialNumber = modelManager.generateNextSerialNumber(new Source("alpha"));
        String expectedSerialNumber = "alpha5";
        assertEquals(actualSerialNumber, expectedSerialNumber);
    }

    @Test
    public void setSerialNumberSet() {
        SerialNumberSet newSerialNumberSet = new SerialNumberSet(new Source("alpha"), new AccumulatedQuantity("4"));
        modelManager.addSerialNumberSet(newSerialNumberSet);
        SerialNumberSet updatedSerialNumberSet = new SerialNumberSet(new Source("beta"), new AccumulatedQuantity("4"));
        modelManager.setSerialNumberSet(newSerialNumberSet, updatedSerialNumberSet);
        assertFalse(modelManager.hasSerialNumberSet(newSerialNumberSet));
        assertTrue(modelManager.hasSerialNumberSet(updatedSerialNumberSet));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setStockBookFilePath(Paths.get("stock/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setStockBookFilePath(Paths.get("new/stock/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setStockBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setStockBookFilePath(null));
    }

    @Test
    public void setStockBookFilePath_validPath_setsStockBookFilePath() {
        Path path = Paths.get("stock/book/file/path");
        modelManager.setStockBookFilePath(path);
        assertEquals(path, modelManager.getStockBookFilePath());
    }

    @Test
    public void hasStock_nullStock_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStock(null));
    }

    @Test
    public void hasStock_stockNotInStockBook_returnsFalse() {
        assertFalse(modelManager.hasStock(APPLE));
    }

    @Test
    public void hasStock_stockInStockBook_returnsTrue() {
        modelManager.addStock(APPLE);
        assertTrue(modelManager.hasStock(APPLE));
    }

    @Test
    public void getFilteredStockList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStockList().remove(0));
    }

    @Test
    public void equals() {
        StockBook stockBook = new StockBookBuilder().withStock(APPLE).withStock(BANANA).build();
        StockBook differentStockBook = new StockBook();
        SerialNumberSetsBook serialNumberSetsBook = new SerialNumberSetsBookBuilder()
                                            .withSerialNumberSet(new SerialNumberSet(APPLE.getSource(),
                                                                        new AccumulatedQuantity("1")))
                                            .withSerialNumberSet(new SerialNumberSet(BANANA.getSource(),
                                                                        new AccumulatedQuantity("1")))
                                            .build();
        SerialNumberSetsBook differentSerialNumberSetsBook = new SerialNumberSetsBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(stockBook, userPrefs, serialNumberSetsBook);
        ModelManager modelManagerCopy = new ModelManager(stockBook, userPrefs, serialNumberSetsBook);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different stockBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentStockBook, userPrefs, serialNumberSetsBook)));

        // different serialNumberSetsBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(stockBook, userPrefs, differentSerialNumberSetsBook)));

        // different filteredList -> returns false
        String[] keywords = APPLE.getName().fullName.split("\\s+");
        modelManager.updateFilteredStockList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(stockBook, userPrefs, serialNumberSetsBook)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredStockList(PREDICATE_SHOW_ALL_STOCKS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setStockBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(stockBook, differentUserPrefs, serialNumberSetsBook)));
    }

}
