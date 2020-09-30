package seedu.stock.model;

import javafx.collections.ObservableList;
import seedu.stock.model.stock.Stock;

/**
 * Unmodifiable view of an stock book
 */
public interface ReadOnlyStockBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Stock> getStockList();

}
