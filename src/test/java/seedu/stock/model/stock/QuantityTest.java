package seedu.stock.model.stock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.stock.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class QuantityTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Quantity(null));
        assertThrows(NullPointerException.class, () -> new Quantity(null, null));
        assertThrows(NullPointerException.class, () -> new Quantity("100", null));
    }

    @Test
    public void constructor_invalidQuantity_throwsIllegalArgumentException() {
        String invalidQuantity = "";
        String validQuantity = "100";
        assertThrows(IllegalArgumentException.class, () -> new Quantity(invalidQuantity));
        assertThrows(IllegalArgumentException.class, () -> new Quantity(invalidQuantity, invalidQuantity));
        assertThrows(IllegalArgumentException.class, () -> new Quantity(invalidQuantity, validQuantity));
    }

    @Test
    public void isValidQuantity() {
        // null quantity
        assertThrows(NullPointerException.class, () -> Quantity.isValidQuantity(null));

        // invalid quantity
        assertFalse(Quantity.isValidQuantity("")); // empty string
        assertFalse(Quantity.isValidQuantity(" ")); // spaces only
        assertFalse(Quantity.isValidQuantity("^")); // only non-alphanumeric characters
        assertFalse(Quantity.isValidQuantity("fdfs")); // alphabets only
        assertFalse(Quantity.isValidQuantity("-199")); // negative number
        assertFalse(Quantity.isValidQuantity(Integer.toString(Integer.MAX_VALUE + 1))); //Overflow

        // valid quantity
        assertTrue(Quantity.isValidQuantity("0")); // zero
        assertTrue(Quantity.isValidQuantity("14")); // numbers only
        assertTrue(Quantity.isValidQuantity("2324254")); // large number
        assertTrue(Quantity.isValidQuantity(Integer.toString(Integer.MAX_VALUE))); //Max integer value
    }

    @Test
    public void isLowOnQuantity() {
        Quantity lowQuantity = new Quantity("100", "1000");
        Quantity notLowQuantity = new Quantity("100");

        // is low on quantity, return true
        assertTrue(lowQuantity.isLowOnQuantity());

        // not low on quantity, return false
        assertFalse(notLowQuantity.isLowOnQuantity()); // comparing with default low quantity
        notLowQuantity = new Quantity("100", "10");
        assertFalse(notLowQuantity.isLowOnQuantity()); // comparing after changing low quantity value
    }

    @Test
    public void equals() {
        Quantity quantity = new Quantity("100");
        Quantity quantityCopy = new Quantity("100");
        Quantity quantityWithLowQuantity = new Quantity("100", "10");

        // returns true
        assertTrue(quantity.equals(quantity)); // compare to self
        assertTrue(quantity.equals(quantityCopy)); // compare to a copy of itself

        // returns false, lowQuantity edited
        assertFalse(quantity.equals(quantityWithLowQuantity));
    }
}
