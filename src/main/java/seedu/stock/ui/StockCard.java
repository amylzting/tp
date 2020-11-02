package seedu.stock.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.stock.model.stock.Stock;

/**
 * An UI component that displays information of a {@code Stock}.
 */
public class StockCard extends UiPart<Region> {

    private static final String FXML = "StockListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Stock stock;

    @FXML
    private HBox cardPane;
    @FXML
    private VBox bookmark;
    @FXML
    private Label name;
    @FXML
    private Label serialNumber;
    @FXML
    private Label id;
    @FXML
    private Label quantity;
    @FXML
    private Label lowQuantity;
    @FXML
    private Label locationStored;
    @FXML
    private Label source;
    @FXML
    private Label notes;

    /**
     * Creates a {@code StockCard} with the given {@code Stock} and index to display.
     */
    public StockCard(Stock stock, int displayedIndex) {
        super(FXML);
        this.stock = stock;
        id.setText(displayedIndex + "");

        String stockName = upperFirst(stock.getName().fullName);
        name.setText(stockName);
        name.getStyleClass().add("list-cell-text");

        String serial = stock.getSerialNumber().getSerialNumberAsString().toUpperCase();
        serialNumber.setText(serial);
        quantity.setText(stock.getQuantity().quantity);

        lowQuantity.setText(stock.getQuantity().getLowQuantity());

        String sourceName = upperFirst(stock.getSource().value);
        source.setText(sourceName);

        String locationName = upperFirst(stock.getLocation().value);
        locationStored.setText(locationName);

        bookmark.setVisible(stock.getIsBookmarked());

        if (stock.getNotes().size() != 0) {
            StringBuilder notesAppended = new StringBuilder();
            for (int i = 0; i < stock.getNotes().size(); i++) {
                notesAppended.append(i + 1).append(". ")
                        .append(stock.getNotes().get(i)).append("\n");
            }
            notes.setText(notesAppended.toString());
        }

        // checks if the stock is low in quantity
        if (stock.getQuantity().isLowOnQuantity()) {
            id.getStyleClass().clear();
            id.getStyleClass().add("list-cell-text-low");

            name.getStyleClass().clear();
            name.getStyleClass().add("list-cell-text-low");

            serialNumber.getStyleClass().clear();
            serialNumber.getStyleClass().add("list-cell-text-low");

            quantity.getStyleClass().clear();
            quantity.getStyleClass().add("list-cell-text-low");

            source.getStyleClass().clear();
            source.getStyleClass().add("list-cell-text-low");

            locationStored.getStyleClass().clear();
            locationStored.getStyleClass().add("list-cell-text-low");

            notes.getStyleClass().clear();
            notes.getStyleClass().add("list-cell-text-low");
        }

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StockCard)) {
            return false;
        }

        // state check
        StockCard card = (StockCard) other;
        return id.getText().equals(card.id.getText())
                && stock.equals(card.stock);
    }

    /**
     * Returns the input string with the first letter being upper-cased
     * @param toChange
     * @return input string with first letter being upper-cased
     */
    public String upperFirst(String toChange) {
        String upperCase = toChange.substring(0, 1).toUpperCase();
        String lowerCase = toChange.substring(1);

        return upperCase + lowerCase;
    }
}
